package es.albertolongo.teamup.rest;

import es.albertolongo.teamup.api.UserApi;
import es.albertolongo.teamup.exception.user.InvalidLikedEntity;
import es.albertolongo.teamup.exception.user.UserNotFound;
import es.albertolongo.teamup.model.dto.UserDTO;
import es.albertolongo.teamup.model.entity.User;
import es.albertolongo.teamup.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
@CrossOrigin
public class UserRestController implements UserApi {

    @Autowired
    UserService userService;

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handlerRestrictions(ConstraintViolationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(UserNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handlerNotFound(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(InvalidLikedEntity.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handlerUserExceptions(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @Override
    public ResponseEntity<UserDTO> getUserById(UUID userId) {
        User user = userService.getUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(user.userDTO());
    }

    @Override
    public ResponseEntity<UserDTO> addLikeToUser(UUID userId, UUID likedId) {
        User user = userService.addLike(userId, likedId);
        return ResponseEntity.status(HttpStatus.OK).body(user.userDTO());
    }

    @Override
    public ResponseEntity<Set<UserDTO>> getMatchedUsers(UUID userId){
        Set<User> matchedUsers = userService.getMatchedUsers(userId);

        Set<UserDTO> matchedUsersDTO = matchedUsers.stream().map(user -> user.userDTO()).collect(Collectors.toSet());

        return ResponseEntity.ok(matchedUsersDTO);
    }
}
