package com.users.auth_api.service;

import com.users.auth_api.dto.request.LoginRequestDTO;
import com.users.auth_api.dto.request.RegistrarUsuarioRequestDTO;
import com.users.auth_api.dto.response.TokenResponse;
import com.users.auth_api.dto.response.UserResponse;
import com.users.auth_api.util.Result;

public interface IAuthService {
    Result<UserResponse, String> register(RegistrarUsuarioRequestDTO clienteRegisterRequestDTOst);
    Result<TokenResponse, String> login(LoginRequestDTO loginRequest);
    Result<TokenResponse, String> refreshToken(String authHeader);
    Result<String, String> logout(String authHeader);
}
