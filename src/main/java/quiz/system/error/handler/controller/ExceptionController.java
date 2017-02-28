package quiz.system.error.handler.controller;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import quiz.service.LocalizationService;
import quiz.system.error.exception.ApiException;
import quiz.system.error.handler.dto.ErrorDto;

@ControllerAdvice
public class ExceptionController {
   private final LocalizationService localizationService;

   @Autowired
   public ExceptionController(LocalizationService localizationService) {
      this.localizationService = localizationService;
   }

   @ExceptionHandler({ApiException.class})
   public ResponseEntity handleRestException(ApiException e, HttpServletRequest request) {
      ErrorDto errorDto = new ErrorDto(e.getHttpStatus().value(), e.getError(), e.getClass().getName(), this.localizationService.getMessage(e.getError(), new String[0]), e.getDevMessage(), request.getServletPath());
      return new ResponseEntity(errorDto, e.getHttpStatus());
   }

   @ExceptionHandler({EntityNotFoundException.class})
   public ResponseEntity handleEntityNotFoundException(EntityNotFoundException e, HttpServletRequest request) {
      String error = "entity-not-found";
      ErrorDto errorDto = new ErrorDto(HttpStatus.NOT_FOUND.value(), error, e.getClass().getName(), this.localizationService.getMessage(error, new String[0]), e.toString(), request.getServletPath());
      return new ResponseEntity(errorDto, HttpStatus.NOT_FOUND);
   }

   @ExceptionHandler({MultipartException.class})
   public ResponseEntity handleMultipartException(MultipartException e, HttpServletRequest request) {
      String error = "file-upload-error";
      ErrorDto errorDto = new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), error, e.getClass().getName(), this.localizationService.getMessage(error, new String[0]), e.toString(), request.getServletPath());
      return new ResponseEntity(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
   }
}
