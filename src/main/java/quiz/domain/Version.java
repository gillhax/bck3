package quiz.domain;

import java.beans.ConstructorProperties;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(
   name = "version"
)
public class Version {
   @Id
   private Integer id;
   @Column(
      name = "avatars"
   )
   private Timestamp avatars;
   @Column(
      name = "helps"
   )
   private Timestamp helps;
   @Column(
      name = "questions"
   )
   private Timestamp questions;
   @Column(
      name = "categories"
   )
   private Timestamp categories;

   public Integer getId() {
      return this.id;
   }

   public Timestamp getAvatars() {
      return this.avatars;
   }

   public Timestamp getHelps() {
      return this.helps;
   }

   public Timestamp getQuestions() {
      return this.questions;
   }

   public Timestamp getCategories() {
      return this.categories;
   }

   public void setId(Integer id) {
      this.id = id;
   }

   public void setAvatars(Timestamp avatars) {
      this.avatars = avatars;
   }

   public void setHelps(Timestamp helps) {
      this.helps = helps;
   }

   public void setQuestions(Timestamp questions) {
      this.questions = questions;
   }

   public void setCategories(Timestamp categories) {
      this.categories = categories;
   }

   @ConstructorProperties({"id", "avatars", "helps", "questions", "categories"})
   public Version(Integer id, Timestamp avatars, Timestamp helps, Timestamp questions, Timestamp categories) {
      this.id = id;
      this.avatars = avatars;
      this.helps = helps;
      this.questions = questions;
      this.categories = categories;
   }

   public Version() {
   }
}
