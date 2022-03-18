package es.albertolongo.teamfinderapp.service;

import es.albertolongo.teamfinderapp.exception.user.InvalidLikedEntity;
import es.albertolongo.teamfinderapp.exception.user.UserNotFound;
import es.albertolongo.teamfinderapp.model.entity.User;
import es.albertolongo.teamfinderapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

@Service
@Validated
public class UserService {

    @Autowired
    UserRepository userRepository;

    public UserService() {
    }

    public User getUser(@NotNull UUID id){

        Optional<User> user = userRepository.findById(id);

        if(!user.isPresent()){
            throw new UserNotFound("User not found");
        }

        return user.get();
    }

    public User addLike(@NotNull UUID id, @NotNull UUID likedEntity){

        Optional<User> user = userRepository.findById(id);

        Optional<User> likedUser = userRepository.findById(likedEntity);

        if (!user.isPresent()) {
            throw new UserNotFound("User not found");
        }

        if(!likedUser.isPresent()){
            throw new UserNotFound("Liked user not found");
        }

        if(user.get().getEntityType().equals(likedUser.get().getEntityType())){
            throw new InvalidLikedEntity("Cannot like the same entity type");
        }

        user.get().getLikedEntities().add(likedUser.get());
        return userRepository.save(user.get());
    }
}
