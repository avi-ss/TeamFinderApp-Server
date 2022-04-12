package es.albertolongo.teamfinderapp.rest;

import es.albertolongo.teamfinderapp.model.dto.PlayerDTO;
import es.albertolongo.teamfinderapp.security.SecurityService;
import es.albertolongo.teamfinderapp.security.jwt.JwtProvider;
import es.albertolongo.teamfinderapp.security.jwt.dto.JwtDTO;
import es.albertolongo.teamfinderapp.security.jwt.dto.PlayerLoginDTO;
import es.albertolongo.teamfinderapp.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/")
@CrossOrigin
public class AuthRestController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PlayerService playerService;

    @Autowired
    JwtProvider jwtProvider;

    @PostMapping("/login")
    public ResponseEntity<JwtDTO> loginPlayer(@Valid @RequestBody PlayerLoginDTO loginDTO){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken
                        (loginDTO.getNickname(), loginDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        JwtDTO jwtDTO = new JwtDTO(jwt,userDetails.getUsername(), userDetails.getAuthorities());

        return new ResponseEntity<>(jwtDTO, HttpStatus.OK);
    }
}
