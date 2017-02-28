package quiz.web.rest;

import com.codahale.metrics.annotation.Timed;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.inject.Inject;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import quiz.domain.MediaContainer;
import quiz.service.ImageService;
import quiz.service.MediaContainerService;
import quiz.web.rest.util.HeaderUtil;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
@RequestMapping({"/api"})
public class MediaResource {
   @Inject
   private MediaContainerService mediaContainerService;
   @Inject
   private ImageService imageService;

   @Timed
   @RequestMapping(
      value = {"media"},
      method = {RequestMethod.POST},
      produces = {"application/json;charset=UTF-8"},
      consumes = {"multipart/form-data"}
   )
   public ResponseEntity createMedia(@RequestParam MultipartFile image) throws URISyntaxException {
      MediaContainer result = this.mediaContainerService.create(image);
      return ((BodyBuilder)ResponseEntity.created(new URI("/api/media/" + result.getId())).headers(HeaderUtil.createEntityCreationAlert("media", result.getId().toString()))).body(result);
   }

   @Timed
   @RequestMapping(
      value = {"media/{id}"},
      method = {RequestMethod.PUT},
      produces = {"application/json;charset=UTF-8"},
      consumes = {"multipart/form-data"}
   )
   public ResponseEntity updateMedia(@PathVariable long id, @RequestParam MultipartFile image) throws URISyntaxException {
      MediaContainer result = this.mediaContainerService.update(id, image);
      return ((BodyBuilder)ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("media", this.toString()))).body(result);
   }

   @GetMapping({"/media"})
   @Timed
   public List getAllMedia() {
      return this.mediaContainerService.findAll();
   }

   @DeleteMapping({"/media/{id}"})
   @Timed
   public ResponseEntity deleteMedia(@PathVariable Long id) {
      this.mediaContainerService.delete(id);
      return ((BodyBuilder)ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("media", id.toString()))).build();
   }
}
