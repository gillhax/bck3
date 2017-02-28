package quiz.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import quiz.converter.PlayerConverter;
import quiz.domain.Avatar;
import quiz.domain.Player;
import quiz.repository.AvatarRepository;
import quiz.repository.PlayerRepository;
import quiz.service.dto.PlayerDtoIn;
import quiz.service.dto.PlayerDtoOut;
import quiz.system.error.ApiAssert;

@Service
public class PlayerService {
   private final Logger log = LoggerFactory.getLogger(PlayerService.class);
   @Inject
   private PlayerRepository playerRepository;
   @Inject
   private PlayerConverter playerConverter;
   @Inject
   private AvatarRepository avatarRepository;

   public Player create(Player player) {
      Player result = (Player)this.playerRepository.save(player);
      return result;
   }

   public Player update(Player player) {
      return player.getId() != null?(Player)this.playerRepository.save(player):null;
   }

   @Transactional(
      readOnly = true
   )
   public Page findAll(Pageable pageable) {
      this.log.debug("Request to get all Players");
      Page result = this.playerRepository.findAll(pageable);
      return result;
   }

   @Transactional(
      readOnly = true
   )
   public Player findOne(Long id) {
      this.log.debug("Request to get Player : {}", id);
      Player player = (Player)this.playerRepository.findOne(id);
      ApiAssert.notFound(player == null, "not-found");
      return player;
   }

   public void delete(Long id) {
      this.log.debug("Request to delete Player : {}", id);
      this.playerRepository.delete(id);
   }

   public PlayerDtoOut getPlayerById(Long id) {
      Player player = (Player)this.playerRepository.findOne(id);
      ApiAssert.notFound(player == null, "not-found.player");
      return this.playerConverter.toDTO(player);
   }

   public PlayerDtoOut updatePlayer(Long userId, PlayerDtoIn playerDtoIn) {
      Player player = (Player)this.playerRepository.findOne(userId);
      ApiAssert.notFound(player == null, "not-found.player");
      if(playerDtoIn.getAvatarId() != null) {
         Avatar savedPlayer = (Avatar)this.avatarRepository.findOne(playerDtoIn.getAvatarId());
         ApiAssert.notFound(savedPlayer == null, "not-found.avatar");
         player.setAvatar(savedPlayer);
      }

      if(playerDtoIn.getName() != null) {
         player.setName(playerDtoIn.getName());
      }

      Player savedPlayer1 = (Player)this.playerRepository.save(player);
      return this.playerConverter.toDTO(savedPlayer1);
   }

   public Map getPlayersTop(Long userId) {
      PageRequest pageable = new PageRequest(0, 100);
      List players = this.playerRepository.findOrderByScore(pageable);
      List playersDto = this.playerConverter.toDTOs(players);
      Long playerPosition = null;
      if(userId != null) {
         playerPosition = this.playerRepository.findPlayerPosition(userId);
      }

      HashMap result = new HashMap();
      result.put("position", playerPosition);
      result.put("players", playersDto);
      return result;
   }

   public Long getProfileScore(Long id) {
      Player player = (Player)this.playerRepository.findOne(id);
      ApiAssert.notFound(player == null, "not-found.player");
      return player.getScore();
   }
}
