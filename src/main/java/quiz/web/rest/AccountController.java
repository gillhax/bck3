package quiz.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.Api;
import java.beans.ConstructorProperties;
import java.util.Optional;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import quiz.domain.User;
import quiz.repository.UserRepository;
import quiz.security.jwt.TokenProvider;
import quiz.service.MailService;
import quiz.service.UserService;
import quiz.system.error.ApiAssert;
import quiz.system.error.handler.dto.ResponseDto;
import quiz.system.util.StaticWrapper;
import quiz.web.rest.JWTToken;
import quiz.web.rest.vm.KeyAndPasswordVM;
import quiz.web.rest.vm.LoginVM;
import quiz.web.rest.vm.ManagedCreateUserVM;

@RestController
@RequestMapping({"/api/v1/"})
@Api(
   tags = {"Account"}
)
public class AccountController {
   @Inject
   private UserService userService;
   @Inject
   private UserRepository userRepository;
   @Inject
   private TokenProvider tokenProvider;
   @Inject
   private MailService mailService;
   @Inject
   private AuthenticationManager authenticationManager;

   @PostMapping(
      path = {"/account/register"},
      produces = {"application/json", "text/plain"}
   )
   @Timed
   public ResponseDto registerAccount(@Valid @RequestBody ManagedCreateUserVM managedCreateUserVM) {
      Optional user = this.userRepository.findOneByLogin(managedCreateUserVM.getLogin().toLowerCase());
      ApiAssert.badRequest(user.isPresent(), "login.email.exist");
      this.userService.createUser(managedCreateUserVM);
      return StaticWrapper.wrap();
   }

   @PostMapping({"/account/authenticate"})
   @Timed
   public ResponseDto authorize(@Valid @RequestBody LoginVM loginVM, HttpServletResponse response) {
      UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());

      try {
         Authentication exception = this.authenticationManager.authenticate(authenticationToken);
         SecurityContextHolder.getContext().setAuthentication(exception);
         boolean rememberMe = loginVM.getRememberMe() == null?false:loginVM.getRememberMe().booleanValue();
         String jwt = this.tokenProvider.createToken(exception, Boolean.valueOf(rememberMe));
         response.addHeader("Authorization", "Bearer " + jwt);
         return StaticWrapper.wrap(new JWTToken(jwt));
      } catch (AuthenticationException var7) {
         ApiAssert.unauthorized(true, var7.getMessage());
         return StaticWrapper.wrap();
      }
   }

   @GetMapping(
      path = {"/account/reset/password/init"}
   )
   @Timed
   public ResponseDto requestPasswordReset(@RequestParam String mail) {
      Optional user = this.userService.requestPasswordReset(mail);
      ApiAssert.badRequest(!user.isPresent(), "not-found.reset.email");
      this.mailService.sendPasswordResetMail((User)user.get());
      return StaticWrapper.wrap();
   }

   @PostMapping(
      path = {"/account/reset/password/finish"}
   )
   @Timed
   public ResponseDto finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword) {
      if(!this.checkPasswordLength(keyAndPassword.getNewPassword())) {
         ApiAssert.unprocessable(true, "Пароль не удовлетворяет условиям");
      }

      Optional user = this.userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey());
      ApiAssert.badRequest(!user.isPresent(), "Введённый ключ не верный");
      return StaticWrapper.wrap();
   }

   @GetMapping(
      path = {"/account/reset/password/key"}
   )
   @Timed
   public ResponseDto checkKeyPasswordReset(@RequestParam String key) {
      Boolean valid = this.userService.checkResetKey(key);
      return StaticWrapper.wrap(new AccountController.checked(valid.booleanValue()));
   }

   private boolean checkPasswordLength(String password) {
      return !StringUtils.isEmpty(password) && password.length() >= 4 && password.length() <= 100;
   }

   private class checked {
      private boolean valid;

      public boolean isValid() {
         return this.valid;
      }

      public void setValid(boolean valid) {
         this.valid = valid;
      }

      @ConstructorProperties({"valid"})
      public checked(boolean valid) {
         this.valid = valid;
      }
   }
}
