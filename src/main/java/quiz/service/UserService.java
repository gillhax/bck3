package quiz.service;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import quiz.converter.PlayerConverter;
import quiz.domain.Authority;
import quiz.domain.Avatar;
import quiz.domain.Player;
import quiz.domain.User;
import quiz.repository.AuthorityRepository;
import quiz.repository.AvatarRepository;
import quiz.repository.PlayerRepository;
import quiz.repository.UserRepository;
import quiz.security.SecurityUtils;
import quiz.service.PlayerService;
import quiz.service.SocialService;
import quiz.service.util.RandomUtil;
import quiz.system.error.ApiAssert;
import quiz.web.rest.vm.ManagedCreateUserVM;
import quiz.web.rest.vm.ManagedUserVM;

@Service
@Transactional
@SuppressWarnings("unchecked")
public class UserService {
   private final Logger log = LoggerFactory.getLogger(UserService.class);
   @Inject
   private SocialService socialService;
   @Inject
   private PasswordEncoder passwordEncoder;
   @Inject
   private UserRepository userRepository;
   @Inject
   private AuthorityRepository authorityRepository;
   @Inject
   private PlayerService playerService;
   @Inject
   private PlayerConverter playerConverter;
   @Inject
   AvatarRepository avatarRepository;
   @Inject
   private PlayerRepository playerRepository;

//   public Optional activateRegistration(String key) {
//      this.log.debug("Activating user for activation key {}", key);
//      return this.userRepository.findOneByActivationKey(key).map((user) -> {
//         user.setActivated(true);
//         user.setActivationKey((String)null);
//         this.log.debug("Activated user: {}", user);
//         return user;
//      });
//   }

   public Optional<User> completePasswordReset(String newPassword, String key) {
      this.log.debug("Reset user password for reset key {}", key);
      return userRepository.findOneByResetKey(key).filter((user) -> {
         ZonedDateTime oneDayAgo = ZonedDateTime.now().minusHours(12L);
         return user.getResetDate().isAfter(oneDayAgo);
      }).map((user) -> {
         user.setPassword(this.passwordEncoder.encode(newPassword));
         user.setResetKey((String)null);
         user.setResetDate((ZonedDateTime)null);
         return user;
      });
   }

   public Boolean checkResetKey(String key) {
      Optional result = this.userRepository.findOneByResetKey(key).filter((user) -> {
         ZonedDateTime oneDayAgo = ZonedDateTime.now().minusHours(12L);
         return user.getResetDate().isAfter(oneDayAgo);
      });
      return Boolean.valueOf(result.isPresent());
   }

   public Optional<User> requestPasswordReset(String mail) {
      return this.userRepository.findOneByLogin(mail).filter(User::isActivated).map((user) -> {
         user.setResetKey(RandomUtil.generateResetKey());
         user.setResetDate(ZonedDateTime.now());
         return user;
      });
   }

   public User createUser(String login, String password, String langKey) {
      User newUser = new User();
      Authority authority = (Authority)this.authorityRepository.findOne("ROLE_USER");
      HashSet authorities = new HashSet();
      String encryptedPassword = this.passwordEncoder.encode(password);
      newUser.setLogin(login);
      newUser.setPassword(encryptedPassword);
      newUser.setLangKey(langKey);
      newUser.setActivated(true);
      authorities.add(authority);
      newUser.setAuthorities(authorities);
      User savedUser = (User)this.userRepository.save(newUser);
      return newUser;
   }

   public User createUser(ManagedCreateUserVM managedCreateUserVM) {
      User newUser = new User();
      Authority authority = (Authority)this.authorityRepository.findOne("ROLE_USER");
      HashSet authorities = new HashSet();
      authorities.add(authority);
      String encryptedPassword = this.passwordEncoder.encode(managedCreateUserVM.getPassword());
      newUser.setLogin(managedCreateUserVM.getLogin());
      newUser.setPassword(encryptedPassword);
      newUser.setLangKey("ru");
      newUser.setActivated(true);
      newUser.setAuthorities(authorities);
      User savedUser = (User)this.userRepository.saveAndFlush(newUser);
      Player player = new Player();
      player.setId(savedUser.getId());
      player.setName(managedCreateUserVM.getName());
      player.setScore(Long.valueOf(0L));
      if(managedCreateUserVM.getAvatarId() != null) {
         Avatar avatar = (Avatar)this.avatarRepository.findOne(managedCreateUserVM.getAvatarId());
         ApiAssert.notFound(avatar == null, "not-found.avatar");
         player.setAvatar(avatar);
      }

      this.playerRepository.save(player);
      this.log.debug("Created Information for User: {}", newUser);
      return newUser;
   }

   public User createUser(ManagedUserVM managedUserVM) {
      User user = new User();
      user.setLogin(managedUserVM.getLogin());
      if(managedUserVM.getLangKey() == null) {
         user.setLangKey("ru");
      } else {
         user.setLangKey(managedUserVM.getLangKey());
      }

      if(managedUserVM.getAuthorities() != null) {
         HashSet encryptedPassword = new HashSet();
         managedUserVM.getAuthorities().forEach((authority) -> {
            encryptedPassword.add(this.authorityRepository.findOne(authority));
         });
         user.setAuthorities(encryptedPassword);
      }

      String encryptedPassword1 = this.passwordEncoder.encode(RandomUtil.generatePassword());
      user.setPassword(encryptedPassword1);
      user.setResetKey(RandomUtil.generateResetKey());
      user.setResetDate(ZonedDateTime.now());
      user.setActivated(true);
      this.userRepository.save(user);
      this.log.debug("Created Information for User: {}", user);
      return user;
   }

   public void updateUser(String langKey) {
      this.userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent((user) -> {
         user.setLangKey(langKey);
         this.log.debug("Changed Information for User: {}", user);
      });
   }

   public void updateUser(Long id, String login, boolean activated, String langKey, Set authorities) {
      Optional.of(this.userRepository.findOne(id)).ifPresent((user) -> {
         user.setLogin(login);
         user.setActivated(activated);
         user.setLangKey(langKey);
         Set managedAuthorities = user.getAuthorities();
         managedAuthorities.clear();
         authorities.forEach((authority) -> {
            managedAuthorities.add(this.authorityRepository.findOne(authority));
         });
         this.log.debug("Changed Information for User: {}", user);
      });
   }

   public void deleteUser(String login) {
      this.userRepository.findOneByLogin(login).ifPresent((user) -> {
         this.socialService.deleteUserSocialConnection(user.getLogin());
         this.userRepository.delete(user);
         this.log.debug("Deleted User: {}", user);
      });
   }

   public void changePassword(String password) {
      this.userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent((user) -> {
         String encryptedPassword = this.passwordEncoder.encode(password);
         user.setPassword(encryptedPassword);
         this.log.debug("Changed password for User: {}", user);
      });
   }

   @Transactional(
      readOnly = true
   )
   public Optional<User> getUserWithAuthoritiesByLogin(String login) {
      return this.userRepository.findOneByLogin(login).map((user) -> {
         user.getAuthorities().size();
         return user;
      });
   }

   @Transactional(
      readOnly = true
   )
   public User getUserWithAuthorities(Long id) {
      User user = (User)this.userRepository.findOne(id);
      user.getAuthorities().size();
      return user;
   }

   @Transactional(
      readOnly = true
   )
   public User getUserWithAuthorities() {
      Optional optionalUser = this.userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
      User user = null;
      if(optionalUser.isPresent()) {
         user = (User)optionalUser.get();
         user.getAuthorities().size();
      }

      return user;
   }

   @Transactional(
      readOnly = true
   )
   public Long getCurrentUserId() {
      return SecurityUtils.isAuthenticated()?this.userRepository.findIdByLogin(SecurityUtils.getCurrentUserLogin()):null;
   }

   @Scheduled(
      cron = "0 0 1 * * ?"
   )
   public void removeNotActivatedUsers() {
      ZonedDateTime now = ZonedDateTime.now();
      List users = this.userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(now.minusDays(3L));
      Iterator var3 = users.iterator();

      while(var3.hasNext()) {
         User user = (User)var3.next();
         this.log.debug("Deleting not activated user {}", user.getLogin());
         this.userRepository.delete(user);
      }

   }

   public void registerAccount(ManagedCreateUserVM managedCreateUserVM) {
      HttpHeaders textPlainHeaders = new HttpHeaders();
      textPlainHeaders.setContentType(MediaType.TEXT_PLAIN);
      this.userRepository.findOneByLogin(managedCreateUserVM.getLogin().toLowerCase()).map((user) -> {
         return new ResponseEntity("e-mail address already in use", textPlainHeaders, HttpStatus.BAD_REQUEST);
      }).orElseGet(() -> {
         this.createUser(managedCreateUserVM);
         return new ResponseEntity(HttpStatus.CREATED);
      });
   }
}
