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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(
   name = "category"
)
@Cache(
   usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE
)
public class Category implements Serializable {
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
   @OneToMany(
      mappedBy = "category",
      fetch = FetchType.LAZY
   )
   private List subcategories;

   public boolean equals(Object o) {
      if(this == o) {
         return true;
      } else if(o != null && this.getClass() == o.getClass()) {
         Category category = (Category)o;
         return category.id != null && this.id != null?Objects.equals(this.id, category.id):false;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.id);
   }

   public String toString() {
      return "Category{id=" + this.id + ", name=\'" + this.name + "\'" + '}';
   }

   public Long getId() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public List getSubcategories() {
      return this.subcategories;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setSubcategories(List subcategories) {
      this.subcategories = subcategories;
   }

   @ConstructorProperties({"id", "name", "subcategories"})
   public Category(Long id, String name, List subcategories) {
      this.id = id;
      this.name = name;
      this.subcategories = subcategories;
   }

   public Category() {
   }
}
