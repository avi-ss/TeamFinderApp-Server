package es.albertolongo.teamup.security.jwt.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.UUID;

public class CustomUser extends User {

    UUID uuid;
    UUID teamId;

    public CustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities, UUID uuid, UUID teamId) {
        super(username, password, authorities);
        this.uuid = uuid;
        this.teamId = teamId;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getTeamId() {
        return teamId;
    }

    public void setTeamId(UUID teamId) {
        this.teamId = teamId;
    }
}
