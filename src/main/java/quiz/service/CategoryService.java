package quiz.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import quiz.converter.CategoryConverter;
import quiz.domain.Category;
import quiz.repository.CategoryRepository;
import quiz.repository.QuestionRepository;
import quiz.service.VersionService;

@Service
@Transactional
public class CategoryService {
   private final Logger log = LoggerFactory.getLogger(CategoryService.class);
   @Inject
   private CategoryRepository categoryRepository;
   @Inject
   private CategoryConverter categoryConverter;
   @Inject
   private QuestionRepository questionRepository;
   @Inject
   private VersionService versionService;

   public Category save(Category category) {
      this.log.debug("Request to save Category : {}", category);
      Category result = (Category)this.categoryRepository.save(category);
      return result;
   }

   @Transactional(
      readOnly = true
   )
   public Page findAll(Pageable pageable) {
      this.log.debug("Request to get all Categories");
      Page result = this.categoryRepository.findAll(pageable);
      result.getContent().size();
      return result;
   }

   @Transactional
   public Map getAll(Long version) {
      long questionsVersion = this.versionService.getVersions().getQuestions();
      if(version != null && version.longValue() >= questionsVersion) {
         return null;
      } else {
         HashMap result = new HashMap();
         result.put("version", Long.valueOf(questionsVersion));
         List categories = this.categoryRepository.findAllEager();
         result.put("categories", this.categoryConverter.toDTOs(categories));
         return result;
      }
   }

   @Transactional(
      readOnly = true
   )
   public Category findOne(Long id) {
      this.log.debug("Request to get Category : {}", id);
      Category category = (Category)this.categoryRepository.findOne(id);
      return category;
   }

   public void delete(Long id) {
      this.log.debug("Request to delete Category : {}", id);
      this.categoryRepository.delete(id);
   }
}
