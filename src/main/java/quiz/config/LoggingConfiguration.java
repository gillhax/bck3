package quiz.config;

import ch.qos.logback.classic.AsyncAppender;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.core.spi.ContextAwareBase;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import net.logstash.logback.appender.LogstashSocketAppender;
import net.logstash.logback.stacktrace.ShortenedThrowableConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import quiz.config.JHipsterProperties;

@Configuration
public class LoggingConfiguration {
   private final Logger log = LoggerFactory.getLogger(LoggingConfiguration.class);
   private LoggerContext context = (LoggerContext)LoggerFactory.getILoggerFactory();
   @Value("${spring.application.name}")
   private String appName;
   @Value("${server.port}")
   private String serverPort;
   @Inject
   private JHipsterProperties jHipsterProperties;

   @PostConstruct
   private void init() {
      if(this.jHipsterProperties.getLogging().getLogstash().isEnabled()) {
         this.addLogstashAppender(this.context);
         LoggingConfiguration.LogbackLoggerContextListener loggerContextListener = new LoggingConfiguration.LogbackLoggerContextListener();
         loggerContextListener.setContext(this.context);
         this.context.addListener(loggerContextListener);
      }

   }

   public void addLogstashAppender(LoggerContext context) {
      this.log.info("Initializing Logstash logging");
      LogstashSocketAppender logstashAppender = new LogstashSocketAppender();
      logstashAppender.setName("LOGSTASH");
      logstashAppender.setContext(context);
      String customFields = "{\"app_name\":\"" + this.appName + "\",\"app_port\":\"" + this.serverPort + "\"}";
      logstashAppender.setSyslogHost(this.jHipsterProperties.getLogging().getLogstash().getHost());
      logstashAppender.setPort(this.jHipsterProperties.getLogging().getLogstash().getPort());
      logstashAppender.setCustomFields(customFields);
      ShortenedThrowableConverter throwableConverter = new ShortenedThrowableConverter();
      throwableConverter.setMaxLength(7500);
      throwableConverter.setRootCauseFirst(true);
      logstashAppender.setThrowableConverter(throwableConverter);
      logstashAppender.start();
      AsyncAppender asyncLogstashAppender = new AsyncAppender();
      asyncLogstashAppender.setContext(context);
      asyncLogstashAppender.setName("ASYNC_LOGSTASH");
      asyncLogstashAppender.setQueueSize(this.jHipsterProperties.getLogging().getLogstash().getQueueSize());
      asyncLogstashAppender.addAppender(logstashAppender);
      asyncLogstashAppender.start();
      context.getLogger("ROOT").addAppender(asyncLogstashAppender);
   }

   class LogbackLoggerContextListener extends ContextAwareBase implements LoggerContextListener {
      public boolean isResetResistant() {
         return true;
      }

      public void onStart(LoggerContext context) {
         LoggingConfiguration.this.addLogstashAppender(context);
      }

      public void onReset(LoggerContext context) {
         LoggingConfiguration.this.addLogstashAppender(context);
      }

      public void onStop(LoggerContext context) {
      }

      public void onLevelChange(ch.qos.logback.classic.Logger logger, Level level) {
      }
   }
}
