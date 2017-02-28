package quiz.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import quiz.domain.util.ImageUrlJsonSerializer;

@Entity
@Table(
   name = "avatar"
)
@Cache(
   usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE
)
public class Avatar implements Serializable {
   private static final long serialVersionUID = 1L;
   @Id
   @GeneratedValue(
      strategy = GenerationType.AUTO
   )
   private Integer id;
   @Size(
      min = 1,
      max = 512
   )
   @Column(
      name = "path",
      length = 512
   )
   @JsonSerialize(
      using = ImageUrlJsonSerializer.class
   )
   private String path;

   public Integer getId() {
      return this.id;
   }

   public void setId(Integer id) {
      this.id = id;
   }

   public String getPath() {
      return this.path;
   }

   public Avatar path(String path) {
      this.path = path;
      return this;
   }

   public void setPath(String path) {
      this.path = path;
   }

   public boolean equals(Object o) {
      if(this == o) {
         return true;
      } else if(o != null && this.getClass() == o.getClass()) {
         Avatar avatar = (Avatar)o;
         return avatar.id != null && this.id != null?Objects.equals(this.id, avatar.id):false;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.id);
   }

   public String toString() {
      return "Avatar{id=" + this.id + ", path=\'" + this.path + "\'" + '}';
   }
}
