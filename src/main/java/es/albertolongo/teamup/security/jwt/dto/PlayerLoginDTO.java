package es.albertolongo.teamup.security.jwt.dto;

import javax.validation.constraints.NotBlank;

public class PlayerLoginDTO {

    @NotBlank
    String nickname;

    @NotBlank
    String password;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
