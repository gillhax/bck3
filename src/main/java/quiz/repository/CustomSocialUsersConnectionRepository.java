package quiz.repository;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import quiz.domain.SocialUserConnection;
import quiz.repository.CustomSocialConnectionRepository;
import quiz.repository.SocialUserConnectionRepository;
@SuppressWarnings("unchecked")
public class CustomSocialUsersConnectionRepository implements UsersConnectionRepository {
   private SocialUserConnectionRepository socialUserConnectionRepository;
   private ConnectionFactoryLocator connectionFactoryLocator;

   public CustomSocialUsersConnectionRepository(SocialUserConnectionRepository socialUserConnectionRepository, ConnectionFactoryLocator connectionFactoryLocator) {
      this.socialUserConnectionRepository = socialUserConnectionRepository;
      this.connectionFactoryLocator = connectionFactoryLocator;
   }

   public List findUserIdsWithConnection(Connection connection) {
      ConnectionKey key = connection.getKey();
      List<SocialUserConnection> socialUserConnections = this.socialUserConnectionRepository.findAllByProviderIdAndProviderUserId(key.getProviderId(), key.getProviderUserId());
      return (List)socialUserConnections.stream().map(SocialUserConnection::getUserId).collect(Collectors.toList());
   }

   public Set findUserIdsConnectedTo(String providerId, Set providerUserIds) {
      List<SocialUserConnection> socialUserConnections = this.socialUserConnectionRepository.findAllByProviderIdAndProviderUserIdIn(providerId, providerUserIds);
      return (Set)socialUserConnections.stream().map(SocialUserConnection::getUserId).collect(Collectors.toSet());
   }

   public ConnectionRepository createConnectionRepository(String userId) {
      if(userId == null) {
         throw new IllegalArgumentException("userId cannot be null");
      } else {
         return new CustomSocialConnectionRepository(userId, this.socialUserConnectionRepository, this.connectionFactoryLocator);
      }
   }
}
