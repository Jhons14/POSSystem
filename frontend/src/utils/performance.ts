// Performance optimization utilities for frontend

/**
 * Image lazy loading utility
 */
export function setupLazyLoading() {
  if ('IntersectionObserver' in window) {
    const imageObserver = new IntersectionObserver((entries) => {
      entries.forEach(entry => {
        if (entry.isIntersecting) {
          const img = entry.target as HTMLImageElement;
          img.src = img.dataset.src || '';
          img.classList.remove('lazy');
          imageObserver.unobserve(img);
        }
      });
    });

    document.querySelectorAll('img[data-src]').forEach(img => {
      imageObserver.observe(img);
    });
  }
}

/**
 * Memory usage monitoring
 */
export function getMemoryUsage() {
  if ('memory' in performance) {
    const memory = (performance as any).memory;
    return {
      used: Math.round(memory.usedJSHeapSize / 1048576 * 100) / 100, // MB
      total: Math.round(memory.totalJSHeapSize / 1048576 * 100) / 100, // MB
      limit: Math.round(memory.jsHeapSizeLimit / 1048576 * 100) / 100, // MB
    };
  }
  return null;
}

/**
 * Performance marks and measures
 */
export const performanceMarks = {
  mark(name: string) {
    if ('performance' in window && 'mark' in performance) {
      performance.mark(name);
    }
  },

  measure(name: string, startMark: string, endMark?: string) {
    if ('performance' in window && 'measure' in performance) {
      performance.measure(name, startMark, endMark);
    }
  },

  getEntries(type?: string) {
    if ('performance' in window && 'getEntriesByType' in performance) {
      return type ? performance.getEntriesByType(type) : performance.getEntries();
    }
    return [];
  },

  clear() {
    if ('performance' in window && 'clearMarks' in performance) {
      performance.clearMarks();
      performance.clearMeasures();
    }
  }
};

/**
 * Bundle size analyzer
 */
export function analyzeBundleSize() {
  const scripts = Array.from(document.querySelectorAll('script[src]'));
  const styles = Array.from(document.querySelectorAll('link[rel="stylesheet"]'));
  
  return {
    scripts: scripts.length,
    styles: styles.length,
    totalResources: scripts.length + styles.length,
  };
}

/**
 * Network quality detection
 */
export function getNetworkInfo() {
  if ('connection' in navigator) {
    const connection = (navigator as any).connection;
    return {
      effectiveType: connection.effectiveType,
      downlink: connection.downlink,
      rtt: connection.rtt,
      saveData: connection.saveData,
    };
  }
  return null;
}

/**
 * Viewport and device info
 */
export function getDeviceInfo() {
  return {
    viewport: {
      width: window.innerWidth,
      height: window.innerHeight,
    },
    screen: {
      width: window.screen.width,
      height: window.screen.height,
    },
    devicePixelRatio: window.devicePixelRatio,
    userAgent: navigator.userAgent,
    platform: navigator.platform,
  };
}

/**
 * Resource timing analysis
 */
export function analyzeResourceTiming() {
  const resources = performance.getEntriesByType('resource') as PerformanceResourceTiming[];
  
  const analysis = {
    totalResources: resources.length,
    slowResources: resources.filter(r => r.duration > 1000),
    largeResources: resources.filter(r => r.transferSize > 100000),
    averageLoadTime: resources.reduce((acc, r) => acc + r.duration, 0) / resources.length,
    byType: {} as Record<string, number>,
  };

  // Group by resource type
  resources.forEach(resource => {
    const type = getResourceType(resource.name);
    analysis.byType[type] = (analysis.byType[type] || 0) + 1;
  });

  return analysis;
}

function getResourceType(url: string): string {
  if (url.includes('.js')) return 'javascript';
  if (url.includes('.css')) return 'stylesheet';
  if (url.match(/\.(png|jpg|jpeg|gif|svg|webp)$/)) return 'image';
  if (url.match(/\.(woff|woff2|ttf|eot)$/)) return 'font';
  return 'other';
}

/**
 * Core Web Vitals monitoring
 */
export function measureCoreWebVitals() {
  return new Promise((resolve) => {
    const vitals = {
      fcp: null as number | null,
      lcp: null as number | null,
      fid: null as number | null,
      cls: null as number | null,
    };

    // First Contentful Paint
    const fcpObserver = new PerformanceObserver((list) => {
      const entries = list.getEntries();
      vitals.fcp = entries[0]?.startTime || null;
    });
    fcpObserver.observe({ entryTypes: ['paint'] });

    // Largest Contentful Paint
    const lcpObserver = new PerformanceObserver((list) => {
      const entries = list.getEntries();
      const lastEntry = entries[entries.length - 1];
      vitals.lcp = lastEntry?.startTime || null;
    });
    lcpObserver.observe({ entryTypes: ['largest-contentful-paint'] });

    // First Input Delay
    const fidObserver = new PerformanceObserver((list) => {
      const entries = list.getEntries();
      vitals.fid = entries[0]?.processingStart - entries[0]?.startTime || null;
    });
    fidObserver.observe({ entryTypes: ['first-input'] });

    // Cumulative Layout Shift
    let clsValue = 0;
    const clsObserver = new PerformanceObserver((list) => {
      for (const entry of list.getEntries()) {
        if (!(entry as any).hadRecentInput) {
          clsValue += (entry as any).value;
        }
      }
      vitals.cls = clsValue;
    });
    clsObserver.observe({ entryTypes: ['layout-shift'] });

    // Return results after a delay to collect data
    setTimeout(() => {
      fcpObserver.disconnect();
      lcpObserver.disconnect();
      fidObserver.disconnect();
      clsObserver.disconnect();
      resolve(vitals);
    }, 5000);
  });
}

/**
 * Performance monitoring service
 */
export class PerformanceMonitor {
  private metrics: Array<{
    timestamp: number;
    memory?: any;
    network?: any;
    timing: any;
  }> = [];

  start() {
    this.collect();
    setInterval(() => this.collect(), 30000); // Collect every 30 seconds
  }

  private collect() {
    const timestamp = Date.now();
    const memory = getMemoryUsage();
    const network = getNetworkInfo();
    const timing = analyzeResourceTiming();

    this.metrics.push({
      timestamp,
      memory,
      network,
      timing,
    });

    // Keep only last 100 measurements
    if (this.metrics.length > 100) {
      this.metrics = this.metrics.slice(-100);
    }
  }

  getMetrics() {
    return this.metrics;
  }

  getAverageLoadTime() {
    if (this.metrics.length === 0) return 0;
    const sum = this.metrics.reduce((acc, m) => acc + m.timing.averageLoadTime, 0);
    return sum / this.metrics.length;
  }

  getMemoryTrend() {
    return this.metrics
      .filter(m => m.memory)
      .map(m => ({
        timestamp: m.timestamp,
        used: m.memory.used,
        percentage: (m.memory.used / m.memory.total) * 100,
      }));
  }
}

// Global performance monitor instance
export const performanceMonitor = new PerformanceMonitor();