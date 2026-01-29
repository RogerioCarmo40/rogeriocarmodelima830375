package br.gov.mt.seplag.backend.service;

import br.gov.mt.seplag.backend.domain.entity.Solicitacao;
import br.gov.mt.seplag.backend.domain.enums.StatusSolicitacao;
import br.gov.mt.seplag.backend.repository.SolicitacaoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SolicitacaoService {

    private final SolicitacaoRepository solicitacaoRepository;

    public SolicitacaoService(SolicitacaoRepository solicitacaoRepository) {
        this.solicitacaoRepository = solicitacaoRepository;
    }

    /**
     * Cria uma nova solicitação
     */
    public Solicitacao criarSolicitacao(Solicitacao solicitacao) {
        solicitacao.setStatus(StatusSolicitacao.PENDENTE);
        solicitacao.setDataCriacao(LocalDateTime.now());
        return solicitacaoRepository.save(solicitacao);
    }

    /**
     * Lista todas as solicitações
     */
    public List<Solicitacao> listarTodas() {
        return solicitacaoRepository.findAll();
    }

    /**
     * Busca solicitação por ID
     */
    public Solicitacao buscarPorId(Long id) {
        return solicitacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitação não encontrada"));
    }

    /**
     * Atualiza o status da solicitação
     */
    public Solicitacao atualizarStatus(Long id, StatusSolicitacao novoStatus) {
        Solicitacao solicitacao = buscarPorId(id);
        solicitacao.setStatus(novoStatus);
        solicitacao.setDataAtualizacao(LocalDateTime.now());
        return solicitacaoRepository.save(solicitacao);
    }
}

