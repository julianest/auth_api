package com.users.auth_api.service.impl;

import com.users.auth_api.entity.Usuario;
import com.users.auth_api.service.IJwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service
public class JwtServiceImpl implements IJwtService{
    
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;
    
    @Override
    public String generateToken(Usuario usuario) {
        return buildToken(usuario, jwtExpiration);
    }

    @Override
    public String generateRefreshToken(Usuario usuario) {
        return buildToken(usuario, refreshExpiration);
        
    }

    @Override
    public String extractUsername(String token) {
        final Claims jwtToken = Jwts.parser()
                .verifyWith(getSingInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return jwtToken.getSubject();
        
    }

    @Override
    public boolean isTokenValid(String token, Usuario usuario) {
        final String username = this.extractUsername(token);
        return (username.equals(usuario.getCorreo())) && !isTokenExpired(token);
        
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        final Claims jwtToken = Jwts.parser()
                .verifyWith(getSingInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return jwtToken.getExpiration();
    }

    private String buildToken(final Usuario usuario, long expiration) {
        var roles = usuario.getRoles().stream()
                .map(role -> "ROLE_" + role.name())
                .toList();
        return Jwts.builder()
                .id(usuario.getId().toString())
                .claims(Map.of(
                        "name", usuario.getNombre(),
                        "roles", roles
                ))
                .subject(usuario.getCorreo())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSingInKey())
                .compact();

    }

    private SecretKey getSingInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
