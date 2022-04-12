package es.albertolongo.teamfinderapp.security;

import es.albertolongo.teamfinderapp.exception.player.PlayerNotFound;
import es.albertolongo.teamfinderapp.exception.user.UserNotFound;
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

@Service
public class PlayerDataService implements UserDetailsService {

    @Autowired
    PlayerService playerService;

    PasswordEncoder encoder;

    public PlayerDataService() {
        encoder = new BCryptPasswordEncoder();
    }

    @Override
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {

        Player player;

        try {
            player = playerService.getPlayerByNickname(nickname);
        }
        catch (PlayerNotFound e){
            throw new UsernameNotFoundException(e.getMessage());
        }

        return User.withUsername(player.getNickname())
                .roles("PLAYER").password(player.getPassword())
                .build();
    }

}
