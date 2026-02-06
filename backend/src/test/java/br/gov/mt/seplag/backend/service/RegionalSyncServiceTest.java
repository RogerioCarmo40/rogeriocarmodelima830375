package br.gov.mt.seplag.backend.service;

import br.gov.mt.seplag.backend.domain.dto.RegionalDTO;
import br.gov.mt.seplag.backend.domain.entity.Regional;
import br.gov.mt.seplag.backend.repository.RegionalRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;


@ExtendWith(MockitoExtension.class)
class RegionalSyncServiceTest {

    @Mock
    private RegionalRepository regionalRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private RegionalSyncService regionalSyncService;

    @BeforeEach
    void setup() {
        org.springframework.test.util.ReflectionTestUtils
            .setField(regionalSyncService, "regionaisApiUrl", "http://fake-api");
    }

    @Test
    void deveInserirNovaRegionalQuandoNaoExistir() {

        RegionalDTO[] apiResponse = {
            new RegionalDTO(1, "Cuiabá")
        };

        given(restTemplate.getForObject(any(String.class), eq(RegionalDTO[].class)))
            .willReturn(apiResponse);

        given(regionalRepository.findAll()).willReturn(List.of());

        regionalSyncService.sincronizarRegionais();

        then(regionalRepository).should(atLeastOnce()).save(any(Regional.class));
    }

    @Test
    void deveAtualizarRegionalQuandoNomeForAlterado() {

        Regional existente = Regional.builder()
            .id(1)
            .nome("Cuiaba")
            .ativo(true)
            .createdAt(java.time.LocalDateTime.now())
            .build();

        RegionalDTO[] apiResponse = {
            new RegionalDTO(1, "Cuiabá")
        };

        given(restTemplate.getForObject(any(String.class), eq(RegionalDTO[].class)))
            .willReturn(apiResponse);

        given(regionalRepository.findAll()).willReturn(List.of(existente));

        regionalSyncService.sincronizarRegionais();

        then(regionalRepository).should().desativarRegional(1);
        then(regionalRepository).should(atLeastOnce()).save(any(Regional.class));
    }

    @Test
    void deveInativarRegionalQuandoNaoVierMaisDaApi() {

        Regional existente = Regional.builder()
            .id(1)
            .nome("Cuiabá")
            .ativo(true)
            .createdAt(java.time.LocalDateTime.now())
            .build();

        given(restTemplate.getForObject(any(String.class), eq(RegionalDTO[].class)))
            .willReturn(new RegionalDTO[]{});

        given(regionalRepository.findAll()).willReturn(List.of(existente));

        regionalSyncService.sincronizarRegionais();

        then(regionalRepository).should().desativarRegional(1);
        then(regionalRepository).should(never()).save(any());
    }
}
