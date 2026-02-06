package br.gov.mt.seplag.backend.repository;

import br.gov.mt.seplag.backend.domain.entity.Regional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegionalRepository extends JpaRepository<Regional, Integer> {

    List<Regional> findAllByAtivoTrue();

    @Modifying
    @Query("UPDATE Regional r SET r.ativo = false WHERE r.id = :id")
    int desativarRegional(Integer id);
}
