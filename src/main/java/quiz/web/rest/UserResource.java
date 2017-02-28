package quiz.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.ApiParam;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quiz.converter.PlayerConverter;
import quiz.domain.User;
import quiz.repository.UserRepository;
import quiz.service.MailService;
import quiz.service.PlayerService;
import quiz.service.UserService;
import quiz.web.rest.util.HeaderUtil;
import quiz.web.rest.util.PaginationUtil;
import quiz.web.rest.vm.ManagedUserVM;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@ApiIgnore
@RequestMapping({"/api"})
public class UserResource {
   private final Logger log = LoggerFactory.getLogger(UserResource.class);
   @Inject
   private UserRepository userRepository;
   @Inject
   private MailService mailService;
   @Inject
   private UserService userService;
   @Inject
   private PlayerService playerService;
   @Inject
   private PlayerConverter playerConverter;

   @PostMapping({"/users"})
   @Timed
   @Secured({"ROLE_ADMIN"})
   public ResponseEntity createUser(@RequestBody ManagedUserVM managedUserVM) throws URISyntaxException {
      this.log.debug("REST request to save User : {}", managedUserVM);
      if(this.userRepository.findOneByLogin(managedUserVM.getLogin().toLowerCase()).isPresent()) {
         return ((BodyBuilder)ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userManagement", "emailexists", "Email already in use"))).body((Object)null);
      } else {
         User newUser = this.userService.createUser(managedUserVM);
         return ((BodyBuilder)ResponseEntity.created(new URI("/api/users/" + newUser.getLogin())).headers(HeaderUtil.createAlert("userManagement.created", newUser.getLogin()))).body(newUser);
      }
   }

   @PutMapping({"/users"})
   @Timed
   @Secured({"ROLE_ADMIN"})
   public ResponseEntity updateUser(@RequestBody ManagedUserVM managedUserVM) {
      this.log.debug("REST request to update User : {}", managedUserVM);
      Optional existingUser = this.userRepository.findOneByLogin(managedUserVM.getLogin().toLowerCase());
      if(existingUser.isPresent() && !((User)existingUser.get()).getId().equals(managedUserVM.getId())) {
         return ((BodyBuilder)ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userManagement", "emailexists", "E-mail already in use"))).body((Object)null);
      } else {
         this.userService.updateUser(managedUserVM.getId(), managedUserVM.getLogin(), managedUserVM.isActivated(), managedUserVM.getLangKey(), managedUserVM.getAuthorities());
         return ((BodyBuilder)ResponseEntity.ok().headers(HeaderUtil.createAlert("userManagement.updated", managedUserVM.getLogin()))).body(new ManagedUserVM(this.userService.getUserWithAuthorities(managedUserVM.getId())));
      }
   }

   @GetMapping({"/users"})
   @Timed
   public ResponseEntity getAllUsers(@ApiParam Pageable pageable) throws URISyntaxException {
      Page<User> page = this.userRepository.findAllWithAuthorities(pageable);
      List managedUserVMs = (List)page.getContent().stream().map(ManagedUserVM::new).collect(Collectors.toList());
      HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/users");
      return new ResponseEntity(managedUserVMs, headers, HttpStatus.OK);
   }

   @GetMapping({"/users/{login:^[_\'.@A-Za-z0-9-]*$}"})
   @Timed
   public ResponseEntity getUser(@PathVariable String login) {
      this.log.debug("REST request to get User : {}", login);
      return (ResponseEntity)this.userService.getUserWithAuthoritiesByLogin(login).map(ManagedUserVM::new).map((managedUserVM) -> {
         return new ResponseEntity(managedUserVM, HttpStatus.OK);
      }).orElse(new ResponseEntity(HttpStatus.NOT_FOUND));
   }

   @DeleteMapping({"/users/{login:^[_\'.@A-Za-z0-9-]*$}"})
   @Timed
   @Secured({"ROLE_ADMIN"})
   public ResponseEntity deleteUser(@PathVariable String login) {
      this.log.debug("REST request to delete User: {}", login);
      this.userService.deleteUser(login);
      return ((BodyBuilder)ResponseEntity.ok().headers(HeaderUtil.createAlert("userManagement.deleted", login))).build();
   }
}
