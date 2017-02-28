package quiz.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersistenceAuditEventRepository extends JpaRepository {
   List findByPrincipal(String var1);

   List findByAuditEventDateAfter(LocalDateTime var1);

   List findByPrincipalAndAuditEventDateAfter(String var1, LocalDateTime var2);

   List findByPrincipalAndAuditEventDateAfterAndAuditEventType(String var1, LocalDateTime var2, String var3);

   Page findAllByAuditEventDateBetween(LocalDateTime var1, LocalDateTime var2, Pageable var3);
}
