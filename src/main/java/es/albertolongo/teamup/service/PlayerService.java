package es.albertolongo.teamup.service;

import es.albertolongo.teamup.exception.game.RankNotFound;
import es.albertolongo.teamup.exception.game.RoleNotFound;
import es.albertolongo.teamup.exception.player.EmailAlreadyInUse;
import es.albertolongo.teamup.exception.player.NicknameAlreadyInUse;
import es.albertolongo.teamup.exception.player.PlayerNotFound;
import es.albertolongo.teamup.exception.team.InvalidTeam;
import es.albertolongo.teamup.model.dto.PlayerDTO;
import es.albertolongo.teamup.model.dto.PreferencesDTO;
import es.albertolongo.teamup.model.entity.*;
import es.albertolongo.teamup.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Validated
public class PlayerService {

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    GameService gameService;

    @Autowired
    UserService userService;

    @Autowired
    TeamService teamService;

    public PlayerService() {
    }

    @Transactional
    public UUID registerPlayer(@Valid PlayerDTO playerDTO) throws ConstraintViolationException {

        Optional<Player> byEmail = playerRepository.findByEmail(playerDTO.getEmail());
        Optional<Player> byNickname = playerRepository.findByNickname(playerDTO.getNickname());

        if (byEmail.isPresent()) {
            throw new EmailAlreadyInUse("Email already in use");
        }

        if (byNickname.isPresent()) {
            throw new NicknameAlreadyInUse("Nickname already in use");
        }

        Preferences preferences = getPreferences(playerDTO.getPreferences());

        Player player = new Player(playerDTO, null, preferences);

        return playerRepository.save(player).getId();
    }

    public Player getPlayerById(@NotNull UUID id) {

        Optional<Player> player = playerRepository.findById(id);

        if (!player.isPresent()) {
            throw new PlayerNotFound("Player not found");
        }

        return player.get();
    }

    public Player getPlayerByNickname(@NotBlank String nickname) {

        Optional<Player> player = playerRepository.findByNickname(nickname);

        if (!player.isPresent()) {
            throw new PlayerNotFound("Player not found");
        }

        return player.get();
    }

    public boolean checkPlayerWithNickname(@NotBlank String nickname) {
        Optional<Player> player = playerRepository.findByNickname(nickname);

        if (!player.isPresent()) {
            return false;
        }
        return true;
    }

    public boolean checkPlayerWithEmail(@NotBlank String email) {
        Optional<Player> player = playerRepository.findByEmail(email);

        if (!player.isPresent()) {
            return false;
        }
        return true;
    }

    @Transactional
    public Player modifyPlayer(@NotNull UUID id, @Valid PlayerDTO playerDTO) {

        Optional<Player> byId = playerRepository.findById(id);

        if (!byId.isPresent()) {
            throw new PlayerNotFound("Player not found");
        }

        Preferences preferences = getPreferences(playerDTO.getPreferences());
        byId.get().set(playerDTO, null, preferences);

        if (playerDTO.getTeam().isPresent()) {
            Team team = teamService.getTeam(playerDTO.getTeam().get());
            byId.get().setTeam(team);
        }

        return playerRepository.save(byId.get());
    }

    @Transactional
    public void deletePlayer(@NotNull UUID id) {

        Optional<Player> byId = playerRepository.findById(id);

        if (!byId.isPresent()) {
            throw new PlayerNotFound("Player not found");
        }

        if (byId.get().getTeam() != null) {
            // Si es el último jugador del equipo, este se elimina.
            if (byId.get().getTeam().getMembers().size() == 1) {
                teamService.deleteTeam(byId.get().getTeam().getId());
            }
            // Si no, se eliminar el jugador
            else{
                teamService.deleteTeamMember(byId.get().getTeam().getId(), byId.get().getId());
            }
        }

        playerRepository.deleteById(id);
    }

    private Preferences getPreferences(PreferencesDTO preferencesDTO) {

        Game game = gameService.getGame(preferencesDTO.getGame());

        Optional<Role> role = game.getRoles().stream()
                .filter(r -> preferencesDTO.getRole().equals(r.getRole()))
                .findFirst();

        if (!role.isPresent()) {
            throw new RoleNotFound("Role not found");
        }

        Optional<Rank> rank = game.getRanks().stream()
                .filter(r -> preferencesDTO.getRank().equals(r.getRank()))
                .findFirst();

        if (!rank.isPresent()) {
            throw new RankNotFound("Rank not found");
        }

        return new Preferences(game, role.get(), rank.get(), preferencesDTO.getFeminine());
    }

    public Set<Player> getAllPlayersForTeam(UUID teamId) {

        Team team = teamService.getTeam(teamId);

        Stream<Player> members = team.getMembers().stream();
        Optional<Player> player = members.findFirst();

        if (!player.isPresent()) {
            throw new InvalidTeam("Team is empty");
        }

        // Get the game
        Game game = team.getTeamPreferences().getGame();

        // Get the availableRoles
        Set<Role> allRoles = gameService.getGame(game.getName()).getRoles();
        List<Role> takenRoles = team.getTeamPreferences().getTakenRoles();
        List<Role> availableRoles = allRoles.stream().filter(role -> !takenRoles.contains(role)).collect(Collectors.toList());

        // Get the maximum and minimum rank
        int averageRank = team.getTeamPreferences().getAverageRank().getValue();
        int minRankValue = averageRank - 3;
        int maxRankValue = averageRank + 3;

        Optional<List<Player>> forTeam = playerRepository.findAllPlayersForTeam(game, minRankValue, maxRankValue, availableRoles);

        // If not found, empty set returned
        if (!forTeam.isPresent()) {
            return Collections.emptySet();
        }

        Set<Player> players = forTeam.get().stream().collect(Collectors.toSet());

        // Removing the users which the TEAM liked from the list of suitable PLAYERS
        Set<UUID> likedUsers = userService.getUser(teamId).getLikedUsers().stream().map(user -> user.getId()).collect(Collectors.toSet());
        players = players.stream().filter(player1 -> !likedUsers.contains(player1.getId())).collect(Collectors.toSet());

        return players;
    }

    public Set<Player> getAllPlayersForPlayer(UUID playerId) {

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

        Optional<List<Player>> forPlayer = playerRepository.findAllPlayersForPlayer(game, role, minRankValue, maxRankValue);

        // If not found, empty set returned
        if (!forPlayer.isPresent()) {
            return Collections.emptySet();
        }

        Set<Player> players = forPlayer.get().stream().collect(Collectors.toSet());

        // Removing the users which the PLAYER liked from the list of suitable PLAYERS
        Set<UUID> likedUsers = userService.getUser(playerId).getLikedUsers().stream().map(user -> user.getId()).collect(Collectors.toSet());
        players = players.stream().filter(player1 -> !likedUsers.contains(player1.getId())).collect(Collectors.toSet());

        return players;
    }
}
