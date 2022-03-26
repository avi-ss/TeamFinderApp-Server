package es.albertolongo.teamfinderapp.rest;

import es.albertolongo.teamfinderapp.api.UserApi;
import es.albertolongo.teamfinderapp.exception.user.InvalidLikedEntity;
import es.albertolongo.teamfinderapp.exception.user.UserNotFound;
import es.albertolongo.teamfinderapp.model.dto.UserDTO;
import es.albertolongo.teamfinderapp.model.entity.User;
import es.albertolongo.teamfinderapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolationException;
import java.util.UUID;

@RestController
@RequestMapping("/")
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
}
