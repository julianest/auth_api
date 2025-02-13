package com.users.auth_api.controller;

import com.users.auth_api.dto.request.LoginRequestDTO;
import com.users.auth_api.dto.request.RegistrarUsuarioRequestDTO;
import com.users.auth_api.dto.response.TokenResponse;
import com.users.auth_api.dto.response.UserResponse;
import com.users.auth_api.service.IAuthService;
import com.users.auth_api.util.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IAuthService authService;

    @InjectMocks
    private AuthController authController;

    @Autowired
    private ObjectMapper objectMapper;

    private RegistrarUsuarioRequestDTO requestDTO;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        requestDTO = RegistrarUsuarioRequestDTO.builder()
                .numeroIdentificacion("12346")
                .nombre("Carlos")
                .correo("carlos@gmail.com")
                .contrasena("Carlos1234!")
                .build();
    }

    @Test
    void testRegisterSuccess() throws Exception {
        UserResponse userResponse = new UserResponse(1L);
        Result<UserResponse, String> result = Result.success(userResponse);

        when(authService.register(any(RegistrarUsuarioRequestDTO.class))).thenReturn(result);

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void testRegisterFailure() throws Exception {
        Result<UserResponse, String> result = Result.failure(List.of("Error"),HttpStatus.BAD_REQUEST);

        when(authService.register(requestDTO)).thenReturn(result);

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterFailureException() throws Exception {

        when(authService.register(requestDTO)).thenThrow(new RuntimeException("Error de registro"));

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testAuthenticateSuccess() throws Exception {
        String authHeader = "Bearer validToken";
        LoginRequestDTO requestDTO = new LoginRequestDTO("carlos@gamail.com", "Carlos1234!");
        TokenResponse tokenResponse = new TokenResponse(1L, authHeader, "Bearer validRefreshToken");
        Result<TokenResponse, String> result = Result.success(tokenResponse);

        when(authService.login(any(LoginRequestDTO.class))).thenReturn(result);

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void testAuthenticateFailure() throws Exception {
        LoginRequestDTO requestDTO = new LoginRequestDTO("carlos@gamail.com", "Carlos1234!");
        Result<TokenResponse, String> result = Result.failure(List.of("Invalid credentials"),HttpStatus.UNAUTHORIZED);

        when(authService.login(requestDTO)).thenReturn(result);

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testAuthenticateException() throws Exception {
        LoginRequestDTO requestDTO = new LoginRequestDTO("carlos@gamail.com", "Carlos1234!");
        doThrow(new RuntimeException("Error de registro"))
                .when(authService).login(requestDTO);

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testRefreshTokenSuccess() throws Exception {
        String authHeader = "Bearer validToken";
        TokenResponse tokenResponse = new TokenResponse(1L, authHeader, "Bearer validRefreshToken");
        Result<TokenResponse, String> result = Result.success(tokenResponse);

        when(authService.refreshToken(any(String.class))).thenReturn(result);

        mockMvc.perform(post("/api/v1/auth/refresh")
                .header(HttpHeaders.AUTHORIZATION, authHeader))
                .andExpect(status().isOk());
    }

    @Test
    void testRefreshTokenFailure() throws Exception {
        String authHeader = "Bearer invalidToken";
        Result<TokenResponse, String> result = Result.failure(List.of("Invalid token"), HttpStatus.UNAUTHORIZED);

        when(authService.refreshToken(any(String.class))).thenReturn(result);

        mockMvc.perform(post("/api/v1/auth/refresh")
                .header(HttpHeaders.AUTHORIZATION, authHeader))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testRefreshTokenException() throws Exception {
        String token = "Bearer invalidToken";

        doThrow(new RuntimeException("Error de registro"))
                .when(authService).refreshToken(token);

        mockMvc.perform(post("/api/v1/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testLogoutSuccess() throws Exception {
        String authHeader = "Bearer validToken";
        Result<String, String> result = Result.success("Logged out");

        when(authService.logout(any(String.class))).thenReturn(result);

        mockMvc.perform(post("/api/v1/auth/logout")
                .header(HttpHeaders.AUTHORIZATION, authHeader))
                .andExpect(status().isOk());
    }

    @Test
    void testLogoutFailure() throws Exception {
        String authHeader = "Bearer invalidToken";
        Result<String, String> result = Result.failure(List.of("Invalid token"), HttpStatus.UNAUTHORIZED);

        when(authService.logout(any(String.class))).thenReturn(result);

        mockMvc.perform(post("/api/v1/auth/logout")
                .header(HttpHeaders.AUTHORIZATION, authHeader))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testLogoutFailureException() throws Exception {
        String token = "Bearer invalidToken";

        doThrow(new RuntimeException("Error de registro"))
                .when(authService).logout(token);

        mockMvc.perform(post("/api/v1/auth/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token))
                .andExpect(status().isInternalServerError());
    }
}