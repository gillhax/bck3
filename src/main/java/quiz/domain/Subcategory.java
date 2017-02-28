package quiz.domain;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import quiz.domain.Category;
import quiz.domain.Question;

@Entity
@Table(
   name = "subcategory"
)
@Cache(
   usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE
)
public class Subcategory implements Serializable {
   private static final long serialVersionUID = 1L;
   @Id
   @GeneratedValue(
      strategy = GenerationType.AUTO
   )
   private Long id;
   @NotNull
   @Size(
      min = 1,
      max = 64
   )
   @Column(
      name = "name",
      length = 64,
      nullable = false
   )
   private String name;
   @ManyToOne(
      targetEntity = Category.class
   )
   @NotNull
   private Category category;
   @OneToMany(
      mappedBy = "subcategory",
      fetch = FetchType.LAZY,
      targetEntity = Question.class
   )
   private List questions;

   public boolean equals(Object o) {
      if(this == o) {
         return true;
      } else if(o != null && this.getClass() == o.getClass()) {
         Subcategory subcategory = (Subcategory)o;
         return subcategory.id != null && this.id != null?Objects.equals(this.id, subcategory.id):false;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.id);
   }

   public String toString() {
      return "Subcategory{id=" + this.id + ", name=\'" + this.name + "\'" + '}';
   }

   public Long getId() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public Category getCategory() {
      return this.category;
   }

   public List getQuestions() {
      return this.questions;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setCategory(Category category) {
      this.category = category;
   }

   public void setQuestions(List questions) {
      this.questions = questions;
   }

   @ConstructorProperties({"id", "name", "category", "questions"})
   public Subcategory(Long id, String name, Category category, List questions) {
      this.id = id;
      this.name = name;
      this.category = category;
      this.questions = questions;
   }

   public Subcategory() {
   }
}
