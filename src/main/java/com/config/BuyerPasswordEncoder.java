package com.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component("buyerPasswordEncoder")
public class BuyerPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        String modifiedPassword = rawPassword + "#Buyer";
        return new BCryptPasswordEncoder().encode(modifiedPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String modifiedPassword = rawPassword + "#Buyer";
        return new BCryptPasswordEncoder().matches(modifiedPassword, encodedPassword);
    }
}
