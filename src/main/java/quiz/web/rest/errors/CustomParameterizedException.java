package quiz.web.rest.errors;

import quiz.web.rest.errors.ParameterizedErrorVM;

public class CustomParameterizedException extends RuntimeException {
   private static final long serialVersionUID = 1L;
   private final String message;
   private final String[] params;

   public CustomParameterizedException(String message, String... params) {
      super(message);
      this.message = message;
      this.params = params;
   }

   public ParameterizedErrorVM getErrorVM() {
      return new ParameterizedErrorVM(this.message, this.params);
   }
}
