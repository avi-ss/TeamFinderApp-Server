package es.albertolongo.teamup.exception.game;

public class RoleAlreadyInGame extends RuntimeException{

    public RoleAlreadyInGame(String message) {
        super(message);
    }
}
