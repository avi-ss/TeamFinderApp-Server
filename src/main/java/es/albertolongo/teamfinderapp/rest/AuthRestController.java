package es.albertolongo.teamfinderapp.rest;

import es.albertolongo.teamfinderapp.security.jwt.JwtProvider;
import es.albertolongo.teamfinderapp.security.jwt.dto.PlayerLoginDTO;
import es.albertolongo.teamfinderapp.security.jwt.dto.Token;
import es.albertolongo.teamfinderapp.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;

@RestController
@RequestMapping("/auth")
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
    public ResponseEntity<Token> loginPlayer(@Valid @RequestBody PlayerLoginDTO loginDTO){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken
                        (loginDTO.getNickname(), loginDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);

        return ResponseEntity.ok(new Token(token));
    }

    @PostMapping("/refresh")
    public ResponseEntity<Token> refreshToken(@RequestBody Token token) throws ParseException {
        String newToken = jwtProvider.refreshToken(token);
        return ResponseEntity.ok(new Token(newToken));
    }
}
