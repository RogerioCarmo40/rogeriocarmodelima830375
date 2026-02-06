package br.gov.mt.seplag.backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {
    
    // Valor padrão caso a propriedade não seja encontrada
    @Value("${jwt.secret:seplagBackendSeniorSecretKeyDefault2026MatoGrosso}")
    private String jwtSecret;
    
    @Value("${jwt.expiration-minutes:5}")
    private int jwtExpirationMinutes;
    
    private SecretKey getKey() {
        // Garantir que a chave tem pelo menos 256 bits (32 caracteres)
        String key = jwtSecret.length() < 32 ? jwtSecret.repeat(2) : jwtSecret;
        return Keys.hmacShaKeyFor(key.substring(0, 32).getBytes(StandardCharsets.UTF_8));
    }
    
    public String createToken(String username) {
        try {
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + jwtExpirationMinutes * 60 * 1000L);
            
            return Jwts.builder()
                    .subject(username)
                    .issuedAt(now)
                    .expiration(expiryDate)
                    .signWith(getKey())
                    .compact();
        } catch (Exception e) {
            log.error("Erro ao criar token JWT: {}", e.getMessage());
            throw new RuntimeException("Erro ao criar token JWT", e);
        }
    }
    
    public String getUsername(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
    
    public boolean validateToken(String token) {
        try {
            if (token == null || token.isBlank()) {
                log.error("JWT vazio ou nulo");
                return false;
            }
            Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Token JWT inválido: {}", e.getMessage());
            return false;
        }
    }
}