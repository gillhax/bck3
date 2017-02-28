package quiz.system.error.handler.dto;

public class ResponseDto {
   int status = 200;
   Object data;

   public ResponseDto() {
   }

   public ResponseDto(Object value) {
      this.data = value;
   }

   public int getStatus() {
      return this.status;
   }

   public Object getData() {
      return this.data;
   }

   public void setStatus(int status) {
      this.status = status;
   }

   public void setData(Object data) {
      this.data = data;
   }
}
