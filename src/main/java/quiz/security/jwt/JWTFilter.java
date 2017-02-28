package quiz.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import quiz.security.jwt.TokenProvider;

public class JWTFilter extends GenericFilterBean {
   private final Logger log = LoggerFactory.getLogger(JWTFilter.class);
   private TokenProvider tokenProvider;

   public JWTFilter(TokenProvider tokenProvider) {
      this.tokenProvider = tokenProvider;
   }

   public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
      try {
         HttpServletRequest eje = (HttpServletRequest)servletRequest;
         String jwt = this.resolveToken(eje);
         if(StringUtils.hasText(jwt) && this.tokenProvider.validateToken(jwt)) {
            Authentication authentication = this.tokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
         }

         filterChain.doFilter(servletRequest, servletResponse);
      } catch (ExpiredJwtException var7) {
         this.log.info("Security exception for user {} - {}", var7.getClaims().getSubject(), var7.getMessage());
         ((HttpServletResponse)servletResponse).setStatus(401);
      }

   }

   private String resolveToken(HttpServletRequest request) {
      String bearerToken = request.getHeader("Authorization");
      if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
         String jwt = bearerToken.substring(7, bearerToken.length());
         return jwt;
      } else {
         return null;
      }
   }
}
