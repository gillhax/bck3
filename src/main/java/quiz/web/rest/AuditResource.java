package quiz.web.rest;

import io.swagger.annotations.ApiParam;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.function.Function;
import javax.inject.Inject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import quiz.service.AuditEventService;
import quiz.web.rest.util.PaginationUtil;

@RestController
@RequestMapping({"/management/audits"})
public class AuditResource {
   private AuditEventService auditEventService;

   @Inject
   public AuditResource(AuditEventService auditEventService) {
      this.auditEventService = auditEventService;
   }

   @GetMapping
   public ResponseEntity getAll(@ApiParam Pageable pageable) throws URISyntaxException {
      Page page = this.auditEventService.findAll(pageable);
      HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/management/audits");
      return new ResponseEntity(page.getContent(), headers, HttpStatus.OK);
   }

   @GetMapping(
      params = {"fromDate", "toDate"}
   )
   public ResponseEntity getByDates(@RequestParam("fromDate") LocalDate fromDate, @RequestParam("toDate") LocalDate toDate, @ApiParam Pageable pageable) throws URISyntaxException {
      Page page = this.auditEventService.findByDates(fromDate.atTime(0, 0), toDate.atTime(23, 59), pageable);
      HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/management/audits");
      return new ResponseEntity(page.getContent(), headers, HttpStatus.OK);
   }

   @GetMapping({"/{id:.+}"})
   public ResponseEntity get(@PathVariable Long id) {
      return (ResponseEntity)this.auditEventService.find(id).map((entity) -> {
         return new ResponseEntity(entity, HttpStatus.OK);
      }).orElse(new ResponseEntity(HttpStatus.NOT_FOUND));
   }
}
