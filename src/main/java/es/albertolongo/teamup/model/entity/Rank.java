package es.albertolongo.teamup.model.entity;

import es.albertolongo.teamup.model.dto.RankDTO;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.util.Objects;

@Entity
public class Rank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @PositiveOrZero
    private int value;

    @NotBlank
    private String rank;

    @ManyToOne()
    @JoinColumn()
    private Game game;

    public Rank() {
    }

    public Rank(int value, String rank, Game game) {
        this.value = value;
        this.rank = rank;
        this.game = game;
    }

    public Long getId() {
        return id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int rankInt) {
        this.value = rankInt;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public RankDTO toDTO(){

        RankDTO rankDTO = new RankDTO();

        rankDTO.setId(id);
        rankDTO.setName(rank);
        rankDTO.setValue(value);

        return rankDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rank rank1 = (Rank) o;
        return value == rank1.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
