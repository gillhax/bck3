package quiz.service;

import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
public class LocalizationService {
   private final MessageSource messageSource;

   @Autowired
   public LocalizationService(MessageSource messageSource) {
      this.messageSource = messageSource;
   }

   public String getMessage(String code, String... args) {
      Locale currentLocale = LocaleContextHolder.getLocale();
      Locale newLocale = new Locale(currentLocale.getLanguage());
      return this.messageSource.getMessage(code, args, code, newLocale);
   }
}
