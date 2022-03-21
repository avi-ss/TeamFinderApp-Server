package es.albertolongo.teamfinderapp.model.entity;

import es.albertolongo.teamfinderapp.model.enums.EntityType;
import es.albertolongo.teamfinderapp.model.enums.Gender;
import es.albertolongo.teamfinderapp.model.dto.PlayerDTO;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@PrimaryKeyJoinColumn()
public class Player extends User implements Serializable {

    @NotBlank
    @Column(unique = true)
    private String nickname;

    @Pattern(
            regexp = "\\\\.[Ii][Oo]$"
    )
    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    private String fullname;

    @Past
    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Embedded
    protected Preferences preferences;

    public Player() {
        super(EntityType.PLAYER);
    }

    public Player(PlayerDTO playerDTO) {
        super(EntityType.PLAYER);
        this.nickname = playerDTO.getNickname();
        this.fullname = playerDTO.getFullname();
        this.email = playerDTO.getEmail();
        this.birthday = playerDTO.getBirthday();
        this.gender = Gender.valueOf(playerDTO.getGender());
        this.preferences = new Preferences(playerDTO.getPreferences());
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Preferences getPreferences() {
        return preferences;
    }

    public void setPreferences(Preferences preferences) {
        this.preferences = preferences;
    }

    public void set(PlayerDTO playerDTO) {
        this.nickname = playerDTO.getNickname();
        this.fullname = playerDTO.getFullname();
        this.email = playerDTO.getEmail();
        this.birthday = playerDTO.getBirthday();
        this.gender = Gender.valueOf(playerDTO.getGender());
        this.preferences = new Preferences(playerDTO.getPreferences());
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
