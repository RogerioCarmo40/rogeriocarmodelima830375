package br.gov.mt.seplag.backend.controller.v1;

import br.gov.mt.seplag.backend.domain.dto.ArtistaDTO;
import br.gov.mt.seplag.backend.service.ArtistaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/artistas")
@RequiredArgsConstructor
@Tag(name = "Artistas", description = "Gerenciamento de artistas")
@SecurityRequirement(name = "bearerAuth")
public class ArtistaController {
    
    private final ArtistaService artistaService;
    
    @GetMapping
    @Operation(summary = "Listar artistas com paginação e busca")
    public ResponseEntity<Page<ArtistaDTO>> listar(
        @Parameter(description = "Nome do artista para busca")
        @RequestParam(required = false) String nome,
        
        @PageableDefault(size = 10, sort = "nome", direction = Sort.Direction.ASC)
        Pageable pageable
    ) {
        return ResponseEntity.ok(artistaService.listarArtistas(nome, pageable));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Buscar artista por ID")
    public ResponseEntity<ArtistaDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(artistaService.buscarPorId(id));
    }
    
    @PostMapping
    @Operation(summary = "Criar novo artista")
    public ResponseEntity<ArtistaDTO> criar(@Valid @RequestBody ArtistaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(artistaService.criarArtista(dto));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar artista")
    public ResponseEntity<ArtistaDTO> atualizar(@PathVariable Long id, @Valid @RequestBody ArtistaDTO dto) {
        return ResponseEntity.ok(artistaService.atualizarArtista(id, dto));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Remover artista")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        artistaService.deletarArtista(id);
        return ResponseEntity.noContent().build();
    }
}