package es.albertolongo.teamup.service;

import es.albertolongo.teamup.exception.player.PlayerNotFound;
import es.albertolongo.teamup.exception.team.*;
import es.albertolongo.teamup.model.dto.TeamDTO;
import es.albertolongo.teamup.model.entity.Player;
import es.albertolongo.teamup.model.entity.Team;
import es.albertolongo.teamup.repository.PlayerRepository;
import es.albertolongo.teamup.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

        // Bidirectional part
        team.setMembers(membersSet);
        membersSet.forEach(player -> player.setTeam(team));

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

        // Bidirectional part
        team.get().getMembers().add(player.get());
        player.get().setTeam(team.get());

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

        // Bidirectional part
        team.get().getMembers().remove(player.get());
        player.get().setTeam(null);

        return teamRepository.save(team.get());
    }

    public void deleteTeam(@NotNull UUID id) {

        Optional<Team> team = teamRepository.findById(id);

        if (!team.isPresent()) {
            throw new TeamNotFound("Team not found");
        }

        // Bidirectional part
        team.get().getMembers().forEach(player -> player.setTeam(null));

        teamRepository.deleteById(id);
    }
}
