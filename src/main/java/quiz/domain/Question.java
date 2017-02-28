package quiz.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import quiz.domain.MediaContainer;
import quiz.domain.Subcategory;

@Entity
@Table(
   name = "question"
)
@Cache(
   usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE
)
public class Question implements Serializable {
   private static final long serialVersionUID = 1L;
   @Id
   @GeneratedValue(
      strategy = GenerationType.AUTO
   )
   private Long id;
   @Column(
      name = "version"
   )
   private Timestamp version;
   @NotNull
   @Size(
      min = 1,
      max = 255
   )
   @Column(
      name = "title",
      length = 255,
      nullable = false
   )
   private String title;
   @ManyToOne(
      targetEntity = MediaContainer.class
   )
   @JoinColumn(
      name = "media_id"
   )
   MediaContainer media;
   @NotNull
   @Size(
      min = 1,
      max = 64
   )
   @Column(
      name = "answer_1",
      length = 64,
      nullable = false
   )
   private String answer1;
   @NotNull
   @Size(
      min = 1,
      max = 64
   )
   @Column(
      name = "answer_2",
      length = 64,
      nullable = false
   )
   private String answer2;
   @NotNull
   @Size(
      min = 1,
      max = 64
   )
   @Column(
      name = "answer_3",
      length = 64,
      nullable = false
   )
   private String answer3;
   @NotNull
   @Size(
      min = 1,
      max = 64
   )
   @Column(
      name = "answer_4",
      length = 64,
      nullable = false
   )
   private String answer4;
   @Min(1L)
   @Max(4L)
   @NotNull
   @Column(
      name = "right_answer",
      nullable = false
   )
   private Integer rightAnswer;
   @ManyToOne(
      targetEntity = Subcategory.class
   )
   @NotNull
   @JoinColumn(
      name = "subcategory_id"
   )
   private Subcategory subcategory;

   public boolean equals(Object o) {
      if(this == o) {
         return true;
      } else if(o != null && this.getClass() == o.getClass()) {
         Question question = (Question)o;
         return question.id != null && this.id != null?Objects.equals(this.id, question.id):false;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.id);
   }

   public String toString() {
      return "Question{id=" + this.id + ", version=\'" + this.version + "\', title=\'" + this.title + "\', answer1=\'" + this.answer1 + "\', answer2=\'" + this.answer2 + "\', answer3=\'" + this.answer3 + "\', answer4=\'" + this.answer4 + "\', rightAnswer=\'" + this.rightAnswer + "\'" + '}';
   }

   public Long getId() {
      return this.id;
   }

   public Timestamp getVersion() {
      return this.version;
   }

   public String getTitle() {
      return this.title;
   }

   public MediaContainer getMedia() {
      return this.media;
   }

   public String getAnswer1() {
      return this.answer1;
   }

   public String getAnswer2() {
      return this.answer2;
   }

   public String getAnswer3() {
      return this.answer3;
   }

   public String getAnswer4() {
      return this.answer4;
   }

   public Integer getRightAnswer() {
      return this.rightAnswer;
   }

   public Subcategory getSubcategory() {
      return this.subcategory;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public void setVersion(Timestamp version) {
      this.version = version;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public void setMedia(MediaContainer media) {
      this.media = media;
   }

   public void setAnswer1(String answer1) {
      this.answer1 = answer1;
   }

   public void setAnswer2(String answer2) {
      this.answer2 = answer2;
   }

   public void setAnswer3(String answer3) {
      this.answer3 = answer3;
   }

   public void setAnswer4(String answer4) {
      this.answer4 = answer4;
   }

   public void setRightAnswer(Integer rightAnswer) {
      this.rightAnswer = rightAnswer;
   }

   public void setSubcategory(Subcategory subcategory) {
      this.subcategory = subcategory;
   }
}
