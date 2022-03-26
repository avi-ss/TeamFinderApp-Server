package es.albertolongo.teamfinderapp.rest;

import es.albertolongo.teamfinderapp.api.GameApi;
import es.albertolongo.teamfinderapp.exception.game.*;
import es.albertolongo.teamfinderapp.model.dto.GameDTO;
import es.albertolongo.teamfinderapp.model.dto.RankDTO;
import es.albertolongo.teamfinderapp.model.dto.RoleDTO;
import es.albertolongo.teamfinderapp.model.entity.Game;
import es.albertolongo.teamfinderapp.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolationException;

@RestController
@RequestMapping("/")
public class GameRestController implements GameApi {

    @Autowired
    GameService gameService;

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handlerRestrictions(ConstraintViolationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler({GameNotFound.class, RankNotFound.class, RoleNotFound.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handlerNotFound(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler({GameAlreadyExists.class, RankAlreadyInGame.class, RoleAlreadyInGame.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handlerGameExceptions(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @Override
    public ResponseEntity<Long> addGame(GameDTO gameDTO) {
        Long id = gameService.addGame(gameDTO);
        return ResponseEntity.ok(id);
    }

    @Override
    public ResponseEntity<GameDTO> getGameById(Long gameId) {
        Game game = gameService.getGame(gameId);
        return ResponseEntity.ok(game.toDTO());
    }

    @Override
    public ResponseEntity<GameDTO> addRankToGame(Long gameId, RankDTO rankDTO) {
        Game game = gameService.addRank(gameId, rankDTO);
        return ResponseEntity.ok(game.toDTO());
    }

    @Override
    public ResponseEntity<GameDTO> addRoleToGame(Long gameId, RoleDTO roleDTO) {
        Game game = gameService.addRole(gameId, roleDTO);
        return ResponseEntity.ok(game.toDTO());
    }
}
