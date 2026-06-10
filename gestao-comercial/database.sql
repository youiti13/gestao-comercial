CREATE DATABASE IF NOT EXISTS gestao_comercial;

USE gestao_comercial;

CREATE TABLE usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    login VARCHAR(50) UNIQUE NOT NULL,
    senha VARCHAR(50) NOT NULL,
    perfil VARCHAR(20) NOT NULL
);

INSERT INTO usuario(login, senha, perfil)
VALUES ('admin', '123', 'ADMIN');

INSERT INTO usuario(login, senha, perfil)
VALUES ('vendedor', '123', 'VENDEDOR');

CREATE TABLE cliente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(20) UNIQUE NOT NULL,
    telefone VARCHAR(20),
    email VARCHAR(100),
    endereco VARCHAR(150),
    ativo BOOLEAN DEFAULT TRUE
);

CREATE TABLE produto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    categoria VARCHAR(50),
    preco DECIMAL(10,2) NOT NULL,
    estoque INT NOT NULL,
    fornecedor VARCHAR(100),
    ativo BOOLEAN DEFAULT TRUE
);


CREATE TABLE pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,

    cliente_id INT NOT NULL,

    produto_id INT NOT NULL,

    quantidade INT NOT NULL,

    valor_total DECIMAL(10,2) NOT NULL,

    data_pedido DATE NOT NULL,

    status VARCHAR(30) NOT NULL,

    FOREIGN KEY (cliente_id)
        REFERENCES cliente(id),

    FOREIGN KEY (produto_id)
        REFERENCES produto(id)
);
SHOW TABLES;
DESCRIBE cliente;
select * from clienteusuario
