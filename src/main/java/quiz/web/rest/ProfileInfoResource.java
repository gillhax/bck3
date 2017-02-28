package quiz.web.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quiz.config.DefaultProfileUtil;
import quiz.config.JHipsterProperties;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
@RequestMapping({"/api"})
public class ProfileInfoResource {
   @Inject
   private Environment env;
   @Inject
   private JHipsterProperties jHipsterProperties;

   @GetMapping({"/profile-info"})
   public ProfileInfoResource.ProfileInfoResponse getActiveProfiles() {
      String[] activeProfiles = DefaultProfileUtil.getActiveProfiles(this.env);
      return new ProfileInfoResource.ProfileInfoResponse(activeProfiles, this.getRibbonEnv(activeProfiles));
   }

   private String getRibbonEnv(String[] activeProfiles) {
      String[] displayOnActiveProfiles = this.jHipsterProperties.getRibbon().getDisplayOnActiveProfiles();
      if(displayOnActiveProfiles == null) {
         return null;
      } else {
         ArrayList ribbonProfiles = new ArrayList(Arrays.asList(displayOnActiveProfiles));
         List springBootProfiles = Arrays.asList(activeProfiles);
         ribbonProfiles.retainAll(springBootProfiles);
         return ribbonProfiles.size() > 0?(String)ribbonProfiles.get(0):null;
      }
   }

   class ProfileInfoResponse {
      public String[] activeProfiles;
      public String ribbonEnv;

      ProfileInfoResponse(String[] activeProfiles, String ribbonEnv) {
         this.activeProfiles = activeProfiles;
         this.ribbonEnv = ribbonEnv;
      }
   }
}
