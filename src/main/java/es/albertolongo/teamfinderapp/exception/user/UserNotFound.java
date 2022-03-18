package es.albertolongo.teamfinderapp.exception.user;

public class UserNotFound extends RuntimeException{

    public UserNotFound(String message) {
        super(message);
    }
}
