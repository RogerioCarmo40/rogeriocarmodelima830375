package br.gov.mt.seplag.backend.service;

import br.gov.mt.seplag.backend.domain.dto.RegionalDTO;
import br.gov.mt.seplag.backend.domain.entity.Regional;
import br.gov.mt.seplag.backend.repository.RegionalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegionalSyncService {

    private final RegionalRepository regionalRepository;
    private final RestTemplate restTemplate;

    @Value("${regionais.api.url}")
    private String regionaisApiUrl;

    @Scheduled(cron = "${regionais.sync.cron}")
    @Transactional
    public void sincronizarRegionais() {

        RegionalDTO[] externos =
            restTemplate.getForObject(regionaisApiUrl, RegionalDTO[].class);

        if (externos == null) {
            log.warn("API de regionais retornou null");
            return;
        }

        List<Regional> internos = regionalRepository.findAll();

        Map<Integer, Regional> internosMap = internos.stream()
            .filter(r -> Boolean.TRUE.equals(r.getAtivo()))
            .collect(Collectors.toMap(Regional::getId, r -> r));

        // 1️⃣ Processa regionais vindas da API
        for (RegionalDTO dto : externos) {

            Regional existente = internosMap.remove(dto.getId());

            if (existente == null) {
                // nova regional
                regionalRepository.save(
                    Regional.builder()
                        .id(dto.getId())
                        .nome(dto.getNome())
                        .ativo(true)
                        .build()
                );
            } else if (!existente.getNome().equals(dto.getNome())) {
                // nome alterado
                regionalRepository.desativarRegional(existente.getId());

                regionalRepository.save(
                    Regional.builder()
                        .id(dto.getId())
                        .nome(dto.getNome())
                        .ativo(true)
                        .build()
                );
            }
        }

        // 2️⃣ Inativar regionais que não vieram da API
        for (Regional restante : internosMap.values()) {
            if (Boolean.TRUE.equals(restante.getAtivo())) {
                regionalRepository.desativarRegional(restante.getId());
            }
        }
    }
}

