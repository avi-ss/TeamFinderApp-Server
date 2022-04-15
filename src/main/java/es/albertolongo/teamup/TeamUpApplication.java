package es.albertolongo.teamup;

import es.albertolongo.teamup.model.dto.*;
import es.albertolongo.teamup.model.entity.Player;
import es.albertolongo.teamup.service.GameService;
import es.albertolongo.teamup.service.PlayerService;
import es.albertolongo.teamup.service.TeamService;
import es.albertolongo.teamup.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@SpringBootApplication
public class TeamUpApplication implements CommandLineRunner {

    @Autowired
    UserService userService;

    @Autowired
    PlayerService playerService;

    @Autowired
    TeamService teamService;

    @Autowired
    GameService gameService;

    private static Logger LOG = LoggerFactory
            .getLogger(TeamUpApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(TeamUpApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        // Setting League of Legends
        {
            LOG.info("Inicializando juego de prueba: League of Legends");
            GameDTO leagueDTO = new GameDTO().name("League of Legends");

            Set<RoleDTO> leagueRoles = new HashSet<>();
            leagueRoles.add(new RoleDTO().name("Top"));
            leagueRoles.add(new RoleDTO().name("Jungle"));
            leagueRoles.add(new RoleDTO().name("Mid"));
            leagueRoles.add(new RoleDTO().name("Bottom"));
            leagueRoles.add(new RoleDTO().name("Support"));
            leagueDTO.setRoles(leagueRoles);

            Set<RankDTO> leagueRanks = new HashSet<>();
            leagueRanks.add(new RankDTO().name("Iron 4").value(1));
            leagueRanks.add(new RankDTO().name("Iron 3").value(2));
            leagueRanks.add(new RankDTO().name("Iron 2").value(3));
            leagueRanks.add(new RankDTO().name("Iron 1").value(4));
            leagueRanks.add(new RankDTO().name("Bronze 4").value(5));
            leagueRanks.add(new RankDTO().name("Bronze 3").value(6));
            leagueRanks.add(new RankDTO().name("Bronze 2").value(7));
            leagueRanks.add(new RankDTO().name("Bronze 1").value(8));
            leagueDTO.setRanks(leagueRanks);

            gameService.addGame(leagueDTO);
        }

        // Setting Valorant
        {
            LOG.info("Inicializando juego de prueba: VALORANT");
            GameDTO valorantDTO = new GameDTO().name("VALORANT");

            Set<RoleDTO> valorantRoles = new HashSet<>();
            valorantRoles.add(new RoleDTO().name("Controller"));
            valorantRoles.add(new RoleDTO().name("Sentinel"));
            valorantRoles.add(new RoleDTO().name("Initiator"));
            valorantRoles.add(new RoleDTO().name("Duelist"));
            valorantDTO.setRoles(valorantRoles);

            Set<RankDTO> valorantRanks = new HashSet<>();
            valorantRanks.add(new RankDTO().name("Iron 1").value(1));
            valorantRanks.add(new RankDTO().name("Iron 2").value(2));
            valorantRanks.add(new RankDTO().name("Iron 3").value(3));
            valorantRanks.add(new RankDTO().name("Bronze 1").value(4));
            valorantRanks.add(new RankDTO().name("Bronze 2").value(5));
            valorantRanks.add(new RankDTO().name("Bronze 3").value(6));
            valorantDTO.setRanks(valorantRanks);

            gameService.addGame(valorantDTO);
        }

        // Setting Player 1
        PlayerDTO player1DTO = new PlayerDTO().nickname("usuario1")
                .email("usuario1@gmail.com").fullname("Usuario Uno")
                .password("password1").gender("MASC").birthday(LocalDate.now().minusYears(16))
                .preferences(new PreferencesDTO().game("VALORANT").rank("Bronze 1").role("Sentinel").feminine(false));

        // Setting Player 2
        PlayerDTO player2DTO = new PlayerDTO().nickname("usuario2")
                .email("usuario2@gmail.com").fullname("Usuario Dos")
                .password("password2").gender("FEM").birthday(LocalDate.now().minusYears(16))
                .preferences(new PreferencesDTO().game("VALORANT").rank("Bronze 2").role("Duelist").feminine(false));

        // Setting Player 3
        PlayerDTO player3DTO = new PlayerDTO().nickname("usuario3")
                .email("usuario3@gmail.com").fullname("Usuario Tres")
                .password("password3").gender("OTHER").birthday(LocalDate.now().minusYears(16))
                .preferences(new PreferencesDTO().game("VALORANT").rank("Bronze 3").role("Duelist").feminine(false));

        // Setting Player 4
        PlayerDTO player4DTO = new PlayerDTO().nickname("usuario4")
                .email("usuario4@gmail.com").fullname("Usuario Cuatro")
                .password("password4").gender("FEM").birthday(LocalDate.now().minusYears(16))
                .preferences(new PreferencesDTO().game("League of Legends").rank("Bronze 4").role("Mid").feminine(false));

        UUID id1 = playerService.registerPlayer(player1DTO);
        LOG.info("USER 1: " + id1.toString());

        UUID id2 = playerService.registerPlayer(player2DTO);
        LOG.info("USER 2: " + id2.toString());

        UUID id3 = playerService.registerPlayer(player3DTO);
        LOG.info("USER 3: " + id3.toString());

        UUID id4 = playerService.registerPlayer(player4DTO);
        LOG.info("USER 4: " + id4.toString());

        // Setting Team 1
        TeamDTO team1DTO = new TeamDTO().addMembersItem(id1);
        UUID teamId1 = teamService.registerTeam(team1DTO);

        player2DTO.team(teamId1);
        playerService.modifyPlayer(id2, player2DTO);

        LOG.info("TEAM 1 - {USER 1}: " + teamId1.toString() + " - MEMBERS: " + teamService.getTeam(teamId1).getMembers().size());

        // Setting a match between USER 1 and USER 2
        userService.addLike(id1, id2);
        userService.addLike(id2, id1);

        // Setting a match between USER 1 and USER 3
        userService.addLike(id1, id3);
        userService.addLike(id3, id1);

        LOG.info("MATCH LIST - USER 1: " + userService.getMatchedUsers(id1).toString());
    }

}