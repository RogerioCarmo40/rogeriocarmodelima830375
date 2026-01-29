package br.gov.mt.seplag.backend.dto;

import br.gov.mt.seplag.backend.domain.enums.StatusSolicitacao;
import jakarta.validation.constraints.NotNull;

public class AtualizacaoStatusDTO {

    @NotNull
    private StatusSolicitacao status;

    public StatusSolicitacao getStatus() {
        return status;
    }

    public void setStatus(StatusSolicitacao status) {
        this.status = status;
    }
}
