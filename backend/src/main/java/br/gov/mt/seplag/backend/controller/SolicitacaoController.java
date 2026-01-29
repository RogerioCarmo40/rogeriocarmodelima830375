package br.gov.mt.seplag.backend.controller;

import br.gov.mt.seplag.backend.domain.entity.Solicitacao;
import br.gov.mt.seplag.backend.dto.*;
import br.gov.mt.seplag.backend.service.SolicitacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/solicitacoes")
public class SolicitacaoController {

    private final SolicitacaoService solicitacaoService;

    public SolicitacaoController(SolicitacaoService solicitacaoService) {
        this.solicitacaoService = solicitacaoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SolicitacaoResponseDTO criar(
            @RequestBody @Valid SolicitacaoRequestDTO dto
    ) {
        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setDescricao(dto.getDescricao());

        Solicitacao salva = solicitacaoService.criarSolicitacao(solicitacao);
        return mapToResponse(salva);
    }

    @GetMapping
    public List<SolicitacaoResponseDTO> listar() {
        return solicitacaoService.listarTodas()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public SolicitacaoResponseDTO buscarPorId(@PathVariable Long id) {
        return mapToResponse(solicitacaoService.buscarPorId(id));
    }

    @PatchMapping("/{id}/status")
    public SolicitacaoResponseDTO atualizarStatus(
            @PathVariable Long id,
            @RequestBody @Valid AtualizacaoStatusDTO dto
    ) {
        return mapToResponse(
                solicitacaoService.atualizarStatus(id, dto.getStatus())
        );
    }

    private SolicitacaoResponseDTO mapToResponse(Solicitacao s) {
        return new SolicitacaoResponseDTO(
                s.getId(),
                s.getDescricao(),
                s.getStatus(),
                s.getDataCriacao(),
                s.getDataAtualizacao()
        );
    }
}
