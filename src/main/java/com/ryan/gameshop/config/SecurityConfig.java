package com.ryan.gameshop.config;

import com.okta.spring.boot.oauth.Okta;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;

@Configuration
public class SecurityConfig {
   @Bean
   public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{

      // Disable Cross Site Request Forgery
      httpSecurity.csrf().disable();

      // Protect endpoints
      httpSecurity.authorizeRequests(configurer ->
            configurer.antMatchers("/games/secure/*",
                        "/admin/secure/*")
                  .authenticated())
            // use OAuth2 as the resource server and JWT as the token format
            .oauth2ResourceServer()
            .jwt();

      // Enables Cross-Origin Resource Sharing
      httpSecurity.cors();

      // Content Negotiation Strategy
      httpSecurity.setSharedObject(ContentNegotiationStrategy.class,
            new HeaderContentNegotiationStrategy());

      // Ensures that the response body for 401 responses(Unauthorized responses) is populated
      // with appropriate error information
      Okta.configureResourceServer401ResponseBody(httpSecurity);

      return httpSecurity.build();
   }
}
