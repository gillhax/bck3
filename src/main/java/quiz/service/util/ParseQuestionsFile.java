package quiz.service.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.apache.commons.collections4.map.LinkedMap;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import quiz.domain.Category;
import quiz.domain.MediaContainer;
import quiz.domain.Question;
import quiz.domain.Subcategory;
import quiz.repository.CategoryRepository;
import quiz.repository.QuestionRepository;
import quiz.repository.SubcategoryRepository;
import quiz.service.MediaContainerService;
import quiz.service.VersionService;
import quiz.system.error.ApiAssert;

@Service
@Transactional
public class ParseQuestionsFile {
   @Inject
   private CategoryRepository categoryRepository;
   @Inject
   private SubcategoryRepository subcategoryRepository;
   @Inject
   private QuestionRepository questionRepository;
   @Inject
   private MediaContainerService mediaContainerService;
   @Inject
   private VersionService versionService;

   public void main(List files) {
      XSSFWorkbook workbook = null;
      HashMap filesDictionary = new HashMap();
      MultipartFile excelMultipartFile = null;
      Iterator questionIdImageMap = files.iterator();

      while(questionIdImageMap.hasNext()) {
         MultipartFile categories = (MultipartFile)questionIdImageMap.next();
         String categorySheet = categories.getOriginalFilename();
         if(FilenameUtils.getExtension(categorySheet).equals("xlsx")) {
            excelMultipartFile = categories;
         } else {
            filesDictionary.put(categorySheet, categories);
         }
      }

      if(excelMultipartFile == null) {
         ApiAssert.unprocessable(true, "Отсутсвует Excel файл");
      }

      LinkedMap var30 = new LinkedMap();

      try {
         File var31 = new File(excelMultipartFile.getOriginalFilename());
         var31.createNewFile();
         FileOutputStream var33 = new FileOutputStream(var31);
         var33.write(excelMultipartFile.getBytes());
         var33.close();
         FileInputStream categoryIterator = new FileInputStream(var31);
         workbook = new XSSFWorkbook(categoryIterator);
      } catch (FileNotFoundException var28) {
         var28.printStackTrace();
      } catch (IOException var29) {
         var29.printStackTrace();
      }

      ArrayList var32 = new ArrayList();
      Sheet var34 = workbook.getSheetAt(1);
      Iterator var35 = var34.iterator();
      HashMap categoriesDictonary = new HashMap();
      Row firstRow = (Row)var35.next();
      Iterator firstRowCellIterator = firstRow.iterator();
      firstRowCellIterator.next();
      firstRowCellIterator.next();

      while(firstRowCellIterator.hasNext()) {
         Cell subcategoriesDictionary = (Cell)firstRowCellIterator.next();
         double subcategoriesRep = subcategoriesDictionary.getNumericCellValue();
         long twoRow = (long)subcategoriesRep;
         Cell categoryId = (Cell)firstRowCellIterator.next();
         String questionsSheet = categoryId.getStringCellValue();
         Category iterator = new Category();
         iterator.setId(Long.valueOf(twoRow));
         iterator.setName(categoryId.getStringCellValue());
         var32.add(iterator);
         categoriesDictonary.put(questionsSheet, Integer.valueOf((int)twoRow));
      }

      HashMap var36 = new HashMap();
      List var37 = this.subcategoryRepository.findAll();
      Map subcategoriesMap = (Map)var37.stream().collect(Collectors.toMap(Subcategory::getId, (p) -> {
         return p;
      }));
      Row var38 = (Row)var35.next();
      Iterator twoRowCellIterator = var38.iterator();
      twoRowCellIterator.next();
      twoRowCellIterator.next();
      int var39 = 0;

      String mediaContainer;
      while(twoRowCellIterator.hasNext()) {
         Cell var40 = (Cell)twoRowCellIterator.next();
         double var43 = var40.getNumericCellValue();
         long mediaDictionary = (long)var43;
         Cell question = (Cell)twoRowCellIterator.next();
         mediaContainer = question.getStringCellValue();
         Subcategory exist = new Subcategory();
         exist.setId(Long.valueOf(mediaDictionary));
         exist.setName(mediaContainer);
         ArrayList value = new ArrayList();
         value.add(exist);
         ((Category)var32.get(var39)).setSubcategories(value);
         ++var39;
         var36.put(mediaContainer, Long.valueOf(mediaDictionary));
      }

      Iterator var44;
      label117:
      while(var35.hasNext()) {
         Row var41 = (Row)var35.next();
         var44 = var41.iterator();
         var44.next();
         var44.next();
         var39 = 0;

         while(true) {
            while(true) {
               if(!var44.hasNext()) {
                  continue label117;
               }

               Cell questions = (Cell)var44.next();
               if(questions.getCellTypeEnum() == CellType.STRING && questions.getStringCellValue().equals("-")) {
                  var44.next();
                  ++var39;
               } else {
                  double var46 = questions.getNumericCellValue();
                  long var50 = (long)var46;
                  Cell var54 = (Cell)var44.next();
                  String var56 = var54.getStringCellValue();
                  Subcategory mediaContainer1 = new Subcategory();
                  mediaContainer1.setId(Long.valueOf(var50));
                  mediaContainer1.setName(var56);
                  ((Category)var32.get(var39)).getSubcategories().add(mediaContainer1);
                  ++var39;
                  var36.put(var56, Long.valueOf(var50));
               }
            }
         }
      }

      Sheet var42 = workbook.getSheetAt(0);
      var44 = var42.iterator();
      var44.next();
      ArrayList var45 = new ArrayList();

      Iterator i;
      Question var51;
      while(var44.hasNext()) {
         Row var47 = (Row)var44.next();
         i = var47.iterator();
         var51 = new Question();
         if(i.hasNext()) {
            Cell var53 = (Cell)i.next();
            var51.setTitle(this.validateCellValue(var53));
            if(i.hasNext()) {
               var53 = (Cell)i.next();
               var51.setAnswer1(this.validateCellValue(var53));
               var51.setRightAnswer(Integer.valueOf(1));
               if(i.hasNext()) {
                  var53 = (Cell)i.next();
                  var51.setAnswer2(this.validateCellValue(var53));
                  if(i.hasNext()) {
                     var53 = (Cell)i.next();
                     var51.setAnswer3(this.validateCellValue(var53));
                     if(i.hasNext()) {
                        var53 = (Cell)i.next();
                        var51.setAnswer4(this.validateCellValue(var53));
                        if(i.hasNext()) {
                           mediaContainer = ((Cell)i.next()).getStringCellValue();
                           String var55 = ((Cell)i.next()).getStringCellValue();
                           var51.setSubcategory((Subcategory)subcategoriesMap.get(var36.get(var55)));
                           if(i.hasNext()) {
                              var53 = (Cell)i.next();
                              var55 = this.validateCellValue(var53);
                              if(!var55.isEmpty() && !var55.equals("")) {
                                 if(filesDictionary.containsKey(var55)) {
                                    var30.put(Long.valueOf((long)var45.size()), var55);
                                 } else {
                                    ApiAssert.unprocessable(true, "Нету картинки, указанной в " + var53.getRowIndex() + "-ой строке Excel файла");
                                 }
                              }
                           }

                           var45.add(var51);
                        }
                     }
                  }
               }
            }
         }
      }

      HashMap var48 = new HashMap();
      i = filesDictionary.entrySet().iterator();

      while(i.hasNext()) {
         Entry var52 = (Entry)i.next();
         mediaContainer = (String)var52.getKey();
         boolean var57 = false;
         Iterator var58 = var30.values().iterator();

         while(var58.hasNext()) {
            String var61 = (String)var58.next();
            if(var61.equals(mediaContainer)) {
               var57 = true;
            }
         }

         if(var57) {
            MultipartFile var60 = (MultipartFile)var52.getValue();
            MediaContainer var62 = this.mediaContainerService.create(var60);
            var48.put(mediaContainer, var62);
         }
      }

      for(int var49 = 0; var49 < var45.size(); ++var49) {
         var51 = (Question)var45.get(var49);
         MediaContainer var59 = (MediaContainer)var48.get(var30.get(Long.valueOf((long)var49)));
         var51.setMedia(var59);
         this.questionRepository.save(var45.get(var49));
      }

      this.versionService.refreshQuestions();
   }

   private String validateCellValue(Cell cell) {
      return cell.getCellTypeEnum() == CellType.STRING?cell.getStringCellValue():(cell.getCellTypeEnum() == CellType.NUMERIC?Long.toString((long)cell.getNumericCellValue()):"");
   }
}
