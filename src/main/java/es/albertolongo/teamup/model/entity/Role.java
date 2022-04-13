package es.albertolongo.teamup.model.entity;

import es.albertolongo.teamup.model.dto.RoleDTO;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String role;

    @ManyToOne()
    @JoinColumn()
    private Game game;

    public Role() {
    }

    public Role(String role, Game game) {
        this.role = role;
        this.game = game;
    }

    public Long getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public RoleDTO toDTO(){

        RoleDTO roleDTO = new RoleDTO();

        roleDTO.setId(id);
        roleDTO.setName(role);

        return roleDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role1 = (Role) o;
        return role.equals(role1.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(role);
    }
}
