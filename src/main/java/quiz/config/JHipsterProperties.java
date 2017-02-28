package quiz.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.cors.CorsConfiguration;

@ConfigurationProperties(
   prefix = "jhipster",
   ignoreUnknownFields = false
)
public class JHipsterProperties {
   private final JHipsterProperties.Async async = new JHipsterProperties.Async();
   private final JHipsterProperties.Http http = new JHipsterProperties.Http();
   private final JHipsterProperties.Cache cache = new JHipsterProperties.Cache();
   private final JHipsterProperties.Mail mail = new JHipsterProperties.Mail();
   private final JHipsterProperties.Security security = new JHipsterProperties.Security();
   private final JHipsterProperties.Swagger swagger = new JHipsterProperties.Swagger();
   private final JHipsterProperties.Metrics metrics = new JHipsterProperties.Metrics();
   private final CorsConfiguration cors = new CorsConfiguration();
   private final JHipsterProperties.Social social = new JHipsterProperties.Social();
   private final JHipsterProperties.Ribbon ribbon = new JHipsterProperties.Ribbon();
   private final JHipsterProperties.Logging logging = new JHipsterProperties.Logging();

   public JHipsterProperties.Async getAsync() {
      return this.async;
   }

   public JHipsterProperties.Http getHttp() {
      return this.http;
   }

   public JHipsterProperties.Cache getCache() {
      return this.cache;
   }

   public JHipsterProperties.Mail getMail() {
      return this.mail;
   }

   public JHipsterProperties.Security getSecurity() {
      return this.security;
   }

   public JHipsterProperties.Swagger getSwagger() {
      return this.swagger;
   }

   public JHipsterProperties.Metrics getMetrics() {
      return this.metrics;
   }

   public CorsConfiguration getCors() {
      return this.cors;
   }

   public JHipsterProperties.Social getSocial() {
      return this.social;
   }

   public JHipsterProperties.Ribbon getRibbon() {
      return this.ribbon;
   }

   public JHipsterProperties.Logging getLogging() {
      return this.logging;
   }

   public static class Ribbon {
      private String[] displayOnActiveProfiles;

      public String[] getDisplayOnActiveProfiles() {
         return this.displayOnActiveProfiles;
      }

      public void setDisplayOnActiveProfiles(String[] displayOnActiveProfiles) {
         this.displayOnActiveProfiles = displayOnActiveProfiles;
      }
   }

   public static class Social {
      private String redirectAfterSignIn = "/#/home";

      public String getRedirectAfterSignIn() {
         return this.redirectAfterSignIn;
      }

      public void setRedirectAfterSignIn(String redirectAfterSignIn) {
         this.redirectAfterSignIn = redirectAfterSignIn;
      }
   }

   public static class Logging {
      private final JHipsterProperties.Logging.Logstash logstash = new JHipsterProperties.Logging.Logstash();

      public JHipsterProperties.Logging.Logstash getLogstash() {
         return this.logstash;
      }

      public static class Logstash {
         private boolean enabled = false;
         private String host = "localhost";
         private int port = 5000;
         private int queueSize = 512;

         public boolean isEnabled() {
            return this.enabled;
         }

         public void setEnabled(boolean enabled) {
            this.enabled = enabled;
         }

         public String getHost() {
            return this.host;
         }

         public void setHost(String host) {
            this.host = host;
         }

         public int getPort() {
            return this.port;
         }

         public void setPort(int port) {
            this.port = port;
         }

         public int getQueueSize() {
            return this.queueSize;
         }

         public void setQueueSize(int queueSize) {
            this.queueSize = queueSize;
         }
      }
   }

   public static class Metrics {
      private final JHipsterProperties.Metrics.Jmx jmx = new JHipsterProperties.Metrics.Jmx();
      private final JHipsterProperties.Metrics.Graphite graphite = new JHipsterProperties.Metrics.Graphite();
      private final JHipsterProperties.Metrics.Prometheus prometheus = new JHipsterProperties.Metrics.Prometheus();
      private final JHipsterProperties.Metrics.Logs logs = new JHipsterProperties.Metrics.Logs();

      public JHipsterProperties.Metrics.Jmx getJmx() {
         return this.jmx;
      }

      public JHipsterProperties.Metrics.Graphite getGraphite() {
         return this.graphite;
      }

      public JHipsterProperties.Metrics.Prometheus getPrometheus() {
         return this.prometheus;
      }

      public JHipsterProperties.Metrics.Logs getLogs() {
         return this.logs;
      }

      public static class Logs {
         private boolean enabled = false;
         private long reportFrequency = 60L;

         public long getReportFrequency() {
            return this.reportFrequency;
         }

         public void setReportFrequency(int reportFrequency) {
            this.reportFrequency = (long)reportFrequency;
         }

         public boolean isEnabled() {
            return this.enabled;
         }

         public void setEnabled(boolean enabled) {
            this.enabled = enabled;
         }
      }

      public static class Prometheus {
         private boolean enabled = false;
         private String endpoint = "/prometheusMetrics";

         public boolean isEnabled() {
            return this.enabled;
         }

         public void setEnabled(boolean enabled) {
            this.enabled = enabled;
         }

         public String getEndpoint() {
            return this.endpoint;
         }

         public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
         }
      }

      public static class Graphite {
         private boolean enabled = false;
         private String host = "localhost";
         private int port = 2003;
         private String prefix = "quiz";

         public boolean isEnabled() {
            return this.enabled;
         }

         public void setEnabled(boolean enabled) {
            this.enabled = enabled;
         }

         public String getHost() {
            return this.host;
         }

         public void setHost(String host) {
            this.host = host;
         }

         public int getPort() {
            return this.port;
         }

         public void setPort(int port) {
            this.port = port;
         }

         public String getPrefix() {
            return this.prefix;
         }

         public void setPrefix(String prefix) {
            this.prefix = prefix;
         }
      }

      public static class Jmx {
         private boolean enabled = true;

         public boolean isEnabled() {
            return this.enabled;
         }

         public void setEnabled(boolean enabled) {
            this.enabled = enabled;
         }
      }
   }

   public static class Swagger {
      private String title = "quiz API";
      private String description = "quiz API documentation";
      private String version = "0.0.1";
      private String termsOfServiceUrl;
      private String contactName;
      private String contactUrl;
      private String contactEmail;
      private String license;
      private String licenseUrl;

      public String getTitle() {
         return this.title;
      }

      public void setTitle(String title) {
         this.title = title;
      }

      public String getDescription() {
         return this.description;
      }

      public void setDescription(String description) {
         this.description = description;
      }

      public String getVersion() {
         return this.version;
      }

      public void setVersion(String version) {
         this.version = version;
      }

      public String getTermsOfServiceUrl() {
         return this.termsOfServiceUrl;
      }

      public void setTermsOfServiceUrl(String termsOfServiceUrl) {
         this.termsOfServiceUrl = termsOfServiceUrl;
      }

      public String getContactName() {
         return this.contactName;
      }

      public void setContactName(String contactName) {
         this.contactName = contactName;
      }

      public String getContactUrl() {
         return this.contactUrl;
      }

      public void setContactUrl(String contactUrl) {
         this.contactUrl = contactUrl;
      }

      public String getContactEmail() {
         return this.contactEmail;
      }

      public void setContactEmail(String contactEmail) {
         this.contactEmail = contactEmail;
      }

      public String getLicense() {
         return this.license;
      }

      public void setLicense(String license) {
         this.license = license;
      }

      public String getLicenseUrl() {
         return this.licenseUrl;
      }

      public void setLicenseUrl(String licenseUrl) {
         this.licenseUrl = licenseUrl;
      }
   }

   public static class Security {
      private final JHipsterProperties.Security.Authentication authentication = new JHipsterProperties.Security.Authentication();

      public JHipsterProperties.Security.Authentication getAuthentication() {
         return this.authentication;
      }

      public static class Authentication {
         private final JHipsterProperties.Security.Authentication.Jwt jwt = new JHipsterProperties.Security.Authentication.Jwt();

         public JHipsterProperties.Security.Authentication.Jwt getJwt() {
            return this.jwt;
         }

         public static class Jwt {
            private String secret;
            private long tokenValidityInSeconds = 3L;
            private long tokenValidityInSecondsForRememberMe = 3L;

            public String getSecret() {
               return this.secret;
            }

            public void setSecret(String secret) {
               this.secret = secret;
            }

            public long getTokenValidityInSeconds() {
               return this.tokenValidityInSeconds;
            }

            public void setTokenValidityInSeconds(long tokenValidityInSeconds) {
               this.tokenValidityInSeconds = tokenValidityInSeconds;
            }

            public long getTokenValidityInSecondsForRememberMe() {
               return this.tokenValidityInSecondsForRememberMe;
            }

            public void setTokenValidityInSecondsForRememberMe(long tokenValidityInSecondsForRememberMe) {
               this.tokenValidityInSecondsForRememberMe = tokenValidityInSecondsForRememberMe;
            }
         }
      }
   }

   public static class Mail {
      private String from = "Викторина! Восстановление пароля";
      private String baseUrl = "smtp.gmail.com";

      public String getFrom() {
         return this.from;
      }

      public void setFrom(String from) {
         this.from = from;
      }

      public String getBaseUrl() {
         return this.baseUrl;
      }

      public void setBaseUrl(String baseUrl) {
         this.baseUrl = baseUrl;
      }
   }

   public static class Cache {
      private final JHipsterProperties.Cache.Ehcache ehcache = new JHipsterProperties.Cache.Ehcache();

      public JHipsterProperties.Cache.Ehcache getEhcache() {
         return this.ehcache;
      }

      public static class Ehcache {
         private String maxBytesLocalHeap = "16M";

         public String getMaxBytesLocalHeap() {
            return this.maxBytesLocalHeap;
         }

         public void setMaxBytesLocalHeap(String maxBytesLocalHeap) {
            this.maxBytesLocalHeap = maxBytesLocalHeap;
         }
      }
   }

   public static class Http {
      private final JHipsterProperties.Http.Cache cache = new JHipsterProperties.Http.Cache();

      public JHipsterProperties.Http.Cache getCache() {
         return this.cache;
      }

      public static class Cache {
         private int timeToLiveInDays = 1461;

         public int getTimeToLiveInDays() {
            return this.timeToLiveInDays;
         }

         public void setTimeToLiveInDays(int timeToLiveInDays) {
            this.timeToLiveInDays = timeToLiveInDays;
         }
      }
   }

   public static class Async {
      private int corePoolSize = 2;
      private int maxPoolSize = 50;
      private int queueCapacity = 10000;

      public int getCorePoolSize() {
         return this.corePoolSize;
      }

      public void setCorePoolSize(int corePoolSize) {
         this.corePoolSize = corePoolSize;
      }

      public int getMaxPoolSize() {
         return this.maxPoolSize;
      }

      public void setMaxPoolSize(int maxPoolSize) {
         this.maxPoolSize = maxPoolSize;
      }

      public int getQueueCapacity() {
         return this.queueCapacity;
      }

      public void setQueueCapacity(int queueCapacity) {
         this.queueCapacity = queueCapacity;
      }
   }
}
