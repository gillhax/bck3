package quiz.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.ApiParam;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.function.Function;
import javax.inject.Inject;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quiz.domain.Subcategory;
import quiz.service.SubcategoryService;
import quiz.service.VersionService;
import quiz.web.rest.util.HeaderUtil;
import quiz.web.rest.util.PaginationUtil;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
@RequestMapping({"/api"})
public class SubcategoryResource {
   private final Logger log = LoggerFactory.getLogger(SubcategoryResource.class);
   @Inject
   private SubcategoryService subcategoryService;
   @Inject
   private VersionService versionService;

   @PostMapping({"/subcategories"})
   @Timed
   public ResponseEntity createSubcategory(@Valid @RequestBody Subcategory subcategory) throws URISyntaxException {
      this.log.debug("REST request to save Subcategory : {}", subcategory);
      if(subcategory.getId() != null) {
         return ((BodyBuilder)ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("subcategory", "idexists", "A new subcategory cannot already have an ID"))).body((Object)null);
      } else {
         Subcategory result = this.subcategoryService.save(subcategory);
         this.versionService.refreshQuestions();
         return ((BodyBuilder)ResponseEntity.created(new URI("/api/subcategories/" + result.getId())).headers(HeaderUtil.createEntityCreationAlert("subcategory", result.getId().toString()))).body(result);
      }
   }

   @PutMapping({"/subcategories"})
   @Timed
   public ResponseEntity updateSubcategory(@Valid @RequestBody Subcategory subcategory) throws URISyntaxException {
      this.log.debug("REST request to update Subcategory : {}", subcategory);
      if(subcategory.getId() == null) {
         return this.createSubcategory(subcategory);
      } else {
         Subcategory result = this.subcategoryService.save(subcategory);
         this.versionService.refreshQuestions();
         return ((BodyBuilder)ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("subcategory", subcategory.getId().toString()))).body(result);
      }
   }

   @GetMapping({"/subcategories"})
   @Timed
   public ResponseEntity getAllSubcategories(@ApiParam Pageable pageable) throws URISyntaxException {
      this.log.debug("REST request to get a page of Subcategories");
      Page page = this.subcategoryService.findAll(pageable);
      HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/subcategories");
      return new ResponseEntity(page.getContent(), headers, HttpStatus.OK);
   }

   @GetMapping({"/subcategories/{id}"})
   @Timed
   public ResponseEntity getSubcategory(@PathVariable Long id) {
      this.log.debug("REST request to get Subcategory : {}", id);
      Subcategory subcategory = this.subcategoryService.findOne(id);
      return (ResponseEntity)Optional.ofNullable(subcategory).map((result) -> {
         return new ResponseEntity(result, HttpStatus.OK);
      }).orElse(new ResponseEntity(HttpStatus.NOT_FOUND));
   }

   @DeleteMapping({"/subcategories/{id}"})
   @Timed
   public ResponseEntity deleteSubcategory(@PathVariable Long id) {
      this.log.debug("REST request to delete Subcategory : {}", id);
      this.subcategoryService.delete(id);
      this.versionService.refreshQuestions();
      return ((BodyBuilder)ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("subcategory", id.toString()))).build();
   }
}
