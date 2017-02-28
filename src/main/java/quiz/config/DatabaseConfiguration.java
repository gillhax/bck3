package quiz.config;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import javax.inject.Inject;
import javax.sql.DataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import quiz.config.liquibase.AsyncSpringLiquibase;

@Configuration
@EnableJpaRepositories({"quiz.repository"})
@EnableJpaAuditing(
   auditorAwareRef = "springSecurityAuditorAware"
)
@EnableTransactionManagement
public class DatabaseConfiguration {
   private final Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);
   @Inject
   private Environment env;

   @Bean
   public SpringLiquibase liquibase(DataSource dataSource, LiquibaseProperties liquibaseProperties) {
      AsyncSpringLiquibase liquibase = new AsyncSpringLiquibase();
      liquibase.setDataSource(dataSource);
      liquibase.setChangeLog("classpath:config/liquibase/master.xml");
      liquibase.setContexts(liquibaseProperties.getContexts());
      liquibase.setDefaultSchema(liquibaseProperties.getDefaultSchema());
      liquibase.setDropFirst(liquibaseProperties.isDropFirst());
      if(this.env.acceptsProfiles(new String[]{"no-liquibase"})) {
         liquibase.setShouldRun(false);
      } else {
         liquibase.setShouldRun(liquibaseProperties.isEnabled());
         this.log.debug("Configuring Liquibase");
      }

      return liquibase;
   }

   @Bean
   public Hibernate5Module hibernate5Module() {
      return new Hibernate5Module();
   }
}
