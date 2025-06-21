-- Criação do banco de dados
CREATE DATABASE IF NOT EXISTS locadora_filmes;
USE locadora_filmes;

-- Tabela filme (classe abstrata)
CREATE TABLE IF NOT EXISTS filme (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    ano_lancamento INT NOT NULL,
    genero VARCHAR(50) NOT NULL,
    tipo ENUM('FISICO', 'STREAMING') NOT NULL
);

-- Tabela filme_fisico
CREATE TABLE IF NOT EXISTS filme_fisico (
    id INT PRIMARY KEY,
    numero_disco INT NOT NULL,
    tipo_midia VARCHAR(20) NOT NULL,
    FOREIGN KEY (id) REFERENCES filme(id) ON DELETE CASCADE
);

-- Tabela filme_streaming
CREATE TABLE IF NOT EXISTS filme_streaming (
    id INT PRIMARY KEY,
    plataforma VARCHAR(50) NOT NULL,
    link VARCHAR(255) NOT NULL,
    FOREIGN KEY (id) REFERENCES filme(id) ON DELETE CASCADE
);

-- Tabela cliente
CREATE TABLE IF NOT EXISTS cliente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    telefone VARCHAR(15),
    email VARCHAR(100)
);

-- Tabela locacao
CREATE TABLE IF NOT EXISTS locacao (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT NOT NULL,
    filme_id INT NOT NULL,
    data_locacao DATE NOT NULL,
    data_devolucao DATE NOT NULL,
    FOREIGN KEY (cliente_id) REFERENCES cliente(id),
    FOREIGN KEY (filme_id) REFERENCES filme(id)
);