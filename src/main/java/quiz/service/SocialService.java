package quiz.service;

import java.util.HashSet;
import java.util.Locale;
import java.util.function.Consumer;
import javax.inject.Inject;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.stereotype.Service;
import quiz.domain.User;
import quiz.repository.AuthorityRepository;
import quiz.repository.UserRepository;
import quiz.service.MailService;

@Service
public class SocialService {
   private final Logger log = LoggerFactory.getLogger(SocialService.class);
   @Inject
   private UsersConnectionRepository usersConnectionRepository;
   @Inject
   private AuthorityRepository authorityRepository;
   @Inject
   private PasswordEncoder passwordEncoder;
   @Inject
   private UserRepository userRepository;
   @Inject
   private MailService mailService;

   public void deleteUserSocialConnection(String login) {
      ConnectionRepository connectionRepository = this.usersConnectionRepository.createConnectionRepository(login);
      connectionRepository.findAllConnections().keySet().stream().forEach((providerId) -> {
         connectionRepository.removeConnections(providerId);
         this.log.debug("Delete user social connection providerId: {}", providerId);
      });
   }

   public void createSocialUser(Connection connection, String langKey) {
      if(connection == null) {
         this.log.error("Cannot create social user because connection is null");
         throw new IllegalArgumentException("Connection cannot be null");
      } else {
         UserProfile userProfile = connection.fetchUserProfile();
         String providerId = connection.getKey().getProviderId();
         User user = this.createUserIfNotExist(userProfile, langKey, providerId);
         this.createSocialConnection(user.getLogin(), connection);
         this.mailService.sendSocialRegistrationValidationEmail(user, providerId);
      }
   }

   private User createUserIfNotExist(UserProfile userProfile, String langKey, String providerId) {
      String email = userProfile.getEmail();
      String userName = userProfile.getUsername();
      if(!StringUtils.isBlank(userName)) {
         userName = userName.toLowerCase(Locale.ENGLISH);
      }

      if(StringUtils.isBlank(email) && StringUtils.isBlank(userName)) {
         this.log.error("Cannot create social user because email and login are null");
         throw new IllegalArgumentException("Email and login cannot be null");
      } else if(StringUtils.isBlank(email) && this.userRepository.findOneByLogin(userName).isPresent()) {
         this.log.error("Cannot create social user because email is null and login already exist, login -> {}", userName);
         throw new IllegalArgumentException("Email cannot be null with an existing login");
      } else {
         String login = this.getLoginDependingOnProviderId(userProfile, providerId);
         String encryptedPassword = this.passwordEncoder.encode(RandomStringUtils.random(10));
         HashSet authorities = new HashSet(1);
         authorities.add(this.authorityRepository.findOne("ROLE_USER"));
         User newUser = new User();
         newUser.setLogin(login);
         newUser.setPassword(encryptedPassword);
         newUser.setActivated(true);
         newUser.setAuthorities(authorities);
         newUser.setLangKey(langKey);
         return (User)this.userRepository.save(newUser);
      }
   }

   private String getLoginDependingOnProviderId(UserProfile userProfile, String providerId) {
      byte var4 = -1;
      switch(providerId.hashCode()) {
      case -916346253:
         if(providerId.equals("twitter")) {
            var4 = 0;
         }
      default:
         switch(var4) {
         case 0:
            return userProfile.getUsername().toLowerCase();
         default:
            return userProfile.getEmail();
         }
      }
   }

   private void createSocialConnection(String login, Connection connection) {
      ConnectionRepository connectionRepository = this.usersConnectionRepository.createConnectionRepository(login);
      connectionRepository.addConnection(connection);
   }
}
