package es.albertolongo.teamfinderapp.model.entity;

import es.albertolongo.teamfinderapp.model.dto.PreferencesDTO;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class Preferences implements Serializable {

    private String game;
    private String role;
    private String rank;
    private boolean onlyFeminine;

    public Preferences() {
    }

    public Preferences(PreferencesDTO preferencesDTO){
        this.game = preferencesDTO.getGame();
        this.role = preferencesDTO.getRole();
        this.rank = preferencesDTO.getRank();
        this.onlyFeminine = preferencesDTO.getFeminine();
    }

    public Preferences(String game, String role, String rank,
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

    public void setOnlyFeminine(boolean feminine) {
        this.onlyFeminine = feminine;
    }

    public PreferencesDTO toDTO(){

        PreferencesDTO preferencesDTO = new PreferencesDTO();
        preferencesDTO.setGame(game);
        preferencesDTO.setRank(rank);
        preferencesDTO.setRole(role);
        preferencesDTO.setFeminine(onlyFeminine);

        return preferencesDTO;
    }
}
