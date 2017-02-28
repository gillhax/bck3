package quiz.service.dto;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;
import quiz.domain.Authority;
import quiz.domain.User;

public class UserDTO {
   @Email
   @Size(
      min = 5,
      max = 100
   )
   private String login;
   private boolean activated;
   @Size(
      min = 2,
      max = 5
   )
   private String langKey;
   private Set authorities;

   public UserDTO() {
      this.activated = false;
   }

   public UserDTO(User user) {
      this(user.getLogin(), user.isActivated(), user.getLangKey(), (Set)user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet()));
   }

   public UserDTO(String login, boolean activated, String langKey, Set authorities) {
      this.activated = false;
      this.login = login;
      this.activated = activated;
      this.langKey = langKey;
      this.authorities = authorities;
   }

   public String toString() {
      return "UserDTO{login=\'" + this.login + '\'' + ", activated=" + this.activated + ", langKey=\'" + this.langKey + '\'' + ", authorities=" + this.authorities + "}";
   }

   public String getLogin() {
      return this.login;
   }

   public boolean isActivated() {
      return this.activated;
   }

   public String getLangKey() {
      return this.langKey;
   }

   public Set getAuthorities() {
      return this.authorities;
   }

   public void setLogin(String login) {
      this.login = login;
   }

   public void setActivated(boolean activated) {
      this.activated = activated;
   }

   public void setLangKey(String langKey) {
      this.langKey = langKey;
   }

   public void setAuthorities(Set authorities) {
      this.authorities = authorities;
   }
}
