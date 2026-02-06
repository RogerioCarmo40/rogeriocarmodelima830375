package br.gov.mt.seplag.backend.config;

import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MinIOConfig {

    private final MinioProperties properties;

    @Bean
    public MinioClient minioClient() {

        return MinioClient.builder()
                .endpoint(properties.getUrl())
                .credentials(
                        properties.getAccessKey(),
                        properties.getSecretKey()
                )
                .build();
    }
}
