package com.pnudev.communalpropertyregistry.analytics;


import com.pnudev.communalpropertyregistry.service.UserActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UserAnalyticsFilter implements Filter {

    private final UserActionService userActionService;

    @Autowired
    public UserAnalyticsFilter(UserActionService userActionService) {
        this.userActionService = userActionService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        userActionService.saveUserAction((HttpServletRequest) servletRequest);

        filterChain.doFilter(servletRequest, servletResponse);
    }

}
