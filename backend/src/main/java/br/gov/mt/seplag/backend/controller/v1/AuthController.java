package br.gov.mt.seplag.backend.controller.v1;

import br.gov.mt.seplag.backend.domain.dto.AuthRequestDTO;
import br.gov.mt.seplag.backend.domain.dto.AuthResponseDTO;
import br.gov.mt.seplag.backend.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints de autenticação JWT")
public class AuthController {

    private final JwtService jwtService;

    @Value("${jwt.expiration-minutes}")
    private int expirationMinutes;

    @PostMapping("/login")
    @Operation(summary = "Realizar login e obter token JWT")
    public ResponseEntity<AuthResponseDTO> login(
            @RequestBody AuthRequestDTO request
    ) {
        try {
            // 🔐 Autenticação simplificada (escopo do desafio)
            if (!"admin".equals(request.getUsername()) ||
                !"admin".equals(request.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String accessToken = jwtService.gerarToken(request.getUsername());
            String refreshToken = jwtService.gerarRefreshToken(request.getUsername());

            return ResponseEntity.ok(
                    AuthResponseDTO.builder()
                            .token(accessToken)
                            .refreshToken(refreshToken)
                            .expiresIn(expirationMinutes * 60)
                            .build()
            );

        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping("/refresh")
    @Operation(summary = "Renovar token JWT")
    public ResponseEntity<AuthResponseDTO> refresh(
            @RequestHeader("Authorization") String authorization
    ) {
        String refreshToken = authorization.replace("Bearer ", "");

        String newAccessToken = jwtService.gerarRefreshToken(refreshToken);

        return ResponseEntity.ok(
                AuthResponseDTO.builder()
                        .token(newAccessToken)
                        .expiresIn(expirationMinutes * 60)
                        .build()
        );
    }
}
