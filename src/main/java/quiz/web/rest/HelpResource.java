package quiz.web.rest;

import com.codahale.metrics.annotation.Timed;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import quiz.domain.Help;
import quiz.service.HelpService;
import quiz.service.VersionService;
import quiz.service.dto.admin.HelpAdminDto;
import quiz.system.error.handler.dto.ResponseDto;
import quiz.system.util.StaticWrapper;
import quiz.web.rest.util.HeaderUtil;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping({"/api"})
public class HelpResource {
   private final Logger log = LoggerFactory.getLogger(HelpResource.class);
   @Inject
   private HelpService helpService;
   @Inject
   private VersionService versionService;

   @Timed
   @RequestMapping(
      value = {"helps"},
      method = {RequestMethod.POST},
      produces = {"application/json;charset=UTF-8"},
      consumes = {"multipart/form-data"}
   )
   public ResponseDto createHelp(@RequestParam String title, @RequestParam String description, @RequestParam MultipartFile image) throws URISyntaxException {
      Help result = this.helpService.save(title, description, image);
      this.versionService.refreshHelps();
      return StaticWrapper.wrap(result);
   }

   @ApiIgnore
   @Timed
   @RequestMapping(
      value = {"help"},
      method = {RequestMethod.POST}
   )
   public ResponseDto createHelpAdmin(@RequestBody HelpAdminDto helpAdminDto) throws URISyntaxException {
      Help result = this.helpService.saveDto(helpAdminDto);
      this.versionService.refreshHelps();
      return StaticWrapper.wrap(result);
   }

   @RequestMapping(
      value = {"helps/{id}"},
      method = {RequestMethod.PUT},
      produces = {"application/json;charset=UTF-8"},
      consumes = {"multipart/form-data"}
   )
   @Timed
   public ResponseDto updateHelp(@PathVariable long id, @RequestParam String title, @RequestParam String description, @RequestParam MultipartFile image) throws URISyntaxException {
      Help result = this.helpService.update(Long.valueOf(id), title, description, image);
      this.versionService.refreshHelps();
      return StaticWrapper.wrap(result);
   }

   @ApiIgnore
   @RequestMapping(
      value = {"help"},
      method = {RequestMethod.PUT}
   )
   @Timed
   public ResponseDto updateHelpAdmin(@RequestBody HelpAdminDto helpAdminDto) {
      Help result = this.helpService.updateDto(helpAdminDto);
      this.versionService.refreshHelps();
      return StaticWrapper.wrap(result);
   }

   @ApiIgnore
   @GetMapping({"/helps"})
   @Timed
   public List getAllHelps() {
      this.log.debug("REST request to get all Helps");
      return this.helpService.findAll();
   }

   @ApiIgnore
   @GetMapping({"/help"})
   @Timed
   public List getAllHelpsAdmin() {
      this.log.debug("REST request to get all Helps");
      return this.helpService.findAll();
   }

   @ApiIgnore
   @GetMapping({"/help/{id}"})
   @Timed
   public ResponseEntity getHelpAdmin(@PathVariable Long id) {
      Help help = this.helpService.findOne(id);
      return (ResponseEntity)Optional.ofNullable(help).map((result) -> {
         return new ResponseEntity(result, HttpStatus.OK);
      }).orElse(new ResponseEntity(HttpStatus.NOT_FOUND));
   }

   @ApiIgnore
   @GetMapping({"/helps/{id}"})
   @Timed
   public ResponseEntity getHelp(@PathVariable Long id) {
      this.log.debug("REST request to get Help : {}", id);
      Help help = this.helpService.findOne(id);
      return (ResponseEntity)Optional.ofNullable(help).map((result) -> {
         return new ResponseEntity(result, HttpStatus.OK);
      }).orElse(new ResponseEntity(HttpStatus.NOT_FOUND));
   }

   @DeleteMapping({"/helps/{id}"})
   @Timed
   public ResponseEntity deleteHelp(@PathVariable Long id) {
      this.log.debug("REST request to delete Help : {}", id);
      this.helpService.delete(id);
      this.versionService.refreshHelps();
      return ((BodyBuilder)ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("help", id.toString()))).build();
   }

   @ApiIgnore
   @DeleteMapping({"/help/{id}"})
   @Timed
   public ResponseEntity deleteHelpAdmin(@PathVariable Long id) {
      this.log.debug("REST request to delete Help : {}", id);
      this.helpService.delete(id);
      this.versionService.refreshHelps();
      return ((BodyBuilder)ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("help", id.toString()))).build();
   }
}
