package quiz.converter;

import javax.inject.Inject;
import org.springframework.stereotype.Component;
import quiz.converter.Converter;
import quiz.converter.SubcategoryConverter;
import quiz.domain.Category;
import quiz.service.dto.CategoryDto;

@Component
public class CategoryConverter extends Converter {
   @Inject
   private SubcategoryConverter subcategoryConverter;

   public CategoryDto toDTO(Category category) {
      if(category == null) {
         return null;
      } else {
         CategoryDto categoryDto = new CategoryDto();
         categoryDto.setId(category.getId());
         categoryDto.setName(category.getName());
         categoryDto.setSubcategories(this.subcategoryConverter.toDTOs(category.getSubcategories()));
         return categoryDto;
      }
   }
}
