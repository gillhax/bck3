package quiz.service;

import java.sql.Timestamp;
import javax.inject.Inject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import quiz.domain.Version;
import quiz.repository.VersionRepository;
import quiz.service.dto.VersionDto;
import quiz.system.error.ApiAssert;

@Service
@Transactional
public class VersionService {
   @Inject
   private VersionRepository versionRepository;

   public void refreshAvatars() {
      Version version = (Version)this.versionRepository.getOne(Integer.valueOf(1));
      ApiAssert.notFound(version == null, "not-found.entity");
      version.setAvatars(new Timestamp(System.currentTimeMillis()));
      this.versionRepository.flush();
   }

   public void refreshCategories() {
      Version version = (Version)this.versionRepository.getOne(Integer.valueOf(1));
      ApiAssert.notFound(version == null, "not-found.entity");
      version.setCategories(new Timestamp(System.currentTimeMillis()));
      this.versionRepository.flush();
   }

   public void refreshHelps() {
      Version version = (Version)this.versionRepository.getOne(Integer.valueOf(1));
      ApiAssert.notFound(version == null, "not-found.entity");
      version.setHelps(new Timestamp(System.currentTimeMillis()));
      this.versionRepository.flush();
   }

   public void refreshQuestions() {
      Version version = (Version)this.versionRepository.getOne(Integer.valueOf(1));
      ApiAssert.notFound(version == null, "not-found.entity");
      version.setQuestions(new Timestamp(System.currentTimeMillis()));
      this.versionRepository.flush();
   }

   public VersionDto getVersions() {
      Version version = (Version)this.versionRepository.getOne(Integer.valueOf(1));
      ApiAssert.notFound(version == null, "not-found.entity");
      VersionDto versionDto = new VersionDto();
      versionDto.setAvatars(version.getAvatars().getTime());
      versionDto.setCategories(version.getCategories().getTime());
      versionDto.setHelps(version.getHelps().getTime());
      versionDto.setQuestions(version.getQuestions().getTime());
      return versionDto;
   }
}
