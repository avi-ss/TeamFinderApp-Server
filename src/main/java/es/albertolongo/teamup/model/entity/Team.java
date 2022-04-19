package es.albertolongo.teamup.model.entity;

import es.albertolongo.teamup.model.dto.TeamDTO;
import es.albertolongo.teamup.model.enums.UserType;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@PrimaryKeyJoinColumn()
public class Team extends User implements Serializable {

    @Column(unique = true)
    @NotBlank
    @Size(min = 5, max = 16)
    @Pattern(regexp = "^[^0-9\\\\]\\w+$")
    private String name;

    @OneToOne(fetch = FetchType.EAGER)
    private Player founder;

    @OneToMany(fetch = FetchType.EAGER,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            mappedBy = "team")
    private Set<Player> members = new HashSet<>();

    @Embedded
    private TeamPreferences teamPreferences;

    public Team() {
        super(UserType.TEAM);
    }

    public Team(String name) {
        super(UserType.TEAM);
        this.name = name;
    }

    public Team(String name, Set<Player> members, TeamPreferences teamPreferences) {
        super(UserType.TEAM);
        this.name = name;
        this.members = members;
        this.teamPreferences = teamPreferences;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public TeamPreferences getTeamPreferences() {
        return teamPreferences;
    }

    public void setTeamPreferences(TeamPreferences preferences) {
        this.teamPreferences = preferences;
    }

    public TeamDTO toDTO() {

        TeamDTO teamDTO = new TeamDTO();

        Set<UUID> dtoMembers = members.stream()
                .map(id -> id.getId())
                .collect(Collectors.toSet());

        teamDTO.setId(id);
        teamDTO.setName(name);
        teamDTO.setFounder(founder.id);
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
