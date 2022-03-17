package es.albertolongo.teamfinderapp.service;

import es.albertolongo.teamfinderapp.model.dto.PlayerDTO;
import es.albertolongo.teamfinderapp.model.entity.Player;
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

    public PlayerService() {
    }

    public UUID registerPlayer(@NotNull @Valid PlayerDTO playerDTO) {
        return playerRepository.save(new Player(playerDTO)).getId();
    }

    public Player getPlayer(@NotNull UUID id){

        Optional<Player> player = playerRepository.findById(id);

        if(player.isPresent()){
            return player.get();
        }
        else{
            throw new RuntimeException();
        }
    }
}
