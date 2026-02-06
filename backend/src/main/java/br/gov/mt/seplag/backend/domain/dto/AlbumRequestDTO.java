package br.gov.mt.seplag.backend.domain.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumRequestDTO {
    private String titulo;
    private Long artistaId;
    private List<String> imagensNomes;
}
