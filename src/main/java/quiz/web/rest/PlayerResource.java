package quiz.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.ApiParam;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quiz.converter.PlayerConverter;
import quiz.domain.Player;
import quiz.domain.User;
import quiz.security.SecurityUtils;
import quiz.service.PlayerService;
import quiz.service.UserService;
import quiz.service.dto.PlayerDtoOut;
import quiz.web.rest.util.HeaderUtil;
import quiz.web.rest.util.PaginationUtil;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
@RequestMapping({"/api"})
public class PlayerResource {
   private final Logger log = LoggerFactory.getLogger(PlayerResource.class);
   @Inject
   private PlayerService playerService;
   @Inject
   private UserService userService;
   @Inject
   private PlayerConverter playerConverter;

   @PutMapping({"/players"})
   @Timed
   public ResponseEntity updatePlayer(@Valid @RequestBody Player player) throws URISyntaxException {
      this.log.debug("REST request to update Player : {}", player);
      Player result = this.playerService.update(player);
      return ((BodyBuilder)ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("player", player.getId().toString()))).body(result);
   }

   @GetMapping({"/players"})
   @Timed
   public ResponseEntity getAllPlayers(@ApiParam Pageable pageable) throws URISyntaxException {
      this.log.debug("REST request to get a page of Players");
      Page page = this.playerService.findAll(pageable);
      HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/players");
      return new ResponseEntity(page.getContent(), headers, HttpStatus.OK);
   }

   @GetMapping({"/players/{id}"})
   @Timed
   public ResponseEntity getPlayer(@PathVariable Long id) {
      this.log.debug("REST request to get Player : {}", id);
      Player player = this.playerService.findOne(id);
      return (ResponseEntity)Optional.ofNullable(player).map((result) -> {
         return new ResponseEntity(result, HttpStatus.OK);
      }).orElse(new ResponseEntity(HttpStatus.NOT_FOUND));
   }

   @DeleteMapping({"/players/{id}"})
   @Timed
   public ResponseEntity deletePlayer(@PathVariable Long id) {
      this.log.debug("REST request to delete Player : {}", id);
      this.playerService.delete(id);
      return ((BodyBuilder)ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("player", id.toString()))).build();
   }

   @GetMapping({"/player/{id}"})
   @Timed
   public ResponseEntity getPlayerById(@PathVariable Long PlayerId) {
      Player player = this.playerService.findOne(PlayerId);
      PlayerDtoOut playerDtoOut = this.playerConverter.toDTO(player);
      return (ResponseEntity)Optional.ofNullable(playerDtoOut).map((result) -> {
         return new ResponseEntity(result, HttpStatus.OK);
      }).orElse(new ResponseEntity(HttpStatus.NOT_FOUND));
   }

   @GetMapping({"/player/profile"})
   @Timed
   public ResponseEntity getPlayerProfile() {
      Optional user = this.userService.getUserWithAuthoritiesByLogin(SecurityUtils.getCurrentUserLogin());
      Player player = this.playerService.findOne(((User)user.get()).getId());
      PlayerDtoOut playerDtoOut = this.playerConverter.toDTO(player);
      return (ResponseEntity)Optional.ofNullable(playerDtoOut).map((result) -> {
         return new ResponseEntity(result, HttpStatus.OK);
      }).orElse(new ResponseEntity(HttpStatus.NOT_FOUND));
   }
}
