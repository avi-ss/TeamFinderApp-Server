package es.albertolongo.teamfinderapp;

import es.albertolongo.teamfinderapp.model.dto.GameDTO;
import es.albertolongo.teamfinderapp.model.dto.RankDTO;
import es.albertolongo.teamfinderapp.model.dto.RoleDTO;
import es.albertolongo.teamfinderapp.service.GameService;
import es.albertolongo.teamfinderapp.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class TeamFinderApplication implements CommandLineRunner {

    @Autowired
    PlayerService playerService;

    @Autowired
    GameService gameService;

    private static Logger LOG = LoggerFactory
            .getLogger(TeamFinderApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(TeamFinderApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        LOG.info("Inicializando juego de prueba: League of Legends");
        GameDTO leagueDTO = new GameDTO().name("League of Legends");

        RoleDTO midDTO = new RoleDTO().name("Mid");
        RankDTO ironDTO = new RankDTO().name("Hierro 1").value(1);

        Set<RoleDTO> leagueRoles = new HashSet<>(); leagueRoles.add(midDTO);
        leagueDTO.setRoles(leagueRoles);

        Set<RankDTO> leagueRanks = new HashSet<>(); leagueRanks.add(ironDTO);
        leagueDTO.setRanks(leagueRanks);

        LOG.info("Inicializando juego de prueba: VALORANT");
        GameDTO valorantDTO = new GameDTO().name("VALORANT");

        RoleDTO duelistDTO = new RoleDTO().name("Duelist");
        RankDTO bronceDTO = new RankDTO().name("Bronce 1").value(1);

        Set<RoleDTO> valorantRoles = new HashSet<>(); valorantRoles.add(duelistDTO);
        valorantDTO.setRoles(valorantRoles);

        Set<RankDTO> valorantRanks = new HashSet<>(); valorantRanks.add(bronceDTO);
        valorantDTO.setRanks(valorantRanks);

        gameService.addGame(leagueDTO);
        gameService.addGame(valorantDTO);
    }
}