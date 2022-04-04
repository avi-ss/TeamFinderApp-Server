package es.albertolongo.teamfinderapp.rest;

import es.albertolongo.teamfinderapp.api.PlayerApi;
import es.albertolongo.teamfinderapp.exception.game.GameNotFound;
import es.albertolongo.teamfinderapp.exception.game.RankNotFound;
import es.albertolongo.teamfinderapp.exception.game.RoleNotFound;
import es.albertolongo.teamfinderapp.exception.player.EmailAlreadyInUse;
import es.albertolongo.teamfinderapp.exception.player.NicknameAlreadyInUse;
import es.albertolongo.teamfinderapp.exception.player.PlayerNotFound;
import es.albertolongo.teamfinderapp.model.dto.PlayerDTO;
import es.albertolongo.teamfinderapp.model.entity.Player;
import es.albertolongo.teamfinderapp.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolationException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class PlayerRestController implements PlayerApi {

    @Autowired
    PlayerService playerService;

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handlerRestrictions(ConstraintViolationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler({EmailAlreadyInUse.class, NicknameAlreadyInUse.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handlerAlreadyInUse(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler({PlayerNotFound.class, GameNotFound.class,
            RankNotFound.class, RoleNotFound.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handlerNotFound(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @Override
    public ResponseEntity<UUID> addPlayer(PlayerDTO playerDTO) {
        UUID id = playerService.registerPlayer(playerDTO);
        return ResponseEntity.status(HttpStatus.OK).body(id);
    }

    @Override
    public ResponseEntity<PlayerDTO> getPlayerById(UUID playerId) {
        Player player = playerService.getPlayerById(playerId);
        return ResponseEntity.status(HttpStatus.OK).body(player.toDTO());
    }

    @Override
    public ResponseEntity<Boolean> checkPlayerWithEmail(String playerEmail) {
        boolean response = playerService.checkPlayerWithEmail(playerEmail);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Boolean> checkPlayerWithNickname(String playerNickname) {
        boolean response = playerService.checkPlayerWithNickname(playerNickname);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Set<PlayerDTO>> getAllPlayers() {
        Set<Player> players = playerService.getAllPlayers();
        Set<PlayerDTO> playerDTOS = players.stream().map(player -> player.toDTO()).collect(Collectors.toSet());
        return ResponseEntity.ok(playerDTOS);
    }

    @Override
    public ResponseEntity<PlayerDTO> modifyPlayer(UUID playerId, PlayerDTO playerDTO) {
        Player newPlayer = playerService.modifyPlayer(playerId, playerDTO);
        return ResponseEntity.status(HttpStatus.OK).body(newPlayer.toDTO());
    }

    @Override
    public ResponseEntity<Void> deletePlayer(UUID playerId) {
        playerService.deletePlayer(playerId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
