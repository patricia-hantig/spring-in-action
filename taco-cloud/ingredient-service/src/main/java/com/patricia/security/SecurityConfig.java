package com.patricia.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.stereotype.Service;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http.authorizeExchange()
                .pathMatchers("/design", "/orders").hasAuthority("USER")
                .anyExchange().permitAll()
                .and()
                .build();
    }

}

// instead of @EnableWebSecurity annotation we use @EnableWebFluxSecurity annotation
// the class doesn't extend WebSecurityConfigurerAdapter => so it doesn't override configure() method
// the body of securityWebFilterChain() is similar with configure()
//      - securityWebFilterChain() uses ServerHttpSecurity obj instead of HttpSecurity obj as configure()
//      - securityWebFilterChain() uses authorizeExchange() instead of authorizeRequests() as configure()
//      - we can use Ant-style wildcard paths but with pathMatchers() instead of antMatchers()
//      - /** is replaced by anyExchange()
// ServerHttpSecurity is new to Spring Security 5 and it's reactive analog for HttpSecurity
// because we used the bean SecurityWebFilterChain instead of overriding a method -> we must call .build() = to assemble all of the security rules into the SecurityWebFilterChain to be returned

