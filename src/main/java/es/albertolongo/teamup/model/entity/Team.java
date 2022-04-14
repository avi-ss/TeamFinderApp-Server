package es.albertolongo.teamup.model.entity;

import es.albertolongo.teamup.model.dto.TeamDTO;
import es.albertolongo.teamup.model.enums.UserType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@PrimaryKeyJoinColumn()
public class Team extends User implements Serializable {

    @OneToMany(fetch = FetchType.EAGER,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            mappedBy = "team")
    private Set<Player> members = new HashSet<>();

    public Team() {
        super(UserType.TEAM);
    }

    public Team(Set<Player> members) {
        super(UserType.TEAM);
        this.members = members;
    }

    public Set<Player> getMembers() {
        return members;
    }

    public void setMembers(Set<Player> members) {
        this.members = members;
    }

    public TeamDTO toDTO() {

        TeamDTO teamDTO = new TeamDTO();
        Set<UUID> dtoMembers = members.stream()
                .map(id -> id.getId())
                .collect(Collectors.toSet());

        teamDTO.setId(id);
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
