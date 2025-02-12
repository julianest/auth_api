package com.users.auth_api.service.impl;

import com.users.auth_api.dto.request.LoginRequestDTO;
import com.users.auth_api.dto.request.RegistrarUsuarioRequestDTO;
import com.users.auth_api.dto.response.TokenResponse;
import com.users.auth_api.dto.response.UserResponse;
import com.users.auth_api.entity.Token;
import com.users.auth_api.entity.Usuario;
import com.users.auth_api.enums.Roles;
import com.users.auth_api.mapper.IUsuarioMapper;
import com.users.auth_api.repository.ITokenRepository;
import com.users.auth_api.repository.IUsuarioRepository;
import com.users.auth_api.service.IJwtService;
import com.users.auth_api.util.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class AuthServiceImplTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private IUsuarioRepository userRepository;

    @Mock
    private ITokenRepository tokenRepository;

    @Mock
    private IJwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private IUsuarioMapper usuarioMapper;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister_UserAlreadyExists() {
        RegistrarUsuarioRequestDTO requestDTO = new RegistrarUsuarioRequestDTO();
        requestDTO.setCorreo("test@example.com");

        when(userRepository.findByCorreoAndActivoTrue(anyString())).thenReturn(Optional.of(new Usuario()));

        Result<UserResponse, String> result = authService.register(requestDTO);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("El usuario con el correo test@example.com , ya se encuentra registrado.", result.getErrors().get(0));
    }

//    @Test
//    void testRegister_Success() {
//        RegistrarUsuarioRequestDTO requestDTO = new RegistrarUsuarioRequestDTO();
//        requestDTO.setCorreo("test@example.com");
//        requestDTO.setContrasena("password");
//
//        Usuario usuario = new Usuario();
//        usuario.setId(1L);
//
//        when(userRepository.findByCorreoAndActivoTrue(anyString())).thenReturn(Optional.empty());
//        when(usuarioMapper.toUsuario(any(RegistrarUsuarioRequestDTO.class))).thenReturn(usuario);
//        when(userRepository.save(any(Usuario.class))).thenReturn(usuario);
//        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
//
//        Result<UserResponse, String> result = authService.register(requestDTO);
//
//        assertEquals(HttpStatus.OK, result.getStatusCode());
//    }

    @Test
    void testLogin_UserNotFound() {
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        when(userRepository.findByCorreoAndActivoTrue(anyString())).thenReturn(Optional.empty());

        Result<TokenResponse, String> result = authService.login(loginRequest);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals("Usuario no registrado.", result.getErrors().get(0));
    }

    @Test
    void login_invalidCredentials_returnsUnauthorized() {
        LoginRequestDTO loginRequest = new LoginRequestDTO("test@example.com", "wrongPassword");
        Usuario usuario = new Usuario();
        usuario.setCorreo("test@example.com");
        usuario.setRoles(Set.of(Roles.USER));

        when(userRepository.findByCorreoAndActivoTrue(loginRequest.getEmail())).thenReturn(Optional.of(usuario));
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Invalid credentials"));

        Result<TokenResponse, String> result = authService.login(loginRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Correo o contraseña incorrecto.", result.getErrors().get(0));
        assertTrue(!result.isSuccess());
    }

    @Test
    void testLogin_Success() {
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setRoles(Collections.emptySet());

        when(userRepository.findByCorreoAndActivoTrue(anyString())).thenReturn(Optional.of(usuario));
        when(jwtService.generateToken(any(Usuario.class))).thenReturn("jwtToken");
        when(jwtService.generateRefreshToken(any(Usuario.class))).thenReturn("refreshToken");

        Result<TokenResponse, String> result = authService.login(loginRequest);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1L, result.getValue().idUser());
        assertEquals("jwtToken", result.getValue().accessToken());
        assertEquals("refreshToken", result.getValue().refreshToken());
    }

    @Test
    void testLogout_InvalidAuthHeader() {
        Result<String, String> result = authService.logout(null);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Invalid authorization header format", result.getErrors().get(0));
    }

    @Test
    void testLogout_TokenNotFound() {
        when(tokenRepository.findByToken(anyString())).thenReturn(Optional.empty());

        Result<String, String> result = authService.logout("Bearer jwtToken");

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Token not found", result.getErrors().get(0));
    }

    @Test
    void testLogout_Success() {
        Token token = new Token();
        token.setExpired(false);
        token.setRevoked(false);

        when(tokenRepository.findByToken(anyString())).thenReturn(Optional.of(token));

        Result<String, String> result = authService.logout("Bearer jwtToken");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Logout successful", result.getValue());
    }

    @Test
    void refreshToken_invalidAuthHeader_returnsBadRequest() {
        String authHeader = "InvalidHeader";

        Result<TokenResponse, String> result = authService.refreshToken(authHeader);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Invalid authorization header format", result.getErrors().get(0));
        assertFalse(result.isSuccess());
    }

    @Test
    void refreshToken_nullAuthHeader_returnsBadRequest() {
        String authHeader = null;

        Result<TokenResponse, String> result = authService.refreshToken(authHeader);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Invalid authorization header format", result.getErrors().get(0));
        assertFalse(result.isSuccess());
    }

    @Test
    void refreshToken_invalidRefreshToken_returnsUnauthorized() {
        String authHeader = "Bearer invalidRefreshToken";

        when(jwtService.extractUsername("invalidRefreshToken")).thenReturn(null);

        Result<TokenResponse, String> result = authService.refreshToken(authHeader);

        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Invalid refresh token", result.getErrors().get(0));
        assertFalse(result.isSuccess());
    }

    @Test
    void refreshToken_userNotFound_returnsNotFound() {
        String authHeader = "Bearer validRefreshToken";
        String userEmail = "test@example.com";

        when(jwtService.extractUsername("validRefreshToken")).thenReturn(userEmail);
        when(userRepository.findByCorreoAndActivoTrue(userEmail)).thenReturn(Optional.empty());

        Result<TokenResponse, String> result = authService.refreshToken(authHeader);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals("User not found", result.getErrors().get(0));
        assertFalse(result.isSuccess());
    }

    @Test
    void refreshToken_tokenNotFound_returnsBadRequest() {
        String authHeader = "Bearer validRefreshToken";
        String userEmail = "test@example.com";
        Usuario usuario = new Usuario();
        usuario.setCorreo(userEmail);
        usuario.setRoles(Set.of(Roles.USER));

        when(jwtService.extractUsername("validRefreshToken")).thenReturn(userEmail);
        when(userRepository.findByCorreoAndActivoTrue(userEmail)).thenReturn(Optional.of(usuario));
        when(tokenRepository.findByToken("validRefreshToken")).thenReturn(Optional.empty());


        Result<TokenResponse, String> result = authService.refreshToken(authHeader);

        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Invalid or expired refresh token", result.getErrors().get(0));
        assertFalse(result.isSuccess());
    }

    @Test
    void refreshToken_invalidToken_returnsUnauthorized() {
        String authHeader = "Bearer validRefreshToken";
        String userEmail = "test@example.com";
        Usuario usuario = new Usuario();
        usuario.setCorreo(userEmail);
        usuario.setRoles(Set.of(Roles.USER));
        Token token = new Token();

        when(jwtService.extractUsername("validRefreshToken")).thenReturn(userEmail);
        when(userRepository.findByCorreoAndActivoTrue(userEmail)).thenReturn(Optional.of(usuario));
        when(tokenRepository.findByToken("validRefreshToken")).thenReturn(Optional.of(token));
        when(jwtService.isTokenValid("validRefreshToken", usuario)).thenReturn(false);

        Result<TokenResponse, String> result = authService.refreshToken(authHeader);

        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Invalid or expired refresh token", result.getErrors().get(0));
        assertFalse(result.isSuccess());
    }

    @Test
    void refreshToken_expiredOrRevokedToken_returnsUnauthorized() {
        String authHeader = "Bearer validRefreshToken";
        String userEmail = "test@example.com";
        Usuario usuario = new Usuario();
        usuario.setCorreo(userEmail);
        usuario.setRoles(Set.of(Roles.USER));
        Token token = new Token();
        token.setExpired(true);

        when(jwtService.extractUsername("validRefreshToken")).thenReturn(userEmail);
        when(userRepository.findByCorreoAndActivoTrue(userEmail)).thenReturn(Optional.of(usuario));
        when(tokenRepository.findByToken("validRefreshToken")).thenReturn(Optional.of(token));
        when(jwtService.isTokenValid("validRefreshToken", usuario)).thenReturn(true);

        Result<TokenResponse, String> result = authService.refreshToken(authHeader);

        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("The token has expired or has been revoked.", result.getErrors().get(0));
        assertFalse(result.isSuccess());
    }

    @Test
    void refreshToken_success() {
        String authHeader = "Bearer validRefreshToken";
        String refreshToken = "validRefreshToken";
        String userEmail = "test@example.com";
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setCorreo(userEmail);
        usuario.setRoles(Set.of(Roles.USER));
        Token token = new Token();

        when(jwtService.extractUsername(refreshToken)).thenReturn(userEmail);
        when(userRepository.findByCorreoAndActivoTrue(userEmail)).thenReturn(Optional.of(usuario));
        when(tokenRepository.findByToken(refreshToken)).thenReturn(Optional.of(token));
        when(jwtService.isTokenValid(refreshToken, usuario)).thenReturn(true);
        when(jwtService.generateToken(usuario)).thenReturn("newAccessToken");

        Result<TokenResponse, String> result = authService.refreshToken(authHeader);

        assertTrue(result.isSuccess());

        TokenResponse tokenResponse = result.getValue();
        assertNotNull(tokenResponse);
        assertEquals(usuario.getId(), tokenResponse.idUser());
        assertEquals("newAccessToken", tokenResponse.accessToken());
        assertEquals(refreshToken, tokenResponse.refreshToken());

        verify(jwtService).generateToken(usuario);
    }

}