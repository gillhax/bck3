package quiz.config;

import javax.inject.Inject;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity.IgnoredRequestConfigurer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer.AuthorizedUrl;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import quiz.security.Http401UnauthorizedEntryPoint;
import quiz.security.jwt.JWTConfigurer;
import quiz.security.jwt.TokenProvider;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
   prePostEnabled = true,
   securedEnabled = true
)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
   @Inject
   private Http401UnauthorizedEntryPoint authenticationEntryPoint;
   @Inject
   private UserDetailsService userDetailsService;
   @Inject
   private TokenProvider tokenProvider;

   @Bean
   public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }

   @Inject
   public void configureGlobal(AuthenticationManagerBuilder auth) {
      try {
         auth.userDetailsService(this.userDetailsService).passwordEncoder(this.passwordEncoder());
      } catch (Exception var3) {
         throw new BeanInitializationException("Security configuration failed", var3);
      }
   }

   public void configure(WebSecurity web) throws Exception {
      ((IgnoredRequestConfigurer)((IgnoredRequestConfigurer)((IgnoredRequestConfigurer)((IgnoredRequestConfigurer)((IgnoredRequestConfigurer)((IgnoredRequestConfigurer)((IgnoredRequestConfigurer)((IgnoredRequestConfigurer)((IgnoredRequestConfigurer)((IgnoredRequestConfigurer)((IgnoredRequestConfigurer)((IgnoredRequestConfigurer)((IgnoredRequestConfigurer)((IgnoredRequestConfigurer)((IgnoredRequestConfigurer)web.ignoring().antMatchers(HttpMethod.OPTIONS, new String[]{"/**"})).antMatchers(new String[]{"/app/**/*.{js,html}"})).antMatchers(new String[]{"/bower_components/**"})).antMatchers(new String[]{"/i18n/**"})).antMatchers(new String[]{"/content/**"})).antMatchers(new String[]{"/swagger-ui/index.html"})).antMatchers(new String[]{"/api/v1/authenticate"})).antMatchers(new String[]{"/api/v1/account/**"})).antMatchers(new String[]{"/api/v1/parser/upload"})).antMatchers(new String[]{"/api/v1/players/top"})).antMatchers(new String[]{"/api/v1/avatars"})).antMatchers(new String[]{"/api/v1/help"})).antMatchers(new String[]{"/api/v1/offer/tradition"})).antMatchers(new String[]{"/api/v1/questions"})).antMatchers(new String[]{"/api/v1/versions"})).antMatchers(new String[]{"/test/**"});
   }

   protected void configure(HttpSecurity http) throws Exception {
      ((HttpSecurity)((AuthorizedUrl)((AuthorizedUrl)((AuthorizedUrl)((AuthorizedUrl)((AuthorizedUrl)((AuthorizedUrl)((AuthorizedUrl)((AuthorizedUrl)((AuthorizedUrl)((AuthorizedUrl)((AuthorizedUrl)((AuthorizedUrl)((AuthorizedUrl)((AuthorizedUrl)((AuthorizedUrl)((AuthorizedUrl)((AuthorizedUrl)((AuthorizedUrl)((AuthorizedUrl)((AuthorizedUrl)((HttpSecurity)((HttpSecurity)((HttpSecurity)((HttpSecurity)http.exceptionHandling().authenticationEntryPoint(this.authenticationEntryPoint).and()).csrf().disable()).headers().frameOptions().disable().and()).sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()).authorizeRequests().antMatchers(new String[]{"/api/register"})).permitAll().antMatchers(new String[]{"/api/activate"})).permitAll().antMatchers(new String[]{"/api/authenticate"})).permitAll().antMatchers(new String[]{"/api/v1/authenticate"})).permitAll().antMatchers(new String[]{"/api/v1/account/**"})).permitAll().antMatchers(new String[]{"/api/v1/questions"})).permitAll().antMatchers(new String[]{"/api/v1/help"})).permitAll().antMatchers(new String[]{"/api/v1/players/top"})).permitAll().antMatchers(new String[]{"/api/v1/avatars"})).permitAll().antMatchers(new String[]{"/api/v1/versions"})).permitAll().antMatchers(new String[]{"/api/v1/parser/upload"})).permitAll().antMatchers(new String[]{"/api/v1/offer/tradition"})).permitAll().antMatchers(new String[]{"/api/account/reset_password/init"})).permitAll().antMatchers(new String[]{"/api/account/reset_password/finish"})).permitAll().antMatchers(new String[]{"/api/profile-info"})).permitAll().antMatchers(new String[]{"/api/**"})).authenticated().antMatchers(new String[]{"/management/**"})).hasAuthority("ROLE_ADMIN").antMatchers(new String[]{"/v2/api-docs/**"})).permitAll().antMatchers(new String[]{"/swagger-resources/configuration/ui"})).permitAll().antMatchers(new String[]{"/swagger-ui/index.html"})).hasAuthority("ROLE_ADMIN").and()).apply(this.securityConfigurerAdapter());
   }

   private JWTConfigurer securityConfigurerAdapter() {
      return new JWTConfigurer(this.tokenProvider);
   }

   @Bean
   public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
      return new SecurityEvaluationContextExtension();
   }
}
