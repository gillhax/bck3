package quiz.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository {
   Optional findOneByTitle(String var1);
}
