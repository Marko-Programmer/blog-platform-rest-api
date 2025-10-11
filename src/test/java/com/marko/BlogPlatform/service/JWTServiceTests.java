package com.marko.BlogPlatform.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class JWTServiceTests {


    @InjectMocks
    private JWTService jwtService;


    @BeforeEach
    public void setUp() {
        jwtService.secretKey = "c2VjcmV0MTIzNDU2Nzg5MGFiY2RlZmdoaWprbG1ub3BxcnN0";
        jwtService.jwtExpiration = 360000;

    }


    @Test
    public void generateToken_returnToken() {
        String username = "username";

        String token = jwtService.generateToken(username);

        Assertions.assertNotNull(token);
        assertTrue(token.length() > 0);
    }


    @Test
    public void extractUsername_returnUsername() {
        String username = "username";

        String token = jwtService.generateToken(username);
        String extractedUsername = jwtService.extractUsername(token);

        Assertions.assertEquals(username, extractedUsername);
    }

    @Test
    public void validateToken_returnsTrue_forValidTokenAndUser() {
        String username = "user1";
        UserDetails userDetails = User.withUsername(username).password("pass").roles("USER").build();

        String token = jwtService.generateToken(username);

        assertTrue(jwtService.validateToken(token, userDetails));
    }

    @Test
    public void validateToken_returnsFalse_forWrongUsername() {
        String tokenUsername = "user1";
        UserDetails userDetails = User.withUsername("user2").password("pass").roles("USER").build();

        String token = jwtService.generateToken(tokenUsername);

        assertFalse(jwtService.validateToken(token, userDetails));
    }

    @Test
    public void validateToken_returnsFalse_forExpiredToken() throws InterruptedException {
        jwtService.jwtExpiration = 1;
        String username = "user1";
        UserDetails userDetails = User.withUsername(username).password("pass").roles("USER").build();

        String token = jwtService.generateToken(username);
        Thread.sleep(10);

        assertFalse(jwtService.validateToken(token, userDetails));
    }


}
