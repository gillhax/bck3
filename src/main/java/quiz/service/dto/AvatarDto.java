package quiz.service.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.beans.ConstructorProperties;
import javax.validation.constraints.Size;
import quiz.domain.util.ImageUrlJsonSerializer;

public class AvatarDto {
   Integer id;
   @Size(
      min = 1,
      max = 512
   )
   @JsonSerialize(
      using = ImageUrlJsonSerializer.class
   )
   String path;

   public Integer getId() {
      return this.id;
   }

   public String getPath() {
      return this.path;
   }

   public void setId(Integer id) {
      this.id = id;
   }

   public void setPath(String path) {
      this.path = path;
   }

   public AvatarDto() {
   }

   @ConstructorProperties({"id", "path"})
   public AvatarDto(Integer id, String path) {
      this.id = id;
      this.path = path;
   }
}
