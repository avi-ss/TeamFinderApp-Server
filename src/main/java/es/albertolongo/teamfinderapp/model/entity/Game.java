package es.albertolongo.teamfinderapp.model.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Game implements Serializable {

    private String name;
    private List<String> roles;
    private List<String> ranks;

    public Game() {
    }

    public Game(String name, List<String> roles, List<String> ranks) {
        this.name = name;
        this.roles = roles;
        this.ranks = ranks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getRanks() {
        return ranks;
    }

    public void setRanks(List<String> ranks) {
        this.ranks = ranks;
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
