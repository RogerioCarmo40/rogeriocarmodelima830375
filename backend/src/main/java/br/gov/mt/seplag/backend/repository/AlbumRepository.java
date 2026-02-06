package br.gov.mt.seplag.backend.repository;

import br.gov.mt.seplag.backend.domain.entity.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

    @EntityGraph(attributePaths = {"artista", "imagens"})
    @Override
    Page<Album> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"artista", "imagens"})
    @Query("""
        SELECT a
        FROM Album a
        WHERE LOWER(a.artista.nome) LIKE LOWER(CONCAT('%', :artistaNome, '%'))
    """)
    Page<Album> findByArtistaNome(
        @Param("artistaNome") String artistaNome,
        Pageable pageable
    );

    @EntityGraph(attributePaths = {"artista", "imagens"})
    Page<Album> findByArtistaId(Long artistaId, Pageable pageable);
}