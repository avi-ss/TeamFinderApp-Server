package es.albertolongo.teamfinderapp.model.entity;

import es.albertolongo.teamfinderapp.model.dto.GamePreferencesDTO;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class GamePreferences implements Serializable {

    private String game;
    private String role;
    private String rank;
    private boolean onlyFeminine;

    public GamePreferences() {
    }

    public GamePreferences(GamePreferencesDTO gamePreferencesDTO){
        this.game = gamePreferencesDTO.getGame();
        this.role = gamePreferencesDTO.getRole();
        this.rank = gamePreferencesDTO.getRank();
        this.onlyFeminine = gamePreferencesDTO.getOnlyFemenine();
    }

    public GamePreferences(String game, String role, String rank,
                           boolean onlyFeminine) {
        this.game = game;
        this.role = role;
        this.rank = rank;
        this.onlyFeminine = onlyFeminine;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public boolean isOnlyFeminine() {
        return onlyFeminine;
    }

    public void setOnlyFeminine(boolean onlyFeminine) {
        this.onlyFeminine = onlyFeminine;
    }

    public GamePreferencesDTO toDTO(){

        GamePreferencesDTO gamePreferencesDTO = new GamePreferencesDTO();
        gamePreferencesDTO.setGame(game);
        gamePreferencesDTO.setRank(rank);
        gamePreferencesDTO.setRole(role);
        gamePreferencesDTO.setOnlyFemenine(onlyFeminine);

        return gamePreferencesDTO;
    }
}
