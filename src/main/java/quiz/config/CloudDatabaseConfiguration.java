package quiz.config;

import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"cloud"})
public class CloudDatabaseConfiguration extends AbstractCloudConfig {
   private final Logger log = LoggerFactory.getLogger(CloudDatabaseConfiguration.class);

   @Bean
   public DataSource dataSource() {
      this.log.info("Configuring JDBC datasource from a cloud provider");
      return this.connectionFactory().dataSource();
   }
}
