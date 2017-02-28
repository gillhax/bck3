package quiz.config.liquibase;

import javax.inject.Inject;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.util.StopWatch;

public class AsyncSpringLiquibase extends SpringLiquibase {
   private final Logger logger = LoggerFactory.getLogger(AsyncSpringLiquibase.class);
   @Inject
   @Qualifier("taskExecutor")
   private TaskExecutor taskExecutor;
   @Inject
   private Environment env;

   public void afterPropertiesSet() throws LiquibaseException {
      if(!this.env.acceptsProfiles(new String[]{"no-liquibase"})) {
         if(this.env.acceptsProfiles(new String[]{"dev", "heroku"})) {
            this.taskExecutor.execute(() -> {
               try {
                  this.logger.warn("Starting Liquibase asynchronously, your database might not be ready at startup!");
                  this.initDb();
               } catch (LiquibaseException var2) {
                  this.logger.error("Liquibase could not start correctly, your database is NOT ready: {}", var2.getMessage(), var2);
               }

            });
         } else {
            this.logger.debug("Starting Liquibase synchronously");
            this.initDb();
         }
      } else {
         this.logger.debug("Liquibase is disabled");
      }

   }

   protected void initDb() throws LiquibaseException {
      StopWatch watch = new StopWatch();
      watch.start();
      super.afterPropertiesSet();
      watch.stop();
      this.logger.debug("Started Liquibase in {} ms", Long.valueOf(watch.getTotalTimeMillis()));
   }
}
