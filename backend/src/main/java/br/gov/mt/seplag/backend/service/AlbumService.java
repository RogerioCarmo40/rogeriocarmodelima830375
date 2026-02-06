package br.gov.mt.seplag.backend.service;

import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import br.gov.mt.seplag.backend.domain.dto.AlbumDTO;
import br.gov.mt.seplag.backend.domain.dto.AlbumRequestDTO;
import br.gov.mt.seplag.backend.domain.entity.Album;
import br.gov.mt.seplag.backend.domain.entity.AlbumImagem;
import br.gov.mt.seplag.backend.domain.entity.Artista;
import br.gov.mt.seplag.backend.repository.AlbumRepository;
import br.gov.mt.seplag.backend.repository.ArtistaRepository;
import br.gov.mt.seplag.backend.websocket.AlbumWebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlbumService {
    
    private final AlbumRepository albumRepository;
    private final ArtistaRepository artistaRepository;
    private final MinIOService minIOService;
    private final AlbumWebSocketHandler webSocketHandler;
    
    @Transactional(readOnly = true)
    public Page<AlbumDTO> listarAlbuns(Long artistaId, String artistaNome, Pageable pageable) {
        Page<Album> albums;
        
        if (artistaId != null) {
            albums = albumRepository.findByArtistaId(artistaId, pageable);
        } else if (artistaNome != null) {
            albums = albumRepository.findByArtistaNome(artistaNome, pageable);
        } else {
            albums = albumRepository.findAll(pageable);
        }
        
        return albums.map(this::toDTO);
    }
    
    @Transactional(readOnly = true)
    public AlbumDTO buscarPorId(Long id) {
        Album album = albumRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Álbum não encontrado"));
        return toDTO(album);
    }
    
    @Transactional
    public AlbumDTO criarAlbum(AlbumRequestDTO dto) {
        Artista artista = artistaRepository.findById(dto.getArtistaId())
        .orElseThrow(() -> new RuntimeException("Artista não encontrado"));
        Album album = albumRepository.save(
            Album.builder()
                .titulo(dto.getTitulo())
                .artista(artista)
                .build()
        );

        // notificar APÓS commit
        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    webSocketHandler.notificarNovoAlbum(
                        album.getId(),
                        album.getTitulo()
                    );
                }
            }
        );

        return toDTO(album);
    }
    
    @Transactional
    public AlbumDTO atualizarAlbum(Long id, AlbumRequestDTO dto) {
        Album album = albumRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Álbum não encontrado"));
            
        Artista artista = artistaRepository.findById(dto.getArtistaId())
            .orElseThrow(() -> new RuntimeException("Artista não encontrado"));
        
        album.setTitulo(dto.getTitulo());
        album.setArtista(artista);
        
        album = albumRepository.save(album);
        return toDTO(album);
    }
    
    @Transactional
    public void deletarAlbum(Long id) {
        if (!albumRepository.existsById(id)) {
            throw new RuntimeException("Álbum não encontrado");
        }
        albumRepository.deleteById(id);
    }
    
    @Transactional
    public void uploadImagem(Long albumId, MultipartFile file) throws IOException {
        Album album = albumRepository.findById(albumId)
            .orElseThrow(() -> new RuntimeException("Álbum não encontrado"));
        
        String filename = minIOService.uploadFile(file);
        String presignedUrl = minIOService.getPresignedUrl(filename);
        
        AlbumImagem imagem = AlbumImagem.builder()
            .album(album)
            .imagemNome(filename)
            .imagemUrl(presignedUrl)
            .build();
        
        album.getImagens().add(imagem);
        albumRepository.save(album);
    }
    
    private AlbumDTO toDTO(Album album) {
        List<String> imagensUrls = album.getImagens().stream()
            .map(AlbumImagem::getImagemUrl)
            .collect(Collectors.toList());
        
        return AlbumDTO.builder()
            .id(album.getId())
            .titulo(album.getTitulo())
            .artistaId(album.getArtista().getId())
            .artistaNome(album.getArtista().getNome())
            .createdAt(album.getCreatedAt())
            .imagensUrls(imagensUrls)
            .build();
    }
}