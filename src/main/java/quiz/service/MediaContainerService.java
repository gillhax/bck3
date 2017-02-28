package quiz.service;

import java.util.List;
import javax.inject.Inject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import quiz.converter.AvatarConverter;
import quiz.domain.MediaContainer;
import quiz.domain.MediaType;
import quiz.repository.MediaContainerRepository;
import quiz.service.ImageService;
import quiz.service.VersionService;
import quiz.system.error.ApiAssert;

@Service
@Transactional
public class MediaContainerService {
   @Inject
   private MediaContainerRepository mediaContainerRepository;
   @Inject
   private ImageService imageService;
   @Inject
   private AvatarConverter avatarConverter;
   @Inject
   private VersionService versionService;

   public MediaContainer create(MultipartFile image) {
      final MediaContainer mediaContainer = new MediaContainer();
      this.imageService.save(image, "questions", new ImageService.ImageBridge[]{new ImageService.ImageBridge() {
         void saveNew(String imageName) {
            mediaContainer.setMedia(imageName);
         }

         String getOld() {
            return null;
         }
      }});
      mediaContainer.setMediaType(MediaType.PHOTO);
      MediaContainer result = (MediaContainer)this.mediaContainerRepository.saveAndFlush(mediaContainer);
      return result;
   }

   public MediaContainer update(long mediaId, MultipartFile image) {
      final MediaContainer mediaContainer = (MediaContainer)this.mediaContainerRepository.findOne(Long.valueOf(mediaId));
      ApiAssert.notFound(mediaContainer == null, "not-found.media");
      this.imageService.save(image, "questions", new ImageService.ImageBridge[]{new ImageService.ImageBridge() {
         void saveNew(String imageName) {
            mediaContainer.setMedia(imageName);
         }

         String getOld() {
            return mediaContainer.getMedia();
         }
      }});
      mediaContainer.setMediaType(MediaType.PHOTO);
      MediaContainer result = (MediaContainer)this.mediaContainerRepository.save(mediaContainer);
      return result;
   }

   @Transactional(
      readOnly = true
   )
   public List findAll() {
      List result = this.mediaContainerRepository.findAll();
      return result;
   }

   @Transactional(
      readOnly = true
   )
   public MediaContainer findOne(Long id) {
      MediaContainer mediaContainer = (MediaContainer)this.mediaContainerRepository.findOne(id);
      ApiAssert.notFound(mediaContainer == null, "not-found.media");
      return mediaContainer;
   }

   public void delete(Long id) {
      MediaContainer mediaContainer = (MediaContainer)this.mediaContainerRepository.findOne(id);
      ApiAssert.notFound(mediaContainer == null, "not-found.media");
      ApiAssert.unprocessable(!mediaContainer.getQuestions().isEmpty(), "media.questions.exist");
      this.imageService.delete(mediaContainer.getMedia());
      this.mediaContainerRepository.delete(id);
   }
}
