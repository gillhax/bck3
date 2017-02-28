package quiz.system.error.handler.dto;

public class ErrorDto {
   long timestamp = System.currentTimeMillis();
   int status;
   String error;
   String exception;
   String message;
   String devMessage;
   String path;

   public ErrorDto(int status, String error, String exception, String message, String devMessage, String path) {
      this.status = status;
      this.error = error;
      this.exception = exception;
      this.message = message;
      this.devMessage = devMessage;
      this.path = path;
   }

   public void setTimestamp(long timestamp) {
      this.timestamp = timestamp;
   }

   public void setStatus(int status) {
      this.status = status;
   }

   public void setError(String error) {
      this.error = error;
   }

   public void setException(String exception) {
      this.exception = exception;
   }

   public void setMessage(String message) {
      this.message = message;
   }

   public void setDevMessage(String devMessage) {
      this.devMessage = devMessage;
   }

   public void setPath(String path) {
      this.path = path;
   }

   public long getTimestamp() {
      return this.timestamp;
   }

   public int getStatus() {
      return this.status;
   }

   public String getError() {
      return this.error;
   }

   public String getException() {
      return this.exception;
   }

   public String getMessage() {
      return this.message;
   }

   public String getDevMessage() {
      return this.devMessage;
   }

   public String getPath() {
      return this.path;
   }
}
