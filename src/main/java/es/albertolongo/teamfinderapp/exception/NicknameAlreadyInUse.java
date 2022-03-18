package es.albertolongo.teamfinderapp.exception;

public class NicknameAlreadyInUse extends RuntimeException{

    public NicknameAlreadyInUse(String message) {
        super(message);
    }
}
