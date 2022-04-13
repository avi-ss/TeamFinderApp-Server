package es.albertolongo.teamup.model.entity;

import es.albertolongo.teamup.model.dto.PreferencesDTO;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class Preferences implements Serializable {

    @ManyToOne(optional = false)
    private Game game;

    @ManyToOne(optional = false)
    private Role role;

    @ManyToOne(optional = false)
    private Rank rank;

    private boolean onlyFeminine;

    public Preferences() {
        onlyFeminine = false;
    }

    public Preferences(Game game, Role role, Rank rank,
                       boolean onlyFeminine) {
        this.game = game;
        this.role = role;
        this.rank = rank;
        this.onlyFeminine = onlyFeminine;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
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
        preferencesDTO.setGame(game.getName());
        preferencesDTO.setRole(role.getRole());
        preferencesDTO.setRank(rank.getRank());
        preferencesDTO.setFeminine(onlyFeminine);

        return preferencesDTO;
    }
}
