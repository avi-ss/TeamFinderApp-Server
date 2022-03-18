package es.albertolongo.teamfinderapp.exception.player;

public class EmailAlreadyInUse extends RuntimeException{

    public EmailAlreadyInUse(String message) {
        super(message);
    }
}
