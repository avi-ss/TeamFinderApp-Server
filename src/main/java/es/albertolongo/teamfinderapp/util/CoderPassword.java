package es.albertolongo.teamfinderapp.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CoderPassword {

    static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private CoderPassword() {
    }

    public static String encode(String password){
        return encoder.encode(password);
    }

    public static boolean isEquals(String password, String encodedPassword) {
        return encoder.matches(password, encodedPassword);
    }
}
