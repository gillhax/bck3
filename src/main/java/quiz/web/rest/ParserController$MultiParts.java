package quiz.web.rest;

import java.beans.ConstructorProperties;
import java.util.List;
import quiz.web.rest.ParserController;

class ParserController$MultiParts {
   private List files;
   // $FF: synthetic field
   final ParserController this$0;

   public List getFiles() {
      return this.files;
   }

   public void setFiles(List files) {
      this.files = files;
   }

   @ConstructorProperties({"files"})
   public ParserController$MultiParts(ParserController var1, List files) {
      this.this$0 = var1;
      this.files = files;
   }
}
