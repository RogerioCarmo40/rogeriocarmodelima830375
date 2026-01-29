package br.gov.mt.seplag.backend.service;

import br.gov.mt.seplag.backend.domain.entity.Solicitacao;
import br.gov.mt.seplag.backend.domain.enums.StatusSolicitacao;
import br.gov.mt.seplag.backend.repository.SolicitacaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SolicitacaoService {

    private final SolicitacaoRepository solicitacaoRepository;

    public SolicitacaoService(SolicitacaoRepository solicitacaoRepository) {
        this.solicitacaoRepository = solicitacaoRepository;
    }

    public Solicitacao criarSolicitacao(Solicitacao solicitacao) {
        // Status e datas são definidos automaticamente pela entidade
        return solicitacaoRepository.save(solicitacao);
    }

    public List<Solicitacao> listarTodas() {
        return solicitacaoRepository.findAll();
    }

    public Solicitacao buscarPorId(Long id) {
        return solicitacaoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Solicitação não encontrada"));
    }

    public Solicitacao atualizarStatus(Long id, StatusSolicitacao novoStatus) {
        Solicitacao solicitacao = buscarPorId(id);
        solicitacao.setStatus(novoStatus);
        return solicitacaoRepository.save(solicitacao);
    }
}


