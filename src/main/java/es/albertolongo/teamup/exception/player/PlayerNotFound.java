package es.albertolongo.teamup.exception.player;

public class PlayerNotFound extends RuntimeException{

    public PlayerNotFound(String message) {
        super(message);
    }
}
