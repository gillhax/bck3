package quiz.security;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import quiz.security.SecurityUtils;

@Component
public class SpringSecurityAuditorAware implements AuditorAware {
   public String getCurrentAuditor() {
      String userName = SecurityUtils.getCurrentUserLogin();
      return userName != null?userName:"system";
   }
}
