package br.gov.mt.seplag.backend.domain.websocket;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlbumNotification {
    private Long albumId;
    private String titulo;
    private String mensagem;
    private Long timestamp;
}