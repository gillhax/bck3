package quiz.web.rest.vm;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.annotation.JsonCreator;

public class LoggerVM {
   private String name;
   private String level;

   public LoggerVM(Logger logger) {
      this.name = logger.getName();
      this.level = logger.getEffectiveLevel().toString();
   }

   @JsonCreator
   public LoggerVM() {
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getLevel() {
      return this.level;
   }

   public void setLevel(String level) {
      this.level = level;
   }

   public String toString() {
      return "LoggerVM{name=\'" + this.name + '\'' + ", level=\'" + this.level + '\'' + '}';
   }
}
