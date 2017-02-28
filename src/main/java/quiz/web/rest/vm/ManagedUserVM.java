package quiz.web.rest.vm;

import java.time.ZonedDateTime;
import java.util.Set;
import javax.validation.constraints.Size;
import quiz.domain.User;
import quiz.service.dto.UserDTO;

public class ManagedUserVM extends UserDTO {
   public static final int PASSWORD_MIN_LENGTH = 4;
   public static final int PASSWORD_MAX_LENGTH = 100;
   private Long id;
   private String createdBy;
   private ZonedDateTime createdDate;
   private String lastModifiedBy;
   private ZonedDateTime lastModifiedDate;
   @Size(
      min = 4,
      max = 100
   )
   private String password;

   public ManagedUserVM() {
   }

   public ManagedUserVM(User user) {
      super(user);
      this.id = user.getId();
      this.createdBy = user.getCreatedBy();
      this.createdDate = user.getCreatedDate();
      this.lastModifiedBy = user.getLastModifiedBy();
      this.lastModifiedDate = user.getLastModifiedDate();
      this.password = null;
   }

   public ManagedUserVM(Long id, String login, String password, boolean activated, String langKey, Set authorities, String createdBy, ZonedDateTime createdDate, String lastModifiedBy, ZonedDateTime lastModifiedDate) {
      super(login, activated, langKey, authorities);
      this.id = id;
      this.createdBy = createdBy;
      this.createdDate = createdDate;
      this.lastModifiedBy = lastModifiedBy;
      this.lastModifiedDate = lastModifiedDate;
      this.password = password;
   }

   public String toString() {
      return "ManagedUserVM{id=" + this.id + ", createdBy=" + this.createdBy + ", createdDate=" + this.createdDate + ", lastModifiedBy=\'" + this.lastModifiedBy + '\'' + ", lastModifiedDate=" + this.lastModifiedDate + "} " + super.toString();
   }

   public Long getId() {
      return this.id;
   }

   public String getCreatedBy() {
      return this.createdBy;
   }

   public ZonedDateTime getCreatedDate() {
      return this.createdDate;
   }

   public String getLastModifiedBy() {
      return this.lastModifiedBy;
   }

   public ZonedDateTime getLastModifiedDate() {
      return this.lastModifiedDate;
   }

   public String getPassword() {
      return this.password;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public void setCreatedBy(String createdBy) {
      this.createdBy = createdBy;
   }

   public void setCreatedDate(ZonedDateTime createdDate) {
      this.createdDate = createdDate;
   }

   public void setLastModifiedBy(String lastModifiedBy) {
      this.lastModifiedBy = lastModifiedBy;
   }

   public void setLastModifiedDate(ZonedDateTime lastModifiedDate) {
      this.lastModifiedDate = lastModifiedDate;
   }

   public void setPassword(String password) {
      this.password = password;
   }
}
