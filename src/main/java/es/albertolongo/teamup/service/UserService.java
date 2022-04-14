package es.albertolongo.teamup.service;

import es.albertolongo.teamup.exception.user.UserNotFound;
import es.albertolongo.teamup.model.entity.User;
import es.albertolongo.teamup.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.*;

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

        // Ahora se puede dar like entre jugadores

//        if(user.get().getUserType().equals(likedUser.get().getUserType())){
//            throw new InvalidLikedEntity("Cannot like the same entity type");
//        }

        user.get().getLikedUsers().add(likedUser.get());
        return userRepository.save(user.get());
    }

    public Set<User> getMatchedUsers(UUID userId) {

        User user = getUser(userId);
        Set<User> likedUsers = user.getLikedUsers();

        Set<User> matchedUsers = new HashSet<>();

        likedUsers.forEach(other -> {
            if(other.hasLikedUser(user)){
                matchedUsers.add(other);
            }
        });

        return matchedUsers;
    }
}
