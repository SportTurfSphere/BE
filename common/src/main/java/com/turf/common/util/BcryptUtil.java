package com.turf.common.util;

import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Log4j2
@UtilityClass
public class BcryptUtil {
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Method to encode password
    public static String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    // Method to match password with encoded password
    public static boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
