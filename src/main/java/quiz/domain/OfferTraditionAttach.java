package quiz.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.beans.ConstructorProperties;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import quiz.domain.util.ImageUrlJsonSerializer;

@Entity
@Table(
   name = "offer_tradition_attach"
)
public class OfferTraditionAttach {
   @Id
   @GeneratedValue(
      strategy = GenerationType.AUTO
   )
   private Long id;
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
   @JsonIgnore
   @Column(
      name = "offer_id"
   )
   private Long offerId;

   public Long getId() {
      return this.id;
   }

   public String getPath() {
      return this.path;
   }

   public Long getOfferId() {
      return this.offerId;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public void setPath(String path) {
      this.path = path;
   }

   public void setOfferId(Long offerId) {
      this.offerId = offerId;
   }

   @ConstructorProperties({"id", "path", "offerId"})
   public OfferTraditionAttach(Long id, String path, Long offerId) {
      this.id = id;
      this.path = path;
      this.offerId = offerId;
   }

   public OfferTraditionAttach() {
   }
}
