package es.albertolongo.teamfinderapp.rest;

import es.albertolongo.teamfinderapp.api.TeamApi;
import es.albertolongo.teamfinderapp.model.dto.GamePreferencesDTO;
import es.albertolongo.teamfinderapp.model.dto.TeamDTO;
import es.albertolongo.teamfinderapp.model.entity.Team;
import es.albertolongo.teamfinderapp.service.TeamService;
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
public class TeamRestController implements TeamApi {

    @Autowired
    TeamService teamService;

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handlerRestrictions(ConstraintViolationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handlerRuntimeExceptions(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @Override
    public ResponseEntity<UUID> teamPost(TeamDTO teamDTO) {
        try {
            UUID id = teamService.registerTeam(teamDTO);
            return ResponseEntity.status(HttpStatus.OK).body(id);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Override
    public ResponseEntity<TeamDTO> teamTeamIdGet(UUID teamId) {
        try {
            Team team = teamService.getTeam(teamId);
            return ResponseEntity.status(HttpStatus.OK).body(team.toDTO());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Override
    public ResponseEntity<TeamDTO> teamTeamIdPatch(UUID teamId, GamePreferencesDTO gamePreferencesDTO) {
        try {
            Team team = teamService.modifyTeamPreferences(teamId, gamePreferencesDTO);
            return ResponseEntity.status(HttpStatus.OK).body(team.toDTO());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Override
    public ResponseEntity<TeamDTO> teamTeamIdPlayerIdPatch(UUID teamId, UUID playerId, String action) {
        try {
            Team team = teamService.modifyTeamMember(teamId, playerId, action);
            return ResponseEntity.status(HttpStatus.OK).body(team.toDTO());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Override
    public ResponseEntity<Void> teamTeamIdDelete(UUID teamId) {
        try {
            teamService.deleteTeam(teamId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


}
