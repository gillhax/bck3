package quiz.service.dto;

import java.beans.ConstructorProperties;
import javax.validation.constraints.Size;

public class PlayerDtoIn {
   @Size(
      min = 1,
      max = 64
   )
   String name;
   Integer avatarId;

   public String getName() {
      return this.name;
   }

   public Integer getAvatarId() {
      return this.avatarId;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setAvatarId(Integer avatarId) {
      this.avatarId = avatarId;
   }

   public PlayerDtoIn() {
   }

   @ConstructorProperties({"name", "avatarId"})
   public PlayerDtoIn(String name, Integer avatarId) {
      this.name = name;
      this.avatarId = avatarId;
   }
}
