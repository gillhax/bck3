package quiz.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;

import javax.transaction.Transactional;

@Repository
@org.springframework.transaction.annotation.Transactional(propagation = Propagation.MANDATORY)
public interface AuthorityRepository extends JpaRepository<AuthorityRepository, Long> {
}
