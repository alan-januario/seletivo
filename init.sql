-- Criação do banco de dados (caso necessário)
CREATE DATABASE seletivo;

-- Criação das tabelas para o sistema seletivo

-- Tabela de Cidade
CREATE TABLE cidade (
    cid_id SERIAL PRIMARY KEY,
    cid_nome VARCHAR(255) NOT NULL,
    cid_uf VARCHAR(2) NOT NULL
);

-- Tabela de Endereco
CREATE TABLE endereco (
    end_id SERIAL PRIMARY KEY,
    end_tipo_logradouro VARCHAR(255),
    end_logradouro VARCHAR(255) NOT NULL,
    end_numero VARCHAR(20),
    end_bairro VARCHAR(255) NOT NULL,
    cid_id INTEGER NOT NULL REFERENCES cidade(cid_id)
);

-- Tabela de Pessoa
CREATE TABLE pessoa (
    pes_id SERIAL PRIMARY KEY,
    pes_nome VARCHAR(255) NOT NULL,
    pes_data_nascimento DATE NOT NULL,
    pes_sexo VARCHAR(255) NOT NULL,
    pes_mae VARCHAR(255),
    pes_pai VARCHAR(255)
);

-- Tabela de relacionamento entre Pessoa e Endereco
CREATE TABLE pessoa_endereco (
    pes_id INTEGER REFERENCES pessoa(pes_id),
    end_id INTEGER REFERENCES endereco(end_id),
    PRIMARY KEY (pes_id, end_id)
);

-- Tabela de Unidade
CREATE TABLE unidade (
    unid_id SERIAL PRIMARY KEY,
    unid_nome VARCHAR(255) NOT NULL,
    unid_sigla VARCHAR(255) NOT NULL
);

-- Tabela de relacionamento entre Unidade e Endereco
CREATE TABLE unidade_endereco (
    unid_id INTEGER REFERENCES unidade(unid_id),
    end_id INTEGER REFERENCES endereco(end_id),
    PRIMARY KEY (unid_id, end_id)
);

-- Tabela de Servidor Efetivo
CREATE TABLE servidor_efetivo (
    pes_id INTEGER PRIMARY KEY REFERENCES pessoa(pes_id),
    se_matricula VARCHAR(255) NOT NULL
);

-- Tabela de Servidor Temporario
CREATE TABLE servidor_temporario (
    pes_id INTEGER PRIMARY KEY REFERENCES pessoa(pes_id),
    st_data_admissao DATE NOT NULL,
    st_data_demissao DATE
);

-- Tabela de Lotacao
CREATE TABLE lotacao (
    lot_id SERIAL PRIMARY KEY,
    pes_id INTEGER NOT NULL REFERENCES pessoa(pes_id),
    unid_id INTEGER NOT NULL REFERENCES unidade(unid_id),
    lot_data_lotacao DATE NOT NULL,
    lot_data_remocao DATE,
    lot_portaria VARCHAR(255)
);

-- Tabela de Foto Pessoa
CREATE TABLE foto_pessoa (
    fp_id SERIAL PRIMARY KEY,
    pes_id INTEGER NOT NULL REFERENCES pessoa(pes_id),
    fp_data DATE,
    fp_bucket VARCHAR(255),
    fp_hash VARCHAR(255)
);

-- Tabela de Usuários
CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL
);

-- Índices para melhorar a performance
CREATE INDEX idx_endereco_cidade ON endereco(cid_id);
CREATE INDEX idx_pessoa_endereco_pessoa ON pessoa_endereco(pes_id);
CREATE INDEX idx_pessoa_endereco_endereco ON pessoa_endereco(end_id);
CREATE INDEX idx_unidade_endereco_unidade ON unidade_endereco(unid_id);
CREATE INDEX idx_unidade_endereco_endereco ON unidade_endereco(end_id);
CREATE INDEX idx_lotacao_pessoa ON lotacao(pes_id);
CREATE INDEX idx_lotacao_unidade ON lotacao(unid_id);
CREATE INDEX idx_foto_pessoa_pessoa ON foto_pessoa(pes_id);
