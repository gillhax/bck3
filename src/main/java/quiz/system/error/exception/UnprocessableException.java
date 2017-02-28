package quiz.system.error.exception;

import org.springframework.http.HttpStatus;
import quiz.system.error.exception.ApiException;

public class UnprocessableException extends ApiException {
   public UnprocessableException() {
      super(HttpStatus.FORBIDDEN.getReasonPhrase(), HttpStatus.UNPROCESSABLE_ENTITY);
   }

   public UnprocessableException(String message) {
      super(message, HttpStatus.UNPROCESSABLE_ENTITY);
   }
}
