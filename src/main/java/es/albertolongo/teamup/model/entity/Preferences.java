package es.albertolongo.teamup.model.entity;

import es.albertolongo.teamup.model.dto.PreferencesDTO;
import es.albertolongo.teamup.model.enums.UserType;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

    @Enumerated(EnumType.STRING)
    private UserType wantedUserType;

    public Preferences() {
        onlyFeminine = false;
        wantedUserType = UserType.PLAYER;
    }

    public Preferences(Game game, Role role, Rank rank,
                       boolean onlyFeminine, String wantedUserType) {
        this.game = game;
        this.role = role;
        this.rank = rank;
        this.onlyFeminine = onlyFeminine;
        this.wantedUserType = UserType.valueOf(wantedUserType.toUpperCase());
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

    public UserType getWantedUserType() {
        return wantedUserType;
    }

    public void setWantedUserType(UserType wantedUserType) {
        this.wantedUserType = wantedUserType;
    }

    public PreferencesDTO toDTO(){

        PreferencesDTO preferencesDTO = new PreferencesDTO();
        preferencesDTO.setGame(game.getName());
        preferencesDTO.setRole(role.getRole());
        preferencesDTO.setRank(rank.getRank());
        preferencesDTO.setFeminine(onlyFeminine);
        preferencesDTO.setWantedUser(wantedUserType.toString());

        return preferencesDTO;
    }
}
