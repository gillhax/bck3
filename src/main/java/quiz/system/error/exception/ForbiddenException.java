package quiz.system.error.exception;

import org.springframework.http.HttpStatus;
import quiz.system.error.exception.ApiException;

public class ForbiddenException extends ApiException {
   public ForbiddenException() {
      super(HttpStatus.FORBIDDEN.getReasonPhrase(), HttpStatus.FORBIDDEN);
   }

   public ForbiddenException(String devMessage) {
      super(HttpStatus.FORBIDDEN.getReasonPhrase(), HttpStatus.FORBIDDEN);
      this.setDevMessage(devMessage);
   }
}
