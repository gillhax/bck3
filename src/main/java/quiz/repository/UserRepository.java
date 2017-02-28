package quiz.repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import quiz.domain.User;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findOneByActivationKey(String var1);

    List<User> findAllByActivatedIsFalseAndCreatedDateBefore(ZonedDateTime var1);

    Optional<User> findOneByResetKey(String var1);

    Optional<User> findOneByLogin(String var1);

    @Query("select u.id from User u where u.login = :login")
    Long findIdByLogin(@Param("login") String var1);

    @Query(
        value = "select distinct user from User user left join fetch user.authorities",
        countQuery = "select count(user) from User user"
    )
    Page<User> findAllWithAuthorities(Pageable var1);
}
