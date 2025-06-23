-- Criação do banco de dados
CREATE DATABASE IF NOT EXISTS locadora_filmes;
USE locadora_filmes;

-- Tabela filme
CREATE TABLE IF NOT EXISTS filme (
    titulo VARCHAR(100) PRIMARY KEY,
    ano_lancamento INT NOT NULL,
    genero VARCHAR(50) NOT NULL,
    tipo ENUM('FISICO', 'STREAMING') NOT NULL,
    numero_disco INT,
    tipo_midia VARCHAR(20),
    plataforma VARCHAR(50),
    link VARCHAR(255)
);

-- Tabela cliente
CREATE TABLE IF NOT EXISTS cliente (
    cpf VARCHAR(14) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    telefone VARCHAR(15),
    email VARCHAR(100)
);

-- Tabela locacao
CREATE TABLE IF NOT EXISTS locacao (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_cpf VARCHAR(14) NOT NULL,
    filme_titulo VARCHAR(100) NOT NULL,
    data_locacao DATE NOT NULL,
    data_devolucao DATE NOT NULL,
    data_devolucao_real DATE NULL,
    status VARCHAR(20) DEFAULT 'ATIVA',
    FOREIGN KEY (cliente_cpf) REFERENCES cliente(cpf) ON DELETE CASCADE,
    FOREIGN KEY (filme_titulo) REFERENCES filme(titulo) ON DELETE CASCADE
);

-- Índices para melhor performance
CREATE INDEX idx_locacao_cliente ON locacao(cliente_cpf);
CREATE INDEX idx_locacao_filme ON locacao(filme_titulo);
CREATE INDEX idx_locacao_status ON locacao(status);
CREATE INDEX idx_locacao_data_devolucao ON locacao(data_devolucao_real);