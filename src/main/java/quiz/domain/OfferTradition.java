package quiz.domain;

import java.beans.ConstructorProperties;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import quiz.domain.User;

@Entity
@Table(
   name = "offer_tradition"
)
public class OfferTradition {
   @Id
   @GeneratedValue(
      strategy = GenerationType.AUTO
   )
   private Long id;
   @ManyToOne(
      fetch = FetchType.LAZY
   )
   private User user;
   @Size(
      min = 1,
      max = 5120
   )
   @Column(
      columnDefinition = "TEXT",
      name = "text",
      length = 5120
   )
   private String text;
   @Column(
      name = "date"
   )
   @Temporal(TemporalType.TIMESTAMP)
   private Date date;
   private boolean withAttaches;
   @OneToMany(
      mappedBy = "offerId",
      fetch = FetchType.LAZY
   )
   List attaches;

   public Long getId() {
      return this.id;
   }

   public User getUser() {
      return this.user;
   }

   public String getText() {
      return this.text;
   }

   public Date getDate() {
      return this.date;
   }

   public boolean isWithAttaches() {
      return this.withAttaches;
   }

   public List getAttaches() {
      return this.attaches;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public void setUser(User user) {
      this.user = user;
   }

   public void setText(String text) {
      this.text = text;
   }

   public void setDate(Date date) {
      this.date = date;
   }

   public void setWithAttaches(boolean withAttaches) {
      this.withAttaches = withAttaches;
   }

   public void setAttaches(List attaches) {
      this.attaches = attaches;
   }

   @ConstructorProperties({"id", "user", "text", "date", "withAttaches", "attaches"})
   public OfferTradition(Long id, User user, String text, Date date, boolean withAttaches, List attaches) {
      this.id = id;
      this.user = user;
      this.text = text;
      this.date = date;
      this.withAttaches = withAttaches;
      this.attaches = attaches;
   }

   public OfferTradition() {
   }
}
