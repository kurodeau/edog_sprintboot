package com.config;

import org.springframework.stereotype.Service;

public class TokenDTO {
    private Integer id;
    private String token;
    private String userType;

  
    public TokenDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TokenDTO(Integer id, String token, String userType) {
		super();
		this.id = id;
		this.token = token;
		this.userType = userType;
	}

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

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    // Builder class
    public static class Builder {
        private Integer id;
        private String token;
        private String userType;

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }
        
        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder userType(String userType) {
            this.userType = userType;
            return this;
        }

        public TokenDTO build() {
            return new TokenDTO(id, token, userType);
        }
    }
}
