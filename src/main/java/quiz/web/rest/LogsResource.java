package quiz.web.rest;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.codahale.metrics.annotation.Timed;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import quiz.web.rest.vm.LoggerVM;

@RestController
@RequestMapping({"/management"})
public class LogsResource {
   @GetMapping({"/logs"})
   @Timed
   public List getList() {
      LoggerContext context = (LoggerContext)LoggerFactory.getILoggerFactory();
      return (List)context.getLoggerList().stream().map(LoggerVM::new).collect(Collectors.toList());
   }

   @PutMapping({"/logs"})
   @ResponseStatus(HttpStatus.NO_CONTENT)
   @Timed
   public void changeLevel(@RequestBody LoggerVM jsonLogger) {
      LoggerContext context = (LoggerContext)LoggerFactory.getILoggerFactory();
      context.getLogger(jsonLogger.getName()).setLevel(Level.valueOf(jsonLogger.getLevel()));
   }
}
