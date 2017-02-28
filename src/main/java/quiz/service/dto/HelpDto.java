package quiz.service.dto;

import java.beans.ConstructorProperties;
import javax.validation.constraints.Size;

public class HelpDto {
   Integer id;
   @Size(
      min = 1,
      max = 512
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

   public HelpDto() {
   }

   @ConstructorProperties({"id", "path"})
   public HelpDto(Integer id, String path) {
      this.id = id;
      this.path = path;
   }
}
