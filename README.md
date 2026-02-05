# Projeto Backend - Eng. Senior – Processo Seletivo SEPLAG/MT 001/2026

Projeto Backend desenvolvido como parte do **Processo Seletivo Simplificado Conjunto nº 001/2026**, do **Governo do Estado de Mato Grosso (SEPLAG/MT)**, para o cargo de **Analista de Tecnologia da Informação – Perfil Engenheiro da Computação (Sênior)**.

A aplicação demonstra a construção de uma solução **full stack moderna**, com foco em **boas práticas de engenharia de software**, **arquitetura limpa**, **segurança**, **documentação**, **containerização** e **clareza técnica**, conforme os requisitos definidos no edital.

---

## 🧠 Visão Geral

O sistema implementa uma API RESTful segura para gerenciamento de **artistas e álbuns**, com autenticação via **JWT**, persistência em **PostgreSQL**, versionamento de banco de dados com **Flyway**, upload de arquivos utilizando **MinIO**, comunicação em tempo real via **WebSocket**, além de um **front-end em React + TypeScript**, responsável por consumir a API e apresentar as funcionalidades principais.

O projeto foi desenvolvido priorizando:
- Legibilidade e manutenibilidade do código  
- Separação clara de responsabilidades  
- Decisões técnicas justificadas  
- Aderência aos requisitos do edital  
- Facilidade de execução via Docker  

---

## 🏗️ Arquitetura

### Back-End
- Java 17
- Spring Boot
- Spring Security + JWT
- Spring Data JPA
- Flyway
- PostgreSQL
- MinIO
- WebSocket
- Swagger / OpenAPI

Estrutura principal:

controller
service
repository
domain
dto
config
security


### Front-End
- React
- TypeScript
- Vite
- Context API + Facade Pattern
- Axios
- Docker

Estrutura principal:

modules/
artists/
albums/
shared/
facades/
services/


---

## 🚀 Funcionalidades

- Autenticação e autorização via JWT
- CRUD de Artistas e Álbuns
- Relacionamento entre entidades
- Paginação e ordenação
- Upload de imagens com armazenamento no MinIO
- Geração de URLs temporárias (presigned URL)
- Comunicação em tempo real via WebSocket
- Rate limit simples por usuário
- Documentação da API com Swagger
- Execução completa via Docker Compose

---

## 🐳 Executando o Projeto

### Pré-requisitos
- Docker
- Docker Compose

### Passos
```bash
git clone <https://github.com/RogerioCarmo40/rogeriocarmodelima830375.git>
cd projeto
docker-compose up --build
```

A aplicação ficará disponível em:

API: http://localhost:8080

Swagger: http://localhost:8080/swagger-ui.html

Front-end: http://localhost:3000

🧪 Testes

O projeto contém testes básicos para demonstrar:

Testes de serviço

Testes de controller

Teste simples de componente no front-end

O objetivo é evidenciar conhecimento em testes automatizados, conforme esperado no perfil do edital.

📌 Decisões Técnicas

Arquitetura em camadas para facilitar manutenção e evolução

Uso de Flyway para controle de versão do banco de dados

JWT para autenticação stateless

Docker para padronização do ambiente

WebSocket implementado de forma simples, focando clareza e funcionalidade
