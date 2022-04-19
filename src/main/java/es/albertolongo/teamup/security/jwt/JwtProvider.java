package es.albertolongo.teamup.security.jwt;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import es.albertolongo.teamup.model.entity.Player;
import es.albertolongo.teamup.security.jwt.dto.Token;
import es.albertolongo.teamup.service.PlayerService;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtProvider {

    private final static Logger LOG = LoggerFactory.getLogger(JwtProvider.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private int expiration;

    @Autowired
    PlayerService playerService;

    public String generateToken(Authentication auth) {

        // PlayerPrincipal extends UserDetails
        UserDetails principal = (UserDetails) auth.getPrincipal();
        List<String> roles = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(principal.getUsername())
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expiration * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String getNicknameFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            LOG.error("Token mal formado...");
        } catch (UnsupportedJwtException e) {
            LOG.error("Token no soportado...");
        } catch (ExpiredJwtException e) {
            LOG.error("Token expirado...");
        } catch (IllegalArgumentException e) {
            LOG.error("Token vacio...");
        } catch (SignatureException e) {
            LOG.error("Error en la firma...");
        }

        return false;
    }

    public String refreshToken(Token token) throws ParseException {

        JWT jwt = JWTParser.parse(token.getToken());
        JWTClaimsSet claimsSet = jwt.getJWTClaimsSet();
        String nickname = claimsSet.getSubject();
        List<String> roles = (List<String>) claimsSet.getClaim("roles");

        return Jwts.builder()
                .setSubject(nickname)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expiration * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
}
