package quiz.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Function;
import javax.inject.Inject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import quiz.config.audit.AuditEventConverter;
import quiz.repository.PersistenceAuditEventRepository;

@Service
@Transactional
public class AuditEventService {
   private PersistenceAuditEventRepository persistenceAuditEventRepository;
   private AuditEventConverter auditEventConverter;

   @Inject
   public AuditEventService(PersistenceAuditEventRepository persistenceAuditEventRepository, AuditEventConverter auditEventConverter) {
      this.persistenceAuditEventRepository = persistenceAuditEventRepository;
      this.auditEventConverter = auditEventConverter;
   }

   public Page findAll(Pageable pageable) {
      Page var10000 = this.persistenceAuditEventRepository.findAll(pageable);
      AuditEventConverter var10001 = this.auditEventConverter;
      this.auditEventConverter.getClass();
      return var10000.map(var10001::convertToAuditEvent);
   }

   public Page findByDates(LocalDateTime fromDate, LocalDateTime toDate, Pageable pageable) {
      Page var10000 = this.persistenceAuditEventRepository.findAllByAuditEventDateBetween(fromDate, toDate, pageable);
      AuditEventConverter var10001 = this.auditEventConverter;
      this.auditEventConverter.getClass();
      return var10000.map(var10001::convertToAuditEvent);
   }

   public Optional find(Long id) {
      Optional var10000 = Optional.ofNullable(this.persistenceAuditEventRepository.findOne(id));
      AuditEventConverter var10001 = this.auditEventConverter;
      this.auditEventConverter.getClass();
      return var10000.map(var10001::convertToAuditEvent);
   }
}
