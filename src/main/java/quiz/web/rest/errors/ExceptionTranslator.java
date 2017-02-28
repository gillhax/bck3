package quiz.web.rest.errors;

import java.util.Iterator;
import java.util.List;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import quiz.web.rest.errors.CustomParameterizedException;
import quiz.web.rest.errors.ErrorVM;
import quiz.web.rest.errors.ParameterizedErrorVM;

@ControllerAdvice
public class ExceptionTranslator {
   @ExceptionHandler({ConcurrencyFailureException.class})
   @ResponseStatus(HttpStatus.CONFLICT)
   @ResponseBody
   public ErrorVM processConcurencyError(ConcurrencyFailureException ex) {
      return new ErrorVM("error.concurrencyFailure");
   }

   @ExceptionHandler({MethodArgumentNotValidException.class})
   @ResponseStatus(HttpStatus.BAD_REQUEST)
   @ResponseBody
   public ErrorVM processValidationError(MethodArgumentNotValidException ex) {
      BindingResult result = ex.getBindingResult();
      List fieldErrors = result.getFieldErrors();
      return this.processFieldErrors(fieldErrors);
   }

   @ExceptionHandler({CustomParameterizedException.class})
   @ResponseStatus(HttpStatus.BAD_REQUEST)
   @ResponseBody
   public ParameterizedErrorVM processParameterizedValidationError(CustomParameterizedException ex) {
      return ex.getErrorVM();
   }

   @ExceptionHandler({AccessDeniedException.class})
   @ResponseStatus(HttpStatus.FORBIDDEN)
   @ResponseBody
   public ErrorVM processAccessDeniedException(AccessDeniedException e) {
      return new ErrorVM("error.accessDenied", e.getMessage());
   }

   private ErrorVM processFieldErrors(List fieldErrors) {
      ErrorVM dto = new ErrorVM("error.validation");
      Iterator var3 = fieldErrors.iterator();

      while(var3.hasNext()) {
         FieldError fieldError = (FieldError)var3.next();
         dto.add(fieldError.getObjectName(), fieldError.getField(), fieldError.getCode());
      }

      return dto;
   }

   @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
   @ResponseBody
   @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
   public ErrorVM processMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
      return new ErrorVM("error.methodNotSupported", exception.getMessage());
   }

   @ExceptionHandler({Exception.class})
   public ResponseEntity processRuntimeException(Exception ex) {
      ResponseStatus responseStatus = (ResponseStatus)AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);
      BodyBuilder builder;
      ErrorVM errorVM;
      if(responseStatus != null) {
         builder = ResponseEntity.status(responseStatus.value());
         errorVM = new ErrorVM("error." + responseStatus.value().value(), responseStatus.reason());
      } else {
         builder = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
         errorVM = new ErrorVM("error.internalServerError", "Internal server error");
      }

      return builder.body(errorVM);
   }
}
