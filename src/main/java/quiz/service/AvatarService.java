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
import quiz.converter.AvatarConverter;
import quiz.domain.Avatar;
import quiz.repository.AvatarRepository;
import quiz.service.ImageService;
import quiz.service.VersionService;
import quiz.system.error.ApiAssert;

@Service
@Transactional
public class AvatarService {
   private final Logger log = LoggerFactory.getLogger(AvatarService.class);
   @Inject
   private AvatarRepository avatarRepository;
   @Inject
   private ImageService imageService;
   @Inject
   private AvatarConverter avatarConverter;
   @Inject
   private VersionService versionService;

   public Avatar create(MultipartFile image) {
      final Avatar avatar = new Avatar();
      this.imageService.save(image, "avatar", new ImageService.ImageBridge[]{new ImageService.ImageBridge() {
         void saveNew(String imageName) {
            avatar.setPath(imageName);
         }

         String getOld() {
            return null;
         }
      }});
      Avatar result = (Avatar)this.avatarRepository.save(avatar);
      return result;
   }

   public Avatar update(int avatarId, MultipartFile image) {
      final Avatar avatar = (Avatar)this.avatarRepository.findOne(Integer.valueOf(avatarId));
      ApiAssert.notFound(avatar == null, "not-found.avatar");
      if(avatar == null) {
         return null;
      } else {
         this.imageService.save(image, "avatar", new ImageService.ImageBridge[]{new ImageService.ImageBridge() {
            void saveNew(String imageName) {
               avatar.setPath(imageName);
            }

            String getOld() {
               return avatar.getPath();
            }
         }});
         Avatar result = (Avatar)this.avatarRepository.save(avatar);
         return result;
      }
   }

   @Transactional(
      readOnly = true
   )
   public List findAll() {
      this.log.debug("Request to get all Avatars");
      List result = this.avatarRepository.findAll();
      return result;
   }

   @Transactional(
      readOnly = true
   )
   public Avatar findOne(Integer id) {
      this.log.debug("Request to get Avatar : {}", id);
      Avatar avatar = (Avatar)this.avatarRepository.findOne(id);
      return avatar;
   }

   public void delete(Integer id) {
      this.log.debug("Request to delete Avatar : {}", id);
      Avatar avatar = (Avatar)this.avatarRepository.findOne(id);
      this.imageService.delete(avatar.getPath());
      this.avatarRepository.delete(id);
   }

   public Map getAll(Long version) {
      long avatarVersion = this.versionService.getVersions().getAvatars();
      if(version != null && version.longValue() >= avatarVersion) {
         return null;
      } else {
         HashMap result = new HashMap();
         result.put("version", Long.valueOf(avatarVersion));
         List avatars = this.avatarRepository.findAll();
         result.put("avatars", this.avatarConverter.toDTOs(avatars));
         return result;
      }
   }
}
