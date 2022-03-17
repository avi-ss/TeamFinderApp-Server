package es.albertolongo.teamfinderapp.model.entity;

import es.albertolongo.teamfinderapp.model.dto.LikeDTO;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class Like implements Serializable {

    private UUID id;
    private Set<UUID> likedEntities;

    public Like() {
        id = UUID.randomUUID();
        likedEntities = new HashSet<>();
    }

    public UUID getId() {
        return id;
    }

    public Set<UUID> getLikedEntities() {
        return likedEntities;
    }

    public void setLikedEntities(Set<UUID> likedEntities) {
        this.likedEntities = likedEntities;
    }

    public LikeDTO toDTO(){

        LikeDTO likeDTO = new LikeDTO();

        likeDTO.setEntityId(id);
        likeDTO.setLikedEntities(likedEntities);

        return likeDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Like like = (Like) o;
        return id.equals(like.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
