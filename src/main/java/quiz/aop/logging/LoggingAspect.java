package quiz.aop.logging;

import java.util.Arrays;
import javax.inject.Inject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

@Aspect
public class LoggingAspect {
   private final Logger log = LoggerFactory.getLogger(this.getClass());
   @Inject
   private Environment env;

   @Pointcut("within(quiz.repository..*) || within(quiz.service..*) || within(quiz.web.rest..*)")
   public void loggingPointcut() {
   }

   @AfterThrowing(
      pointcut = "loggingPointcut()",
      throwing = "e"
   )
   public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
      if(this.env.acceptsProfiles(new String[]{"dev"})) {
         this.log.error("Exception in {}.{}() with cause = \'{}\' and exception = \'{}\'", new Object[]{joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), e.getCause() != null?e.getCause():"NULL", e.getMessage(), e});
      } else {
         this.log.error("Exception in {}.{}() with cause = {}", new Object[]{joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), e.getCause() != null?e.getCause():"NULL"});
      }

   }

   @Around("loggingPointcut()")
   public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
      if(this.log.isDebugEnabled()) {
         this.log.debug("Enter: {}.{}() with argument[s] = {}", new Object[]{joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs())});
      }

      try {
         Object e = joinPoint.proceed();
         if(this.log.isDebugEnabled()) {
            this.log.debug("Exit: {}.{}() with result = {}", new Object[]{joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), e});
         }

         return e;
      } catch (IllegalArgumentException var3) {
         this.log.error("Illegal argument: {} in {}.{}()", new Object[]{Arrays.toString(joinPoint.getArgs()), joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName()});
         throw var3;
      }
   }
}
