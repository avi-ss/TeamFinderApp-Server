package es.albertolongo.teamfinderapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SecurityService extends WebSecurityConfigurerAdapter {

    @Autowired
    PlayerDataService playerDataService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(playerDataService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers().frameOptions().disable();
        http.httpBasic();

        //Permitir el acceso a cualquiera para que se registre como jugador
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/player").anonymous();
        http.authorizeRequests().anyRequest().anonymous();
    }
}
