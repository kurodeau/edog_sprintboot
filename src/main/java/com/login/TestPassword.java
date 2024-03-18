package com.login;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class TestPassword {

public static void main(String[] args) {
	  PasswordEncoder encoder = new BCryptPasswordEncoder();
      String result1 = encoder.encode("Test123456#Manager");


      System.out.println( "{bcrypt}" +result1);

      
      String result2 = encoder.encode("password");
      System.out.println("{bcrypt}" +result2);
      
      
      // Check if a plain text password matches the encoded password
      System.out.println(encoder.matches("password", result1));
      System.out.println(encoder.matches("password", result2));

      

      System.out.println(encoder.matches("Test123456#Manager", "$2a$10$VAHoLewjAHro12EGZmD1zu1TNOSiUGrJJ6FMLf1FzKAcp/kzqrMly"));
}

}