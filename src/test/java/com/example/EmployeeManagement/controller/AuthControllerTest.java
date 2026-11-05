
package com.example.EmployeeManagement.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import com.example.EmployeeManagement.dto.AuthResponse;
import com.example.EmployeeManagement.dto.LoginRequest;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtEncoder jwtEncoder;

    @InjectMocks
    private AuthController authController;

    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest();
        loginRequest.setUsername("testadmin");
        loginRequest.setPassword("testPassword123");
    }

    @Test
    void testLoginSuccess() {

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                    "testadmin",
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
                );

        when(authenticationManager.authenticate(
                any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        Jwt jwt = Jwt.withTokenValue("sample.jwt.token")
                .header("alg", "HS256")
                .subject("testadmin")
                .claim("roles", List.of("ROLE_ADMIN"))
                .build();

        when(jwtEncoder.encode(any(JwtEncoderParameters.class)))
                .thenReturn(jwt);

        ResponseEntity<AuthResponse> response =
                authController.login(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(authenticationManager).authenticate(
                any(UsernamePasswordAuthenticationToken.class));

        verify(jwtEncoder).encode(
                any(JwtEncoderParameters.class));
    }

    @Test
    void testLoginInvalidPassword() {

        when(authenticationManager.authenticate(
                any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(
                    new BadCredentialsException("Invalid credentials")
                );

        assertThrows(
                BadCredentialsException.class,
                () -> authController.login(loginRequest)
        );

        verifyNoInteractions(jwtEncoder);
    }

    @Test
    void testJwtContainsCorrectClaims() {

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                    "testadmin",
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
                );

        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication);

        Jwt jwt = Jwt.withTokenValue("sample.jwt.token")
                .header("alg", "HS256")
                .subject("testadmin")
                .build();

        when(jwtEncoder.encode(any(JwtEncoderParameters.class)))
                .thenReturn(jwt);

        authController.login(loginRequest);

        var captor = org.mockito.ArgumentCaptor.forClass(
                JwtEncoderParameters.class);

        verify(jwtEncoder).encode(captor.capture());

        JwtEncoderParameters parameters = captor.getValue();

        assertEquals(
                MacAlgorithm.HS256,
                parameters.getJwsHeader().getAlgorithm()
        );

        assertEquals(
                "testadmin",
                parameters.getClaims().getSubject()
        );


assertEquals(
    "employee-management",
    parameters.getClaims().getClaimAsString("iss")
);


        assertEquals(
                List.of("ROLE_ADMIN"),
                parameters.getClaims().getClaim("roles")
        );

        Instant issuedAt = parameters.getClaims().getIssuedAt();
        Instant expiresAt = parameters.getClaims().getExpiresAt();

        assertNotNull(issuedAt);
        assertNotNull(expiresAt);

        assertEquals(
                3600,
                Duration.between(issuedAt, expiresAt).getSeconds()
        );
    }
}
