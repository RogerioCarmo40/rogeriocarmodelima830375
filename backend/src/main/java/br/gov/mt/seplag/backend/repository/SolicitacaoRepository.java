package br.gov.mt.seplag.backend.domain.repository;

import br.gov.mt.seplag.backend.domain.entity.Solicitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long> {
}
