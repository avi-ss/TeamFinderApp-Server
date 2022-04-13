package es.albertolongo.teamup.exception.player;

public class EmailAlreadyInUse extends RuntimeException{

    public EmailAlreadyInUse(String message) {
        super(message);
    }
}
