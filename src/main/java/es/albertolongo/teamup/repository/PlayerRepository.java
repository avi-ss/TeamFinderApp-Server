package es.albertolongo.teamup.repository;

import es.albertolongo.teamup.model.entity.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlayerRepository extends CrudRepository<Player, UUID> {

    Optional<Player> findByEmail(@Pattern(regexp = "\\\\.[Ii][Oo]$") @Email String email);
    Optional<Player> findByNickname(@NotNull String nickname);
}
