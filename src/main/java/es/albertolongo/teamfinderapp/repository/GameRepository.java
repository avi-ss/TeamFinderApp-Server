package es.albertolongo.teamfinderapp.repository;

import es.albertolongo.teamfinderapp.model.entity.Game;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

@Repository
public interface GameRepository extends CrudRepository<Game, Long> {

    Optional<Game> findByName(@NotBlank String name);
}
