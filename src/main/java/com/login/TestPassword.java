package com.login;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class TestPassword {

public static void main(String[] args) {
	  PasswordEncoder encoder = new BCryptPasswordEncoder(4);
	  String result = encoder.encode("password");
	  System.out.println(result);

	  
	  System.out.println(encoder.matches(("password"), result));
}

}