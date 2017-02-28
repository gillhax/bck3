package quiz.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.ApiParam;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.function.Function;
import javax.inject.Inject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quiz.domain.OfferTradition;
import quiz.service.OfferTraditionService;
import quiz.web.rest.util.HeaderUtil;
import quiz.web.rest.util.PaginationUtil;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@ApiIgnore
@RequestMapping({"/api"})
public class OfferResource {
   @Inject
   private OfferTraditionService offerTraditionService;

   @GetMapping({"/offers"})
   @Timed
   public ResponseEntity getAllOfferTradition(@ApiParam Pageable pageable) throws URISyntaxException {
      Page offerTradition = this.offerTraditionService.findAll(pageable);
      HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(offerTradition, "/api/offer/tradition");
      return new ResponseEntity(offerTradition.getContent(), headers, HttpStatus.OK);
   }

   @GetMapping({"/offers/{id}"})
   @Timed
   public ResponseEntity getOfferTradition(@PathVariable Long id) {
      OfferTradition offerTradition = this.offerTraditionService.findOne(id);
      return (ResponseEntity)Optional.ofNullable(offerTradition).map((result) -> {
         return new ResponseEntity(result, HttpStatus.OK);
      }).orElse(new ResponseEntity(HttpStatus.NOT_FOUND));
   }

   @DeleteMapping({"/offers/{id}"})
   @Timed
   public ResponseEntity deleteOfferTradition(@PathVariable Long id) {
      this.offerTraditionService.delete(id);
      return ((BodyBuilder)ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("offerTradition", id.toString()))).build();
   }
}
