package quiz.web.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.inject.Inject;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import quiz.service.QuestionService;
import quiz.service.dto.QuestionDto;
import quiz.system.error.handler.dto.ResponseDto;
import quiz.system.util.StaticWrapper;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping({"/api/v1/"})
@ApiIgnore
@Api(
   tags = {"Question"}
)
public class QuestionController {
   @Inject
   private QuestionService questionService;

   @RequestMapping(
      value = {"/questions/{id}"},
      method = {RequestMethod.GET}
   )
   @ApiOperation(
      value = "Get question by id",
      response = QuestionDto.class
   )
   public ResponseDto getQuestion(@PathVariable Long id) {
      return StaticWrapper.wrap(this.questionService.findById(id));
   }
}
