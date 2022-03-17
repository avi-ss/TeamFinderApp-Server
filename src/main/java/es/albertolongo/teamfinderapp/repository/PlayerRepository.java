package es.albertolongo.teamfinderapp.repository;

import es.albertolongo.teamfinderapp.model.entity.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlayerRepository extends CrudRepository<Player, UUID> {

    // Create/Update methods
    @Override
    <S extends Player> S save(S entity);

    // Read methods
    Optional<Player> findById(UUID id);

    @Override
    Iterable<Player> findAll();

    @Override
    long count();

    // Delete methods
    @Override
    void deleteById(UUID uuid);

    @Override
    void delete(Player entity);
}
