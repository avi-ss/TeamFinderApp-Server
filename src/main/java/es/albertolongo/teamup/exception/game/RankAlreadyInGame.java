package es.albertolongo.teamup.exception.game;

public class RankAlreadyInGame extends RuntimeException{

    public RankAlreadyInGame(String message) {
        super(message);
    }
}
