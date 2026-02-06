package br.gov.mt.seplag.backend.service;

import br.gov.mt.seplag.backend.domain.dto.ArtistaDTO;
import br.gov.mt.seplag.backend.domain.entity.Artista;
import br.gov.mt.seplag.backend.repository.ArtistaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ArtistaServiceTest {
    
    @Mock
    private ArtistaRepository artistaRepository;
    
    @InjectMocks
    private ArtistaService artistaService;
    
    private Artista artista;
    private ArtistaDTO artistaDTO;
    
    @BeforeEach
    void setUp() {
        artista = Artista.builder()
            .id(1L)
            .nome("Serj Tankian")
            .tipo(Artista.TipoArtista.CANTOR)
            .build();
            
        artistaDTO = ArtistaDTO.builder()
            .id(1L)
            .nome("Serj Tankian")
            .tipo("CANTOR")
            .build();
    }
    
    @Test
    void deveListarArtistasComPaginacao() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Artista> page = new PageImpl<>(List.of(artista));
        
        given(artistaRepository.findAll(pageable)).willReturn(page);
        
        Page<ArtistaDTO> result = artistaService.listarArtistas(null, pageable);
        
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getNome()).isEqualTo("Serj Tankian");
    }
    
    @Test
    void deveBuscarArtistaPorId() {
        given(artistaRepository.findById(1L)).willReturn(Optional.of(artista));
        
        ArtistaDTO result = artistaService.buscarPorId(1L);
        
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNome()).isEqualTo("Serj Tankian");
    }
    
    @Test
    void deveLancarExcecaoQuandoArtistaNaoEncontrado() {
        given(artistaRepository.findById(99L)).willReturn(Optional.empty());
        
        assertThatThrownBy(() -> artistaService.buscarPorId(99L))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Artista não encontrado");
    }
    
    @Test
    void deveCriarNovoArtista() {
        given(artistaRepository.existsByNome(any(String.class))).willReturn(false);
        given(artistaRepository.save(any(Artista.class))).willReturn(artista);

        ArtistaDTO result = artistaService.criarArtista(artistaDTO);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getNome()).isEqualTo("Serj Tankian");
    }

    @Test
    void deveLancarExcecaoQuandoArtistaJaExiste() {
        given(artistaRepository.existsByNome(any(String.class))).willReturn(true);

        assertThatThrownBy(() -> artistaService.criarArtista(artistaDTO))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Artista já existe");
    }

}