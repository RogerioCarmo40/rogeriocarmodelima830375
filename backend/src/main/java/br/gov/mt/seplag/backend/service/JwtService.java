package br.gov.mt.seplag.backend.service;

import br.gov.mt.seplag.backend.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtTokenProvider tokenProvider;

    public String gerarToken(String username) {
        return tokenProvider.createToken(username);
    }

    public String gerarRefreshToken(String token) {
        if (!tokenProvider.validateToken(token)) {
            throw new RuntimeException("Token inválido");
        }
        return tokenProvider.createToken(
            tokenProvider.getUsername(token)
        );
    }
}
