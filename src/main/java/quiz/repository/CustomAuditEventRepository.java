package quiz.repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import quiz.config.audit.AuditEventConverter;
import quiz.domain.PersistentAuditEvent;
import quiz.repository.PersistenceAuditEventRepository;

@Repository
public class CustomAuditEventRepository implements AuditEventRepository {
   private static final String AUTHORIZATION_FAILURE = "AUTHORIZATION_FAILURE";
   private static final String ANONYMOUS_USER = "anonymoususer";
   @Inject
   private PersistenceAuditEventRepository persistenceAuditEventRepository;
   @Inject
   private AuditEventConverter auditEventConverter;

   public List find(Date after) {
      List persistentAuditEvents = this.persistenceAuditEventRepository.findByAuditEventDateAfter(LocalDateTime.from(after.toInstant()));
      return this.auditEventConverter.convertToAuditEvent((Iterable)persistentAuditEvents);
   }

   public List find(String principal, Date after) {
      List persistentAuditEvents;
      if(principal == null && after == null) {
         persistentAuditEvents = this.persistenceAuditEventRepository.findAll();
      } else if(after == null) {
         persistentAuditEvents = this.persistenceAuditEventRepository.findByPrincipal(principal);
      } else {
         persistentAuditEvents = this.persistenceAuditEventRepository.findByPrincipalAndAuditEventDateAfter(principal, LocalDateTime.from(after.toInstant()));
      }

      return this.auditEventConverter.convertToAuditEvent((Iterable)persistentAuditEvents);
   }

   public List find(String principal, Date after, String type) {
      List persistentAuditEvents = this.persistenceAuditEventRepository.findByPrincipalAndAuditEventDateAfterAndAuditEventType(principal, LocalDateTime.from(after.toInstant()), type);
      return this.auditEventConverter.convertToAuditEvent((Iterable)persistentAuditEvents);
   }

   @Transactional(
      propagation = Propagation.REQUIRES_NEW
   )
   public void add(AuditEvent event) {
      if(!"AUTHORIZATION_FAILURE".equals(event.getType()) && !"anonymoususer".equals(event.getPrincipal())) {
         PersistentAuditEvent persistentAuditEvent = new PersistentAuditEvent();
         persistentAuditEvent.setPrincipal(event.getPrincipal());
         persistentAuditEvent.setAuditEventType(event.getType());
         Instant instant = Instant.ofEpochMilli(event.getTimestamp().getTime());
         persistentAuditEvent.setAuditEventDate(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()));
         persistentAuditEvent.setData(this.auditEventConverter.convertDataToStrings(event.getData()));
         this.persistenceAuditEventRepository.save(persistentAuditEvent);
      }

   }
}
