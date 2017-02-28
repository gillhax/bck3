package quiz.web.rest.vm;

import io.swagger.annotations.ApiModelProperty;
import java.beans.ConstructorProperties;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

public class ManagedCreateUserVM {
   @Email
   @NotBlank
   @Size(
      min = 5,
      max = 100
   )
   @ApiModelProperty(
      required = true
   )
   private String login;
   @NotBlank
   @Size(
      min = 4,
      max = 100
   )
   @ApiModelProperty(
      required = true
   )
   private String password;
   private Integer avatarId;
   @Size(
      min = 1,
      max = 64
   )
   private String name;

   public String getLogin() {
      return this.login;
   }

   public String getPassword() {
      return this.password;
   }

   public Integer getAvatarId() {
      return this.avatarId;
   }

   public String getName() {
      return this.name;
   }

   public void setLogin(String login) {
      this.login = login;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public void setAvatarId(Integer avatarId) {
      this.avatarId = avatarId;
   }

   public void setName(String name) {
      this.name = name;
   }

   @ConstructorProperties({"login", "password", "avatarId", "name"})
   public ManagedCreateUserVM(String login, String password, Integer avatarId, String name) {
      this.login = login;
      this.password = password;
      this.avatarId = avatarId;
      this.name = name;
   }

   public ManagedCreateUserVM() {
   }
}
