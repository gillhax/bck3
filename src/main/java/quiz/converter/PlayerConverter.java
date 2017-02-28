package quiz.converter;

import javax.inject.Inject;
import org.springframework.stereotype.Component;
import quiz.converter.AvatarConverter;
import quiz.converter.Converter2Args;
import quiz.domain.Player;
import quiz.repository.PlayerRepository;
import quiz.service.dto.PlayerDtoOut;

@Component
public class PlayerConverter extends Converter2Args {
   @Inject
   PlayerRepository playerRepository;
   @Inject
   private AvatarConverter avatarConverter;

   public PlayerDtoOut toDTO(Player player) {
      if(player == null) {
         return null;
      } else {
         PlayerDtoOut playerDto = new PlayerDtoOut();
         playerDto.setAvatar(this.avatarConverter.toDTO(player.getAvatar()));
         playerDto.setName(player.getName());
         playerDto.setScore(player.getScore());
         playerDto.setId(player.getId());
         return playerDto;
      }
   }
}
