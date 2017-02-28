package quiz.security.social;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import quiz.config.JHipsterProperties;
import quiz.security.jwt.TokenProvider;

public class CustomSignInAdapter implements SignInAdapter {
   private final Logger log = LoggerFactory.getLogger(CustomSignInAdapter.class);
   @Inject
   private UserDetailsService userDetailsService;
   @Inject
   private JHipsterProperties jHipsterProperties;
   @Inject
   private TokenProvider tokenProvider;

   public String signIn(String userId, Connection connection, NativeWebRequest request) {
      try {
         UserDetails exception = this.userDetailsService.loadUserByUsername(userId);
         UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(exception, (Object)null, exception.getAuthorities());
         SecurityContextHolder.getContext().setAuthentication(authenticationToken);
         String jwt = this.tokenProvider.createToken(authenticationToken, Boolean.valueOf(false));
         ServletWebRequest servletWebRequest = (ServletWebRequest)request;
         servletWebRequest.getResponse().addCookie(this.getSocialAuthenticationCookie(jwt));
      } catch (AuthenticationException var8) {
         this.log.error("Social authentication error");
      }

      return this.jHipsterProperties.getSocial().getRedirectAfterSignIn();
   }

   private Cookie getSocialAuthenticationCookie(String token) {
      Cookie socialAuthCookie = new Cookie("social-authentication", token);
      socialAuthCookie.setPath("/");
      socialAuthCookie.setMaxAge(10);
      return socialAuthCookie;
   }
}
