
package com.example.EmployeeManagement.config;

import com.example.EmployeeManagement.service.UserDetailService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // 1. Load users from MySQL
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailService();
    }

    // 2. BCrypt password hashing
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 3. Database authentication provider
    @Bean
    public AuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();

        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    // 4. AuthenticationManager for /auth/login
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationProvider authenticationProvider) {

        return new ProviderManager(authenticationProvider);
    }

    // 5. Convert JWT roles into Spring Security authorities
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {

        JwtGrantedAuthoritiesConverter authoritiesConverter =
                new JwtGrantedAuthoritiesConverter();

        authoritiesConverter.setAuthoritiesClaimName("roles");

        // JWT contains ROLE_ADMIN, ROLE_HR, etc.
        // Do not add another prefix.
        authoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtConverter =
                new JwtAuthenticationConverter();

        jwtConverter.setJwtGrantedAuthoritiesConverter(
                authoritiesConverter
        );

        return jwtConverter;
    }

    // 6. Configure JWT security and authorization
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationConverter jwtConverter)
            throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            .authorizeHttpRequests(auth -> auth

                // Public endpoints
                .requestMatchers(
                    "/auth/login",
                    "/userInfo/userSaved",
                    "/welcome"
                ).permitAll()

                // ADMIN can delete employees
                .requestMatchers(
                    HttpMethod.DELETE,
                    "/delete/**"
                ).hasRole("ADMIN")

                // ADMIN and HR can create employees
                .requestMatchers(
                    HttpMethod.POST,
                    "/posted"
                ).hasAnyRole("ADMIN", "HR")

                // ADMIN and HR can update employees
                .requestMatchers(
                    HttpMethod.PUT,
                    "/updated/**"
                ).hasAnyRole("ADMIN", "HR")

                // ADMIN and HR can view employees
                .requestMatchers(
                    HttpMethod.GET,
                    "/getAll",
                    "/getBy/**"
                ).hasAnyRole("ADMIN", "HR")

                // Any other endpoint requires authentication
                .anyRequest().authenticated()
            )

            // Enable JWT authentication
            .oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwt ->
                    jwt.jwtAuthenticationConverter(jwtConverter)
                )
            )

            // Disable old login mechanisms
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable());

        return http.build();
    }
}
