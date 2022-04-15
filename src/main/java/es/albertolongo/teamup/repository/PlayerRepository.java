package es.albertolongo.teamup.repository;

import es.albertolongo.teamup.model.entity.Game;
import es.albertolongo.teamup.model.entity.Player;
import es.albertolongo.teamup.model.entity.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlayerRepository extends CrudRepository<Player, UUID> {

    Optional<Player> findByEmail(@Pattern(regexp = "\\\\.[Ii][Oo]$") @Email String email);

    Optional<Player> findByNickname(@NotNull String nickname);

    @Query("SELECT p FROM Player p WHERE " +
            "p.team.id IS NULL AND " +
            "p.preferences.game=:game AND " +
            "p.preferences.rank.value>:minRankValue AND p.preferences.rank.value<:maxRankValue AND " +
            "p.preferences.role NOT IN :availableRoles")
    Optional<List<Player>> findAllByPreferences(@Param("game") Game game,
                                                @Param("minRankValue") int minRankValue, @Param("maxRankValue") int maxRankValue,
                                                @Param("availableRoles") List<Role> availableRoles);
}
