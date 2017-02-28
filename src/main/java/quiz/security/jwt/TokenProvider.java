package quiz.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import quiz.config.JHipsterProperties;

@Component
public class TokenProvider {
   private final Logger log = LoggerFactory.getLogger(TokenProvider.class);
   private static final String AUTHORITIES_KEY = "auth";
   private String secretKey;
   private long tokenValidityInMilliseconds;
   private long tokenValidityInMillisecondsForRememberMe;
   @Inject
   private JHipsterProperties jHipsterProperties;

   @PostConstruct
   public void init() {
      this.secretKey = this.jHipsterProperties.getSecurity().getAuthentication().getJwt().getSecret();
      this.tokenValidityInMilliseconds = 1000L * this.jHipsterProperties.getSecurity().getAuthentication().getJwt().getTokenValidityInSeconds();
      this.tokenValidityInMillisecondsForRememberMe = 1000L * this.jHipsterProperties.getSecurity().getAuthentication().getJwt().getTokenValidityInSecondsForRememberMe();
   }

   public String createToken(Authentication authentication, Boolean rememberMe) {
      String authorities = (String)authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
      long now = (new Date()).getTime();
      if(rememberMe.booleanValue()) {
         new Date(now + this.tokenValidityInMillisecondsForRememberMe);
      } else {
         new Date(now + this.tokenValidityInMilliseconds);
      }

      return Jwts.builder().setSubject(authentication.getName()).claim("auth", authorities).signWith(SignatureAlgorithm.HS512, this.secretKey).compact();
   }

   public Authentication getAuthentication(String token) {
      Claims claims = (Claims)Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token).getBody();
      Collection authorities = (Collection)Arrays.stream(claims.get("auth").toString().split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
      User principal = new User(claims.getSubject(), "", authorities);
      return new UsernamePasswordAuthenticationToken(principal, "", authorities);
   }

   public boolean validateToken(String authToken) {
      try {
         Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(authToken);
         return true;
      } catch (SignatureException var3) {
         this.log.info("Invalid JWT signature: " + var3.getMessage());
         return false;
      }
   }
}
