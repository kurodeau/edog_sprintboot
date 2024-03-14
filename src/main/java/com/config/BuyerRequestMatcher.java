package com.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

@Component
@Qualifier("buyerRequestMatcher")
public class BuyerRequestMatcher implements RequestMatcher {
    private AntPathRequestMatcher matcher;

    public BuyerRequestMatcher() {
        this.matcher = new AntPathRequestMatcher("/buyer/login/check", HttpMethod.POST.name());
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return matcher.matches(request);
    }
}