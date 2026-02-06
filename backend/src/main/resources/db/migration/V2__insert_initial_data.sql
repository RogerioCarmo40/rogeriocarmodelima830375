-- Inserir artistas e álbuns de exemplo conforme edital

-- Serj Tankian (CANTOR)
INSERT INTO artista (id, nome, tipo) VALUES (1, 'Serj Tankian', 'CANTOR');
INSERT INTO album (id, titulo, artista_id) VALUES 
(1, 'Harakiri', 1),
(2, 'Black Blooms', 1),
(3, 'The Rough Dog', 1);

-- Mike Shinoda (CANTOR)
INSERT INTO artista (id, nome, tipo) VALUES (2, 'Mike Shinoda', 'CANTOR');
INSERT INTO album (id, titulo, artista_id) VALUES 
(4, 'The Rising Tied', 2),
(5, 'Post Traumatic', 2),
(6, 'Post Traumatic EP', 2),
(7, 'Where''d You Go', 2);

-- Michel Teló (CANTOR)
INSERT INTO artista (id, nome, tipo) VALUES (3, 'Michel Teló', 'CANTOR');
INSERT INTO album (id, titulo, artista_id) VALUES 
(8, 'Bem Sertanejo', 3),
(9, 'Bem Sertanejo - O Show (Ao Vivo)', 3),
(10, 'Bem Sertanejo - (1ª Temporada) - EP', 3);

-- Guns N'' Roses (BANDA)
INSERT INTO artista (id, nome, tipo) VALUES (4, 'Guns N'' Roses', 'BANDA');
INSERT INTO album (id, titulo, artista_id) VALUES 
(11, 'Use Your Illusion I', 4),
(12, 'Use Your Illusion II', 4),
(13, 'Greatest Hits', 4);

-- Resetar sequences
SELECT setval('artista_id_seq', (SELECT MAX(id) FROM artista));
SELECT setval('album_id_seq', (SELECT MAX(id) FROM album));