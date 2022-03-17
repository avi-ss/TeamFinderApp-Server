package es.albertolongo.teamfinderapp.model.entity;

import es.albertolongo.teamfinderapp.model.enums.Gender;
import es.albertolongo.teamfinderapp.model.dto.PlayerDTO;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Player implements Serializable {

    @Id
    @Type(type = "uuid-char")
    private UUID id;

    @NotBlank
    private String nickname;

    @Pattern(
            regexp = "\\\\.[Ii][Oo]$"
    )
    @Email
    private String email;

    @NotBlank
    private String fullname;

    @Past
    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Embedded
    private GamePreferences preferences;

    public Player() {
        id = UUID.randomUUID();
    }

    public Player(PlayerDTO playerDTO) {
        id = UUID.randomUUID();
        this.nickname = playerDTO.getNickname();
        this.fullname = playerDTO.getFullname();
        this.email = playerDTO.getEmail();
        this.birthday = playerDTO.getBirthday();
        this.gender = Gender.valueOf(playerDTO.getGender());
        this.preferences = new GamePreferences(playerDTO.getPreferences());
    }

    public Player(String nickname, String fullname, String email,
                  LocalDate birthday, Gender gender,
                  GamePreferences gamePreferences) {
        id = UUID.randomUUID();
        this.nickname = nickname;
        this.fullname = fullname;
        this.email = email;
        this.birthday = birthday;
        this.gender = gender;
        this.preferences = gamePreferences;
    }

    public UUID getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public GamePreferences getPreferences() {
        return preferences;
    }

    public void setPreferences(GamePreferences preferences) {
        this.preferences = preferences;
    }

    public PlayerDTO toDTO() {

        PlayerDTO playerDTO = new PlayerDTO();

        playerDTO.setId(id);
        playerDTO.setNickname(nickname);
        playerDTO.setFullname(fullname);
        playerDTO.setEmail(email);
        playerDTO.setBirthday(birthday);
        playerDTO.setGender(gender.toString());
        playerDTO.setPreferences(preferences.toDTO());

        return playerDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return id.equals(player.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
