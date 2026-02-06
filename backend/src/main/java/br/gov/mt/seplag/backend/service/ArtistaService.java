package br.gov.mt.seplag.backend.service;

import br.gov.mt.seplag.backend.domain.dto.ArtistaDTO;
import br.gov.mt.seplag.backend.domain.entity.Artista;
import br.gov.mt.seplag.backend.domain.entity.Artista.TipoArtista;
import br.gov.mt.seplag.backend.repository.ArtistaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArtistaService {
    
    private final ArtistaRepository artistaRepository;
    
    @Transactional(readOnly = true)
    public Page<ArtistaDTO> listarArtistas(String nome, Pageable pageable) {
        Page<Artista> artistas = nome != null ?
            artistaRepository.findByNomeContainingIgnoreCase(nome, pageable) :
            artistaRepository.findAll(pageable);

            
        return artistas.map(this::toDTO);
    }
    
    @Transactional(readOnly = true)
    public ArtistaDTO buscarPorId(Long id) {
        Artista artista = artistaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Artista não encontrado"));
        return toDTO(artista);
    }
    
    @Transactional
    public ArtistaDTO criarArtista(ArtistaDTO dto) {
        if (artistaRepository.existsByNome(dto.getNome())) {
            throw new RuntimeException("Artista já existe");
        }
        
        Artista artista = Artista.builder()
            .nome(dto.getNome())
            .tipo(TipoArtista.valueOf(dto.getTipo()))
            .build();
        
        artista = artistaRepository.save(artista);
        return toDTO(artista);
    }
    
    @Transactional
    public ArtistaDTO atualizarArtista(Long id, ArtistaDTO dto) {
        Artista artista = artistaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Artista não encontrado"));
            
        artista.setNome(dto.getNome());
        artista.setTipo(TipoArtista.valueOf(dto.getTipo()));
        
        artista = artistaRepository.save(artista);
        return toDTO(artista);
    }
    
    @Transactional
    public void deletarArtista(Long id) {
        if (!artistaRepository.existsById(id)) {
            throw new RuntimeException("Artista não encontrado");
        }
        artistaRepository.deleteById(id);
    }
    
    private ArtistaDTO toDTO(Artista artista) {
        return ArtistaDTO.builder()
            .id(artista.getId())
            .nome(artista.getNome())
            .tipo(artista.getTipo().name())
            .build();
    }
}
