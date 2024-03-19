package com.manager;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

@Component
@Qualifier("managerRequestMatcher")
public class ManagerRequestMatcher implements RequestMatcher {
    private AntPathRequestMatcher postMatcher;

    public ManagerRequestMatcher() {
        this.postMatcher = new AntPathRequestMatcher("/back/api/seller/**", HttpMethod.POST.name());
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return  postMatcher.matches(request);
    }
}