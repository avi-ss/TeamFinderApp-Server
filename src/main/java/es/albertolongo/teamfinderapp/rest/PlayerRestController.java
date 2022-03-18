package es.albertolongo.teamfinderapp.rest;

import es.albertolongo.teamfinderapp.api.PlayerApi;
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
import java.util.UUID;

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

    @ExceptionHandler(PlayerNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handlerPlayerNotFound(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @Override
    public ResponseEntity<UUID> playerPost(PlayerDTO playerDTO) {
        UUID id = playerService.registerPlayer(playerDTO);
        return ResponseEntity.status(HttpStatus.OK).body(id);
    }

    @Override
    public ResponseEntity<PlayerDTO> playerPlayerIdGet(UUID playerId) {
        Player player = playerService.getPlayer(playerId);
        return ResponseEntity.status(HttpStatus.OK).body(player.toDTO());
    }

    @Override
    public ResponseEntity<PlayerDTO> playerPlayerIdPut(UUID playerId, PlayerDTO playerDTO) {
        Player player = playerService.modifyPlayer(playerId, playerDTO);
        return ResponseEntity.status(HttpStatus.OK).body(player.toDTO());
    }

    @Override
    public ResponseEntity<Void> playerPlayerIdDelete(UUID playerId) {
        playerService.deletePlayer(playerId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
