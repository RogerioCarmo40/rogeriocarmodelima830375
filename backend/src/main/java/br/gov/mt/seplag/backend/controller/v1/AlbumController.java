package br.gov.mt.seplag.backend.controller.v1;

import br.gov.mt.seplag.backend.domain.dto.AlbumDTO;
import br.gov.mt.seplag.backend.domain.dto.AlbumRequestDTO;
import br.gov.mt.seplag.backend.service.AlbumService;
import br.gov.mt.seplag.backend.service.RateLimitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/v1/albuns")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Álbuns", description = "Gerenciamento de álbuns")
@SecurityRequirement(name = "bearerAuth")
public class AlbumController {
    
    private final AlbumService albumService;
    private final RateLimitService rateLimitService;
    
    @GetMapping
    @Operation(summary = "Listar álbuns com paginação, filtros e ordenação")
    public ResponseEntity<Page<AlbumDTO>> listar(
        @Parameter(description = "ID do artista para filtrar")
        @RequestParam(required = false) Long artistaId,
        
        @Parameter(description = "Nome do artista para busca")
        @RequestParam(required = false) String artistaNome,
        
        @Parameter(description = "Ordenação asc/desc por nome do artista")
        @RequestParam(defaultValue = "asc") String sortDirection,
        
        @PageableDefault(size = 10, sort = "titulo", direction = Sort.Direction.ASC)
        Pageable pageable,
        
        HttpServletRequest request
    ) {
        if (!rateLimitService.isAllowed(request)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
        
        // Ajustar ordenação por nome do artista se solicitado
        if ("nomeArtista".equals(sortDirection)) {
            pageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(
                    Sort.Order.asc("artistw.nome"),
                    Sort.Order.asc("titulo")
                )
            );
        }
        
        return ResponseEntity.ok(albumService.listarAlbuns(artistaId, artistaNome, pageable));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Buscar álbum por ID")
    public ResponseEntity<AlbumDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(albumService.buscarPorId(id));
    }
    
    @PostMapping
    @Operation(summary = "Criar novo álbum")
    public ResponseEntity<AlbumDTO> criar(@Valid @RequestBody AlbumRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(albumService.criarAlbum(dto));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar álbum")
    public ResponseEntity<AlbumDTO> atualizar(@PathVariable Long id, @Valid @RequestBody AlbumRequestDTO dto) {
        return ResponseEntity.ok(albumService.atualizarAlbum(id, dto));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Remover álbum")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        albumService.deletarAlbum(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{id}/imagens")
    @Operation(summary = "Upload de imagem do álbum")
    public ResponseEntity<Void> uploadImagem(
        @PathVariable Long id,
        @RequestParam("file") MultipartFile file
    ) throws IOException {
        albumService.uploadImagem(id, file);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
