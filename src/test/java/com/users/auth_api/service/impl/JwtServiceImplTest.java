package com.users.auth_api.service.impl;

import com.users.auth_api.entity.Usuario;
import com.users.auth_api.enums.Roles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Base64;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class JwtServiceImplTest {

    @InjectMocks
    private JwtServiceImpl jwtService;

    @Mock
    private Usuario usuario;

    private static final String SECRET_KEY = "mySecretKeyForTestingPurpose1234567890";
    private static final long JWT_EXPIRATION = 3600000;
    private static final long REFRESH_EXPIRATION = 7200000;
    
    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtService, "secretKey", Base64.getEncoder().encodeToString(SECRET_KEY.getBytes()));
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", JWT_EXPIRATION);
        ReflectionTestUtils.setField(jwtService, "refreshExpiration", REFRESH_EXPIRATION);
    }

    @Test
    void shouldGenerateTokenSuccessfully() {
        when(usuario.getId()).thenReturn(1L);
        when(usuario.getNombre()).thenReturn("Carlos");
        when(usuario.getCorreo()).thenReturn("carlos@gmail.com");
        when(usuario.getRoles()).thenReturn(Set.of(Roles.USER));

        String token = jwtService.generateToken(usuario);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void shouldGenerateRefreshTokenSuccessfully() {
        when(usuario.getId()).thenReturn(1L);
        when(usuario.getNombre()).thenReturn("Carlos");
        when(usuario.getCorreo()).thenReturn("carlos@gmail.com");
        when(usuario.getRoles()).thenReturn(Set.of(Roles.USER));

        String refreshToken = jwtService.generateRefreshToken(usuario);

        assertNotNull(refreshToken);
        assertFalse(refreshToken.isEmpty());
    }

    @Test
    void shouldExtractUsernameFromToken() {
        when(usuario.getId()).thenReturn(1L);
        when(usuario.getNombre()).thenReturn("Carlos");
        when(usuario.getCorreo()).thenReturn("carlos@gmail.com");
        when(usuario.getRoles()).thenReturn(Set.of(Roles.USER));

        String token = jwtService.generateToken(usuario);
        String extractedUsername = jwtService.extractUsername(token);

        assertEquals("carlos@gmail.com", extractedUsername);
    }

    @Test
    void shouldValidateTokenSuccessfully() {
        when(usuario.getId()).thenReturn(1L);
        when(usuario.getNombre()).thenReturn("Carlos");
        when(usuario.getCorreo()).thenReturn("carlos@gmail.com");
        when(usuario.getRoles()).thenReturn(Set.of(Roles.USER));

        String token = jwtService.generateToken(usuario);
        boolean isValid = jwtService.isTokenValid(token, usuario);

        assertTrue(isValid);
    }

}