package br.gov.mt.seplag.backend.domain.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistaDTO {
    private Long id;
    private String nome;
    private String tipo;
    private List<AlbumDTO> albums;
}
