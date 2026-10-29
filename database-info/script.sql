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
