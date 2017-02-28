package quiz.service;

import java.util.Locale;
import javax.inject.Inject;
import javax.mail.internet.MimeMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;
import quiz.config.JHipsterProperties;
import quiz.domain.User;
import quiz.system.error.ApiAssert;

@Service
public class MailService {
   private final Logger log = LoggerFactory.getLogger(MailService.class);
   private static final String USER = "user";
   private static final String BASE_URL = "127.0.0.1:8080";
   @Inject
   private JHipsterProperties jHipsterProperties;
   @Inject
   private JavaMailSenderImpl javaMailSender;
   @Inject
   private MessageSource messageSource;
   @Inject
   private SpringTemplateEngine templateEngine;

   @Async
   public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
      this.log.debug("Send e-mail[multipart \'{}\' and html \'{}\'] to \'{}\' with subject \'{}\' and content={}", new Object[]{Boolean.valueOf(isMultipart), Boolean.valueOf(isHtml), to, subject, content});
      MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();

      try {
         MimeMessageHelper e = new MimeMessageHelper(mimeMessage, isMultipart, "UTF-8");
         e.setTo(to);
         e.setFrom(this.jHipsterProperties.getMail().getFrom());
         e.setSubject(subject);
         e.setText(content, isHtml);
         this.javaMailSender.send(mimeMessage);
         this.log.debug("Sent e-mail to User \'{}\'", to);
      } catch (Exception var8) {
         this.log.warn("E-mail could not be sent to user \'{}\'", to, var8);
         ApiAssert.internal(true, var8.getMessage());
      }

   }

   @Async
   public void sendActivationEmail(User user) {
      this.log.debug("Sending activation e-mail to \'{}\'", user.getLogin());
      Locale locale = Locale.forLanguageTag(user.getLangKey());
      Context context = new Context(locale);
      context.setVariable("user", user);
      context.setVariable("127.0.0.1:8080", this.jHipsterProperties.getMail().getBaseUrl());
      String content = this.templateEngine.process("activationEmail", context);
      String subject = this.messageSource.getMessage("email.activation.title", (Object[])null, locale);
      this.sendEmail(user.getLogin(), subject, content, false, true);
   }

   @Async
   public void sendCreationEmail(User user) {
      this.log.debug("Sending creation e-mail to \'{}\'", user.getLogin());
      Locale locale = Locale.forLanguageTag(user.getLangKey());
      Context context = new Context(locale);
      context.setVariable("user", user);
      context.setVariable("127.0.0.1:8080", this.jHipsterProperties.getMail().getBaseUrl());
      String content = this.templateEngine.process("creationEmail", context);
      String subject = this.messageSource.getMessage("email.activation.title", (Object[])null, locale);
      this.sendEmail(user.getLogin(), subject, content, false, true);
   }

   @Async
   public void sendPasswordResetMail(User user) {
      this.log.debug("Sending password reset e-mail to \'{}\'", user.getLogin());
      Locale locale = Locale.forLanguageTag(user.getLangKey());
      Context context = new Context(locale);
      context.setVariable("user", user);
      context.setVariable("127.0.0.1:8080", this.jHipsterProperties.getMail().getBaseUrl());
      String content = this.templateEngine.process("passwordResetEmail", context);
      String subject = this.messageSource.getMessage("email.reset.title", (Object[])null, locale);
      this.sendEmail(user.getLogin(), subject, content, false, true);
   }

   @Async
   public void sendSocialRegistrationValidationEmail(User user, String provider) {
      this.log.debug("Sending social registration validation e-mail to \'{}\'", user.getLogin());
      Locale locale = Locale.forLanguageTag(user.getLangKey());
      Context context = new Context(locale);
      context.setVariable("user", user);
      context.setVariable("provider", StringUtils.capitalize(provider));
      String content = this.templateEngine.process("socialRegistrationValidationEmail", context);
      String subject = this.messageSource.getMessage("email.social.registration.title", (Object[])null, locale);
      this.sendEmail(user.getLogin(), subject, content, false, true);
   }
}
