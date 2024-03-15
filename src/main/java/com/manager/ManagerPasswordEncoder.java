package com.manager;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component("managerPasswordEncoder")
public class ManagerPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        String modifiedPassword = rawPassword + "#Manager";
        return new BCryptPasswordEncoder().encode(modifiedPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String modifiedPassword = rawPassword + "#Manager";
        return new BCryptPasswordEncoder().matches(modifiedPassword, encodedPassword);
    }
}
