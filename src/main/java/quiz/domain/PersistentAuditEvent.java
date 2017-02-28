package quiz.domain;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(
   name = "jhi_persistent_audit_event"
)
public class PersistentAuditEvent {
   @Id
   @GeneratedValue(
      strategy = GenerationType.AUTO
   )
   @Column(
      name = "event_id"
   )
   private Long id;
   @NotNull
   @Column(
      nullable = false
   )
   private String principal;
   @Column(
      name = "event_date"
   )
   private LocalDateTime auditEventDate;
   @Column(
      name = "event_type"
   )
   private String auditEventType;
   @ElementCollection
   @MapKeyColumn(
      name = "name"
   )
   @Column(
      name = "value"
   )
   @CollectionTable(
      name = "jhi_persistent_audit_evt_data",
      joinColumns = {         @JoinColumn(
            name = "event_id"
         )}
   )
   private Map data = new HashMap();

   public Long getId() {
      return this.id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getPrincipal() {
      return this.principal;
   }

   public void setPrincipal(String principal) {
      this.principal = principal;
   }

   public LocalDateTime getAuditEventDate() {
      return this.auditEventDate;
   }

   public void setAuditEventDate(LocalDateTime auditEventDate) {
      this.auditEventDate = auditEventDate;
   }

   public String getAuditEventType() {
      return this.auditEventType;
   }

   public void setAuditEventType(String auditEventType) {
      this.auditEventType = auditEventType;
   }

   public Map getData() {
      return this.data;
   }

   public void setData(Map data) {
      this.data = data;
   }
}
