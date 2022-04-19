package es.albertolongo.teamup.security;

import es.albertolongo.teamup.security.jwt.JwtEntryPoint;
import es.albertolongo.teamup.security.jwt.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityService extends WebSecurityConfigurerAdapter {

    @Autowired
    PlayerDataService playerDataService;

    @Autowired
    JwtEntryPoint jwtEntryPoint;

    @Bean
    public JwtTokenFilter jwtTokenFilter() {
        return new JwtTokenFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(playerDataService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();

        // Para poder visualizar la consola de H2
        http.headers().frameOptions().disable();

        http.authorizeRequests()
                // Establecer con WebSocket para todos?
                .antMatchers("/ws/**").permitAll()
                .antMatchers("/messages/**").permitAll()
                // Para poder entrar en la consola de la base de datos
                .antMatchers("/h2/**").permitAll()
                // las rutas relacionadas con autentificación
                .antMatchers("/auth/**").permitAll()
                // El registro debe de estar autorizado para todos
                .antMatchers(HttpMethod.POST, "/player").permitAll()
                // La lista de juegos debe de ser de dominio publico
                .antMatchers(HttpMethod.GET, "/game/**").permitAll()
                // El registro debe poder acceder a las comprobaciones de email y nickname
                .antMatchers(HttpMethod.GET, "/player/checkNickname/**").permitAll()
                .antMatchers(HttpMethod.GET, "/player/checkEmail/**").permitAll()
                // El resto de peticiones tienes que estar autenticado
                .anyRequest().authenticated()
                .and().exceptionHandling().authenticationEntryPoint(jwtEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}
