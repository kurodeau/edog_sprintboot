package com.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component("sellerPasswordEncoder")
public class SellerPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        // Add "@" to the raw password before encoding
        String modifiedPassword = rawPassword + "@Seller";
        // Use your preferred password encoder (e.g., BCryptPasswordEncoder) to perform the actual encoding
        // Replace BCryptPasswordEncoder with the actual encoder you want to use
        return new BCryptPasswordEncoder().encode(modifiedPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        // Add "@" to the raw password before matching
        String modifiedPassword = rawPassword + "@Seller";
        // Use your preferred password encoder to perform the matching
        // Replace BCryptPasswordEncoder with the actual encoder you used in the encode method
        return new BCryptPasswordEncoder().matches(modifiedPassword, encodedPassword);
    }
}
