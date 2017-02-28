package quiz.service;

import java.util.Iterator;
import javax.inject.Inject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import quiz.domain.OfferTradition;
import quiz.domain.OfferTraditionAttach;
import quiz.domain.User;
import quiz.repository.OfferTraditionAttachRepository;
import quiz.repository.OfferTraditionRepository;
import quiz.service.ImageService;
import quiz.service.dto.OfferTraditionDtoIn;
import quiz.system.error.ApiAssert;

@Service
@Transactional
public class OfferTraditionService {
   @Inject
   private ImageService imageService;
   @Inject
   private OfferTraditionRepository OfferTraditionRepository;
   @Inject
   private OfferTraditionAttachRepository OfferTraditionAttachRepository;

   public void create(User user, OfferTraditionDtoIn offerTraditionDtoIn) {
      boolean withAttaches = offerTraditionDtoIn.getBase64Images() != null;
      OfferTradition offerTradition = new OfferTradition();
      offerTradition.setText(offerTraditionDtoIn.getText());
      offerTradition.setWithAttaches(withAttaches);
      if(user != null) {
         offerTradition.setUser(user);
      }

      OfferTradition savedOfferTradition = (OfferTradition)this.OfferTraditionRepository.saveAndFlush(offerTradition);
      if(withAttaches) {
         Iterator imagesIterator = offerTraditionDtoIn.getBase64Images().iterator();
         int i = 1;

         do {
            String path = this.imageService.saveBase64ImageWithLimit((String)imagesIterator.next(), "offer_tradition");
            OfferTraditionAttach attach = new OfferTraditionAttach();
            attach.setPath(path);
            attach.setOfferId(savedOfferTradition.getId());
            this.OfferTraditionAttachRepository.save(attach);
            ++i;
         } while(imagesIterator.hasNext() && i <= 3);
      }

   }

   public Page findAll(Pageable pageable) {
      Page offerTraditions = this.OfferTraditionRepository.findAll(pageable);
      Iterator var3 = offerTraditions.iterator();

      while(var3.hasNext()) {
         OfferTradition offer = (OfferTradition)var3.next();
         if(offer.isWithAttaches()) {
            offer.getAttaches().size();
         }
      }

      return offerTraditions;
   }

   public OfferTradition findOne(Long id) {
      OfferTradition offerTradition = (OfferTradition)this.OfferTraditionRepository.findOne(id);
      ApiAssert.notFound(offerTradition == null, "not-found.entity");
      if(offerTradition.isWithAttaches()) {
         offerTradition.getAttaches().size();
      }

      offerTradition.getUser().getId();
      return offerTradition;
   }

   public void delete(Long id) {
      OfferTradition offerTradition = (OfferTradition)this.OfferTraditionRepository.findOne(id);
      ApiAssert.notFound(offerTradition == null, "not-found.entity");
      if(!offerTradition.getAttaches().isEmpty()) {
         Iterator var3 = offerTradition.getAttaches().iterator();

         while(var3.hasNext()) {
            OfferTraditionAttach attach = (OfferTraditionAttach)var3.next();
            this.imageService.delete(attach.getPath());
            this.OfferTraditionAttachRepository.delete(attach);
         }
      }

      this.OfferTraditionRepository.delete(id);
   }
}
