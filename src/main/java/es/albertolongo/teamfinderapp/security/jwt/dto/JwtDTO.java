package es.albertolongo.teamfinderapp.security.jwt.dto;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtDTO {

    private String token;
    private String bearer = "Bearer";
    private String nickname;
    private Collection<? extends GrantedAuthority> authorities;

    public JwtDTO(String token, String nickname, Collection<? extends GrantedAuthority> authorities) {
        this.token = token;
        this.nickname = nickname;
        this.authorities = authorities;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBearer() {
        return bearer;
    }

    public void setBearer(String bearer) {
        this.bearer = bearer;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }
}
