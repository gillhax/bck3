package quiz.system.error.exception;

import org.springframework.http.HttpStatus;
import quiz.system.error.exception.ApiException;

public class SecurityUserException extends ApiException {
   public SecurityUserException(String error) {
      super(error, HttpStatus.BAD_REQUEST);
   }
}
