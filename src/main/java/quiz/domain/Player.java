package quiz.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.annotation.JsonIgnore;
import quiz.domain.Avatar;
import quiz.domain.User;

@Entity
@Table(
   name = "player"
)
@Cache(
   usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE
)
public class Player implements Serializable {
   private static final long serialVersionUID = 1L;
   @OneToOne(
      fetch = FetchType.LAZY,
      optional = false
   )
   @PrimaryKeyJoinColumn
   @JsonIgnore
   private User user;
   @Id
   private Long id;
   @Size(
      min = 1,
      max = 64
   )
   @Column(
      name = "name",
      length = 64
   )
   private String name;
   @Column(
      name = "score"
   )
   private Long score;
   @ManyToOne
   private Avatar avatar;

   public boolean equals(Object o) {
      if(this == o) {
         return true;
      } else if(o != null && this.getClass() == o.getClass()) {
         Player player = (Player)o;
         return player.id != null && this.id != null?Objects.equals(this.id, player.id):false;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.id);
   }

   public User getUser() {
      return this.user;
   }

   public Long getId() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public Long getScore() {
      return this.score;
   }

   public Avatar getAvatar() {
      return this.avatar;
   }

   public void setUser(User user) {
      this.user = user;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setScore(Long score) {
      this.score = score;
   }

   public void setAvatar(Avatar avatar) {
      this.avatar = avatar;
   }
}
