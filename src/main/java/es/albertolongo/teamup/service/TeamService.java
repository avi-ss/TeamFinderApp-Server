package es.albertolongo.teamup.service;

import es.albertolongo.teamup.exception.player.PlayerNotFound;
import es.albertolongo.teamup.exception.team.*;
import es.albertolongo.teamup.model.dto.TeamDTO;
import es.albertolongo.teamup.model.entity.*;
import es.albertolongo.teamup.model.enums.UserType;
import es.albertolongo.teamup.repository.PlayerRepository;
import es.albertolongo.teamup.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;
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

    @Autowired
    UserService userService;

    public TeamService() {
    }

    public UUID registerTeam(@NotNull @Valid TeamDTO teamDTO) {

        Team team = new Team(teamDTO.getName());

        Iterable<Player> members = playerRepository.findAllById(teamDTO.getMembers());
        Set<Player> membersSet = StreamSupport.stream(members.spliterator(),
                false).collect(Collectors.toSet());

        if (membersSet.isEmpty()) {
            throw new InvalidTeam("Members cannot be empty");
        }

        // Bidirectional part
        team.setMembers(membersSet);
        membersSet.forEach(player -> player.setTeam(team));
        membersSet.forEach(player -> player.getPreferences().setWantedUserType(UserType.PLAYER));

        // Update the team preferences;
        return updateTeamPreferences(team, membersSet).getId();
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
        player.get().getPreferences().setWantedUserType(UserType.PLAYER);

        // Update the team preferences;
        return updateTeamPreferences(team.get(), members);
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

        // Update the team preferences;
        return updateTeamPreferences(team.get(), members);
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

    public Set<Team> getAllTeamsForPlayer(UUID playerId) {

        Optional<Player> player = playerRepository.findById(playerId);

        if (!player.isPresent()) {
            throw new PlayerNotFound("Player not found");
        }

        // Get the game
        Game game = player.get().getPreferences().getGame();

        // Get the role
        Role role = player.get().getPreferences().getRole();

        // Get the maximum and minimum rank
        int rankValue = player.get().getPreferences().getRank().getValue();
        int minRankValue = rankValue - 3;
        int maxRankValue = rankValue + 3;

        Optional<List<Team>> forPlayer = teamRepository.findAllTeamsForPlayer(game, minRankValue, maxRankValue, role);

        // If not found, empty set returned
        if (!forPlayer.isPresent()) {
            return Collections.emptySet();
        }

        Set<Team> teams = forPlayer.get().stream().collect(Collectors.toSet());

        // Removing the users which the PLAYER liked from the list of suitable TEAMS
        Set<UUID> likedUsers = userService.getUser(playerId).getLikedUsers().stream().map(user -> user.getId()).collect(Collectors.toSet());
        teams = teams.stream().filter(team -> !likedUsers.contains(team.getId())).collect(Collectors.toSet());

        return teams;
    }

    private Team updateTeamPreferences(Team team, Set<Player> membersSet){

        // Getting the game
        Game game = membersSet.stream().findFirst().get().getPreferences().getGame();

        // Getting the taken roles
        List<Role> takenRoles = membersSet.stream().map(p -> p.getPreferences().getRole()).collect(Collectors.toList());

        // Getting the average rank
        int averageRankValue = (int) (membersSet.stream().map(p -> p.getPreferences().getRank().getValue()).reduce(0, Integer::sum) / membersSet.stream().count());
        Rank averageRank = game.getRanks().stream().filter(rank -> rank.getValue() == averageRankValue ).findFirst().orElse(game.getRanks().stream().findFirst().get());

        // Setting the preferences
        team.setTeamPreferences(new TeamPreferences(game, takenRoles, averageRank));

        return teamRepository.save(team);
    }
}
