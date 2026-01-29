package br.gov.mt.seplag.backend.dto;

import java.time.LocalDateTime;

public class ErroRespostaDTO {

    private LocalDateTime timestamp;
    private int status;
    private String erro;
    private String caminho;

    public ErroRespostaDTO(
            LocalDateTime timestamp,
            int status,
            String erro,
            String caminho
    ) {
        this.timestamp = timestamp;
        this.status = status;
        this.erro = erro;
        this.caminho = caminho;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getErro() {
        return erro;
    }

    public String getCaminho() {
        return caminho;
    }
}
