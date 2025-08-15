package com.codewithmosh.store.common.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

import com.codewithmosh.store.common.SecurityRules;

@Component
public class SwaggerSecurityRules implements SecurityRules{
    @Override
    public void configure(
        AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry
    ) {
        registry.requestMatchers(
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**"
        ).permitAll();
    }
}
