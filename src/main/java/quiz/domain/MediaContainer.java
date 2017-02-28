package quiz.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.annotation.JsonIgnore;
import quiz.domain.MediaType;
import quiz.domain.util.ImageUrlJsonSerializer;

@Entity
@Table(
   name = "media_container"
)
@Cache(
   usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE
)
public class MediaContainer implements Serializable {
   @Id
   @GeneratedValue(
      strategy = GenerationType.AUTO
   )
   private Long id;
   @NotNull
   @Column(
      name = "media_type",
      length = 20
   )
   @Enumerated(EnumType.STRING)
   private MediaType mediaType;
   @Size(
      min = 1,
      max = 512
   )
   @NotNull
   @Column(
      name = "media",
      length = 512
   )
   @JsonSerialize(
      using = ImageUrlJsonSerializer.class
   )
   private String media;
   @OneToMany(
      mappedBy = "media",
      fetch = FetchType.LAZY
   )
   @JsonIgnore
   private List questions;

   public Long getId() {
      return this.id;
   }

   public MediaType getMediaType() {
      return this.mediaType;
   }

   public String getMedia() {
      return this.media;
   }

   public List getQuestions() {
      return this.questions;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public void setMediaType(MediaType mediaType) {
      this.mediaType = mediaType;
   }

   public void setMedia(String media) {
      this.media = media;
   }

   public void setQuestions(List questions) {
      this.questions = questions;
   }

   @ConstructorProperties({"id", "mediaType", "media", "questions"})
   public MediaContainer(Long id, MediaType mediaType, String media, List questions) {
      this.id = id;
      this.mediaType = mediaType;
      this.media = media;
      this.questions = questions;
   }

   public MediaContainer() {
   }
}
