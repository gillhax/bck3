package quiz.service.dto;

import java.beans.ConstructorProperties;
import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class CategoryDto {
   Long id;
   @Min(1L)
   @Max(64L)
   String name;
   List subcategories;

   public Long getId() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public List getSubcategories() {
      return this.subcategories;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setSubcategories(List subcategories) {
      this.subcategories = subcategories;
   }

   public CategoryDto() {
   }

   @ConstructorProperties({"id", "name", "subcategories"})
   public CategoryDto(Long id, String name, List subcategories) {
      this.id = id;
      this.name = name;
      this.subcategories = subcategories;
   }
}
