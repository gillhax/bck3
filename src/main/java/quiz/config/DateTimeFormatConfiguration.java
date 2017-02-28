package quiz.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class DateTimeFormatConfiguration extends WebMvcConfigurerAdapter {
   public void addFormatters(FormatterRegistry registry) {
      DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
      registrar.setUseIsoFormat(true);
      registrar.registerFormatters(registry);
   }
}