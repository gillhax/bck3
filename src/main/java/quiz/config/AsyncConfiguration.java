package quiz.config;

import java.util.concurrent.Executor;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import quiz.async.ExceptionHandlingAsyncTaskExecutor;
import quiz.config.JHipsterProperties;

@Configuration
@EnableAsync
@EnableScheduling
public class AsyncConfiguration implements AsyncConfigurer {
   private final Logger log = LoggerFactory.getLogger(AsyncConfiguration.class);
   @Inject
   private JHipsterProperties jHipsterProperties;

   @Bean(
      name = {"taskExecutor"}
   )
   public Executor getAsyncExecutor() {
      this.log.debug("Creating Async Task Executor");
      ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
      executor.setCorePoolSize(this.jHipsterProperties.getAsync().getCorePoolSize());
      executor.setMaxPoolSize(this.jHipsterProperties.getAsync().getMaxPoolSize());
      executor.setQueueCapacity(this.jHipsterProperties.getAsync().getQueueCapacity());
      executor.setThreadNamePrefix("quiz-Executor-");
      return new ExceptionHandlingAsyncTaskExecutor(executor);
   }

   public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
      return new SimpleAsyncUncaughtExceptionHandler();
   }
}
