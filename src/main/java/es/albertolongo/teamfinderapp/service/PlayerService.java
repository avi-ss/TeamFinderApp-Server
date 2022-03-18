package es.albertolongo.teamfinderapp.service;

import es.albertolongo.teamfinderapp.exception.EmailAlreadyInUse;
import es.albertolongo.teamfinderapp.exception.NicknameAlreadyInUse;
import es.albertolongo.teamfinderapp.model.dto.PlayerDTO;
import es.albertolongo.teamfinderapp.model.entity.Player;
import es.albertolongo.teamfinderapp.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

    public PlayerService() {
    }

    public UUID registerPlayer(@NotNull @Valid PlayerDTO playerDTO) {

        Optional<Player> byEmail = playerRepository.findByEmail(playerDTO.getEmail());
        Optional<Player> byNickname = playerRepository.findByNickname(playerDTO.getNickname());

        if(byEmail.isPresent()){
            throw new EmailAlreadyInUse("Email already in use");
        }

        if(byNickname.isPresent()){
            throw new NicknameAlreadyInUse("Nickname already in use");
        }

        return playerRepository.save(new Player(playerDTO)).getId();
    }

    public Player getPlayer(@NotNull UUID id) {

        Optional<Player> player = playerRepository.findById(id);

        if (player.isPresent()) {
            return player.get();
        } else {
            throw new RuntimeException();
        }
    }

    public Player modifyPlayer(@NotNull UUID id, @NotNull @Valid PlayerDTO playerDTO) {

        Optional<Player> player = playerRepository.findById(id);

        if (player.isPresent()) {
            player.get().set(playerDTO);
            return playerRepository.save(player.get());
        } else {
            throw new RuntimeException();
        }
    }

    public void deletePlayer(@NotNull UUID id) {
        if (playerRepository.existsById(id)) {
            playerRepository.deleteById(id);
        } else {
            throw new RuntimeException();
        }
    }
}
