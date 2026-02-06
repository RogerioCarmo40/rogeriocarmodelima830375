package br.gov.mt.seplag.backend.repository;

import br.gov.mt.seplag.backend.domain.entity.Artista;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistaRepository extends JpaRepository<Artista, Long> {

    Page<Artista> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    boolean existsByNome(String nome);
}

