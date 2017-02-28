package quiz.service;

import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import quiz.domain.Subcategory;
import quiz.repository.SubcategoryRepository;

@Service
@Transactional
public class SubcategoryService {
   private final Logger log = LoggerFactory.getLogger(SubcategoryService.class);
   @Inject
   private SubcategoryRepository subcategoryRepository;

   public Subcategory save(Subcategory subcategory) {
      this.log.debug("Request to save Subcategory : {}", subcategory);
      Subcategory result = (Subcategory)this.subcategoryRepository.save(subcategory);
      return result;
   }

   @Transactional(
      readOnly = true
   )
   public Page findAll(Pageable pageable) {
      this.log.debug("Request to get all Subcategories");
      Page result = this.subcategoryRepository.findAll(pageable);
      return result;
   }

   @Transactional(
      readOnly = true
   )
   public Subcategory findOne(Long id) {
      this.log.debug("Request to get Subcategory : {}", id);
      Subcategory subcategory = (Subcategory)this.subcategoryRepository.findOne(id);
      return subcategory;
   }

   public void delete(Long id) {
      this.log.debug("Request to delete Subcategory : {}", id);
      this.subcategoryRepository.delete(id);
   }
}
