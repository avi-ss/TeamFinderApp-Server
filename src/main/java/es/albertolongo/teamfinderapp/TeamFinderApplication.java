package es.albertolongo.teamfinderapp;

import es.albertolongo.teamfinderapp.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TeamFinderApplication implements CommandLineRunner {

    @Autowired
    PlayerService playerService;

    private static Logger LOG = LoggerFactory
            .getLogger(TeamFinderApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(TeamFinderApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        LOG.info("Mensaje de prueba");
    }
}