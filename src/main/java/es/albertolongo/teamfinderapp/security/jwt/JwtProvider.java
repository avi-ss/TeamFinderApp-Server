package es.albertolongo.teamfinderapp.security.jwt;

import es.albertolongo.teamfinderapp.model.entity.Player;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {

    private final static Logger LOG = LoggerFactory.getLogger(JwtProvider.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private int expiration;

    public String generateToken(Authentication auth) {

        // PlayerPrincipal extends UserDetails
        UserDetails principal = (UserDetails) auth.getPrincipal();

        return Jwts.builder().setSubject(principal.getUsername()).setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expiration * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
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
}
