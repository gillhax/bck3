package quiz.service.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.beans.ConstructorProperties;
import java.util.Set;
import javax.validation.constraints.Size;
import quiz.domain.util.ImageUrlJsonSerializer;

public class QuestionDto {
   Long id;
   @Size(
      min = 1,
      max = 255
   )
   String title;
   String category;
   long version;
   @Size(
      min = 1,
      max = 512
   )
   @JsonSerialize(
      using = ImageUrlJsonSerializer.class
   )
   String media;
   String mediaType;
   Set answers;

   public Long getId() {
      return this.id;
   }

   public String getTitle() {
      return this.title;
   }

   public String getCategory() {
      return this.category;
   }

   public long getVersion() {
      return this.version;
   }

   public String getMedia() {
      return this.media;
   }

   public String getMediaType() {
      return this.mediaType;
   }

   public Set getAnswers() {
      return this.answers;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public void setCategory(String category) {
      this.category = category;
   }

   public void setVersion(long version) {
      this.version = version;
   }

   public void setMedia(String media) {
      this.media = media;
   }

   public void setMediaType(String mediaType) {
      this.mediaType = mediaType;
   }

   public void setAnswers(Set answers) {
      this.answers = answers;
   }

   public QuestionDto() {
   }

   @ConstructorProperties({"id", "title", "category", "version", "media", "mediaType", "answers"})
   public QuestionDto(Long id, String title, String category, long version, String media, String mediaType, Set answers) {
      this.id = id;
      this.title = title;
      this.category = category;
      this.version = version;
      this.media = media;
      this.mediaType = mediaType;
      this.answers = answers;
   }
}
