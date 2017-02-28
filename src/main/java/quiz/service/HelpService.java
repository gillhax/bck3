package quiz.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import quiz.domain.Help;
import quiz.repository.HelpRepository;
import quiz.service.ImageService;
import quiz.service.VersionService;
import quiz.service.dto.admin.HelpAdminDto;
import quiz.system.error.ApiAssert;

@Service
@Transactional
public class HelpService {
   private final Logger log = LoggerFactory.getLogger(HelpService.class);
   @Inject
   private HelpRepository helpRepository;
   @Inject
   private ImageService imageService;
   @Inject
   private VersionService versionService;

   public Help save(String title, String description, MultipartFile image) {
      final Help newHelp = new Help();
      this.imageService.save(image, "help", new ImageService.ImageBridge[]{new ImageService.ImageBridge() {
         void saveNew(String imageName) {
            newHelp.setImage(imageName);
         }

         String getOld() {
            return null;
         }
      }});
      newHelp.setTitle(title);
      newHelp.setDescription(description);
      Help result = (Help)this.helpRepository.save(newHelp);
      return result;
   }

   public Help saveDto(HelpAdminDto helpAdminDto) {
      Help newHelp = new Help();
      newHelp.setTitle(helpAdminDto.getTitle());
      newHelp.setDescription(helpAdminDto.getDescription());
      String imagePath = this.imageService.saveBase64ImageNoLimit(helpAdminDto.getFile(), "help");
      newHelp.setImage(imagePath);
      Help result = (Help)this.helpRepository.save(newHelp);
      return result;
   }

   public Help updateDto(HelpAdminDto helpAdminDto) {
      Help help = (Help)this.helpRepository.findOne(helpAdminDto.getId());
      ApiAssert.notFound(help == null, "not-found.entity");
      if(helpAdminDto.getFile() != null) {
         String result = this.imageService.saveBase64ImageNoLimit(helpAdminDto.getFile(), "help");
         this.imageService.delete(helpAdminDto.getImage());
         help.setImage(result);
      }

      help.setDescription(helpAdminDto.getDescription());
      help.setTitle(helpAdminDto.getTitle());
      Help result1 = (Help)this.helpRepository.save(help);
      return result1;
   }

   public Help update(Long id, String title, String description, MultipartFile image) {
      final Help help = (Help)this.helpRepository.findOne(id);
      ApiAssert.notFound(help == null, "not-found.entity");
      this.imageService.save(image, "help", new ImageService.ImageBridge[]{new ImageService.ImageBridge() {
         void saveNew(String imageName) {
            help.setImage(imageName);
         }

         String getOld() {
            return help.getImage();
         }
      }});
      Help result = (Help)this.helpRepository.save(help);
      return result;
   }

   @Transactional(
      readOnly = true
   )
   public List findAll() {
      this.log.debug("Request to get all Helps");
      List result = this.helpRepository.findAll();
      return result;
   }

   @Transactional(
      readOnly = true
   )
   public Help findOne(Long id) {
      this.log.debug("Request to get Help : {}", id);
      Help help = (Help)this.helpRepository.findOne(id);
      return help;
   }

   public void delete(Long id) {
      Help help = (Help)this.helpRepository.findOne(id);
      ApiAssert.notFound(help == null, "not-found.entity");
      this.imageService.delete(help.getImage());
      this.helpRepository.delete(id);
   }

   public Map getHelp(Long version) {
      long helpVersion = this.versionService.getVersions().getHelps();
      if(version != null && version.longValue() >= helpVersion) {
         return null;
      } else {
         HashMap result = new HashMap();
         result.put("version", Long.valueOf(helpVersion));
         List helps = this.helpRepository.findAll();
         result.put("help", helps);
         return result;
      }
   }
}
