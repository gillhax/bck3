package quiz.web.rest.errors;

import java.io.Serializable;

public class ParameterizedErrorVM implements Serializable {
   private static final long serialVersionUID = 1L;
   private final String message;
   private final String[] params;

   public ParameterizedErrorVM(String message, String... params) {
      this.message = message;
      this.params = params;
   }

   public String getMessage() {
      return this.message;
   }

   public String[] getParams() {
      return this.params;
   }
}
