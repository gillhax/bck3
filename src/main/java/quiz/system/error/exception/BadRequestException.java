package quiz.system.error.exception;

import org.springframework.http.HttpStatus;
import quiz.system.error.exception.ApiException;

public class BadRequestException extends ApiException {
   public BadRequestException() {
      super(HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST);
   }

   public BadRequestException(String message) {
      super(message, HttpStatus.BAD_REQUEST);
   }

   public BadRequestException(String message, String devMessage) {
      super(message, HttpStatus.BAD_REQUEST, devMessage);
   }
}
