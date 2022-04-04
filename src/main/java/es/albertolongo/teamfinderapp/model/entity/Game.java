package es.albertolongo.teamfinderapp.model.entity;

import es.albertolongo.teamfinderapp.model.dto.GameDTO;
import es.albertolongo.teamfinderapp.model.dto.RankDTO;
import es.albertolongo.teamfinderapp.model.dto.RoleDTO;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Game implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Rank> ranks = new HashSet<>();

    public Game() {
    }

    public Game(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Rank> getRanks() {
        return ranks;
    }

    public void setRanks(Set<Rank> ranks) {
        this.ranks = ranks;
    }

    public GameDTO toDTO(){

        GameDTO gameDTO = new GameDTO();

        gameDTO.setId(id);
        gameDTO.setName(name);

        Set<RoleDTO> roleDTOS = roles.stream()
                .map(role -> role.toDTO())
                .collect(Collectors.toSet());

        Set<RankDTO> rankDTOS = ranks.stream()
                .map(rank -> rank.toDTO())
                .collect(Collectors.toSet());

        gameDTO.setRoles(roleDTOS);
        gameDTO.setRanks(rankDTOS);

        return gameDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return name.equals(game.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
