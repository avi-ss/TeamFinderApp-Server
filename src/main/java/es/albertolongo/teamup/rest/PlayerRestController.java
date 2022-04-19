package es.albertolongo.teamup.rest;

import es.albertolongo.teamup.api.PlayerApi;
import es.albertolongo.teamup.exception.game.GameNotFound;
import es.albertolongo.teamup.exception.game.RankNotFound;
import es.albertolongo.teamup.exception.game.RoleNotFound;
import es.albertolongo.teamup.exception.player.EmailAlreadyInUse;
import es.albertolongo.teamup.exception.player.NicknameAlreadyInUse;
import es.albertolongo.teamup.exception.player.PlayerNotFound;
import es.albertolongo.teamup.model.dto.PlayerDTO;
import es.albertolongo.teamup.model.entity.Player;
import es.albertolongo.teamup.security.jwt.JwtProvider;
import es.albertolongo.teamup.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.validation.ConstraintViolationException;
import java.security.Principal;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
@CrossOrigin
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
    public ResponseEntity<PlayerDTO> getPlayerByNickname(String playerNickname) {
        Player player = playerService.getPlayerByNickname(playerNickname);
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
    public ResponseEntity<Set<PlayerDTO>> getAllPlayersForTeam(UUID teamId) {
        Set<Player> players = playerService.getAllPlayersForTeam(teamId);

        Set<PlayerDTO> playerDTOS = players.stream().map(p -> p.toDTO()).collect(Collectors.toSet());

        return ResponseEntity.ok(playerDTOS);
    }

    @Override
    public ResponseEntity<Set<PlayerDTO>> getAllPlayersForPlayer(UUID playerId) {
        Set<Player> players = playerService.getAllPlayersForPlayer(playerId);

        Set<PlayerDTO> playerDTOS = players.stream().map(p -> p.toDTO()).collect(Collectors.toSet());

        return ResponseEntity.ok(playerDTOS);
    }

    @Override
    @PreAuthorize("#playerDTO.playerId == authentication.principal.uuid")
    public ResponseEntity<PlayerDTO> modifyPlayer(UUID playerId, PlayerDTO playerDTO) {
        Player newPlayer = playerService.modifyPlayer(playerId, playerDTO);
        return ResponseEntity.status(HttpStatus.OK).body(newPlayer.toDTO());
    }

    @Override
    @PreAuthorize("#playerId == authentication.principal.uuid")
    public ResponseEntity<Void> deletePlayer(UUID playerId) {
        playerService.deletePlayer(playerId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
