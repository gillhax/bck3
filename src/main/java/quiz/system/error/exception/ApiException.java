package quiz.system.error.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {
   private String error;
   private HttpStatus httpStatus;
   private String devMessage;

   public ApiException(String error, HttpStatus httpStatus) {
      this(error, httpStatus, "");
   }

   public ApiException(String error, HttpStatus httpStatus, String devMessage) {
      this.error = error;
      this.httpStatus = httpStatus;
      this.devMessage = devMessage;
   }

   public String getError() {
      return this.error;
   }

   public HttpStatus getHttpStatus() {
      return this.httpStatus;
   }

   public String getDevMessage() {
      return this.devMessage;
   }

   public void setDevMessage(String devMessage) {
      this.devMessage = devMessage;
   }
}
