package quiz;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.MetricFilterAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.MetricRepositoryAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import quiz.config.DefaultProfileUtil;
import quiz.config.JHipsterProperties;

@ComponentScan
@EnableAutoConfiguration(
   exclude = {MetricFilterAutoConfiguration.class, MetricRepositoryAutoConfiguration.class}
)
@EnableConfigurationProperties({JHipsterProperties.class, LiquibaseProperties.class})
public class QuizApp {
   private static final Logger log = LoggerFactory.getLogger(QuizApp.class);
   @Inject
   private Environment env;

   @PostConstruct
   public void initApplication() {
      log.info("Running with Spring profile(s) : {}", Arrays.toString(this.env.getActiveProfiles()));
      List activeProfiles = Arrays.asList(this.env.getActiveProfiles());
      if(activeProfiles.contains("dev") && activeProfiles.contains("prod")) {
         log.error("You have misconfigured your application! It should not run with both the \'dev\' and \'prod\' profiles at the same time.");
      }

      if(activeProfiles.contains("dev") && activeProfiles.contains("cloud")) {
         log.error("You have misconfigured your application! It should notrun with both the \'dev\' and \'cloud\' profiles at the same time.");
      }

   }

   public static void main(String[] args) throws UnknownHostException {
      SpringApplication app = new SpringApplication(new Object[]{QuizApp.class});
      DefaultProfileUtil.addDefaultProfile(app);
      ConfigurableEnvironment env = app.run(args).getEnvironment();
      log.info("\n----------------------------------------------------------\n\tApplication \'{}\' is running! Access URLs:\n\tLocal: \t\thttp://localhost:{}\n\tExternal: \thttp://{}:{}\n----------------------------------------------------------", new Object[]{env.getProperty("spring.application.name"), env.getProperty("server.port"), InetAddress.getLocalHost().getHostAddress(), env.getProperty("server.port")});
   }
}
