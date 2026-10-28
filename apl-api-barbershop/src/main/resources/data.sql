-- ============================================================
-- db_barbershop - H2 Database
-- Dados iniciais e fictícios para desenvolvimento local.
-- Executado pelo Spring Boot após o schema.sql.
-- ============================================================

-- ------------------------------------------------------------
-- Dados-base
-- IMPORTANTE: os IDs agora são gerados pelo banco, em ordem de inserção
-- (1, 2, 3...). As FKs abaixo dependem dessa ordem; não reordene as linhas.
-- ------------------------------------------------------------

INSERT INTO tipo_usuario (id_tipo_usuario, titulo) VALUES
                                                       (default, 'ADMINISTRADOR'),
                                                       (default, 'BARBEIRO'),
                                                       (default, 'RECEPCIONISTA');

INSERT INTO usuario
(id_usuario, nome, email, celular, fk_tipo_usuario, senha) VALUES
                                                                           (default, 'Administrador Teste', 'admin@barbershop.test', '11990000001', 1, 'senha123'),
                                                                           (default, 'Lucas Almeida', 'lucas@barbershop.test', '11990000002', 2, 'senha123'),
                                                                           (default, 'Rafael Santos', 'rafael@barbershop.test', '11990000003', 2, 'senha123'),
                                                                           (default, 'Marina Costa', 'marina@barbershop.test', '11990000004', 3, 'senha123');

INSERT INTO status_agendamento (id_status_agendamento, titulo) VALUES
                                                                   (default, 'AGENDADO'),
                                                                   (default, 'CONCLUIDO'),
                                                                   (default, 'CANCELADO'),
                                                                   (default, 'EM_ANDAMENTO'),
                                                                   (default, 'NAO_COMPARECEU');

INSERT INTO cliente (id_cliente, nome, apelido, celular) VALUES
                                                             (default, 'Pedro Oliveira', 'Pedrinho', '11981110001'),
                                                             (default, 'João Ferreira', 'Jota', '11981110002'),
                                                             (default, 'André Lima', NULL, '11981110003'),
                                                             (default, 'Bruno Martins', 'Brunão', '11981110004'),
                                                             (default, 'Felipe Souza', NULL, '11981110005'),
                                                             (default, 'Gustavo Rocha', 'Guga', '11981110006'),
                                                             (default, 'Matheus Ribeiro', NULL, '11981110007'),
                                                             (default, 'Diego Carvalho', 'Diego', '11981110008');

INSERT INTO forma_pagamento (id_forma_pagamento, titulo) VALUES
                                                             (default, 'PIX'),
                                                             (default, 'DINHEIRO'),
                                                             (default, 'CARTAO_CREDITO'),
                                                             (default, 'CARTAO_DEBITO');

INSERT INTO servico (id_servico, titulo, valor_atual, duracao_atual, ativo) VALUES
                                                                                (default, 'Corte tradicional', 45.00, 30, TRUE),
                                                                                (default, 'Barba', 30.00, 20, TRUE),
                                                                                (default, 'Corte + barba', 65.00, 50, TRUE),
                                                                                (default, 'Degradê', 50.00, 40, TRUE),
                                                                                (default, 'Sobrancelha', 15.00, 10, TRUE),
                                                                                (default, 'Hidratação', 25.00, 20, FALSE);

-- ------------------------------------------------------------
-- Pagamentos de teste
-- pago = TRUE/FALSE; datas são fictícias para testes locais.
-- ------------------------------------------------------------

INSERT INTO pagamento (id_pagamento, valor, pago, data_pagamento, fk_forma_pagamento) VALUES
                                                                                          (default, 45.00, TRUE,  DATE '2026-10-08', 1),
                                                                                          (default, 30.00, TRUE,  DATE '2026-10-09', 2),
                                                                                          (default, 65.00, FALSE, NULL, NULL),
                                                                                          (default, 50.00, FALSE, NULL, NULL),
                                                                                          (default, 45.00, TRUE,  DATE '2026-10-10', 4),
                                                                                          (default, 80.00, TRUE,  DATE '2026-10-11', 1),
                                                                                          (default, 30.00, FALSE, NULL, NULL),
                                                                                          (default, 65.00, FALSE, NULL, NULL);

-- ------------------------------------------------------------
-- Agendamentos
-- ------------------------------------------------------------

INSERT INTO agendamento
(id_agendamento, horario_inicio, horario_fim, titulo, fk_status_agendamento, barbeiro, fk_cliente, fk_pagamento) VALUES
                                                                                                                     (default, TIMESTAMP '2026-10-08 09:00:00', TIMESTAMP '2026-10-08 09:30:00', 'Pedro Oliveira - Corte tradicional', 4, 2, 1, 1),
                                                                                                                     (default, TIMESTAMP '2026-10-09 10:00:00', TIMESTAMP '2026-10-09 10:20:00', 'João Ferreira - Barba', 4, 3, 2, 2),
                                                                                                                     (default, TIMESTAMP '2026-10-12 13:00:00', TIMESTAMP '2026-10-12 13:50:00', 'André Lima - Corte + barba', 4, 2, 3, 3),
                                                                                                                     (default, TIMESTAMP '2026-10-12 14:00:00', TIMESTAMP '2026-10-12 14:40:00', 'Bruno Martins - Degradê', 1, 3, 4, 4),
                                                                                                                     (default, TIMESTAMP '2026-10-10 11:00:00', TIMESTAMP '2026-10-10 11:30:00', 'Felipe Souza - Corte tradicional', 4, 2, 5, 5),
                                                                                                                     (default, TIMESTAMP '2026-10-11 15:00:00', TIMESTAMP '2026-10-11 15:50:00', 'Gustavo Rocha - Corte + barba', 4, 3, 6, 6),
                                                                                                                     (default, TIMESTAMP '2026-10-13 09:30:00', TIMESTAMP '2026-10-13 09:50:00', 'Matheus Ribeiro - Barba', 2, 2, 7, 7),
                                                                                                                     (default, TIMESTAMP '2026-10-14 16:00:00', TIMESTAMP '2026-10-14 16:50:00', 'Diego Carvalho - Corte + barba', 1, 3, 8, 8);

INSERT INTO agendamento_servico
(id_agendamento_servico, fk_agendamento, fk_servico, valor_unitario, duracao) VALUES
                                                                                   (default, 1, 1, 45.00, 30),
                                                                                   (default, 2, 2, 30.00, 20),
                                                                                   (default, 3, 3, 65.00, 50),
                                                                                   (default, 4, 4, 50.00, 40),
                                                                                   (default, 5, 1, 45.00, 30),
                                                                                   (default, 6, 3, 65.00, 50),
                                                                                   (default, 6, 5, 15.00, 10),
                                                                                   (default, 7, 2, 30.00, 20),
                                                                                   (default, 8, 3, 65.00, 50);

-- ------------------------------------------------------------
-- Expediente: dia_semana usando 1=segunda ... 7=domingo.
-- Ajuste a convenção se sua aplicação utilizar outra numeração.
-- ------------------------------------------------------------

INSERT INTO expediente
(id_expediente, dia_semana, fk_usuario, aberto, hora_abertura, hora_fechamento, intervalo_inicio, intervalo_fim) VALUES
                                                                                                                     (default, 1, 2, TRUE, TIME '09:00:00', TIME '18:00:00', TIME '12:00:00', TIME '13:00:00'),
                                                                                                                     (default, 2, 2, TRUE, TIME '09:00:00', TIME '18:00:00', TIME '12:00:00', TIME '13:00:00'),
                                                                                                                     (default, 3, 2, TRUE, TIME '09:00:00', TIME '18:00:00', TIME '12:00:00', TIME '13:00:00'),
                                                                                                                     (default, 4, 2, TRUE, TIME '09:00:00', TIME '18:00:00', TIME '12:00:00', TIME '13:00:00'),
                                                                                                                     (default, 5, 2, TRUE, TIME '09:00:00', TIME '18:00:00', TIME '12:00:00', TIME '13:00:00'),
                                                                                                                     (default, 6, 2, TRUE, TIME '09:00:00', TIME '14:00:00', NULL, NULL),
                                                                                                                     (default, 7, 2, FALSE, NULL, NULL, NULL, NULL),
                                                                                                                     (default, 1, 3, TRUE, TIME '10:00:00', TIME '19:00:00', TIME '13:00:00', TIME '14:00:00'),
                                                                                                                     (default, 2, 3, TRUE, TIME '10:00:00', TIME '19:00:00', TIME '13:00:00', TIME '14:00:00'),
                                                                                                                     (default, 3, 3, TRUE, TIME '10:00:00', TIME '19:00:00', TIME '13:00:00', TIME '14:00:00'),
                                                                                                                     (default, 4, 3, TRUE, TIME '10:00:00', TIME '19:00:00', TIME '13:00:00', TIME '14:00:00'),
                                                                                                                     (default, 5, 3, TRUE, TIME '10:00:00', TIME '19:00:00', TIME '13:00:00', TIME '14:00:00'),
                                                                                                                     (default, 6, 3, TRUE, TIME '09:00:00', TIME '15:00:00', NULL, NULL),
                                                                                                                     (default, 7, 3, FALSE, NULL, NULL, NULL, NULL);

INSERT INTO bloqueio (id_bloqueio, fk_usuario, inicio, fim, motivo) VALUES
                                                                        (default, 2, TIMESTAMP '2026-10-12 15:00:00', TIMESTAMP '2026-10-12 16:00:00', 'Compromisso pessoal'),
                                                                        (default, 3, TIMESTAMP '2026-10-13 12:00:00', TIMESTAMP '2026-10-13 13:00:00', 'Pausa excepcional');

INSERT INTO despesa (id_despesa, fk_usuario, descricao, valor, data_despesa) VALUES
                                                                                 (default, 1, 'Compra de lâminas e navalhas', 120.50, DATE '2026-10-02'),
                                                                                 (default, 1, 'Produtos para cabelo e barba', 245.90, DATE '2026-10-05'),
                                                                                 (default, 1, 'Manutenção de equipamento', 180.00, DATE '2026-10-09'),
                                                                                 (default, 2, 'Material de uso individual', 35.00, DATE '2026-10-10');

-- Fim do script.
