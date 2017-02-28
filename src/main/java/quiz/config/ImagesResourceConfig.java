package quiz.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class ImagesResourceConfig extends WebMvcConfigurerAdapter {
   @Value("${project.image-folder}")
   private String IMAGE_FOLDER;

   public void addResourceHandlers(ResourceHandlerRegistry registry) {
      registry.addResourceHandler(new String[]{"/images/**"}).addResourceLocations(new String[]{"file:" + this.IMAGE_FOLDER + "/"});
   }
}
