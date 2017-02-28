package quiz.security.jwt;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import quiz.security.jwt.JWTFilter;
import quiz.security.jwt.TokenProvider;

public class JWTConfigurer extends SecurityConfigurerAdapter {
   public static final String AUTHORIZATION_HEADER = "Authorization";
   private TokenProvider tokenProvider;

   public JWTConfigurer(TokenProvider tokenProvider) {
      this.tokenProvider = tokenProvider;
   }

   public void configure(HttpSecurity http) throws Exception {
      JWTFilter customFilter = new JWTFilter(this.tokenProvider);
      http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
   }
}
