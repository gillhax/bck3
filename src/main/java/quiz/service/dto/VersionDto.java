package quiz.service.dto;

import java.beans.ConstructorProperties;

public class VersionDto {
   long avatars;
   long helps;
   long questions;
   long categories;

   public long getAvatars() {
      return this.avatars;
   }

   public long getHelps() {
      return this.helps;
   }

   public long getQuestions() {
      return this.questions;
   }

   public long getCategories() {
      return this.categories;
   }

   public void setAvatars(long avatars) {
      this.avatars = avatars;
   }

   public void setHelps(long helps) {
      this.helps = helps;
   }

   public void setQuestions(long questions) {
      this.questions = questions;
   }

   public void setCategories(long categories) {
      this.categories = categories;
   }

   public VersionDto() {
   }

   @ConstructorProperties({"avatars", "helps", "questions", "categories"})
   public VersionDto(long avatars, long helps, long questions, long categories) {
      this.avatars = avatars;
      this.helps = helps;
      this.questions = questions;
      this.categories = categories;
   }
}
