package com.token;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

@Service
public class TokenDTO {

	public Integer id;

	public String token;

	public LocalDateTime expired;

	// seller/
	public String userType;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public LocalDateTime getExpired() {
		return expired;
	}

	public void setExpired(LocalDateTime expired) {
		this.expired = expired;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	
}
