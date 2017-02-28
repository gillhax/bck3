package quiz.config.jcache;

import com.codahale.metrics.JmxAttributeGauge;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.MetricSet;
import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.cache.management.CacheStatisticsMXBean;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.QueryExp;

public class JCacheGaugeSet implements MetricSet {
   private static final String M_BEAN_COORDINATES = "javax.cache:type=CacheStatistics,CacheManager=*,Cache=*";

   public Map getMetrics() {
      Set objectInstances = this.getCacheMBeans();
      HashMap gauges = new HashMap();
      List availableStatsNames = this.retrieveStatsNames();
      Iterator var4 = objectInstances.iterator();

      while(var4.hasNext()) {
         ObjectInstance objectInstance = (ObjectInstance)var4.next();
         ObjectName objectName = objectInstance.getObjectName();
         String cacheName = objectName.getKeyProperty("Cache");
         Iterator var8 = availableStatsNames.iterator();

         while(var8.hasNext()) {
            String statsName = (String)var8.next();
            JmxAttributeGauge jmxAttributeGauge = new JmxAttributeGauge(objectName, statsName);
            gauges.put(MetricRegistry.name(cacheName, new String[]{toDashCase(statsName)}), jmxAttributeGauge);
         }
      }

      return Collections.unmodifiableMap(gauges);
   }

   private Set getCacheMBeans() {
      try {
         return ManagementFactory.getPlatformMBeanServer().queryMBeans(ObjectName.getInstance("javax.cache:type=CacheStatistics,CacheManager=*,Cache=*"), (QueryExp)null);
      } catch (MalformedObjectNameException var2) {
         throw new InternalError("Shouldn\'t happen since the query is hardcoded", var2);
      }
   }

   private List retrieveStatsNames() {
      Class c = CacheStatisticsMXBean.class;
      return (List)Arrays.stream(c.getMethods()).filter((method) -> {
         return method.getName().startsWith("get");
      }).map((method) -> {
         return method.getName().substring(3);
      }).collect(Collectors.toList());
   }

   private static String toDashCase(String camelCase) {
      return camelCase.replaceAll("(.)(\\p{Upper})", "$1-$2").toLowerCase();
   }
}
