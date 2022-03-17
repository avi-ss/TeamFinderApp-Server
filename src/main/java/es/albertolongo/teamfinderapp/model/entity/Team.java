package es.albertolongo.teamfinderapp.model.entity;

import es.albertolongo.teamfinderapp.model.dto.TeamDTO;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class Team implements Serializable {
    private UUID id;
    private Player founder;
    private Set<Player> members = new HashSet<>();
    private GamePreferences preferences;

    public Team() {
        id = UUID.randomUUID();
    }

    public Team(Player founder, Set<Player> members, GamePreferences preferences) {
        id = UUID.randomUUID();
        this.founder = founder;
        this.members = members;
        this.preferences = preferences;
    }

    public UUID getId() {
        return id;
    }

    public Player getFounder() {
        return founder;
    }

    public void setFounder(Player founder) {
        this.founder = founder;
    }

    public Set<Player> getMembers() {
        return members;
    }

    public void setMembers(Set<Player> members) {
        this.members = members;
    }

    public GamePreferences getPreferences() {
        return preferences;
    }

    public void setPreferences(GamePreferences preferences) {
        this.preferences = preferences;
    }

    public TeamDTO toDTO(){

        TeamDTO teamDTO = new TeamDTO();
        Set<UUID> dtoMembers = members.stream()
                .map(id -> id.getId())
                .collect(Collectors.toSet());

        teamDTO.setId(id);
        teamDTO.setFounder(founder.getId());
        teamDTO.setMembers(dtoMembers);
        teamDTO.setPreferences(preferences.toDTO());

        return teamDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return id.equals(team.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
