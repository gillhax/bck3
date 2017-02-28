package quiz.web.rest;

import io.swagger.annotations.Api;
import javax.inject.Inject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import quiz.domain.Question;
import quiz.service.util.ParseQuestionsFile;
import quiz.web.rest.util.FilesUpload;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping({"/api/v1/parser/"})
@Api(
   tags = {"Parser"}
)
@ApiIgnore
public class ParserController {
   @Inject
   private ParseQuestionsFile parseQuestionsFile;

   @RequestMapping(
      value = {"upload"},
      method = {RequestMethod.POST}
   )
   public ResponseEntity uploadFilesParse(FilesUpload filesUpload) {
      this.parseQuestionsFile.main(filesUpload.getFiles());
      HttpHeaders headers = new HttpHeaders();
      headers.add("X-quizApp-alert", "quizApp.question.ExcelUploaded");
      headers.add("X-quizApp-params", "");
      return ((BodyBuilder)ResponseEntity.ok().headers(headers)).body(new Question());
   }
}
