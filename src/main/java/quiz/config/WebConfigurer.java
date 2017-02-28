package quiz.config;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.servlet.InstrumentedFilter;
import com.codahale.metrics.servlets.MetricsServlet;
import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.EnumSet;
import javax.inject.Inject;
import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.FilterRegistration.Dynamic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.MimeMappings;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import quiz.config.JHipsterProperties;
import quiz.web.filter.CachingHttpHeadersFilter;

@Configuration
public class WebConfigurer implements ServletContextInitializer, EmbeddedServletContainerCustomizer {
   private final Logger log = LoggerFactory.getLogger(WebConfigurer.class);
   @Inject
   private Environment env;
   @Inject
   private JHipsterProperties jHipsterProperties;
   @Autowired(
      required = false
   )
   private MetricRegistry metricRegistry;

   public void onStartup(ServletContext servletContext) throws ServletException {
      if(this.env.getActiveProfiles().length != 0) {
         this.log.info("Web application configuration, using profiles: {}", Arrays.toString(this.env.getActiveProfiles()));
      }

      EnumSet disps = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.ASYNC);
      this.initMetrics(servletContext, disps);
      if(this.env.acceptsProfiles(new String[]{"prod"})) {
         this.initCachingHttpHeadersFilter(servletContext, disps);
      }

      this.log.info("Web application fully configured");
   }

   public void customize(ConfigurableEmbeddedServletContainer container) {
      MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
      mappings.add("html", "text/html;charset=utf-8");
      mappings.add("json", "text/html;charset=utf-8");
      container.setMimeMappings(mappings);
      this.setLocationForStaticAssets(container);
   }

   private void setLocationForStaticAssets(ConfigurableEmbeddedServletContainer container) {
      String prefixPath = this.resolvePathPrefix();
      File root;
      if(this.env.acceptsProfiles(new String[]{"prod"})) {
         root = new File(prefixPath + "target/www/");
      } else {
         root = new File(prefixPath + "src/main/webapp/");
      }

      if(root.exists() && root.isDirectory()) {
         container.setDocumentRoot(root);
      }

   }

   private String resolvePathPrefix() {
      String fullExecutablePath = this.getClass().getResource("").getPath();
      String rootPath = Paths.get(".", new String[0]).toUri().normalize().getPath();
      String extractedPath = fullExecutablePath.replace(rootPath, "");
      int extractionEndIndex = extractedPath.indexOf("target/");
      return extractionEndIndex <= 0?"":extractedPath.substring(0, extractionEndIndex);
   }

   private void initCachingHttpHeadersFilter(ServletContext servletContext, EnumSet disps) {
      this.log.debug("Registering Caching HTTP Headers Filter");
      Dynamic cachingHttpHeadersFilter = servletContext.addFilter("cachingHttpHeadersFilter", new CachingHttpHeadersFilter(this.jHipsterProperties));
      cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, new String[]{"/content/*"});
      cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, new String[]{"/app/*"});
      cachingHttpHeadersFilter.setAsyncSupported(true);
   }

   private void initMetrics(ServletContext servletContext, EnumSet disps) {
      this.log.debug("Initializing Metrics registries");
      servletContext.setAttribute(InstrumentedFilter.REGISTRY_ATTRIBUTE, this.metricRegistry);
      servletContext.setAttribute(MetricsServlet.METRICS_REGISTRY, this.metricRegistry);
      this.log.debug("Registering Metrics Filter");
      Dynamic metricsFilter = servletContext.addFilter("webappMetricsFilter", new InstrumentedFilter());
      metricsFilter.addMappingForUrlPatterns(disps, true, new String[]{"/*"});
      metricsFilter.setAsyncSupported(true);
      this.log.debug("Registering Metrics Servlet");
      javax.servlet.ServletRegistration.Dynamic metricsAdminServlet = servletContext.addServlet("metricsServlet", new MetricsServlet());
      metricsAdminServlet.addMapping(new String[]{"/management/metrics/*"});
      metricsAdminServlet.setAsyncSupported(true);
      metricsAdminServlet.setLoadOnStartup(2);
   }

   @Bean
   @ConditionalOnProperty(
      name = {"jhipster.cors.allowed-origins"}
   )
   public CorsFilter corsFilter() {
      this.log.debug("Registering CORS filter");
      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      CorsConfiguration config = this.jHipsterProperties.getCors();
      source.registerCorsConfiguration("/api/**", config);
      source.registerCorsConfiguration("/v2/api-docs", config);
      source.registerCorsConfiguration("/oauth/**", config);
      return new CorsFilter(source);
   }
}
