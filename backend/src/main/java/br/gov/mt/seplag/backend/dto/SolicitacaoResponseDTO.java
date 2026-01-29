package br.gov.mt.seplag.backend.dto;

import br.gov.mt.seplag.backend.domain.enums.StatusSolicitacao;

import java.time.LocalDateTime;

public class SolicitacaoResponseDTO {

    private Long id;
    private String descricao;
    private StatusSolicitacao status;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public SolicitaçãoResponseDTO(
            Long id,
            String descricao,
            StatusSolicitacao status,
            LocalDateTime dataCriacao,
            LocalDateTime dataAtualizacao
    ) {
        this.id = id;
        this.descricao = descricao;
        this.status = status;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
    }

    public Long getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public StatusSolicitacao getStatus() {
        return status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }
}
