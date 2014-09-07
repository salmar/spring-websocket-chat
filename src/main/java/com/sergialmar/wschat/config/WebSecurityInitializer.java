package com.sergialmar.wschat.config;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;

public class WebSecurityInitializer extends AbstractSecurityWebApplicationInitializer {
    @Override
    protected String getDispatcherWebApplicationContextSuffix() {
        return AbstractDispatcherServletInitializer.DEFAULT_SERVLET_NAME;
    }
}