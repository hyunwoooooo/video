package com.example.video.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${securityPath}")
    private String securityPath;

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(securityPath);
    }
}