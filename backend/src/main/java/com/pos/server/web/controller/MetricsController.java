package com.pos.server.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/metrics")
public class MetricsController {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private CacheManager cacheManager;

    @GetMapping("/database")
    public ResponseEntity<Map<String, Object>> getDatabaseMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        
        try (Connection connection = dataSource.getConnection()) {
            // Connection pool stats
            if (connection.getMetaData().getDriverName().contains("HikariCP")) {
                metrics.put("connectionPool", "HikariCP");
                // Add HikariCP specific metrics if available
            }
            
            // Database size metrics
            try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT schemaname, tablename, pg_size_pretty(pg_total_relation_size(schemaname||'.'||tablename)) as size " +
                "FROM pg_tables WHERE schemaname = 'public' ORDER BY pg_total_relation_size(schemaname||'.'||tablename) DESC")) {
                
                ResultSet rs = stmt.executeQuery();
                Map<String, String> tableSizes = new HashMap<>();
                while (rs.next()) {
                    tableSizes.put(rs.getString("tablename"), rs.getString("size"));
                }
                metrics.put("tableSizes", tableSizes);
            }
            
            // Record counts
            try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT 'products' as table_name, COUNT(*) as count FROM productos " +
                "UNION ALL SELECT 'categories', COUNT(*) FROM categorias " +
                "UNION ALL SELECT 'customers', COUNT(*) FROM clientes " +
                "UNION ALL SELECT 'purchases', COUNT(*) FROM compras")) {
                
                ResultSet rs = stmt.executeQuery();
                Map<String, Integer> recordCounts = new HashMap<>();
                while (rs.next()) {
                    recordCounts.put(rs.getString("table_name"), rs.getInt("count"));
                }
                metrics.put("recordCounts", recordCounts);
            }
            
        } catch (Exception e) {
            metrics.put("error", e.getMessage());
        }
        
        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/cache")
    public ResponseEntity<Map<String, Object>> getCacheMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        
        // Cache statistics
        Map<String, Object> cacheStats = new HashMap<>();
        for (String cacheName : cacheManager.getCacheNames()) {
            org.springframework.cache.Cache cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                cacheStats.put(cacheName, "active");
            }
        }
        
        metrics.put("caches", cacheStats);
        metrics.put("cacheManager", cacheManager.getClass().getSimpleName());
        
        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/performance")
    public ResponseEntity<Map<String, Object>> getPerformanceMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        
        try (Connection connection = dataSource.getConnection()) {
            // Slow query detection
            try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT query, calls, total_time, mean_time " +
                "FROM pg_stat_statements " +
                "WHERE mean_time > 100 " +
                "ORDER BY mean_time DESC LIMIT 10")) {
                
                ResultSet rs = stmt.executeQuery();
                Map<String, Object> slowQueries = new HashMap<>();
                int count = 0;
                while (rs.next() && count < 5) {
                    Map<String, Object> queryStats = new HashMap<>();
                    queryStats.put("calls", rs.getLong("calls"));
                    queryStats.put("totalTime", rs.getDouble("total_time"));
                    queryStats.put("meanTime", rs.getDouble("mean_time"));
                    slowQueries.put("query_" + (++count), queryStats);
                }
                metrics.put("slowQueries", slowQueries);
                
            } catch (Exception e) {
                // pg_stat_statements extension may not be available
                metrics.put("slowQueries", "pg_stat_statements not available");
            }
            
            // Index usage statistics
            try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT schemaname, tablename, attname, n_distinct, correlation " +
                "FROM pg_stats WHERE schemaname = 'public' " +
                "ORDER BY n_distinct DESC LIMIT 10")) {
                
                ResultSet rs = stmt.executeQuery();
                Map<String, Object> indexStats = new HashMap<>();
                while (rs.next()) {
                    String key = rs.getString("tablename") + "." + rs.getString("attname");
                    Map<String, Object> stats = new HashMap<>();
                    stats.put("distinct_values", rs.getInt("n_distinct"));
                    stats.put("correlation", rs.getDouble("correlation"));
                    indexStats.put(key, stats);
                }
                metrics.put("columnStats", indexStats);
            }
            
        } catch (Exception e) {
            metrics.put("error", e.getMessage());
        }
        
        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/business")
    public ResponseEntity<Map<String, Object>> getBusinessMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        
        try (Connection connection = dataSource.getConnection()) {
            // Sales metrics for last 30 days
            try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT COUNT(*) as total_sales, SUM(total) as revenue, " +
                "AVG(total) as avg_sale, COUNT(DISTINCT id_cliente) as unique_customers " +
                "FROM compras WHERE fecha >= CURRENT_DATE - INTERVAL '30 days' AND estado = 'C'")) {
                
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    Map<String, Object> salesMetrics = new HashMap<>();
                    salesMetrics.put("totalSales", rs.getInt("total_sales"));
                    salesMetrics.put("revenue", rs.getDouble("revenue"));
                    salesMetrics.put("averageSale", rs.getDouble("avg_sale"));
                    salesMetrics.put("uniqueCustomers", rs.getInt("unique_customers"));
                    metrics.put("last30Days", salesMetrics);
                }
            }
            
            // Low stock products
            try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT COUNT(*) as low_stock_count FROM productos " +
                "WHERE cantidad_stock < 10 AND estado = true")) {
                
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    metrics.put("lowStockProducts", rs.getInt("low_stock_count"));
                }
            }
            
            // Top categories by sales
            try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT c.descripcion, COUNT(cp.id_producto) as sales_count " +
                "FROM categorias c " +
                "JOIN productos p ON c.id_categoria = p.id_categoria " +
                "JOIN compras_productos cp ON p.id_producto = cp.id_producto " +
                "JOIN compras co ON cp.id_compra = co.id_compra " +
                "WHERE co.fecha >= CURRENT_DATE - INTERVAL '30 days' AND co.estado = 'C' " +
                "GROUP BY c.descripcion ORDER BY sales_count DESC LIMIT 5")) {
                
                ResultSet rs = stmt.executeQuery();
                Map<String, Integer> topCategories = new HashMap<>();
                while (rs.next()) {
                    topCategories.put(rs.getString("descripcion"), rs.getInt("sales_count"));
                }
                metrics.put("topCategories", topCategories);
            }
            
        } catch (Exception e) {
            metrics.put("error", e.getMessage());
        }
        
        return ResponseEntity.ok(metrics);
    }
}