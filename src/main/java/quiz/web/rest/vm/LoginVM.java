package quiz.web.rest.vm;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class LoginVM {
   @Pattern(
      regexp = "^[_\'.@A-Za-z0-9-]*$"
   )
   @NotNull
   @Size(
      min = 1,
      max = 50
   )
   private String username;
   @NotNull
   @Size(
      min = 4,
      max = 100
   )
   private String password;
   private Boolean rememberMe;

   public String toString() {
      return "LoginVM{password=\'*****\', username=\'" + this.username + '\'' + ", rememberMe=" + this.rememberMe + '}';
   }

   public String getUsername() {
      return this.username;
   }

   public String getPassword() {
      return this.password;
   }

   public Boolean getRememberMe() {
      return this.rememberMe;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public void setRememberMe(Boolean rememberMe) {
      this.rememberMe = rememberMe;
   }
}
