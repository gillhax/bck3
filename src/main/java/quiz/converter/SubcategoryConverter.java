package quiz.converter;

import javax.inject.Inject;
import org.springframework.stereotype.Component;
import quiz.converter.Converter;
import quiz.converter.QuestionConverter;
import quiz.domain.Subcategory;
import quiz.service.dto.SubcategoryDto;

@Component
public class SubcategoryConverter extends Converter {
   @Inject
   private QuestionConverter questionConverter;

   public SubcategoryDto toDTO(Subcategory subcategory) {
      if(subcategory == null) {
         return null;
      } else {
         SubcategoryDto subcategoryDto = new SubcategoryDto();
         subcategoryDto.setId(subcategory.getId());
         subcategoryDto.setName(subcategory.getName());
         subcategoryDto.setQuestions(this.questionConverter.toDTOs(subcategory.getQuestions()));
         return subcategoryDto;
      }
   }
}
