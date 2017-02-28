package quiz.system.error.exception;

import org.springframework.http.HttpStatus;
import quiz.system.error.exception.ApiException;

public class UnauthorizedException extends ApiException {
   public UnauthorizedException() {
      super(HttpStatus.UNAUTHORIZED.getReasonPhrase(), HttpStatus.UNAUTHORIZED);
   }

   public UnauthorizedException(String message) {
      super(message, HttpStatus.UNAUTHORIZED);
   }
}
