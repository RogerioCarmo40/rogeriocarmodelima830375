package br.gov.mt.seplag.backend.controller.v1;

import br.gov.mt.seplag.backend.domain.entity.Regional;
import br.gov.mt.seplag.backend.repository.RegionalRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/regionais")
@RequiredArgsConstructor
@Tag(name = "Regionais", description = "Consulta de regionais sincronizadas")
@SecurityRequirement(name = "bearerAuth")
public class RegionalController {
    
    private final RegionalRepository regionalRepository;
    
    @GetMapping
    @Operation(summary = "Listar regionais ativas")
    public ResponseEntity<List<Regional>> listarAtivas() {
        return ResponseEntity.ok(regionalRepository.findAllByAtivoTrue());
    }
    
    @GetMapping("/todas")
    @Operation(summary = "Listar todas regionais")
    public ResponseEntity<List<Regional>> listarTodas() {
        return ResponseEntity.ok(regionalRepository.findAll());
    }
}
