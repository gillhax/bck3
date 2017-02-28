package quiz.repository;

import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import quiz.domain.SocialUserConnection;

public interface SocialUserConnectionRepository extends JpaRepository<SocialUserConnection, Long> {
   List<SocialUserConnection> findAllByProviderIdAndProviderUserId(String var1, String var2);

   List<SocialUserConnection> findAllByProviderIdAndProviderUserIdIn(String var1, Set var2);

   List<SocialUserConnection> findAllByUserIdOrderByProviderIdAscRankAsc(String var1);

   List<SocialUserConnection> findAllByUserIdAndProviderIdOrderByRankAsc(String var1, String var2);

   List<SocialUserConnection> findAllByUserIdAndProviderIdAndProviderUserIdIn(String var1, String var2, List var3);

   SocialUserConnection findOneByUserIdAndProviderIdAndProviderUserId(String var1, String var2, String var3);

   void deleteByUserIdAndProviderId(String var1, String var2);

   void deleteByUserIdAndProviderIdAndProviderUserId(String var1, String var2, String var3);
}
