package es.albertolongo.teamup.security;

import es.albertolongo.teamup.exception.player.PlayerNotFound;
import es.albertolongo.teamup.model.entity.Player;
import es.albertolongo.teamup.model.entity.Team;
import es.albertolongo.teamup.security.jwt.dto.CustomUser;
import es.albertolongo.teamup.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {

        Player player;

        try {
            player = playerService.getPlayerByNickname(nickname);
        } catch (PlayerNotFound e) {
            throw new UsernameNotFoundException(e.getMessage());
        }

        // Get the team ID
        Team team = player.getFounderOf();
        UUID teamId = null;

        if(team != null){
            teamId = team.getId();
        }

        // We create a list of Authorities (only PLAYER)
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("PLAYER"));

        // Create a custom PRINCIPAL adding the PLAYER'S UUID and
        CustomUser customUser = new CustomUser(nickname, player.getPassword(), authorities, player.getId(), teamId);

        // Return it, as the User class implements UserDetails
        return customUser;
    }

}
