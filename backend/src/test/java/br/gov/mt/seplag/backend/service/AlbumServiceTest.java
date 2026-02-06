package br.gov.mt.seplag.backend.service;

import br.gov.mt.seplag.backend.domain.dto.AlbumDTO;
import br.gov.mt.seplag.backend.domain.dto.AlbumRequestDTO;
import br.gov.mt.seplag.backend.domain.entity.Album;
import br.gov.mt.seplag.backend.domain.entity.Artista;
import br.gov.mt.seplag.backend.repository.AlbumRepository;
import br.gov.mt.seplag.backend.repository.ArtistaRepository;
import br.gov.mt.seplag.backend.websocket.AlbumWebSocketHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AlbumServiceTest {

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private ArtistaRepository artistaRepository;

    @Mock
    private MinIOService minIOService;

    @Mock
    private AlbumWebSocketHandler webSocketHandler;

    @InjectMocks
    private AlbumService albumService;

    private Artista artista;
    private Album album;
    private AlbumRequestDTO albumDTO;

    @BeforeEach
    void setUp() {
        artista = Artista.builder()
            .id(1L)
            .nome("Serj Tankian")
            .tipo(Artista.TipoArtista.CANTOR)
            .build();

        album = Album.builder()
            .id(1L)
            .titulo("Harakiri")
            .artista(artista)
            .imagens(new java.util.ArrayList<>())
            .build();

        albumDTO = new AlbumRequestDTO();
        albumDTO.setTitulo("Harakiri");
        albumDTO.setArtistaId(1L);
    }

    @Test
    void deveListarAlbunsPorArtista() {
        PageRequest pageable = PageRequest.of(0, 10);

        given(albumRepository.findByArtistaId(1L, pageable))
            .willReturn(new PageImpl<>(List.of(album)));

        Page<AlbumDTO> result = albumService.listarAlbuns(1L, null, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTitulo()).isEqualTo("Harakiri");
    }

    @Test
    void deveCriarAlbumENotificarWebSocket() {
        TransactionSynchronizationManager.initSynchronization();

        given(artistaRepository.findById(1L)).willReturn(Optional.of(artista));
        given(albumRepository.save(any(Album.class))).willReturn(album);

        AlbumDTO result = albumService.criarAlbum(albumDTO);

        TransactionSynchronizationManager.getSynchronizations()
            .forEach(TransactionSynchronization::afterCommit);

        assertThat(result.getTitulo()).isEqualTo("Harakiri");
        then(webSocketHandler).should().notificarNovoAlbum(1L, "Harakiri");

        TransactionSynchronizationManager.clearSynchronization();
    }

    @Test
    void deveFazerUploadDeImagem() throws IOException {
        MultipartFile file = mock(MultipartFile.class);

        given(file.getOriginalFilename()).willReturn("capa.jpg");
        given(albumRepository.findById(1L)).willReturn(Optional.of(album));
        given(minIOService.uploadFile(file)).willReturn("uuid_capa.jpg");
        given(minIOService.getPresignedUrl("uuid_capa.jpg"))
            .willReturn("http://presigned.url");
        given(albumRepository.save(any(Album.class))).willReturn(album);

        albumService.uploadImagem(1L, file);

        then(minIOService).should().uploadFile(file);
        assertThat(album.getImagens()).hasSize(1);
    }
}
