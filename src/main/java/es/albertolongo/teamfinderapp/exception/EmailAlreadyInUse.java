package es.albertolongo.teamfinderapp.exception;

public class EmailAlreadyInUse extends RuntimeException{

    public EmailAlreadyInUse(String message) {
        super(message);
    }
}
