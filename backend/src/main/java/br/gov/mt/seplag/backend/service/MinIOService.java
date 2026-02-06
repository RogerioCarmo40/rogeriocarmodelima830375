package br.gov.mt.seplag.backend.service;

import jakarta.annotation.PostConstruct;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class MinIOService {

    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    private static final int PRESIGNED_URL_EXPIRY_MINUTES = 30;

    @PostConstruct
    public void init() {
        initBucket();
    }

    public void initBucket() {
        try {
            boolean exists = minioClient.bucketExists(
                BucketExistsArgs.builder().bucket(bucketName).build()
            );
            if (!exists) {
                minioClient.makeBucket(
                    MakeBucketArgs.builder().bucket(bucketName).build()
                );
                log.info("Bucket {} criado com sucesso", bucketName);
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao inicializar bucket MinIO", e);
        }
    }    
    
    public String uploadFile(MultipartFile file) throws IOException {
        String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        
        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(filename)
                    .stream(inputStream, file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build()
            );
            log.info("Arquivo {} enviado com sucesso", filename);
            return filename;
        } catch (MinioException | InvalidKeyException | NoSuchAlgorithmException e) {
            log.error("Erro ao fazer upload do arquivo: {}", e.getMessage());
            throw new RuntimeException("Erro ao fazer upload do arquivo", e);
        }
    }
    
    public String getPresignedUrl(String filename) {
        try {
            return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .object(filename)
                    .expiry(PRESIGNED_URL_EXPIRY_MINUTES, TimeUnit.MINUTES)
                    .build()
            );
        } catch (Exception e) {
            log.error("Erro ao gerar URL pré-assinada: {}", e.getMessage());
            throw new RuntimeException("Erro ao gerar URL pré-assinada", e);
        }
    }
    
    public void deleteFile(String filename) {
        try {
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(filename)
                    .build()
            );
            log.info("Arquivo {} removido com sucesso", filename);
        } catch (Exception e) {
            log.error("Erro ao remover arquivo: {}", e.getMessage());
        }
    }
}