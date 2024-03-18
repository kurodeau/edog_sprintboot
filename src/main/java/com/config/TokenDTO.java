package com.config;

import java.util.List;
import java.util.List;

public class TokenDTO {
    private Integer id;
    private List<String> authorities;
    private String sub;
    private Long iat;
    private Long exp;

    // Constructor
    public TokenDTO(Integer id, List<String> authorities, String sub, Long iat, Long exp) {
        this.id = id;
        this.authorities = authorities;
        this.sub = sub;
        this.iat = iat;
        this.exp = exp;
    }

    public TokenDTO() {
		super();
	}

	// Getter and Setter methods
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public Long getIat() {
        return iat;
    }

    public void setIat(Long iat) {
        this.iat = iat;
    }

    public Long getExp() {
        return exp;
    }

    public void setExp(Long exp) {
        this.exp = exp;
    }

    // Builder pattern
    public static class Builder {
        private Integer id;
        private List<String> authorities;
        private String sub;
        private Long iat;
        private Long exp;

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder authorities(List<String> authorities) {
            this.authorities = authorities;
            return this;
        }

        public Builder sub(String sub) {
            this.sub = sub;
            return this;
        }

        public Builder iat(Long iat) {
            this.iat = iat;
            return this;
        }

        public Builder exp(Long exp) {
            this.exp = exp;
            return this;
        }

        public TokenDTO build() {
            return new TokenDTO(id, authorities, sub, iat, exp);
        }
    }
}
