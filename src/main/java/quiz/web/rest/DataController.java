package quiz.web.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.inject.Inject;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import quiz.domain.Help;
import quiz.service.AvatarService;
import quiz.service.CategoryService;
import quiz.service.HelpService;
import quiz.service.OfferTraditionService;
import quiz.service.UserService;
import quiz.service.VersionService;
import quiz.service.dto.AvatarDto;
import quiz.service.dto.CategoryDto;
import quiz.service.dto.OfferTraditionDtoIn;
import quiz.service.dto.VersionDto;
import quiz.system.error.handler.dto.ResponseDto;
import quiz.system.util.StaticWrapper;

@RestController
@RequestMapping({"/api/v1/"})
@Api(
   tags = {"Data"}
)
public class DataController {
   @Inject
   private AvatarService avatarService;
   @Inject
   private HelpService helpService;
   @Inject
   private VersionService versionService;
   @Inject
   private OfferTraditionService offerTraditionService;
   @Inject
   private CategoryService categoryService;
   @Inject
   private UserService userService;

   @RequestMapping(
      value = {"/avatars"},
      method = {RequestMethod.GET}
   )
   @ApiOperation(
      value = "Get avatar list",
      response = AvatarDto.class
   )
   public ResponseDto getAvatars(@RequestParam(
   required = false
) Long version) {
      return StaticWrapper.wrap(this.avatarService.getAll(version));
   }

   @RequestMapping(
      value = {"/help"},
      method = {RequestMethod.GET}
   )
   @ApiOperation(
      value = "Get help list",
      response = Help.class
   )
   public ResponseDto getHelp(@RequestParam(
   required = false
) Long version) {
      return StaticWrapper.wrap(this.helpService.getHelp(version));
   }

   @RequestMapping(
      value = {"/versions"},
      method = {RequestMethod.GET}
   )
   @ApiOperation(
      value = "Get data versions",
      response = VersionDto.class
   )
   public ResponseDto getVersions() {
      return StaticWrapper.wrap(this.versionService.getVersions());
   }

   @Secured({"ROLE_USER"})
   @RequestMapping(
      value = {"/offer/tradition"},
      method = {RequestMethod.POST}
   )
   @ApiOperation(
      value = "Offer ethnic tradition",
      response = OfferTraditionDtoIn.class
   )
   public ResponseDto offerTradition(@RequestBody OfferTraditionDtoIn offerTraditionDtoIn) {
      this.offerTraditionService.create(this.userService.getUserWithAuthorities(), offerTraditionDtoIn);
      return StaticWrapper.wrap();
   }

   @RequestMapping(
      value = {"/questions"},
      method = {RequestMethod.GET}
   )
   @ApiOperation(
      value = "Questions with categories",
      response = CategoryDto.class
   )
   public ResponseDto getAllWrapQuestions(@RequestParam(
   required = false
) Long version) {
      return StaticWrapper.wrap(this.categoryService.getAll(version));
   }
}
