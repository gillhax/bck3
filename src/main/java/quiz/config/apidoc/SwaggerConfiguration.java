package quiz.config.apidoc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import quiz.config.JHipsterProperties;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Import({BeanValidatorPluginsConfiguration.class})
@Profile({"swagger"})
public class SwaggerConfiguration {
   private final Logger log = LoggerFactory.getLogger(SwaggerConfiguration.class);
   public static final String DEFAULT_INCLUDE_PATTERN = "/api/.*";

   @Bean
   public Docket swaggerSpringfoxDocket(JHipsterProperties jHipsterProperties) {
      this.log.debug("Starting Swagger");
      StopWatch watch = new StopWatch();
      watch.start();
      Contact contact = new Contact(jHipsterProperties.getSwagger().getContactName(), jHipsterProperties.getSwagger().getContactUrl(), jHipsterProperties.getSwagger().getContactEmail());
      ApiInfo apiInfo = new ApiInfo(jHipsterProperties.getSwagger().getTitle(), jHipsterProperties.getSwagger().getDescription(), jHipsterProperties.getSwagger().getVersion(), jHipsterProperties.getSwagger().getTermsOfServiceUrl(), contact, jHipsterProperties.getSwagger().getLicense(), jHipsterProperties.getSwagger().getLicenseUrl());
      Docket docket = (new Docket(DocumentationType.SWAGGER_2)).apiInfo(apiInfo).forCodeGeneration(true).genericModelSubstitutes(new Class[]{ResponseEntity.class}).select().paths(PathSelectors.regex("/api/.*")).build();
      watch.stop();
      this.log.debug("Started Swagger in {} ms", Long.valueOf(watch.getTotalTimeMillis()));
      return docket;
   }
}
