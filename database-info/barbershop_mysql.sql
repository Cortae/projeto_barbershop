-- ============================================================
-- db_barbershop - MySQL 8
-- Cria o schema, as tabelas e os dados iniciais de desenvolvimento.
-- Execute o arquivo inteiro (Ctrl+Shift+Enter) com um usuario administrativo.
-- Para recriar do zero, descomente a linha abaixo (APAGA TODOS OS DADOS):
-- DROP SCHEMA IF EXISTS `db_barbershop`;
-- ============================================================

-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema db_barbershop
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema db_barbershop
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `db_barbershop` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `db_barbershop` ;

-- -----------------------------------------------------
-- Table `db_barbershop`.`tipo_usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `db_barbershop`.`tipo_usuario` (
  `id_tipo_usuario` INT NOT NULL AUTO_INCREMENT,
  `titulo` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_tipo_usuario`),
  UNIQUE INDEX `titulo_UNIQUE` (`titulo` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_barbershop`.`usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `db_barbershop`.`usuario` (
  `id_usuario` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(60) NULL,
  `email` VARCHAR(60) NOT NULL,
  `celular` VARCHAR(11) NULL,
  `fk_tipo_usuario` INT NOT NULL,
  `senha` VARCHAR(255) NULL,
  PRIMARY KEY (`id_usuario`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE,
  UNIQUE INDEX `celular_UNIQUE` (`celular` ASC) VISIBLE,
  INDEX `fk_usuario_tipo_usuario_idx` (`fk_tipo_usuario` ASC) VISIBLE,
  CONSTRAINT `fk_usuario_tipo_usuario`
    FOREIGN KEY (`fk_tipo_usuario`)
    REFERENCES `db_barbershop`.`tipo_usuario` (`id_tipo_usuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_barbershop`.`status_agendamento`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `db_barbershop`.`status_agendamento` (
  `id_status_agendamento` INT NOT NULL AUTO_INCREMENT,
  `titulo` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`id_status_agendamento`),
  UNIQUE INDEX `titulo_UNIQUE` (`titulo` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_barbershop`.`cliente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `db_barbershop`.`cliente` (
  `id_cliente` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(60) NOT NULL,
  `apelido` VARCHAR(45) NULL,
  `celular` VARCHAR(11) NULL,
  `nascimento` DATE NULL,
  PRIMARY KEY (`id_cliente`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_barbershop`.`forma_pagamento`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `db_barbershop`.`forma_pagamento` (
  `id_forma_pagamento` INT NOT NULL AUTO_INCREMENT,
  `titulo` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_forma_pagamento`),
  UNIQUE INDEX `titulo_UNIQUE` (`titulo` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_barbershop`.`pagamento`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `db_barbershop`.`pagamento` (
  `id_pagamento` INT NOT NULL AUTO_INCREMENT,
  `valor` DECIMAL(10,2) NOT NULL,
  `pago` TINYINT(1) NOT NULL,
  `data_pagamento` DATETIME NULL,
  `fk_forma_pagamento` INT NULL,
  PRIMARY KEY (`id_pagamento`),
  INDEX `fk_pagamento_forma_pagamento1_idx` (`fk_forma_pagamento` ASC) VISIBLE,
  CONSTRAINT `fk_pagamento_forma_pagamento1`
    FOREIGN KEY (`fk_forma_pagamento`)
    REFERENCES `db_barbershop`.`forma_pagamento` (`id_forma_pagamento`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_barbershop`.`agendamento`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `db_barbershop`.`agendamento` (
  `id_agendamento` INT NOT NULL AUTO_INCREMENT,
  `horario_inicio` DATETIME NOT NULL,
  `horario_fim` DATETIME NOT NULL,
  `titulo` VARCHAR(80) NULL COMMENT 'Será o nome do usuário (Inserido via backend)\n\nFront end deve apresentar: Cliente + Serviço',
  `fk_status_agendamento` INT NOT NULL,
  `barbeiro` INT NOT NULL,
  `fk_cliente` INT NOT NULL,
  `fk_pagamento` INT NOT NULL,
  PRIMARY KEY (`id_agendamento`),
  INDEX `fk_agendamento_status_agendamento1_idx` (`fk_status_agendamento` ASC) VISIBLE,
  INDEX `fk_agendamento_usuario1_idx` (`barbeiro` ASC) VISIBLE,
  INDEX `fk_agendamento_cliente1_idx` (`fk_cliente` ASC) VISIBLE,
  INDEX `fk_agendamento_pagamento1_idx` (`fk_pagamento` ASC) VISIBLE,
  CONSTRAINT `fk_agendamento_status_agendamento1`
    FOREIGN KEY (`fk_status_agendamento`)
    REFERENCES `db_barbershop`.`status_agendamento` (`id_status_agendamento`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_agendamento_usuario1`
    FOREIGN KEY (`barbeiro`)
    REFERENCES `db_barbershop`.`usuario` (`id_usuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_agendamento_cliente1`
    FOREIGN KEY (`fk_cliente`)
    REFERENCES `db_barbershop`.`cliente` (`id_cliente`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_agendamento_pagamento1`
    FOREIGN KEY (`fk_pagamento`)
    REFERENCES `db_barbershop`.`pagamento` (`id_pagamento`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_barbershop`.`servico`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `db_barbershop`.`servico` (
  `id_servico` INT NOT NULL AUTO_INCREMENT,
  `titulo` VARCHAR(45) NOT NULL,
  `valor_atual` DECIMAL(10,2) NOT NULL,
  `duracao_atual` INT NOT NULL,
  `ativo` TINYINT NOT NULL,
  PRIMARY KEY (`id_servico`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_barbershop`.`agendamento_servico`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `db_barbershop`.`agendamento_servico` (
  `id_agendamento_servico` INT NOT NULL AUTO_INCREMENT,
  `fk_agendamento` INT NOT NULL,
  `fk_servico` INT NOT NULL,
  `valor_unitario` DECIMAL(10,2) NOT NULL,
  `duracao` INT NOT NULL,
  PRIMARY KEY (`id_agendamento_servico`),
  INDEX `fk_agendamento_servicos_agendamento1_idx` (`fk_agendamento` ASC) VISIBLE,
  INDEX `fk_agendamento_servicos_servico1_idx` (`fk_servico` ASC) VISIBLE,
  UNIQUE INDEX `uq_agendamento_servico` (`fk_agendamento` ASC, `fk_servico` ASC) VISIBLE,
  CONSTRAINT `fk_agendamento_servicos_agendamento1`
    FOREIGN KEY (`fk_agendamento`)
    REFERENCES `db_barbershop`.`agendamento` (`id_agendamento`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_agendamento_servicos_servico1`
    FOREIGN KEY (`fk_servico`)
    REFERENCES `db_barbershop`.`servico` (`id_servico`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_barbershop`.`api_request_log`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `db_barbershop`.`api_request_log` (
  `id_api_request_log` BIGINT NOT NULL AUTO_INCREMENT,
  `request_id` CHAR(36) NOT NULL,
  `started_at` DATETIME(3) NOT NULL,
  `finished_at` DATETIME(3) NOT NULL,
  `duration_ms` BIGINT NOT NULL,
  `event_type` VARCHAR(30) NOT NULL,
  `http_method` VARCHAR(10) NOT NULL,
  `endpoint` VARCHAR(500) NOT NULL,
  `query_params` JSON NULL,
  `request_headers` JSON NULL,
  `request_body` JSON NULL,
  `request_body_bytes` BIGINT NOT NULL DEFAULT 0,
  `request_body_truncated` TINYINT(1) NOT NULL DEFAULT 0,
  `response_status` SMALLINT NOT NULL,
  `response_headers` JSON NULL,
  `response_body` JSON NULL,
  `response_body_bytes` BIGINT NOT NULL DEFAULT 0,
  `response_body_truncated` TINYINT(1) NOT NULL DEFAULT 0,
  `source_ip` VARCHAR(45) NULL,
  `user_agent` VARCHAR(500) NULL,
  `fk_usuario` INT NULL,
  `error_code` VARCHAR(50) NULL,
  PRIMARY KEY (`id_api_request_log`),
  UNIQUE INDEX `uq_api_request_log_request_id` (`request_id` ASC) VISIBLE,
  INDEX `idx_api_request_log_started_at` (`started_at` ASC) VISIBLE,
  INDEX `idx_api_request_log_status` (`response_status` ASC) VISIBLE,
  INDEX `idx_api_request_log_event_type` (`event_type` ASC) VISIBLE,
  INDEX `idx_api_request_log_usuario_started_at` (`fk_usuario` ASC, `started_at` ASC) VISIBLE,
  CONSTRAINT `fk_api_request_log_usuario`
    FOREIGN KEY (`fk_usuario`)
    REFERENCES `db_barbershop`.`usuario` (`id_usuario`)
    ON DELETE SET NULL
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_barbershop`.`expediente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `db_barbershop`.`expediente` (
  `id_expediente` INT NOT NULL AUTO_INCREMENT,
  `dia_semana` TINYINT NOT NULL,
  `fk_usuario` INT NOT NULL,
  `aberto` TINYINT NULL,
  `hora_abertura` TIME NULL,
  `hora_fechamento` TIME NULL,
  `intervalo_inicio` TIME NULL,
  `intervalo_fim` TIME NULL,
  PRIMARY KEY (`id_expediente`),
  INDEX `fk_expediente_usuario1_idx` (`fk_usuario` ASC) VISIBLE,
  UNIQUE INDEX `unq_usuario_dia` (`fk_usuario` ASC, `dia_semana` ASC) VISIBLE,
  CONSTRAINT `fk_expediente_usuario1`
    FOREIGN KEY (`fk_usuario`)
    REFERENCES `db_barbershop`.`usuario` (`id_usuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_barbershop`.`bloqueio`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `db_barbershop`.`bloqueio` (
  `id_bloqueio` INT NOT NULL AUTO_INCREMENT,
  `fk_usuario` INT NOT NULL,
  `inicio` DATETIME NOT NULL,
  `fim` DATETIME NOT NULL,
  `motivo` VARCHAR(100) NULL,
  PRIMARY KEY (`id_bloqueio`),
  INDEX `fk_bloqueio_usuario1_idx` (`fk_usuario` ASC) VISIBLE,
  CONSTRAINT `fk_bloqueio_usuario1`
    FOREIGN KEY (`fk_usuario`)
    REFERENCES `db_barbershop`.`usuario` (`id_usuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_barbershop`.`despesa`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `db_barbershop`.`despesa` (
  `id_despesa` INT NOT NULL AUTO_INCREMENT,
  `fk_usuario` INT NOT NULL,
  `descricao` VARCHAR(100) NOT NULL,
  `valor` DECIMAL(10,2) NOT NULL,
  `data_despesa` DATE NOT NULL,
  PRIMARY KEY (`id_despesa`),
  INDEX `fk_despesa_usuario1_idx` (`fk_usuario` ASC) VISIBLE,
  CONSTRAINT `fk_despesa_usuario1`
    FOREIGN KEY (`fk_usuario`)
    REFERENCES `db_barbershop`.`usuario` (`id_usuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;


-- ============================================================
-- Dados iniciais (desenvolvimento)
-- Os IDs são gerados pelo AUTO_INCREMENT, em ordem de inserção
-- (1, 2, 3...). As FKs abaixo dependem dessa ordem: não reordene
-- as linhas e execute este arquivo apenas uma vez.
-- ============================================================
USE `db_barbershop`;

-- Dados-base (o sistema busca estes registros pelo título)
INSERT INTO tipo_usuario (titulo) VALUES
  ('ADMINISTRADOR'),
  ('BARBEIRO');

INSERT INTO status_agendamento (titulo) VALUES
  ('AGENDADO'),
  ('CONCLUIDO'),
  ('CANCELADO'),
  ('EM_ANDAMENTO'),
  ('NAO_COMPARECEU');

INSERT INTO forma_pagamento (titulo) VALUES
  ('PIX'),
  ('DINHEIRO'),
  ('CARTAO_CREDITO'),
  ('CARTAO_DEBITO');

-- Usuários (login por e-mail; a senha é comparada como texto no backend atual)
INSERT INTO usuario (nome, email, celular, fk_tipo_usuario, senha) VALUES
  ('admin_app', 'admin_app@barbershop.test', '11990000001', 1, 'senha123'),
  ('Junior',    'junior@barbershop.test',    '11990000002', 2, 'senha123');

INSERT INTO cliente (nome, apelido, celular) VALUES
  ('Pedro Oliveira',  'Pedrinho', '11981110001'),
  ('João Ferreira',   'Jota',     '11981110002'),
  ('André Lima',      NULL,       '11981110003'),
  ('Bruno Martins',   'Brunão',   '11981110004'),
  ('Felipe Souza',    NULL,       '11981110005'),
  ('Gustavo Rocha',   'Guga',     '11981110006'),
  ('Matheus Ribeiro', NULL,       '11981110007'),
  ('Diego Carvalho',  'Diego',    '11981110008');

INSERT INTO servico (titulo, valor_atual, duracao_atual, ativo) VALUES
  ('Corte tradicional', 45.00, 30, TRUE),
  ('Barba',             30.00, 20, TRUE),
  ('Corte + barba',     65.00, 50, TRUE),
  ('Degradê',           50.00, 40, TRUE),
  ('Sobrancelha',       15.00, 10, TRUE),
  ('Hidratação',        25.00, 20, FALSE);

-- Pagamentos (1 a 8: agendamentos de outubro; 9 a 12: agendamentos de 15/12)
INSERT INTO pagamento (valor, pago, data_pagamento, fk_forma_pagamento) VALUES
  (45.00, TRUE,  '2026-10-08', 1),
  (30.00, TRUE,  '2026-10-09', 2),
  (65.00, FALSE, NULL, NULL),
  (50.00, FALSE, NULL, NULL),
  (45.00, TRUE,  '2026-10-10', 4),
  (80.00, TRUE,  '2026-10-11', 1),
  (30.00, FALSE, NULL, NULL),
  (65.00, FALSE, NULL, NULL),
  (45.00, FALSE, NULL, NULL),
  (30.00, FALSE, NULL, NULL),
  (65.00, FALSE, NULL, NULL),
  (50.00, FALSE, NULL, NULL);

-- Agendamentos (barbeiro = 2, Junior; os quatro últimos são de 15/12/2026)
INSERT INTO agendamento (horario_inicio, horario_fim, titulo, fk_status_agendamento, barbeiro, fk_cliente, fk_pagamento) VALUES
  ('2026-10-08 09:00:00', '2026-10-08 09:30:00', 'Pedro Oliveira - Corte tradicional', 4, 2, 1, 1),
  ('2026-10-09 10:00:00', '2026-10-09 10:20:00', 'João Ferreira - Barba',               4, 2, 2, 2),
  ('2026-10-12 13:00:00', '2026-10-12 13:50:00', 'André Lima - Corte + barba',          4, 2, 3, 3),
  ('2026-10-12 14:00:00', '2026-10-12 14:40:00', 'Bruno Martins - Degradê',             1, 2, 4, 4),
  ('2026-10-10 11:00:00', '2026-10-10 11:30:00', 'Felipe Souza - Corte tradicional',    4, 2, 5, 5),
  ('2026-10-11 15:00:00', '2026-10-11 15:50:00', 'Gustavo Rocha - Corte + barba',       4, 2, 6, 6),
  ('2026-10-13 09:30:00', '2026-10-13 09:50:00', 'Matheus Ribeiro - Barba',             2, 2, 7, 7),
  ('2026-10-14 16:00:00', '2026-10-14 16:50:00', 'Diego Carvalho - Corte + barba',      1, 2, 8, 8),
  ('2026-12-15 09:00:00', '2026-12-15 09:30:00', 'Pedro Oliveira - Corte tradicional',  1, 2, 1, 9),
  ('2026-12-15 10:00:00', '2026-12-15 10:20:00', 'João Ferreira - Barba',               1, 2, 2, 10),
  ('2026-12-15 14:00:00', '2026-12-15 14:50:00', 'André Lima - Corte + barba',          1, 2, 3, 11),
  ('2026-12-15 16:00:00', '2026-12-15 16:40:00', 'Bruno Martins - Degradê',             1, 2, 4, 12);

INSERT INTO agendamento_servico (fk_agendamento, fk_servico, valor_unitario, duracao) VALUES
  (1,  1, 45.00, 30),
  (2,  2, 30.00, 20),
  (3,  3, 65.00, 50),
  (4,  4, 50.00, 40),
  (5,  1, 45.00, 30),
  (6,  3, 65.00, 50),
  (6,  5, 15.00, 10),
  (7,  2, 30.00, 20),
  (8,  3, 65.00, 50),
  (9,  1, 45.00, 30),
  (10, 2, 30.00, 20),
  (11, 3, 65.00, 50),
  (12, 4, 50.00, 40);

-- Expediente do Junior (dia_semana: 1 = segunda ... 7 = domingo)
INSERT INTO expediente (dia_semana, fk_usuario, aberto, hora_abertura, hora_fechamento, intervalo_inicio, intervalo_fim) VALUES
  (1, 2, TRUE,  '09:00:00', '18:00:00', '12:00:00', '13:00:00'),
  (2, 2, TRUE,  '09:00:00', '18:00:00', '12:00:00', '13:00:00'),
  (3, 2, TRUE,  '09:00:00', '18:00:00', '12:00:00', '13:00:00'),
  (4, 2, TRUE,  '09:00:00', '18:00:00', '12:00:00', '13:00:00'),
  (5, 2, TRUE,  '09:00:00', '18:00:00', '12:00:00', '13:00:00'),
  (6, 2, TRUE,  '09:00:00', '14:00:00', NULL, NULL),
  (7, 2, FALSE, NULL, NULL, NULL, NULL);

INSERT INTO bloqueio (fk_usuario, inicio, fim, motivo) VALUES
  (2, '2026-10-12 15:00:00', '2026-10-12 16:00:00', 'Compromisso pessoal'),
  (2, '2026-10-13 12:00:00', '2026-10-13 13:00:00', 'Pausa excepcional');

INSERT INTO despesa (fk_usuario, descricao, valor, data_despesa) VALUES
  (1, 'Compra de lâminas e navalhas', 120.50, '2026-10-02'),
  (1, 'Produtos para cabelo e barba', 245.90, '2026-10-05'),
  (1, 'Manutenção de equipamento',    180.00, '2026-10-09'),
  (2, 'Material de uso individual',    35.00, '2026-10-10');
