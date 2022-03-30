package es.albertolongo.teamfinderapp.security;

import es.albertolongo.teamfinderapp.model.entity.Player;
import es.albertolongo.teamfinderapp.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PlayerDataService implements UserDetailsService {

    @Autowired
    PlayerService playerService;

    PasswordEncoder encoder;

    public PlayerDataService() {
        encoder = new BCryptPasswordEncoder();
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Player player = playerService.getPlayerById(UUID.fromString(id));

        return User.withUsername(player.getNickname())
                .roles("PLAYER").password(player.getPassword())
                .build();
    }

}
