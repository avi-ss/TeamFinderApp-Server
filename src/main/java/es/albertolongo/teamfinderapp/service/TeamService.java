package es.albertolongo.teamfinderapp.service;

import es.albertolongo.teamfinderapp.exception.player.PlayerNotFound;
import es.albertolongo.teamfinderapp.exception.team.*;
import es.albertolongo.teamfinderapp.model.dto.TeamDTO;
import es.albertolongo.teamfinderapp.model.entity.Player;
import es.albertolongo.teamfinderapp.model.entity.Team;
import es.albertolongo.teamfinderapp.repository.PlayerRepository;
import es.albertolongo.teamfinderapp.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Validated
@Transactional
public class TeamService {

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    TeamRepository teamRepository;

    public TeamService() {
    }

    public UUID registerTeam(@NotNull @Valid TeamDTO teamDTO) {

        Team team = new Team();

        Iterable<Player> members = playerRepository.findAllById(teamDTO.getMembers());
        Set<Player> membersSet = StreamSupport.stream(members.spliterator(),
                false).collect(Collectors.toSet());

        if (membersSet.isEmpty()) {
            throw new InvalidTeam("Members cannot be empty");
        }

        team.setMembers(membersSet);

        return teamRepository.save(team).getId();
    }

    public Team getTeam(@NotNull UUID id) {

        Optional<Team> team = teamRepository.findById(id);

        if (!team.isPresent()) {
            throw new TeamNotFound("Team not found");
        }

        return team.get();
    }

    public Set<Team> getAllTeams(){

        Iterable<Team> teamsIt = teamRepository.findAll();

        Set<Team> teams = StreamSupport.stream(teamsIt.spliterator(), false).collect(Collectors.toSet());

        return teams;
    }

    public Team addTeamMember(@NotNull UUID teamId, @NotNull UUID playerId) {

        Optional<Team> team = teamRepository.findById(teamId);
        Optional<Player> player = playerRepository.findById(playerId);

        if (!team.isPresent()) {
            throw new TeamNotFound("Team not found");
        }

        if (!player.isPresent()) {
            throw new PlayerNotFound("Player not found");
        }

        Set<Player> members = team.get().getMembers();

        if (members.contains(player.get())) {
            throw new MemberAlreadyInTeam("Member already in team");
        }

        team.get().getMembers().add(player.get());

        return teamRepository.save(team.get());
    }

    public Team deleteTeamMember(@NotNull UUID teamId, @NotNull UUID playerId) {

        Optional<Team> team = teamRepository.findById(teamId);
        Optional<Player> player = playerRepository.findById(playerId);

        if (!team.isPresent()) {
            throw new TeamNotFound("Team not found");
        }

        if (!player.isPresent()) {
            throw new PlayerNotFound("Player not found");
        }

        Set<Player> members = team.get().getMembers();

        if (!members.contains(player.get())) {
            throw new MemberNotInTeam("Member not in team");
        }

        if (members.size() < 2) {
            throw new MemberNumberIsLow("Cannot delete member when there is only one left");
        }

        team.get().getMembers().remove(player.get());

        return teamRepository.save(team.get());
    }

    public void deleteTeam(@NotNull UUID id) {

        if (!teamRepository.existsById(id)) {
            throw new TeamNotFound("Team not found");
        }
        teamRepository.deleteById(id);
    }
}
