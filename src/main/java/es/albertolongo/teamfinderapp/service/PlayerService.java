package es.albertolongo.teamfinderapp.service;

import es.albertolongo.teamfinderapp.exception.game.RankNotFound;
import es.albertolongo.teamfinderapp.exception.game.RoleNotFound;
import es.albertolongo.teamfinderapp.exception.player.EmailAlreadyInUse;
import es.albertolongo.teamfinderapp.exception.player.NicknameAlreadyInUse;
import es.albertolongo.teamfinderapp.exception.player.PlayerNotFound;
import es.albertolongo.teamfinderapp.model.dto.PlayerDTO;
import es.albertolongo.teamfinderapp.model.dto.PreferencesDTO;
import es.albertolongo.teamfinderapp.model.entity.*;
import es.albertolongo.teamfinderapp.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

@Service
@Validated
public class PlayerService {

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    GameService gameService;

    public PlayerService() {
    }

    public UUID registerPlayer(@NotNull @Valid PlayerDTO playerDTO) {

        Optional<Player> byEmail = playerRepository.findByEmail(playerDTO.getEmail());
        Optional<Player> byNickname = playerRepository.findByNickname(playerDTO.getNickname());

        if (byEmail.isPresent()) {
            throw new EmailAlreadyInUse("Email already in use");
        }

        if (byNickname.isPresent()) {
            throw new NicknameAlreadyInUse("Nickname already in use");
        }

        Preferences preferences = getPreferences(playerDTO.getPreferences());

        return playerRepository.save(new Player(playerDTO, preferences)).getId();
    }

    public Player getPlayer(@NotNull UUID id) {

        Optional<Player> player = playerRepository.findById(id);

        if (!player.isPresent()) {
            throw new PlayerNotFound("Player not found");
        }

        return player.get();
    }

    public Player modifyPlayer(@NotNull UUID id, @NotNull @Valid PlayerDTO playerDTO) {

        Optional<Player> player = playerRepository.findById(id);

        if (!player.isPresent()) {
            throw new PlayerNotFound("Player not found");
        }

        Preferences preferences = getPreferences(playerDTO.getPreferences());

        player.get().set(playerDTO, preferences);
        return playerRepository.save(player.get());
    }

    public void deletePlayer(@NotNull UUID id) {

        if (!playerRepository.existsById(id)) {
            throw new PlayerNotFound("Player not found");
        }

        playerRepository.deleteById(id);
    }

    private Preferences getPreferences(PreferencesDTO preferencesDTO) {

        Game game = gameService.getGame(preferencesDTO.getGame());

        Optional<Role> role = game.getRoles().stream()
                .filter(r -> preferencesDTO.getRole().equals(r.getRole()))
                .findAny();

        if (!role.isPresent()) {
            throw new RoleNotFound("Role not found");
        }

        Optional<Rank> rank = game.getRanks().stream()
                .filter(r -> preferencesDTO.getRank().equals(r.getRank()))
                .findAny();

        if (!rank.isPresent()) {
            throw new RankNotFound("Rank not found");
        }

        return new Preferences(game, role.get(), rank.get(), preferencesDTO.getFeminine());
    }
}
