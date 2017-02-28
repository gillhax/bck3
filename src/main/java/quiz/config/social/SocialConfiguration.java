package quiz.config.social;

import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;
import quiz.repository.CustomSocialUsersConnectionRepository;
import quiz.repository.SocialUserConnectionRepository;
import quiz.security.social.CustomSignInAdapter;

@Configuration
@EnableSocial
public class SocialConfiguration implements SocialConfigurer {
   private final Logger log = LoggerFactory.getLogger(SocialConfiguration.class);
   @Inject
   private SocialUserConnectionRepository socialUserConnectionRepository;
   @Inject
   Environment environment;

   @Bean
   public ConnectController connectController(ConnectionFactoryLocator connectionFactoryLocator, ConnectionRepository connectionRepository) {
      ConnectController controller = new ConnectController(connectionFactoryLocator, connectionRepository);
      controller.setApplicationUrl(this.environment.getProperty("spring.application.url"));
      return controller;
   }

   public void addConnectionFactories(ConnectionFactoryConfigurer connectionFactoryConfigurer, Environment environment) {
      String googleClientId = environment.getProperty("spring.social.google.clientId");
      String googleClientSecret = environment.getProperty("spring.social.google.clientSecret");
      if(googleClientId != null && googleClientSecret != null) {
         this.log.debug("Configuring GoogleConnectionFactory");
         connectionFactoryConfigurer.addConnectionFactory(new GoogleConnectionFactory(googleClientId, googleClientSecret));
      } else {
         this.log.error("Cannot configure GoogleConnectionFactory id or secret null");
      }

      String facebookClientId = environment.getProperty("spring.social.facebook.clientId");
      String facebookClientSecret = environment.getProperty("spring.social.facebook.clientSecret");
      if(facebookClientId != null && facebookClientSecret != null) {
         this.log.debug("Configuring FacebookConnectionFactory");
         connectionFactoryConfigurer.addConnectionFactory(new FacebookConnectionFactory(facebookClientId, facebookClientSecret));
      } else {
         this.log.error("Cannot configure FacebookConnectionFactory id or secret null");
      }

      String twitterClientId = environment.getProperty("spring.social.twitter.clientId");
      String twitterClientSecret = environment.getProperty("spring.social.twitter.clientSecret");
      if(twitterClientId != null && twitterClientSecret != null) {
         this.log.debug("Configuring TwitterConnectionFactory");
         connectionFactoryConfigurer.addConnectionFactory(new TwitterConnectionFactory(twitterClientId, twitterClientSecret));
      } else {
         this.log.error("Cannot configure TwitterConnectionFactory id or secret null");
      }

   }

   public UserIdSource getUserIdSource() {
      return new AuthenticationNameUserIdSource();
   }

   public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
      return new CustomSocialUsersConnectionRepository(this.socialUserConnectionRepository, connectionFactoryLocator);
   }

   @Bean
   public SignInAdapter signInAdapter() {
      return new CustomSignInAdapter();
   }

   @Bean
   public ProviderSignInController providerSignInController(ConnectionFactoryLocator connectionFactoryLocator, UsersConnectionRepository usersConnectionRepository, SignInAdapter signInAdapter) throws Exception {
      ProviderSignInController providerSignInController = new ProviderSignInController(connectionFactoryLocator, usersConnectionRepository, signInAdapter);
      providerSignInController.setSignUpUrl("/social/signup");
      providerSignInController.setApplicationUrl(this.environment.getProperty("spring.application.url"));
      return providerSignInController;
   }

   @Bean
   public ProviderSignInUtils getProviderSignInUtils(ConnectionFactoryLocator connectionFactoryLocator, UsersConnectionRepository usersConnectionRepository) {
      return new ProviderSignInUtils(connectionFactoryLocator, usersConnectionRepository);
   }
}
