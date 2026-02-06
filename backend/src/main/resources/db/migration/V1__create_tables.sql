CREATE TABLE artista (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(300) NOT NULL,
    tipo VARCHAR(50) NOT NULL CHECK (tipo IN ('CANTOR', 'BANDA')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(nome)
);

CREATE TABLE album (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(300) NOT NULL,
    artista_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (artista_id) REFERENCES artista(id) ON DELETE CASCADE
);

CREATE TABLE regional (
    id INTEGER PRIMARY KEY,
    nome VARCHAR(200) NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(id)
);

CREATE TABLE album_imagem (
    id BIGSERIAL PRIMARY KEY,
    album_id BIGINT NOT NULL,
    imagem_nome VARCHAR(500) NOT NULL,
    imagem_url TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (album_id) REFERENCES album(id) ON DELETE CASCADE
);

CREATE TABLE usuario (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Inserir usuário padrão para testes (senha: admin123)
INSERT INTO usuario (username, password, email) VALUES 
('admin', '$2a$10$rN7bF5./fNbC1jlfJdQS7eP9DELHv8G2Y3rXvM6ZI1o7X8z1J4ZK2', 'admin@seplag.mt.gov.br');