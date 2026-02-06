package br.gov.mt.seplag.backend.domain.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlbumDTO {
    private Long id;
    private String titulo;
    private Long artistaId;
    private String artistaNome;
    private LocalDateTime createdAt;
    private List<String> imagensUrls;
}
