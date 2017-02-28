package quiz.security;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import quiz.repository.UserRepository;
import quiz.domain.User;
import quiz.security.UserNotActivatedException;


@Component("userDetailsService")
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
   private final Logger log = LoggerFactory.getLogger(UserDetailsService.class);
   @Inject
   private UserRepository userRepository;

   @Transactional
   public UserDetails loadUserByUsername(String login) {
      this.log.debug("Authenticating {}", login);
      String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);
      Optional<User> userFromDatabase = this.userRepository.findOneByLogin(lowercaseLogin);
      return (UserDetails)userFromDatabase.map((user) -> {
         if(!user.isActivated()) {
            throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
         } else {
            List grantedAuthorities = (List)user.getAuthorities().stream().map((authority) -> {
               return new SimpleGrantedAuthority(authority.getName());
            }).collect(Collectors.toList());
            return new org.springframework.security.core.userdetails.User(lowercaseLogin, user.getPassword(), grantedAuthorities);
         }
      }).orElseThrow(() -> {
         return new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database");
      });
   }
}
