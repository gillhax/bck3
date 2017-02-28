package quiz.system.error.handler.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import quiz.service.LocalizationService;
import quiz.system.error.handler.dto.ValidationErrorDto;

@ControllerAdvice
public class ValidationErrorHandlerController {
   private static final List codesOrder = Arrays.asList(new String[]{"NotBlank", "NotNull", "Length", "Pattern"});
   private final LocalizationService localizationService;

   public ValidationErrorHandlerController(LocalizationService localizationService) {
      this.localizationService = localizationService;
   }

   @ExceptionHandler({MethodArgumentNotValidException.class})
   public ResponseEntity methodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
      HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
      ValidationErrorDto validationError = new ValidationErrorDto(httpStatus.value(), "validation-error", e.getClass().getName(), this.localizationService.getMessage("validation-error", new String[0]), e.toString(), request.getServletPath(), this.convertErrors(e.getBindingResult().getAllErrors()));
      return new ResponseEntity(validationError, httpStatus);
   }

   private List convertErrors(List allErrors) {
      ArrayList messages = new ArrayList(allErrors.size());
      HashMap fieldErrorMap = new HashMap(allErrors.size(), 1.0F);
      Iterator var4 = allErrors.iterator();

      while(true) {
         FieldError fieldError1;
         FieldError addedError;
         do {
            ObjectError fieldError;
            do {
               if(!var4.hasNext()) {
                  var4 = fieldErrorMap.values().iterator();

                  while(var4.hasNext()) {
                     FieldError fieldError2 = (FieldError)var4.next();
                     messages.add(new ValidationErrorDto.ValidationErrorMessage(fieldError2.getField(), fieldError2.getDefaultMessage()));
                  }

                  return messages;
               }

               fieldError = (ObjectError)var4.next();
            } while(!(fieldError instanceof FieldError));

            fieldError1 = (FieldError)fieldError;
            if(!fieldErrorMap.containsKey(fieldError1.getField())) {
               break;
            }

            addedError = (FieldError)fieldErrorMap.get(fieldError1.getField());
         } while(this.indexOfCode(addedError.getCode()) < this.indexOfCode(fieldError1.getCode()));

         fieldErrorMap.put(fieldError1.getField(), fieldError1);
      }
   }

   private int indexOfCode(String code) {
      int index = codesOrder.indexOf(code);
      return index >= 0?index:Integer.MAX_VALUE;
   }
}
