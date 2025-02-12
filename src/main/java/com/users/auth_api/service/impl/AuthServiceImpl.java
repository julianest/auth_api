package com.users.auth_api.service.impl;

import com.users.auth_api.dto.request.LoginRequestDTO;
import com.users.auth_api.dto.request.RegistrarUsuarioRequestDTO;
import com.users.auth_api.dto.response.TokenResponse;
import com.users.auth_api.dto.response.UserResponse;
import com.users.auth_api.entity.Token;
import com.users.auth_api.entity.Usuario;
import com.users.auth_api.mapper.IUsuarioMapper;
import com.users.auth_api.repository.ITokenRepository;
import com.users.auth_api.repository.IUsuarioRepository;
import com.users.auth_api.service.IAuthService;
import com.users.auth_api.service.IJwtService;
import com.users.auth_api.util.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final PasswordEncoder passwordEncoder;
    private final IUsuarioRepository userRepository;
    private final ITokenRepository tokenRepository;
    private final IJwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final IUsuarioMapper usuarioMapper;

    @Override
    public Result<UserResponse, String> register(RegistrarUsuarioRequestDTO registrarUsuarioRequestDTO) {
        final Optional<Usuario> userOptional = userRepository.findByCorreoAndActivoTrue(registrarUsuarioRequestDTO.getCorreo());
        if(userOptional.isPresent()){
            return Result.failure(List.of("El usuario con el correo " + registrarUsuarioRequestDTO.getCorreo()+ " , ya se encuentra registrado."), HttpStatus.BAD_REQUEST);
        }
        var user = buildCliente(registrarUsuarioRequestDTO);
        Usuario userSaved = userRepository.save(user);
        return Result.success(new UserResponse(userSaved.getId()));
    }

    @Override
    public Result<TokenResponse, String> login(LoginRequestDTO loginRequest) {

        final Optional<Usuario> userOptional = userRepository.findByCorreoAndActivoTrue(loginRequest.getEmail());

        if(userOptional.isEmpty()){
            return createErrorResult("Usuario no registrado.", HttpStatus.NOT_FOUND);
        }
        Usuario usuario = userOptional.get();

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword(),
                    usuario.getRoles().stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                            .toList()));
        } catch (AuthenticationException e) {
            return createErrorResult("Correo o contraseña incorrecto.", HttpStatus.UNAUTHORIZED);
        }

        var jwtToken = jwtService.generateToken(usuario);
        var refreshToken = jwtService.generateRefreshToken(usuario);
        revokeAllUserTokens(usuario);
        saveUserToken(usuario, jwtToken);
        return Result.success(new TokenResponse(usuario.getId(),jwtToken, refreshToken));
    }

    @Override
    public Result<TokenResponse, String> refreshToken(String authHeader) {
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            return createErrorResult("Invalid authorization header format", HttpStatus.BAD_REQUEST);
        }

        final String refreshToken = authHeader.substring(7);
        final String userEmail = jwtService.extractUsername(refreshToken);

        if (userEmail == null) {
            return createErrorResult("Invalid refresh token", HttpStatus.UNAUTHORIZED);
        }

        final Optional<Usuario> userOptional = userRepository.findByCorreoAndActivoTrue(userEmail);

        if(userOptional.isEmpty()){
            return createErrorResult("User not found", HttpStatus.NOT_FOUND);
        }
        Usuario usuario = userOptional.get();

        if(!jwtService.isTokenValid(refreshToken, usuario)){
            return createErrorResult("Invalid or expired refresh token", HttpStatus.UNAUTHORIZED);
        }

        final Optional<Token> optionalToken = tokenRepository.findByToken(refreshToken);
        if(optionalToken.isEmpty()){
            return createErrorResult("Token not found", HttpStatus.BAD_REQUEST);
        }

        Token token = optionalToken.get();
        if (token.isExpired() || token.isRevoked()) {
            return createErrorResult("The token has expired or has been revoked.", HttpStatus.UNAUTHORIZED);
        }

        final String accessToken = jwtService.generateToken(usuario);
        revokeAllUserTokens(usuario);
        saveUserToken(usuario, accessToken);
        return Result.success(new TokenResponse(usuario.getId(), accessToken, refreshToken));

    }

    @Override
    public Result<String, String> logout(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Result.failure(List.of("Invalid authorization header format"), HttpStatus.BAD_REQUEST);
        }
        final String jwtToken = authHeader.substring(7);
        final Optional<Token> optionalToken = tokenRepository.findByToken(jwtToken);
        if(optionalToken.isEmpty()){
            return Result.failure(Collections.singletonList("Token not found"), HttpStatus.BAD_REQUEST);
        }
        Token token = optionalToken.get();
        token.setRevoked(true);
        token.setExpired(true);
        tokenRepository.save(token);
        SecurityContextHolder.clearContext();
        return Result.success("Logout successful");
    }

    private void saveUserToken(Usuario usuario, String  jwtToken) {
        var token = Token.builder()
                .usuario(usuario)
                .token(jwtToken)
                .type(Token.TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(Usuario usuario) {
        final List<Token> validUserToken = tokenRepository
                .findAllValidIsFalseOrRevokedIsFalseByUsuarioId(usuario.getId());
        if (!validUserToken.isEmpty()) {
            for (final Token token : validUserToken) {
                token.setExpired(true);
                token.setRevoked(true);
            }
            tokenRepository.saveAll(validUserToken);
        }

    }

    private Result<TokenResponse, String> createErrorResult(String errorMessage, HttpStatus httpStatus) {
        return Result.failure(Collections.singletonList(errorMessage), httpStatus);
    }

    private Usuario buildCliente(RegistrarUsuarioRequestDTO registrarUsuarioRequestDTO){
        registrarUsuarioRequestDTO.setContrasena(passwordEncoder.encode(registrarUsuarioRequestDTO.getContrasena()));
        return usuarioMapper.toUsuario(registrarUsuarioRequestDTO);
    }

}
