package quiz.config;

import java.util.HashMap;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.Environment;

public final class DefaultProfileUtil {
   private static final String SPRING_PROFILE_DEFAULT = "spring.profiles.default";

   public static void addDefaultProfile(SpringApplication app) {
      HashMap defProperties = new HashMap();
      defProperties.put("spring.profiles.default", "dev");
      app.setDefaultProperties(defProperties);
   }

   public static String[] getActiveProfiles(Environment env) {
      String[] profiles = env.getActiveProfiles();
      return profiles.length == 0?env.getDefaultProfiles():profiles;
   }
}
