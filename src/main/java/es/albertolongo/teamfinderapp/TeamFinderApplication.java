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
        LOG.info("Inicializando juego de prueba");
        GameDTO gameDTO = new GameDTO().name("League of Legends");

        RoleDTO roleDTO = new RoleDTO().name("Mid");
        RankDTO rankDTO = new RankDTO().name("Hierro 1").value(1);

        Set<RoleDTO> roles = new HashSet<>(); roles.add(roleDTO);
        gameDTO.setRoles(roles);

        Set<RankDTO> ranks = new HashSet<>(); ranks.add(rankDTO);
        gameDTO.setRanks(ranks);

        gameService.addGame(gameDTO);
    }
}