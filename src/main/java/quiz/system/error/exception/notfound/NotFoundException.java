package quiz.system.error.exception.notfound;

import org.springframework.http.HttpStatus;
import quiz.system.error.exception.ApiException;

public class NotFoundException extends ApiException {
   public NotFoundException(String error, String devMessage) {
      super(error, HttpStatus.NOT_FOUND, devMessage);
   }

   public NotFoundException(String error) {
      super(error, HttpStatus.NOT_FOUND);
   }

   public NotFoundException() {
      super("not-fount.entity", HttpStatus.NOT_FOUND);
   }
}
