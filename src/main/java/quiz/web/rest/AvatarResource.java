package quiz.web.rest;

import com.codahale.metrics.annotation.Timed;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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
import quiz.domain.Avatar;
import quiz.service.AvatarService;
import quiz.service.ImageService;
import quiz.service.VersionService;
import quiz.web.rest.util.HeaderUtil;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping({"/api"})
public class AvatarResource {
   private final Logger log = LoggerFactory.getLogger(AvatarResource.class);
   @Inject
   private AvatarService avatarService;
   @Inject
   private ImageService imageService;
   @Inject
   private VersionService versionService;

   @Timed
   @RequestMapping(
      value = {"avatars"},
      method = {RequestMethod.POST},
      produces = {"application/json;charset=UTF-8"},
      consumes = {"multipart/form-data"}
   )
   public ResponseEntity createAvatar(@RequestParam MultipartFile image) throws URISyntaxException {
      Avatar result = this.avatarService.create(image);
      this.versionService.refreshAvatars();
      return ((BodyBuilder)ResponseEntity.created(new URI("/api/avatars/" + result.getId())).headers(HeaderUtil.createEntityCreationAlert("avatar", result.getId().toString()))).body(result);
   }

   @Timed
   @RequestMapping(
      value = {"avatars/{id}"},
      method = {RequestMethod.PUT},
      produces = {"application/json;charset=UTF-8"},
      consumes = {"multipart/form-data"}
   )
   public ResponseEntity updateAvatar(@PathVariable int id, @RequestParam MultipartFile image) throws URISyntaxException {
      Avatar result = this.avatarService.update(id, image);
      this.versionService.refreshAvatars();
      return ((BodyBuilder)ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("avatar", this.toString()))).body(result);
   }

   @ApiIgnore
   @GetMapping({"/avatars"})
   @Timed
   public List getAllAvatars() {
      this.log.debug("REST request to get all Avatars");
      return this.avatarService.findAll();
   }

   @ApiIgnore
   @GetMapping({"/avatars/{id}"})
   @Timed
   public ResponseEntity getAvatar(@PathVariable Integer id) {
      this.log.debug("REST request to get Avatar : {}", id);
      Avatar avatar = this.avatarService.findOne(id);
      return (ResponseEntity)Optional.ofNullable(avatar).map((result) -> {
         return new ResponseEntity(result, HttpStatus.OK);
      }).orElse(new ResponseEntity(HttpStatus.NOT_FOUND));
   }

   @DeleteMapping({"/avatars/{id}"})
   @Timed
   public ResponseEntity deleteAvatar(@PathVariable Integer id) {
      this.log.debug("REST request to delete Avatar : {}", id);
      this.avatarService.delete(id);
      this.versionService.refreshAvatars();
      return ((BodyBuilder)ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("avatar", id.toString()))).build();
   }
}
