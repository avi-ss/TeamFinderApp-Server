package es.albertolongo.teamfinderapp.service;

import es.albertolongo.teamfinderapp.api.PlayerApi;
import es.albertolongo.teamfinderapp.api.TeamApi;
import es.albertolongo.teamfinderapp.model.dto.GamePreferencesDTO;
import es.albertolongo.teamfinderapp.model.dto.TeamDTO;
import es.albertolongo.teamfinderapp.model.entity.GamePreferences;
import es.albertolongo.teamfinderapp.model.entity.Player;
import es.albertolongo.teamfinderapp.model.entity.Team;
import es.albertolongo.teamfinderapp.model.enums.Action;
import es.albertolongo.teamfinderapp.repository.PlayerRepository;
import es.albertolongo.teamfinderapp.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Validated
public class TeamService {

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    TeamRepository teamRepository;

    public TeamService() {
    }

    public UUID registerTeam(@NotNull @Valid TeamDTO teamDTO) {

        Team team = new Team();

        Optional<Player> founder = playerRepository.findById(teamDTO.getFounder());
        Iterable<Player> members = playerRepository.findAllById(teamDTO.getMembers());

        Set<Player> membersSet = StreamSupport.stream(members.spliterator(),
                false).collect(Collectors.toSet());

        if (founder.isPresent() && membersSet.contains(founder.get())) {
            team.setFounder(founder.get());
            team.setMembers(membersSet);
            team.setPreferences(new GamePreferences(teamDTO.getPreferences()));

            return teamRepository.save(team).getId();
        } else {
            throw new RuntimeException();
        }
    }

    public Team getTeam(@NotNull UUID id) {

        Optional<Team> team = teamRepository.findById(id);

        if (team.isPresent()) {
            return team.get();
        } else {
            throw new RuntimeException();
        }
    }

    public Team modifyTeamMember(@NotNull UUID teamId, @NotNull UUID playerId, String actionStr) {

        Optional<Team> team = teamRepository.findById(teamId);
        Optional<Player> player = playerRepository.findById(playerId);

        if (team.isPresent() && player.isPresent()) {
            Action action = Action.valueOf(actionStr.toUpperCase());
            Set<Player> members = team.get().getMembers();
            switch (action) {
                case ADD -> {
                    // Si no equipo no contiene el miembro, lo añade
                    if (!members.contains(player.get())) {
                        team.get().getMembers().add(player.get());
                    } else {
                        throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>());
                    }
                }
                case REMOVE -> {
                    // Si el equipo contiene el miembro, lo elimina
                    if (members.contains(player.get())) {

                        team.get().getMembers().remove(player.get());
                        // Si el fundador era el miembro eliminado, coge el primero
                        if (team.get().getFounder().equals(player.get())) {

                            Optional<Player> first = team.get().getMembers().stream().findFirst();
                            // Si hay primero, se pone de fundador
                            if (first.isPresent()) {
                                team.get().setFounder(first.get());
                            } else {
                                throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>());
                            }
                        }
                    } else {
                        throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>());
                    }
                }
                case PROMOTE -> {
                    // Si el equipo contiene el miembro, se asciende
                    if (members.contains(player.get())) {
                        team.get().setFounder(player.get());
                    } else {
                        throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>());
                    }
                }
                default -> {
                    throw new RuntimeException();
                }
            }
            return teamRepository.save(team.get());
        } else {
            throw new RuntimeException();
        }
    }

    public Team modifyTeamPreferences(@NotNull UUID id, @NotNull @Valid GamePreferencesDTO gamePreferencesDTO) {

        Optional<Team> team = teamRepository.findById(id);

        if (team.isPresent()) {
            team.get().setPreferences(new GamePreferences(gamePreferencesDTO));
            return teamRepository.save(team.get());
        } else {
            throw new RuntimeException();
        }
    }

    public void deleteTeam(@NotNull UUID id) {
        if (teamRepository.existsById(id)) {
            teamRepository.deleteById(id);
        } else {
            throw new RuntimeException();
        }
    }
}
