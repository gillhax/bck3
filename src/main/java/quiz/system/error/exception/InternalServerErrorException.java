package quiz.system.error.exception;

import org.springframework.http.HttpStatus;
import quiz.system.error.exception.ApiException;

public class InternalServerErrorException extends ApiException {
   public InternalServerErrorException() {
      super(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), HttpStatus.INTERNAL_SERVER_ERROR);
   }

   public InternalServerErrorException(String message) {
      super(message, HttpStatus.INTERNAL_SERVER_ERROR);
   }

   public InternalServerErrorException(String message, String devMessage) {
      super(message, HttpStatus.INTERNAL_SERVER_ERROR, devMessage);
   }
}
