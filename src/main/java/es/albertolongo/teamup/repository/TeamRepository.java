package es.albertolongo.teamup.repository;

import es.albertolongo.teamup.model.entity.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TeamRepository extends CrudRepository<Team, UUID> {

    @Query("SELECT t FROM Team t LEFT JOIN t.teamPreferences.takenRoles r WHERE " +
            "t.teamPreferences.game=:game AND " +
            "t.teamPreferences.averageRank.value>:minRankValue AND " +
            "t.teamPreferences.averageRank.value < :maxRankValue AND " +
            ":playerRole NOT IN r")
    Optional<List<Team>> findAllTeamsForPlayer(@Param("game") Game game,
                                               @Param("minRankValue") int minRankValue,
                                               @Param("maxRankValue") int maxRankValue, @Param("playerRole") Role playerRole);
}
