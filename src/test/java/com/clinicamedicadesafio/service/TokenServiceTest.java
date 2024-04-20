package com.clinicamedicadesafio.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.clinicamedicadesafio.model.User;

@SpringBootTest
class TokenServiceTest {

    private final TokenService tokenService = new TokenService();
    private final User user = new User("username", "password");

    @Test
    void testGenerateToken() {
        TokenService.TokenResponse tokenResponse = tokenService.generateToken(user);
        assertNotNull(tokenResponse);
        assertNotNull(tokenResponse.getToken());
        assertNotNull(tokenResponse.getExpiration());
    }

    @Test
    void testValidateToken() {
        TokenService.TokenResponse tokenResponse = tokenService.generateToken(user);
        String token = tokenResponse.getToken();
        String username = tokenService.validateToken(token);
        assertEquals(user.getLogin(), username);
    }
}
