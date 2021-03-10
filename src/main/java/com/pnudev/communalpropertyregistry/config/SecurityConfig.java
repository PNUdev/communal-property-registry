package com.pnudev.communalpropertyregistry.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .authorizeRequests()
                .antMatchers("/admin/**").authenticated()
                .antMatchers(
                        "/", "/api/statistics", "/api/properties/report", "/api/properties/map-locations",
                        "/api/properties", "/api/properties/{id}", "/api/categories-by-purpose",
                        "/images/**", "/css/main.css", "/js/**" )
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .defaultSuccessUrl("/admin", true)
                .permitAll()
                .and()
                .logout()
                .permitAll();

    }

}
