package com.login;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class TestPassword {

public static void main(String[] args) {
	  PasswordEncoder encoder = new BCryptPasswordEncoder();
      String result = encoder.encode("password");

	   // Include {bcrypt} prefix
      String resultWithPrefix = "{bcrypt}" + result;

      System.out.println(resultWithPrefix);

      // Check if a plain text password matches the encoded password
      System.out.println(encoder.matches("password", result));
}

}