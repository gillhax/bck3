package quiz.config;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.jvm.BufferPoolMetricSet;
import com.codahale.metrics.jvm.FileDescriptorRatioGauge;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;
import com.zaxxer.hikari.HikariDataSource;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.dropwizard.DropwizardExports;
import io.prometheus.client.exporter.MetricsServlet;
import java.lang.management.ManagementFactory;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import quiz.config.JHipsterProperties;
import quiz.config.jcache.JCacheGaugeSet;

@Configuration
@EnableMetrics(
   proxyTargetClass = true
)
public class MetricsConfiguration extends MetricsConfigurerAdapter {
   private static final String PROP_METRIC_REG_JVM_MEMORY = "jvm.memory";
   private static final String PROP_METRIC_REG_JVM_GARBAGE = "jvm.garbage";
   private static final String PROP_METRIC_REG_JVM_THREADS = "jvm.threads";
   private static final String PROP_METRIC_REG_JVM_FILES = "jvm.files";
   private static final String PROP_METRIC_REG_JVM_BUFFERS = "jvm.buffers";
   private static final String PROP_METRIC_REG_JCACHE_STATISTICS = "jcache.statistics";
   private final Logger log = LoggerFactory.getLogger(MetricsConfiguration.class);
   private MetricRegistry metricRegistry = new MetricRegistry();
   private HealthCheckRegistry healthCheckRegistry = new HealthCheckRegistry();
   @Inject
   private JHipsterProperties jHipsterProperties;
   @Autowired(
      required = false
   )
   private HikariDataSource hikariDataSource;

   @Bean
   public MetricRegistry getMetricRegistry() {
      return this.metricRegistry;
   }

   @Bean
   public HealthCheckRegistry getHealthCheckRegistry() {
      return this.healthCheckRegistry;
   }

   @PostConstruct
   public void init() {
      this.log.debug("Registering JVM gauges");
      this.metricRegistry.register("jvm.memory", new MemoryUsageGaugeSet());
      this.metricRegistry.register("jvm.garbage", new GarbageCollectorMetricSet());
      this.metricRegistry.register("jvm.threads", new ThreadStatesGaugeSet());
      this.metricRegistry.register("jvm.files", new FileDescriptorRatioGauge());
      this.metricRegistry.register("jvm.buffers", new BufferPoolMetricSet(ManagementFactory.getPlatformMBeanServer()));
      this.metricRegistry.register("jcache.statistics", new JCacheGaugeSet());
      if(this.hikariDataSource != null) {
         this.log.debug("Monitoring the datasource");
         this.hikariDataSource.setMetricRegistry(this.metricRegistry);
      }

      if(this.jHipsterProperties.getMetrics().getJmx().isEnabled()) {
         this.log.debug("Initializing Metrics JMX reporting");
         JmxReporter reporter = JmxReporter.forRegistry(this.metricRegistry).build();
         reporter.start();
      }

      if(this.jHipsterProperties.getMetrics().getLogs().isEnabled()) {
         this.log.info("Initializing Metrics Log reporting");
         Slf4jReporter reporter1 = Slf4jReporter.forRegistry(this.metricRegistry).outputTo(LoggerFactory.getLogger("metrics")).convertRatesTo(TimeUnit.SECONDS).convertDurationsTo(TimeUnit.MILLISECONDS).build();
         reporter1.start(this.jHipsterProperties.getMetrics().getLogs().getReportFrequency(), TimeUnit.SECONDS);
      }

   }

   @Configuration
   @ConditionalOnClass({CollectorRegistry.class})
   public static class PrometheusRegistry implements ServletContextInitializer {
      private final Logger log = LoggerFactory.getLogger(MetricsConfiguration.PrometheusRegistry.class);
      @Inject
      private MetricRegistry metricRegistry;
      @Inject
      private JHipsterProperties jHipsterProperties;

      public void onStartup(ServletContext servletContext) throws ServletException {
         if(this.jHipsterProperties.getMetrics().getPrometheus().isEnabled()) {
            String endpoint = this.jHipsterProperties.getMetrics().getPrometheus().getEndpoint();
            this.log.info("Initializing Metrics Prometheus endpoint at {}", endpoint);
            CollectorRegistry collectorRegistry = new CollectorRegistry();
            collectorRegistry.register(new DropwizardExports(this.metricRegistry));
            servletContext.addServlet("prometheusMetrics", new MetricsServlet(collectorRegistry)).addMapping(new String[]{endpoint});
         }

      }
   }

   @Configuration
   @ConditionalOnClass({Graphite.class})
   public static class GraphiteRegistry {
      private final Logger log = LoggerFactory.getLogger(MetricsConfiguration.GraphiteRegistry.class);
      @Inject
      private MetricRegistry metricRegistry;
      @Inject
      private JHipsterProperties jHipsterProperties;

      @PostConstruct
      private void init() {
         if(this.jHipsterProperties.getMetrics().getGraphite().isEnabled()) {
            this.log.info("Initializing Metrics Graphite reporting");
            String graphiteHost = this.jHipsterProperties.getMetrics().getGraphite().getHost();
            Integer graphitePort = Integer.valueOf(this.jHipsterProperties.getMetrics().getGraphite().getPort());
            String graphitePrefix = this.jHipsterProperties.getMetrics().getGraphite().getPrefix();
            Graphite graphite = new Graphite(new InetSocketAddress(graphiteHost, graphitePort.intValue()));
            GraphiteReporter graphiteReporter = GraphiteReporter.forRegistry(this.metricRegistry).convertRatesTo(TimeUnit.SECONDS).convertDurationsTo(TimeUnit.MILLISECONDS).prefixedWith(graphitePrefix).build(graphite);
            graphiteReporter.start(1L, TimeUnit.MINUTES);
         }

      }
   }
}
