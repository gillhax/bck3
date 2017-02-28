package quiz.config.jcache;

import java.io.IOException;
import java.util.Properties;
import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.jcache.JCacheRegionFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

public class SpringCacheRegionFactory extends JCacheRegionFactory {
   public void start(SessionFactoryOptions options, Properties properties) throws CacheException {
      String uri = properties.getProperty("hibernate.javax.cache.uri");
      Resource resource = (new DefaultResourceLoader()).getResource(uri);

      try {
         properties.setProperty("hibernate.javax.cache.uri", resource.getURI().toString());
      } catch (IOException var6) {
         throw new CacheException(var6);
      }

      super.start(options, properties);
   }
}
