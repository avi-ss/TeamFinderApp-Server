package es.albertolongo.teamup.exception.player;

public class NicknameAlreadyInUse extends RuntimeException{

    public NicknameAlreadyInUse(String message) {
        super(message);
    }
}
