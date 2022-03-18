package es.albertolongo.teamfinderapp.model.entity;

import es.albertolongo.teamfinderapp.model.dto.TeamDTO;
import es.albertolongo.teamfinderapp.model.enums.EntityType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
public class Team extends User implements Serializable {

    @OneToOne(cascade = CascadeType.ALL)
    private Player founder;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable()
    private Set<Player> members = new HashSet<>();

    public Team() {
        super(EntityType.TEAM);
    }

    public Team(Player founder, Set<Player> members) {
        super(EntityType.TEAM);
        this.founder = founder;
        this.members = members;
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

    public TeamDTO teamDTO(){

        TeamDTO teamDTO = new TeamDTO();
        Set<UUID> dtoMembers = members.stream()
                .map(id -> id.getId())
                .collect(Collectors.toSet());

        teamDTO.setId(id);
        teamDTO.setFounder(founder.getId());
        teamDTO.setMembers(dtoMembers);

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
