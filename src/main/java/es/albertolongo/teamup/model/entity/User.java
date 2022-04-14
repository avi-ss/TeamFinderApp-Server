package es.albertolongo.teamup.model.entity;

import es.albertolongo.teamup.model.dto.UserDTO;
import es.albertolongo.teamup.model.enums.UserType;
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
    UserType userType;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<User> likedUsers;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "likedUsers")
    private Set<User> likedUsersOf;

    public User() {
        id = UUID.randomUUID();
        likedUsers = new HashSet<>();
        likedUsersOf = new HashSet<>();
    }

    public User(UserType userType) {
        id = UUID.randomUUID();
        likedUsers = new HashSet<>();
        likedUsersOf = new HashSet<>();
        this.userType = userType;
    }

    public boolean hasLikedUser(User user){
        return likedUsers.contains(user);
    }

    public UUID getId() {
        return id;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public Set<User> getLikedUsers() {
        return likedUsers;
    }

    public void setLikedUsers(Set<User> likedEntities) {
        this.likedUsers = likedEntities;
    }

    public Set<User> getLikedUsersOf() {
        return likedUsersOf;
    }

    public void setLikedUsersOf(Set<User> likedOf) {
        this.likedUsersOf = likedOf;
    }

    public UserDTO userDTO(){

        UserDTO userDTO = new UserDTO();

        userDTO.setId(id);
        userDTO.setEntityType(UserDTO.EntityTypeEnum.valueOf(userType.toString()));

        Set<UUID> liked = likedUsers.stream()
                .map(id -> id.getId())
                .collect(Collectors.toSet());

        Set<UUID> likedOf = likedUsersOf.stream()
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
