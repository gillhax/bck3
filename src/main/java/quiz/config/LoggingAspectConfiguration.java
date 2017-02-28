package quiz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;
import quiz.aop.logging.LoggingAspect;

@Configuration
@EnableAspectJAutoProxy
public class LoggingAspectConfiguration {
   @Bean
   @Profile({"dev"})
   public LoggingAspect loggingAspect() {
      return new LoggingAspect();
   }
}
