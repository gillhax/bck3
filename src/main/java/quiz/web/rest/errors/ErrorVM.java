package quiz.web.rest.errors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import quiz.web.rest.errors.FieldErrorVM;

public class ErrorVM implements Serializable {
   private static final long serialVersionUID = 1L;
   private final String message;
   private final String description;
   private List fieldErrors;

   public ErrorVM(String message) {
      this(message, (String)null);
   }

   public ErrorVM(String message, String description) {
      this.message = message;
      this.description = description;
   }

   public ErrorVM(String message, String description, List fieldErrors) {
      this.message = message;
      this.description = description;
      this.fieldErrors = fieldErrors;
   }

   public void add(String objectName, String field, String message) {
      if(this.fieldErrors == null) {
         this.fieldErrors = new ArrayList();
      }

      this.fieldErrors.add(new FieldErrorVM(objectName, field, message));
   }

   public String getMessage() {
      return this.message;
   }

   public String getDescription() {
      return this.description;
   }

   public List getFieldErrors() {
      return this.fieldErrors;
   }
}
