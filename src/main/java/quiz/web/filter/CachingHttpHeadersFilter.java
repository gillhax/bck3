package quiz.web.filter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import quiz.config.JHipsterProperties;

public class CachingHttpHeadersFilter implements Filter {
   private static final long LAST_MODIFIED = System.currentTimeMillis();
   private long CACHE_TIME_TO_LIVE;
   private JHipsterProperties jHipsterProperties;

   public CachingHttpHeadersFilter(JHipsterProperties jHipsterProperties) {
      this.CACHE_TIME_TO_LIVE = TimeUnit.DAYS.toMillis(1461L);
      this.jHipsterProperties = jHipsterProperties;
   }

   public void init(FilterConfig filterConfig) throws ServletException {
      this.CACHE_TIME_TO_LIVE = TimeUnit.DAYS.toMillis((long)this.jHipsterProperties.getHttp().getCache().getTimeToLiveInDays());
   }

   public void destroy() {
   }

   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
      HttpServletResponse httpResponse = (HttpServletResponse)response;
      httpResponse.setHeader("Cache-Control", "max-age=" + this.CACHE_TIME_TO_LIVE + ", public");
      httpResponse.setHeader("Pragma", "cache");
      httpResponse.setDateHeader("Expires", this.CACHE_TIME_TO_LIVE + System.currentTimeMillis());
      httpResponse.setDateHeader("Last-Modified", LAST_MODIFIED);
      chain.doFilter(request, response);
   }
}
