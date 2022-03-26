package es.albertolongo.teamfinderapp.exception.game;

public class GameAlreadyExists extends RuntimeException{

    public GameAlreadyExists(String message) {
        super(message);
    }
}
