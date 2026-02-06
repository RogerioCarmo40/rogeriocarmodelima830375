package br.gov.mt.seplag.backend.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {

    private JwtTokenProvider tokenProvider;

    @BeforeEach
    void setUp() {
        tokenProvider = new JwtTokenProvider();

        ReflectionTestUtils.setField(
            tokenProvider,
            "jwtSecret",
            "testSecretKeyMuitoLongaParaFuncionarCorretamente123"
        );

        ReflectionTestUtils.setField(
            tokenProvider,
            "jwtExpirationMinutes",
            5
        );
    }

    @Test
    void deveCriarEValidarToken() {
        String token = tokenProvider.createToken("admin");

        assertThat(token).isNotNull();
        assertThat(tokenProvider.validateToken(token)).isTrue();
        assertThat(tokenProvider.getUsername(token)).isEqualTo("admin");
    }

    @Test
    void deveRejeitarTokenInvalido() {
        String tokenInvalido = "token.falso.assinatura";

        assertThat(tokenProvider.validateToken(tokenInvalido)).isFalse();
    }
}
