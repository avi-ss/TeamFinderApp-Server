package es.albertolongo.teamup.model.entity;

import es.albertolongo.teamup.model.dto.PreferencesDTO;
import es.albertolongo.teamup.model.dto.TeamPreferencesDTO;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class TeamPreferences implements Serializable {

    @ManyToOne()
    private Game game;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> takenRoles = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    private Rank averageRank;

    public TeamPreferences() {
    }

    public TeamPreferences(Game game, List<Role> takenRoles, Rank averageRank) {
        this.game = game;
        this.takenRoles = takenRoles;
        this.averageRank = averageRank;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public List<Role> getTakenRoles() {
        return takenRoles;
    }

    public void setTakenRoles(List<Role> takenRoles) {
        this.takenRoles = takenRoles;
    }

    public Rank getAverageRank() {
        return averageRank;
    }

    public void setAverageRank(Rank averageRank) {
        this.averageRank = averageRank;
    }

    public TeamPreferencesDTO toDTO() {
        TeamPreferencesDTO preferencesDTO = new TeamPreferencesDTO();
        preferencesDTO.setGame(game.getName());
        preferencesDTO.setAverageRank(averageRank.getRank());

        List<String> takenRolesDTO = takenRoles.stream().map(role -> role.getRole()).collect(Collectors.toList());
        preferencesDTO.setTakenRoles(takenRolesDTO);

        return preferencesDTO;
    }
}
