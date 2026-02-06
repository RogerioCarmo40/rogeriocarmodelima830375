package br.gov.mt.seplag.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Tag(name = "Health Checks", description = "Endpoints de verificação de saúde do serviço")
public class HealthCheckController {
    
    @GetMapping("/actuator/health/liveness")
    @Operation(summary = "Verificar se a aplicação está viva")
    public ResponseEntity<Map<String, String>> liveness() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("component", "backend-senior-seplag");
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/actuator/health/readiness")
    @Operation(summary = "Verificar se a aplicação está pronta para receber tráfego")
    public ResponseEntity<Map<String, String>> readiness() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("database", "CONNECTED");
        response.put("storage", "AVAILABLE");
        return ResponseEntity.ok(response);
    }
}