package quiz.config;

import javax.annotation.PreDestroy;
import javax.cache.CacheManager;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import quiz.config.DatabaseConfiguration;
import quiz.config.MetricsConfiguration;
import quiz.config.WebConfigurer;

@Configuration
@EnableCaching
@AutoConfigureAfter({MetricsConfiguration.class})
@AutoConfigureBefore({WebConfigurer.class, DatabaseConfiguration.class})
public class CacheConfiguration {
   private final Logger log = LoggerFactory.getLogger(CacheConfiguration.class);
   @Inject
   private CacheManager cacheManager;

   @PreDestroy
   public void destroy() {
      this.log.info("Closing Cache Manager");
      this.cacheManager.close();
   }
}
