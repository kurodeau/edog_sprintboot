package com.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component("oauthAuthenticationSuccessHandler")
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		OAuth2AuthenticationToken persistedAuth = (OAuth2AuthenticationToken) authentication;
		DefaultOAuth2User user = new DefaultOAuth2User(persistedAuth.getAuthorities(), persistedAuth.getPrincipal().getAttributes(), "name");

		
		
		System.out.println(persistedAuth.getPrincipal().getName());
		
        response.sendRedirect("http://localhost:8081/");

        /*
         * Sets new OAuth2AuthenticationToken with mapped authorities
         */
//        SecurityContextHolder.getContext()
//                .setAuthentication(new OAuth2AuthenticationToken(user, mapAuthorities(persistedAuth.getPrincipal().getAttributes()), persistedAuth
//                        .getAuthorizedClientRegistrationId()));

        /*
         * Redirecting to the index page of website
         */
//        var redirectUri = requestCache.getRequest(request, response).getRedirectUrl();
//        redirectStrategy.sendRedirect(request, response, redirectUri);
	}

	
}
