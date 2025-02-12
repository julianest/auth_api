package com.users.auth_api.service;

import com.users.auth_api.entity.Usuario;

public interface IJwtService {
    String generateToken(final Usuario usuario);
    String generateRefreshToken(final Usuario usuario);
    String extractUsername(String token);
    boolean isTokenValid(String token, Usuario usuario);
}
