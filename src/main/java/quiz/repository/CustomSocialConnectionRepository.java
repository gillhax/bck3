package quiz.repository;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.NoSuchConnectionException;
import org.springframework.social.connect.NotConnectedException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import quiz.domain.SocialUserConnection;
import quiz.repository.SocialUserConnectionRepository;

public class CustomSocialConnectionRepository implements ConnectionRepository {
   private String userId;
   private SocialUserConnectionRepository socialUserConnectionRepository;
   private ConnectionFactoryLocator connectionFactoryLocator;

   public CustomSocialConnectionRepository(String userId, SocialUserConnectionRepository socialUserConnectionRepository, ConnectionFactoryLocator connectionFactoryLocator) {
      this.userId = userId;
      this.socialUserConnectionRepository = socialUserConnectionRepository;
      this.connectionFactoryLocator = connectionFactoryLocator;
   }

   public MultiValueMap findAllConnections() {
      List socialUserConnections = this.socialUserConnectionRepository.findAllByUserIdOrderByProviderIdAscRankAsc(this.userId);
      List connections = this.socialUserConnectionsToConnections(socialUserConnections);
      LinkedMultiValueMap connectionsByProviderId = new LinkedMultiValueMap();
      Set registeredProviderIds = this.connectionFactoryLocator.registeredProviderIds();
      Iterator var5 = registeredProviderIds.iterator();

      while(var5.hasNext()) {
         String connection = (String)var5.next();
         connectionsByProviderId.put(connection, Collections.emptyList());
      }

      String providerId;
      Connection connection1;
      for(var5 = connections.iterator(); var5.hasNext(); connectionsByProviderId.add(providerId, connection1)) {
         connection1 = (Connection)var5.next();
         providerId = connection1.getKey().getProviderId();
         if(((List)connectionsByProviderId.get(providerId)).size() == 0) {
            connectionsByProviderId.put(providerId, new LinkedList());
         }
      }

      return connectionsByProviderId;
   }

   public List findConnections(String providerId) {
      List socialUserConnections = this.socialUserConnectionRepository.findAllByUserIdAndProviderIdOrderByRankAsc(this.userId, providerId);
      return this.socialUserConnectionsToConnections(socialUserConnections);
   }

   public List findConnections(Class apiType) {
      List connections = this.findConnections(this.getProviderId(apiType));
      return connections;
   }

   public MultiValueMap findConnectionsToUsers(MultiValueMap providerUserIdsByProviderId) {
      if(providerUserIdsByProviderId != null && !providerUserIdsByProviderId.isEmpty()) {
         LinkedMultiValueMap connectionsForUsers = new LinkedMultiValueMap();
         Iterator var3 = providerUserIdsByProviderId.entrySet().iterator();

         while(var3.hasNext()) {
            Entry entry = (Entry)var3.next();
            String providerId = (String)entry.getKey();
            List providerUserIds = (List)entry.getValue();
            List connections = this.providerUserIdsToConnections(providerId, providerUserIds);
            connections.forEach((connection) -> {
               connectionsForUsers.add(providerId, connection);
            });
         }

         return connectionsForUsers;
      } else {
         throw new IllegalArgumentException("Unable to execute find: no providerUsers provided");
      }
   }

   public Connection getConnection(ConnectionKey connectionKey) {
      SocialUserConnection socialUserConnection = this.socialUserConnectionRepository.findOneByUserIdAndProviderIdAndProviderUserId(this.userId, connectionKey.getProviderId(), connectionKey.getProviderUserId());
      return (Connection)Optional.ofNullable(socialUserConnection).map(this::socialUserConnectionToConnection).orElseThrow(() -> {
         return new NoSuchConnectionException(connectionKey);
      });
   }

   public Connection getConnection(Class apiType, String providerUserId) {
      String providerId = this.getProviderId(apiType);
      return this.getConnection(new ConnectionKey(providerId, providerUserId));
   }

   public Connection getPrimaryConnection(Class apiType) {
      String providerId = this.getProviderId(apiType);
      Connection connection = this.findPrimaryConnection(providerId);
      if(connection == null) {
         throw new NotConnectedException(providerId);
      } else {
         return connection;
      }
   }

   public Connection findPrimaryConnection(Class apiType) {
      String providerId = this.getProviderId(apiType);
      return this.findPrimaryConnection(providerId);
   }

   @Transactional
   public void addConnection(Connection connection) {
      Long rank = Long.valueOf(this.getNewMaxRank(connection.getKey().getProviderId()).longValue());
      SocialUserConnection socialUserConnectionToSave = this.connectionToUserSocialConnection(connection, rank);
      this.socialUserConnectionRepository.save(socialUserConnectionToSave);
   }

   @Transactional
   public void updateConnection(Connection connection) {
      SocialUserConnection socialUserConnection = this.socialUserConnectionRepository.findOneByUserIdAndProviderIdAndProviderUserId(this.userId, connection.getKey().getProviderId(), connection.getKey().getProviderUserId());
      if(socialUserConnection != null) {
         SocialUserConnection socialUserConnectionToUdpate = this.connectionToUserSocialConnection(connection, socialUserConnection.getRank());
         socialUserConnectionToUdpate.setId(socialUserConnection.getId());
         this.socialUserConnectionRepository.save(socialUserConnectionToUdpate);
      }

   }

   @Transactional
   public void removeConnections(String providerId) {
      this.socialUserConnectionRepository.deleteByUserIdAndProviderId(this.userId, providerId);
   }

   @Transactional
   public void removeConnection(ConnectionKey connectionKey) {
      this.socialUserConnectionRepository.deleteByUserIdAndProviderIdAndProviderUserId(this.userId, connectionKey.getProviderId(), connectionKey.getProviderUserId());
   }

   private Double getNewMaxRank(String providerId) {
      List<SocialUserConnection> socialUserConnections = this.socialUserConnectionRepository.findAllByUserIdAndProviderIdOrderByRankAsc(this.userId, providerId);
      return Double.valueOf(socialUserConnections.stream().mapToDouble(SocialUserConnection::getRank).max().orElse(0.0D) + 1.0D);
   }

   private Connection findPrimaryConnection(String providerId) {
      List socialUserConnections = this.socialUserConnectionRepository.findAllByUserIdAndProviderIdOrderByRankAsc(this.userId, providerId);
      return socialUserConnections.size() > 0?this.socialUserConnectionToConnection((SocialUserConnection)socialUserConnections.get(0)):null;
   }

   private SocialUserConnection connectionToUserSocialConnection(Connection connection, Long rank) {
      ConnectionData connectionData = connection.createData();
      return new SocialUserConnection(this.userId, connection.getKey().getProviderId(), connection.getKey().getProviderUserId(), rank, connection.getDisplayName(), connection.getProfileUrl(), connection.getImageUrl(), connectionData.getAccessToken(), connectionData.getSecret(), connectionData.getRefreshToken(), connectionData.getExpireTime());
   }

   private List providerUserIdsToConnections(String providerId, List providerUserIds) {
      List socialUserConnections = this.socialUserConnectionRepository.findAllByUserIdAndProviderIdAndProviderUserIdIn(this.userId, providerId, providerUserIds);
      return this.socialUserConnectionsToConnections(socialUserConnections);
   }

   private List socialUserConnectionsToConnections(List<SocialUserConnection> socialUserConnections) {
      return (List)socialUserConnections.stream().map(this::socialUserConnectionToConnection).collect(Collectors.toList());
   }

   private Connection socialUserConnectionToConnection(SocialUserConnection socialUserConnection) {
      ConnectionData connectionData = new ConnectionData(socialUserConnection.getProviderId(), socialUserConnection.getProviderUserId(), socialUserConnection.getDisplayName(), socialUserConnection.getProfileURL(), socialUserConnection.getImageURL(), socialUserConnection.getAccessToken(), socialUserConnection.getSecret(), socialUserConnection.getRefreshToken(), socialUserConnection.getExpireTime());
      ConnectionFactory connectionFactory = this.connectionFactoryLocator.getConnectionFactory(connectionData.getProviderId());
      return connectionFactory.createConnection(connectionData);
   }

   private String getProviderId(Class apiType) {
      return this.connectionFactoryLocator.getConnectionFactory(apiType).getProviderId();
   }
}
