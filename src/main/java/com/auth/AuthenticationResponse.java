package com.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthenticationResponse {

	@JsonProperty("access_token")
	private String accessToken;
	@JsonProperty("refresh_token")
	private String refreshToken;

	
	private AuthenticationResponse() {
        // 私有构造函数，防止直接实例化
    }

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public static class Builder {
        private String accessToken;
        private String refreshToken;

        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        // 构建 AuthenticationResponse 对象
        public AuthenticationResponse build() {
            AuthenticationResponse response = new AuthenticationResponse();
            response.accessToken = this.accessToken;
            response.refreshToken = this.refreshToken;
            return response;
        }
    }
}
