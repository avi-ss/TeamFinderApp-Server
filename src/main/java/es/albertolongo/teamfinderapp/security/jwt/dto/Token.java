package es.albertolongo.teamfinderapp.security.jwt.dto;

import java.io.Serializable;

public class Token implements Serializable {

    private String token;

    public Token() {
    }

    public Token(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
