-- Performance optimizations and additional indexes

-- Composite indexes for common queries
CREATE INDEX idx_productos_categoria_estado ON productos(id_categoria, estado);
CREATE INDEX idx_productos_stock_bajo ON productos(cantidad_stock) WHERE cantidad_stock < 10;

-- Partial indexes for active records
CREATE INDEX idx_clientes_activos ON clientes(id_cliente, nombre) WHERE estado = true;
CREATE INDEX idx_productos_activos ON productos(id_producto, nombre, precio_venta) WHERE estado = true;

-- Indexes for reporting queries
CREATE INDEX idx_compras_fecha_estado ON compras(fecha, estado);
CREATE INDEX idx_compras_total_fecha ON compras(total, fecha) WHERE estado = 'C';

-- Optimize purchase item queries
CREATE INDEX idx_compras_productos_compra_cantidad ON compras_productos(id_compra, cantidad, total);

-- Full-text search indexes for products
CREATE INDEX idx_productos_busqueda ON productos USING gin(to_tsvector('spanish', nombre || ' ' || COALESCE(codigo_barras, '')));

-- Create materialized view for sales statistics
CREATE MATERIALIZED VIEW IF NOT EXISTS estadisticas_ventas AS
SELECT 
    DATE_TRUNC('day', c.fecha) as fecha,
    COUNT(*) as total_ventas,
    SUM(c.total) as ingresos_totales,
    AVG(c.total) as venta_promedio,
    COUNT(DISTINCT c.id_cliente) as clientes_unicos
FROM compras c
WHERE c.estado = 'C'
GROUP BY DATE_TRUNC('day', c.fecha)
ORDER BY fecha DESC;

CREATE UNIQUE INDEX idx_estadisticas_ventas_fecha ON estadisticas_ventas(fecha);

-- Create view for low stock products
CREATE OR REPLACE VIEW productos_stock_bajo AS
SELECT 
    p.id_producto,
    p.nombre,
    p.cantidad_stock,
    c.descripcion as categoria,
    p.precio_venta
FROM productos p
JOIN categorias c ON p.id_categoria = c.id_categoria
WHERE p.cantidad_stock < 10 
  AND p.estado = true
ORDER BY p.cantidad_stock ASC;

-- Create view for top selling products
CREATE OR REPLACE VIEW productos_mas_vendidos AS
SELECT 
    p.id_producto,
    p.nombre,
    c.descripcion as categoria,
    SUM(cp.cantidad) as total_vendido,
    SUM(cp.total) as ingresos_generados,
    COUNT(DISTINCT cp.id_compra) as numero_ventas
FROM productos p
JOIN compras_productos cp ON p.id_producto = cp.id_producto
JOIN compras co ON cp.id_compra = co.id_compra
JOIN categorias c ON p.id_categoria = c.id_categoria
WHERE co.estado = 'C'
  AND co.fecha >= CURRENT_DATE - INTERVAL '30 days'
GROUP BY p.id_producto, p.nombre, c.descripcion
ORDER BY total_vendido DESC;

-- Function to refresh materialized views
CREATE OR REPLACE FUNCTION refresh_statistics()
RETURNS void AS $$
BEGIN
    REFRESH MATERIALIZED VIEW estadisticas_ventas;
END;
$$ LANGUAGE plpgsql;

-- Stored procedure for purchase total calculation
CREATE OR REPLACE FUNCTION calcular_total_compra(compra_id INTEGER)
RETURNS DECIMAL(16,2) AS $$
DECLARE
    total_calculado DECIMAL(16,2);
BEGIN
    SELECT COALESCE(SUM(total), 0)
    INTO total_calculado
    FROM compras_productos
    WHERE id_compra = compra_id;
    
    UPDATE compras 
    SET total = total_calculado,
        fecha_actualizacion = CURRENT_TIMESTAMP
    WHERE id_compra = compra_id;
    
    RETURN total_calculado;
END;
$$ LANGUAGE plpgsql;

-- Trigger to automatically update purchase total
CREATE OR REPLACE FUNCTION trigger_actualizar_total_compra()
RETURNS TRIGGER AS $$
BEGIN
    PERFORM calcular_total_compra(COALESCE(NEW.id_compra, OLD.id_compra));
    RETURN COALESCE(NEW, OLD);
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_compras_productos_total
    AFTER INSERT OR UPDATE OR DELETE ON compras_productos
    FOR EACH ROW EXECUTE FUNCTION trigger_actualizar_total_compra();

-- Function to update product stock
CREATE OR REPLACE FUNCTION actualizar_stock_producto(
    producto_id INTEGER,
    cantidad_vendida INTEGER
)
RETURNS void AS $$
BEGIN
    UPDATE productos 
    SET cantidad_stock = cantidad_stock - cantidad_vendida,
        fecha_actualizacion = CURRENT_TIMESTAMP
    WHERE id_producto = producto_id;
    
    -- Log if stock goes below minimum
    IF (SELECT cantidad_stock FROM productos WHERE id_producto = producto_id) < 5 THEN
        RAISE NOTICE 'STOCK BAJO: Producto % tiene % unidades restantes', 
                     producto_id, 
                     (SELECT cantidad_stock FROM productos WHERE id_producto = producto_id);
    END IF;
END;
$$ LANGUAGE plpgsql;