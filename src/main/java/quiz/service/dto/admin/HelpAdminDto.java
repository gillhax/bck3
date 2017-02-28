package quiz.service.dto.admin;

import java.beans.ConstructorProperties;
import quiz.domain.Help;

public class HelpAdminDto extends Help {
   String file;

   public String getFile() {
      return this.file;
   }

   public void setFile(String file) {
      this.file = file;
   }

   @ConstructorProperties({"file"})
   public HelpAdminDto(String file) {
      this.file = file;
   }

   public HelpAdminDto() {
   }
}
