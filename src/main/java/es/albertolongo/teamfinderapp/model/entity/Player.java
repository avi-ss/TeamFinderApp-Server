package es.albertolongo.teamfinderapp.model.entity;

import es.albertolongo.teamfinderapp.model.enums.EntityType;
import es.albertolongo.teamfinderapp.model.enums.Gender;
import es.albertolongo.teamfinderapp.model.dto.PlayerDTO;
import es.albertolongo.teamfinderapp.util.CoderPassword;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@PrimaryKeyJoinColumn()
public class Player extends User implements Serializable {

    @Column(unique = true)
    @NotBlank
    @Size(min = 5, max = 16)
    private String nickname;

    @Column(unique = true)
    @Email
    private String email;

    @NotBlank
    String password;

    @NotBlank
    @Size(min = 8, max = 25)
    @Pattern(regexp = "^[A-Z][a-z]*(\\s[A-Z][a-z]*)?")
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

    public Player(PlayerDTO playerDTO, Preferences preferences) {
        super(EntityType.PLAYER);
        set(playerDTO, preferences);
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

    public String getPassword() {
        return password;
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

    public void set(PlayerDTO playerDTO, Preferences preferences) {
        this.nickname = playerDTO.getNickname();
        this.email = playerDTO.getEmail();
        this.password = CoderPassword.encode(playerDTO.getPassword());
        this.fullname = playerDTO.getFullname();
        this.birthday = playerDTO.getBirthday();
        this.gender = Gender.valueOf(playerDTO.getGender().toUpperCase());
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
