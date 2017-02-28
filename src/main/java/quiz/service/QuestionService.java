package quiz.service;

import java.util.List;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import quiz.converter.QuestionConverter;
import quiz.domain.Question;
import quiz.repository.QuestionRepository;
import quiz.service.dto.QuestionDto;
import quiz.system.error.ApiAssert;

@Service
@Transactional
public class QuestionService {
   private final Logger log = LoggerFactory.getLogger(QuestionService.class);
   @Inject
   private QuestionRepository questionRepository;
   @Inject
   private QuestionConverter questionConverter;

   public Question save(Question question) {
      this.log.debug("Request to save Question : {}", question);
      Question result = (Question)this.questionRepository.save(question);
      return result;
   }

   @Transactional(
      readOnly = true
   )
   public Page findAll(Pageable pageable) {
      this.log.debug("Request to get all Questions");
      Page result = this.questionRepository.findAll(pageable);
      return result;
   }

   @Transactional(
      readOnly = true
   )
   public Question findOne(Long id) {
      this.log.debug("Request to get Question : {}", id);
      Question question = (Question)this.questionRepository.findOne(id);
      return question;
   }

   public void delete(Long id) {
      this.log.debug("Request to delete Question : {}", id);
      this.questionRepository.delete(id);
   }

   public List getAllOffline() {
      List questions = this.questionRepository.findAll();
      return this.questionConverter.toDTOs(questions);
   }

   public QuestionDto findById(Long id) {
      Question question = (Question)this.questionRepository.findOne(id);
      ApiAssert.notFound(question == null, "not-found.question");
      return this.questionConverter.toDTO(question);
   }
}
