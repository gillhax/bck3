package quiz.config.audit;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import quiz.domain.PersistentAuditEvent;

@Component
public class AuditEventConverter {
   public List convertToAuditEvent(Iterable persistentAuditEvents) {
      if(persistentAuditEvents == null) {
         return Collections.emptyList();
      } else {
         ArrayList auditEvents = new ArrayList();
         Iterator var3 = persistentAuditEvents.iterator();

         while(var3.hasNext()) {
            PersistentAuditEvent persistentAuditEvent = (PersistentAuditEvent)var3.next();
            auditEvents.add(this.convertToAuditEvent(persistentAuditEvent));
         }

         return auditEvents;
      }
   }

   public AuditEvent convertToAuditEvent(PersistentAuditEvent persistentAuditEvent) {
      Instant instant = persistentAuditEvent.getAuditEventDate().atZone(ZoneId.systemDefault()).toInstant();
      return new AuditEvent(Date.from(instant), persistentAuditEvent.getPrincipal(), persistentAuditEvent.getAuditEventType(), this.convertDataToObjects(persistentAuditEvent.getData()));
   }

   public Map convertDataToObjects(Map data) {
      HashMap results = new HashMap();
      if(data != null) {
         Iterator var3 = data.entrySet().iterator();

         while(var3.hasNext()) {
            Entry entry = (Entry)var3.next();
            results.put(entry.getKey(), entry.getValue());
         }
      }

      return results;
   }

   public Map convertDataToStrings(Map data) {
      HashMap results = new HashMap();
      if(data != null) {
         Iterator var3 = data.entrySet().iterator();

         while(var3.hasNext()) {
            Entry entry = (Entry)var3.next();
            Object object = entry.getValue();
            if(object instanceof WebAuthenticationDetails) {
               WebAuthenticationDetails authenticationDetails = (WebAuthenticationDetails)object;
               results.put("remoteAddress", authenticationDetails.getRemoteAddress());
               results.put("sessionId", authenticationDetails.getSessionId());
            } else if(object != null) {
               results.put(entry.getKey(), object.toString());
            } else {
               results.put(entry.getKey(), "null");
            }
         }
      }

      return results;
   }
}
