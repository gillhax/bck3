package quiz.web.rest;

import com.codahale.metrics.annotation.Timed;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quiz.domain.User;
import quiz.repository.UserRepository;
import quiz.security.SecurityUtils;
import quiz.service.MailService;
import quiz.service.UserService;
import quiz.service.dto.UserDTO;
import quiz.web.rest.util.HeaderUtil;
import quiz.web.rest.vm.KeyAndPasswordVM;
import quiz.web.rest.vm.ManagedCreateUserVM;
import quiz.web.rest.vm.ManagedUserVM;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
@RequestMapping({"/api"})
public class AccountResource {
   private final Logger log = LoggerFactory.getLogger(AccountResource.class);
   @Inject
   private UserRepository userRepository;
   @Inject
   private UserService userService;
   @Inject
   private MailService mailService;

   @PostMapping(
      path = {"/register"},
      produces = {"application/json", "text/plain"}
   )
   @Timed
   public ResponseEntity registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM) {
      HttpHeaders textPlainHeaders = new HttpHeaders();
      textPlainHeaders.setContentType(MediaType.TEXT_PLAIN);
      return (ResponseEntity)this.userRepository.findOneByLogin(managedUserVM.getLogin().toLowerCase()).map((user) -> {
         return new ResponseEntity("e-mail address already in use", textPlainHeaders, HttpStatus.BAD_REQUEST);
      }).orElseGet(() -> {
         User user = this.userService.createUser(managedUserVM.getLogin(), managedUserVM.getPassword(), managedUserVM.getLangKey());
         return new ResponseEntity(HttpStatus.CREATED);
      });
   }

   @GetMapping({"/authenticate"})
   @Timed
   public String isAuthenticated(HttpServletRequest request) {
      this.log.debug("REST request to check if the current user is authenticated");
      return request.getRemoteUser();
   }

   @GetMapping({"/account"})
   @Timed
   public ResponseEntity getAccount() {
      return (ResponseEntity)Optional.ofNullable(this.userService.getUserWithAuthorities()).map((user) -> {
         return new ResponseEntity(new UserDTO(user), HttpStatus.OK);
      }).orElse(new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR));
   }

   @PostMapping({"/account"})
   @Timed
   public ResponseEntity saveAccount(@Valid @RequestBody UserDTO userDTO) {
      Optional existingUser = this.userRepository.findOneByLogin(userDTO.getLogin());
      return existingUser.isPresent() && !((User)existingUser.get()).getLogin().equalsIgnoreCase(userDTO.getLogin())?((BodyBuilder)ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("user-management", "emailexists", "Email already in use"))).body((Object)null):(ResponseEntity)this.userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).map((u) -> {
         this.userService.updateUser(userDTO.getLangKey());
         return new ResponseEntity(HttpStatus.OK);
      }).orElseGet(() -> {
         return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
      });
   }

   @PostMapping(
      path = {"/account/change_password"},
      produces = {"text/plain"}
   )
   @Timed
   public ResponseEntity changePassword(@RequestBody String password) {
      if(!this.checkPasswordLength(password)) {
         return new ResponseEntity("Incorrect password", HttpStatus.BAD_REQUEST);
      } else {
         this.userService.changePassword(password);
         return new ResponseEntity(HttpStatus.OK);
      }
   }

   @PostMapping(
      path = {"/account/reset_password/init"},
      produces = {"text/plain"}
   )
   @Timed
   public ResponseEntity requestPasswordReset(@RequestBody String mail) {
      return (ResponseEntity)this.userService.requestPasswordReset(mail).map((user) -> {
         this.mailService.sendPasswordResetMail(user);
         return new ResponseEntity("e-mail was sent", HttpStatus.OK);
      }).orElse(new ResponseEntity("e-mail address not registered", HttpStatus.BAD_REQUEST));
   }

   @PostMapping(
      path = {"/account/reset_password/finish"},
      produces = {"text/plain"}
   )
   @Timed
   public ResponseEntity finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword) {
      return !this.checkPasswordLength(keyAndPassword.getNewPassword())?new ResponseEntity("Incorrect password", HttpStatus.BAD_REQUEST):(ResponseEntity)this.userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey()).map((user) -> {
         return new ResponseEntity(HttpStatus.OK);
      }).orElse(new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR));
   }

   @PostMapping(
      path = {"/account/register"},
      produces = {"application/json", "text/plain"}
   )
   @Timed
   public ResponseEntity registerAccount(@Valid @RequestBody ManagedCreateUserVM managedCreateUserVM) {
      HttpHeaders textPlainHeaders = new HttpHeaders();
      textPlainHeaders.setContentType(MediaType.TEXT_PLAIN);
      return (ResponseEntity)this.userRepository.findOneByLogin(managedCreateUserVM.getLogin().toLowerCase()).map((user) -> {
         return new ResponseEntity("e-mail address already in use", textPlainHeaders, HttpStatus.BAD_REQUEST);
      }).orElseGet(() -> {
         this.userService.createUser(managedCreateUserVM);
         return new ResponseEntity(HttpStatus.CREATED);
      });
   }

   private boolean checkPasswordLength(String password) {
      return !StringUtils.isEmpty(password) && password.length() >= 4 && password.length() <= 100;
   }
}
