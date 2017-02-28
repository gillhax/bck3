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
import quiz.domain.Category;
import quiz.service.CategoryService;
import quiz.service.VersionService;
import quiz.web.rest.util.HeaderUtil;
import quiz.web.rest.util.PaginationUtil;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@ApiIgnore
@RequestMapping({"/api"})
public class CategoryResource {
   private final Logger log = LoggerFactory.getLogger(CategoryResource.class);
   @Inject
   private CategoryService categoryService;
   @Inject
   private VersionService versionService;

   @PostMapping({"/categories"})
   @Timed
   public ResponseEntity createCategory(@Valid @RequestBody Category category) throws URISyntaxException {
      this.log.debug("REST request to save Category : {}", category);
      if(category.getId() != null) {
         return ((BodyBuilder)ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("category", "idexists", "A new category cannot already have an ID"))).body((Object)null);
      } else {
         Category result = this.categoryService.save(category);
         this.versionService.refreshCategories();
         this.versionService.refreshQuestions();
         return ((BodyBuilder)ResponseEntity.created(new URI("/api/categories/" + result.getId())).headers(HeaderUtil.createEntityCreationAlert("category", result.getId().toString()))).body(result);
      }
   }

   @PutMapping({"/categories"})
   @Timed
   public ResponseEntity updateCategory(@Valid @RequestBody Category category) throws URISyntaxException {
      this.log.debug("REST request to update Category : {}", category);
      if(category.getId() == null) {
         return this.createCategory(category);
      } else {
         Category result = this.categoryService.save(category);
         this.versionService.refreshCategories();
         this.versionService.refreshQuestions();
         return ((BodyBuilder)ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("category", category.getId().toString()))).body(result);
      }
   }

   @GetMapping({"/categories"})
   @Timed
   public ResponseEntity getAllCategories(@ApiParam Pageable pageable) throws URISyntaxException {
      this.log.debug("REST request to get a page of Categories");
      Page page = this.categoryService.findAll(pageable);
      HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/categories");
      return new ResponseEntity(page.getContent(), headers, HttpStatus.OK);
   }

   @GetMapping({"/categories/{id}"})
   @Timed
   public ResponseEntity getCategory(@PathVariable Long id) {
      this.log.debug("REST request to get Category : {}", id);
      Category category = this.categoryService.findOne(id);
      return (ResponseEntity)Optional.ofNullable(category).map((result) -> {
         return new ResponseEntity(result, HttpStatus.OK);
      }).orElse(new ResponseEntity(HttpStatus.NOT_FOUND));
   }

   @DeleteMapping({"/categories/{id}"})
   @Timed
   public ResponseEntity deleteCategory(@PathVariable Long id) {
      this.log.debug("REST request to delete Category : {}", id);
      this.categoryService.delete(id);
      this.versionService.refreshCategories();
      this.versionService.refreshQuestions();
      return ((BodyBuilder)ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("category", id.toString()))).build();
   }
}
