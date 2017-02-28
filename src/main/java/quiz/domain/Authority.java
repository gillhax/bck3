package quiz.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(
   name = "jhi_authority"
)
@Cache(
   usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE
)
public class Authority implements Serializable {
   private static final long serialVersionUID = 1L;
   @NotNull
   @Size(
      min = 0,
      max = 50
   )
   @Id
   @Column(
      length = 50
   )
   private String name;

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public boolean equals(Object o) {
      if(this == o) {
         return true;
      } else if(o != null && this.getClass() == o.getClass()) {
         Authority authority = (Authority)o;
         if(this.name != null) {
            if(!this.name.equals(authority.name)) {
               return false;
            }
         } else if(authority.name != null) {
            return false;
         }

         return true;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.name != null?this.name.hashCode():0;
   }

   public String toString() {
      return "Authority{name=\'" + this.name + '\'' + "}";
   }
}
