package quiz.system.error.handler.dto;

import java.beans.ConstructorProperties;
import java.util.List;
import quiz.system.error.handler.dto.ErrorDto;

public class ValidationErrorDto extends ErrorDto {
   private List valid;

   public ValidationErrorDto(int status, String error, String exception, String message, String devMessage, String path, List valid) {
      super(status, error, exception, message, devMessage, path);
      this.valid = valid;
   }

   public void setValid(List valid) {
      this.valid = valid;
   }

   public List getValid() {
      return this.valid;
   }

   public static class ValidationErrorMessage {
      String field;
      String message;

      public void setField(String field) {
         this.field = field;
      }

      public void setMessage(String message) {
         this.message = message;
      }

      public String getField() {
         return this.field;
      }

      public String getMessage() {
         return this.message;
      }

      @ConstructorProperties({"field", "message"})
      public ValidationErrorMessage(String field, String message) {
         this.field = field;
         this.message = message;
      }
   }
}
