package es.albertolongo.teamfinderapp.repository;

import es.albertolongo.teamfinderapp.model.entity.Player;
import es.albertolongo.teamfinderapp.model.entity.Team;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TeamRepository extends CrudRepository<Team, UUID> {

    Optional<Team> findByFounder(Player founder);
}
