package quiz.web.rest.util;

import java.beans.ConstructorProperties;
import java.util.List;

public class FilesUpload {
   private List files;

   public List getFiles() {
      return this.files;
   }

   public void setFiles(List files) {
      this.files = files;
   }

   public FilesUpload() {
   }

   @ConstructorProperties({"files"})
   public FilesUpload(List files) {
      this.files = files;
   }
}
