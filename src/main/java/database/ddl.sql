-- ==============================================================================
-- UNIVERSIDAD EUROPEA - GESTION ACADEMICA (DDL)
-- Definicion de Estructura de Datos para MariaDB / MySQL (Laragon)
-- ==============================================================================

DROP DATABASE IF EXISTS `universidad_europea`;
CREATE DATABASE `universidad_europea` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `universidad_europea`;

-- Tabla de Usuarios del Campus (Docentes y Estudiantes)
CREATE TABLE `usuarios` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `identificacion` VARCHAR(20) NOT NULL UNIQUE,
    `nombre` VARCHAR(50) NOT NULL,
    `apellido` VARCHAR(80) NOT NULL,
    `username` VARCHAR(50) NOT NULL UNIQUE,
    `password_hash` VARCHAR(64) NOT NULL,
    `rol` ENUM('PROFESOR', 'ALUMNO') NOT NULL,
    `creado_en` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- Tabla de Cursos Academicos
CREATE TABLE `cursos` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `nombre` VARCHAR(100) NOT NULL UNIQUE
) ENGINE=InnoDB;

-- Tabla de Modulos / Asignaturas Oficiales
CREATE TABLE `modulos` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `nombre` VARCHAR(100) NOT NULL,
    `curso_id` INT NOT NULL,
    `profesor_id` INT NOT NULL,
    CONSTRAINT `fk_modulos_curso` FOREIGN KEY (`curso_id`) REFERENCES `cursos`(`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_modulos_profesor` FOREIGN KEY (`profesor_id`) REFERENCES `usuarios`(`id`) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- Tabla de Matriculas (Relacion Alumno - Modulo)
CREATE TABLE `matriculas` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `alumno_id` INT NOT NULL,
    `modulo_id` INT NOT NULL,
    UNIQUE KEY `uk_alumno_modulo` (`alumno_id`, `modulo_id`),
    CONSTRAINT `fk_matricula_alumno` FOREIGN KEY (`alumno_id`) REFERENCES `usuarios`(`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_matricula_modulo` FOREIGN KEY (`modulo_id`) REFERENCES `modulos`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Tabla de Evaluaciones y Calificaciones
CREATE TABLE `notas` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `matricula_id` INT NOT NULL UNIQUE,
    `nota` DECIMAL(4,2) DEFAULT NULL,
    `observaciones` VARCHAR(255) DEFAULT NULL,
    `actualizado_en` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `fk_notas_matricula` FOREIGN KEY (`matricula_id`) REFERENCES `matriculas`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB;
