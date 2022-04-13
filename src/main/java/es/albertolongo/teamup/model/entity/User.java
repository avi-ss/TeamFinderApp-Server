package es.albertolongo.teamup.model.entity;

import es.albertolongo.teamup.model.dto.UserDTO;
import es.albertolongo.teamup.model.enums.EntityType;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    @Id
    @Type(type = "uuid-char")
    protected UUID id;

    @Enumerated(EnumType.STRING)
    EntityType entityType;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable()
    protected Set<User> likedEntities = new HashSet<>();

    public User() {
        id = UUID.randomUUID();
    }

    public User(EntityType entityType) {
        id = UUID.randomUUID();
        this.entityType = entityType;
    }

    public UUID getId() {
        return id;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public Set<User> getLikedEntities() {
        return likedEntities;
    }

    public void setLikedEntities(Set<User> likedEntities) {
        this.likedEntities = likedEntities;
    }

    public UserDTO userDTO(){

        UserDTO userDTO = new UserDTO();

        userDTO.setId(id);
        userDTO.setEntityType(UserDTO.EntityTypeEnum.valueOf(entityType.toString()));

        Set<UUID> liked = likedEntities.stream()
                .map(id -> id.getId())
                .collect(Collectors.toSet());

        userDTO.setLikedEntities(liked);

        return userDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
