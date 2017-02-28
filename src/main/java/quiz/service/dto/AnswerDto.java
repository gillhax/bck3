package quiz.service.dto;

import java.beans.ConstructorProperties;
import javax.validation.constraints.Size;

public class AnswerDto {
   @Size(
      min = 1,
      max = 64
   )
   String title;
   boolean right = false;

   public AnswerDto(String answer) {
      this.title = answer;
   }

   public String getTitle() {
      return this.title;
   }

   public boolean isRight() {
      return this.right;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public void setRight(boolean right) {
      this.right = right;
   }

   public AnswerDto() {
   }

   @ConstructorProperties({"title", "right"})
   public AnswerDto(String title, boolean right) {
      this.title = title;
      this.right = right;
   }
}
