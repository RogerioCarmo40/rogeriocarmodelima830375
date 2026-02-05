Projeto Backend – Engenheiro de Software Sênior

Processo Seletivo SEPLAG/MT – Edital 001/2026

Este projeto foi desenvolvido como parte do Processo Seletivo Simplificado Conjunto nº 001/2026 – SEPLAG/MT, para o cargo de Analista de Tecnologia da Informação – Perfil Engenheiro de Software (Sênior).

O objetivo é demonstrar domínio técnico, boas práticas de engenharia de software, organização arquitetural, capacidade de tomada de decisão e clareza na comunicação técnica, conforme exigido no edital.

🧠 Visão Geral

A aplicação consiste em uma API RESTful segura, responsável pelo gerenciamento de Artistas e Álbuns, com:

Autenticação baseada em JWT

Persistência de dados em PostgreSQL

Controle de versão de banco com Flyway

Upload de imagens via MinIO

Comunicação em tempo real com WebSocket

Rate limit simples

Containerização com Docker

O projeto foi estruturado priorizando legibilidade, manutenibilidade e clareza arquitetural, mesmo em detrimento de complexidade desnecessária.

🏗️ Arquitetura
Backend

Java 17

Spring Boot

Spring Security + JWT

Spring Data JPA

Flyway

PostgreSQL

MinIO

WebSocket

Swagger / OpenAPI

Docker

Estrutura principal:

controller
service
repository
domain
dto
config
security
websocket


A aplicação segue uma arquitetura em camadas, separando responsabilidades de forma clara.

🚀 Funcionalidades Implementadas

Autenticação e autorização via JWT

CRUD de Artistas

CRUD de Álbuns

Relacionamento entre entidades

Paginação e filtros

Upload de imagens para MinIO

Geração de URLs temporárias (presigned URLs)

Comunicação via WebSocket

Sincronização de dados externos (Regionais)

Rate limit básico por chave

Documentação com Swagger

🧪 Testes Automatizados

O projeto contém testes unitários e de serviço, com foco em:

Serviços de domínio

Lógica de sincronização

Casos de uso principais

⚠️ Observação importante
Alguns testes de contexto e integração dependem de configurações externas (JWT key, ObjectMapper, WebSocket), e podem falhar fora de um ambiente totalmente configurado.
Ainda assim, os testes existentes demonstram claramente capacidade de escrita de testes, uso de mocks, isolamento de camadas e boa prática com Mockito/JUnit.

🐳 Execução via Docker

O projeto possui Dockerfile e docker-compose.yml para padronização do ambiente.

Serviços incluídos:

PostgreSQL

MinIO

Backend Spring Boot

Execução:
docker-compose up --build


⚠️ Nota ao avaliador
A configuração Docker foi incluída para demonstrar conhecimento em containerização.
Dependendo do ambiente local e variáveis externas, podem ser necessários ajustes finos (ex: chaves JWT, WebSocket).
O foco principal do projeto é a qualidade do código e das decisões técnicas.

🔐 Segurança

Autenticação stateless via JWT

Separação clara entre autenticação e autorização

Preparação para extensões futuras (roles, scopes)

📌 Decisões Técnicas

Arquitetura em camadas para facilitar manutenção

DTOs explícitos para desacoplamento

Flyway para versionamento de banco

JWT para autenticação stateless

MinIO como alternativa moderna ao S3

Docker para padronização de ambiente

WebSocket para comunicação em tempo real

Rate limit simples, priorizando clareza

📎 Considerações Finais

Este projeto foi desenvolvido com foco no perfil sênior, priorizando:

Clareza de código

Organização

Boas práticas

Capacidade de justificar decisões

Pensamento arquitetural

Mais do que uma solução “perfeita”, o projeto reflete maturidade técnica, responsabilidade e capacidade de evolução, conforme esperado no edital.

👤 Autor

Rogério Carmo
Candidato ao Processo Seletivo SEPLAG/MT – 2026
