package quiz.system.error.handler.controller;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import quiz.service.LocalizationService;

@RestController
@RequestMapping({"/error"})
public class ErrorControllerImpl implements ErrorController {
   private final ErrorAttributes errorAttributes;
   private final LocalizationService localizationService;

   @Autowired
   public ErrorControllerImpl(ErrorAttributes errorAttributes, LocalizationService localizationService) {
      this.errorAttributes = errorAttributes;
      this.localizationService = localizationService;
   }

   public String getErrorPath() {
      return "/error";
   }

   @RequestMapping
   public Object error(HttpServletRequest request) {
      Map body = this.getErrorAttributes(request, false);
      HttpStatus status = this.getStatus(request);
      return !((String)body.get("path")).startsWith("/api")?new ModelAndView("/error", body):new ResponseEntity(body, status);
   }

   private Map getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
      ServletRequestAttributes requestAttributes = new ServletRequestAttributes(request);
      Map errorAttributes = this.errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace);
      errorAttributes.put("devMessage", errorAttributes.get("message"));
      String error = (String)errorAttributes.get("error");
      String message = this.localizationService.getMessage(error, new String[0]);
      errorAttributes.put("message", message);
      return errorAttributes;
   }

   private HttpStatus getStatus(HttpServletRequest request) {
      Integer statusCode = (Integer)request.getAttribute("javax.servlet.error.status_code");
      if(statusCode == null) {
         return HttpStatus.INTERNAL_SERVER_ERROR;
      } else {
         try {
            return HttpStatus.valueOf(statusCode.intValue());
         } catch (Exception var4) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
         }
      }
   }
}
