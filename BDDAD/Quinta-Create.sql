CREATE TABLE Campo (
    ID INTEGER,
    Designacao VARCHAR2(255) NOT NULL,
    Hectares INTEGER NOT NULL,
    CONSTRAINT CAMPO_HECTARES_CK CHECK(Hectares > 0),
    CONSTRAINT CAMPO_ID_PK PRIMARY KEY (ID)
);

CREATE TABLE Produto (
    ID INTEGER,
    Nome VARCHAR2(255) NOT NULL CONSTRAINT Especie_Vegetal_Nome_UQ UNIQUE,
    CONSTRAINT PRODUTO_ID_PK PRIMARY KEY (ID)
);


CREATE TABLE Cultura (
    ID INTEGER,
    Produto_ID INTEGER,
    CONSTRAINT CULTURA_PRODUTO_ID_FK FOREIGN KEY (Produto_ID) REFERENCES Produto(ID),
    CONSTRAINT CULTURA_ID_PK PRIMARY KEY (ID)
);

CREATE TABLE Cultura_Temporaria (
    Cultura_ID INTEGER,
    Ciclo_Vegetativo INTEGER NOT NULL,
    CONSTRAINT CULTURA_TEMPORARIA_CICLO_VEGETATIVO_CK CHECK (Ciclo_Vegetativo > 0 AND Ciclo_Vegetativo <= 12),
    CONSTRAINT CULTURA_TEMPORARIA_CULTURA_ID_FK FOREIGN KEY (Cultura_ID) REFERENCES Cultura(ID),
    CONSTRAINT CULTURA_TEMPORARIA_CULTURA_ID_PK PRIMARY KEY (Cultura_ID)
);

CREATE TABLE Cultura_Permanente (
    Cultura_ID INTEGER,
    CONSTRAINT CULTURA_PERMANENTE_CULTURA_ID_FK FOREIGN KEY (Cultura_ID) REFERENCES Cultura(ID),
    CONSTRAINT CULTURA_PERMANENTE_CULTURA_ID_PK PRIMARY KEY (Cultura_ID)
);

CREATE TABLE Plantacao (
    ID INTEGER,
    Campo_ID INTEGER,
    Cultura_ID INTEGER,
    Data_Inicio_Cultura DATE NOT NULL,
    Data_Fim_Cultura DATE,
    CONSTRAINT PLANTACAO_ID_PK PRIMARY KEY (ID),
    CONSTRAINT PLANTACAO_DATA_INICIO_CK CHECK (TO_CHAR(Data_Inicio_Cultura, 'YYYY-MM-DD') < TO_CHAR(Data_Fim_Cultura, 'YYYY-MM-DD')),
    CONSTRAINT PLANTACAO_CAMPO_ID_FK FOREIGN KEY (Campo_ID) REFERENCES Campo(ID),
    CONSTRAINT PLANTACAO_CULTURA_ID_FK FOREIGN KEY (Cultura_ID) REFERENCES Cultura(ID)
);


CREATE TABLE Accao(
    ID INTEGER,
    Plantacao_ID INTEGER,
    Data_Accao DATE NOT NULL,
    CONSTRAINT ACCAO_PLANTACAO_ID_FK FOREIGN KEY(Plantacao_ID) REFERENCES Plantacao(ID),
    CONSTRAINT ACCAO_ID_PK PRIMARY KEY (ID) 
);

CREATE TABLE Colheita (
    Accao_ID INTEGER,
    Produto_ID INTEGER NOT NULL,
    Quantidade NUMBER NOT NULL,
    Preco NUMBER NOT NULL,
    CONSTRAINT COLHEITA_QUANTIDADE_CK CHECK (Quantidade > 0),
    CONSTRAINT COLHEITA_PRODUTOL_ID_FK FOREIGN KEY (Produto_ID) REFERENCES Produto(ID),
    CONSTRAINT COLHEITA_ACCAO_ID_FK FOREIGN KEY (Accao_ID) REFERENCES Accao(ID),
    CONSTRAINT COLHEITA_ACCAO_ID_PK PRIMARY KEY (Accao_ID)
);

CREATE TABLE Tipo_Cliente (
    ID INTEGER,
    Designacao VARCHAR2(255) NOT NULL CONSTRAINT TIPO_CLIENTE_DESIGNACAO_UQ UNIQUE,
    CONSTRAINT TIPO_CLIENTE_ID_PK PRIMARY KEY (ID)
);

CREATE TABLE Localidade (
    ID INTEGER,
    Nome VARCHAR2(255) NOT NULL CONSTRAINT LOCALIDADE_NOME_UQ UNIQUE,
    CONSTRAINT LOCALIDADE_ID_PK PRIMARY KEY (ID)
);

CREATE TABLE Pais (
    ID INTEGER,
    Nome VARCHAR2(255) NOT NULL CONSTRAINT PAIS_NOME_UQ UNIQUE,
    CONSTRAINT PAIS_ID_PK PRIMARY KEY (ID)
);

CREATE TABLE Morada (
    ID INTEGER,
    Localidade_ID INTEGER NOT NULL,
    Pais_ID INTEGER NOT NULL,
    Nome_Rua VARCHAR2(255) NOT NULL,
    Numero_Porta INTEGER NOT NULL,
    Codigo_Postal VARCHAR(8) NOT NULL,
    CONSTRAINT MORADA_NUMERO_PORTA_CK CHECK (Numero_Porta > 0),
    --CONSTRAINT MORADA_CODIGO_POSTAL_CK CHECK (Codigo_Postal LIKE '[0-9][0-9][0-9][0-9]-[0-9][0-9][0-9]'),
    CONSTRAINT MORADA_PAIS_ID_FK FOREIGN KEY (Pais_ID) REFERENCES Pais(ID),
    CONSTRAINT MORADA_LOCALIDADE_ID_FK FOREIGN KEY (Localidade_ID) REFERENCES Localidade(ID),
    CONSTRAINT MORADA_ID_PK PRIMARY KEY (ID)
);

CREATE TABLE Cliente (
    Codigo_Unico INTEGER,
    Tipo_Cliente_ID INTEGER NOT NULL,
    Morada_Correspondencia_ID INTEGER NOT NULL,
    Morada_Entrega_ID INTEGER NOT NULL,
    Nome VARCHAR2(255) NOT NULL,
    Numero_Fiscal INTEGER NOT NULL CONSTRAINT CLIENTE_NUMERO_FISCAL_UQ UNIQUE,
    Email VARCHAR2(255) NOT NULL CONSTRAINT CLIENTE_EMAIL_UQ UNIQUE,
    Plafond NUMBER NOT NULL,
    Numero_Total_Encomendas INTEGER NOT NULL,
    Valor_Total_Encomendas INTEGER NOT NULL, 
    CONSTRAINT CLIENTE_EMAIL_CK CHECK (Email LIKE '%@%.%'),
    CONSTRAINT CLIENTE_NUMERO_FISCAL_CK CHECK (Numero_Fiscal > 99999999 AND Numero_Fiscal < 1000000000),
    CONSTRAINT CLIENTE_MORADA_ENTREGA_ID_FK FOREIGN KEY (Morada_Entrega_ID) REFERENCES Morada(ID),
    CONSTRAINT CLIENTE_MORADA_CORRESPONDENCIA_ID_FK FOREIGN KEY (Morada_Correspondencia_ID) REFERENCES Morada(ID),
    CONSTRAINT CLIENTE_TIPO_CLIENTE_ID_FK FOREIGN KEY (Tipo_Cliente_ID) REFERENCES Tipo_Cliente(ID),
    CONSTRAINT CLIENTE_CODIGO_UNICO_PK PRIMARY KEY (Codigo_Unico)
);

CREATE TABLE Encomenda (
    ID INTEGER,
    Cliente_Codigo_Unico INTEGER NOT NULL,
    Data_Encomenda DATE NOT NULL,
    Data_Limite_Pagamento DATE NOT NULL,
    Data_Pagamento DATE,
    CONSTRAINT ENCOMENDA_CLIENTE_CODIGO_UNICO_FK FOREIGN KEY (Cliente_Codigo_Unico) REFERENCES Cliente(Codigo_Unico),
    CONSTRAINT ENCOMENDA_ID_PK PRIMARY KEY (ID)
);


CREATE TABLE Incidente (
    Encomenda_ID INTEGER CONSTRAINT INCIDENTE_ENCOMENDA_ID_UQ UNIQUE,
    Data_Incumprimento DATE NOT NULL,
    CONSTRAINT INCIDENTE_ENCOMENDA_ID_FK FOREIGN KEY (Encomenda_ID) REFERENCES Encomenda(ID)
);

CREATE TABLE Encomenda_Produto(
    Encomenda_ID INTEGER,
    Produto_ID INTEGER,
    Quantidade NUMBER NOT NULL,
    Preco NUMBER NOT NULL,
    CONSTRAINT ENCOMENDA_PRODUTO_ENCOMENDA_ID_FK FOREIGN KEY (Encomenda_ID) REFERENCES Encomenda(ID),
    CONSTRAINT ENCOMENDA_PRODUTO_PRODUTO_ID_FK FOREIGN KEY (Produto_ID) REFERENCES Produto(ID),
    CONSTRAINT ENCOMENDA_PRODUTO_COMP_PK PRIMARY KEY (Encomenda_ID, Produto_ID),
    CONSTRAINT ENCOMENDA_PRODUTO_QUANTIDADE_CK CHECK (Quantidade > 0),
    CONSTRAINT ECOMENDA_PRODUTO_PRECO_CK CHECK (Preco >= 0)
);


CREATE TABLE Metodo_Distribuicao(
    ID INTEGER,
    Designacao VARCHAR2(255) NOT NULL CONSTRAINT METODO_DISTRIBUICAO_DESIGNACAO_UQ UNIQUE,
    CONSTRAINT METODO_DISTRIBUICAO_ID_PK PRIMARY KEY (ID)
);

CREATE TABLE Rega (
    Accao_ID INTEGER,
    Metodo_Distribuicao_ID INTEGER NOT NULL,
    Quantidade NUMBER NOT NULL,
    CONSTRAINT REGA_QUANTIDADE_CK CHECK (Quantidade > 0),
    CONSTRAINT REGA_ACCAO_ID_FK FOREIGN KEY (Accao_ID) REFERENCES Accao(ID),
    CONSTRAINT REGA_METODO_DISTRIBUICAO_ID_FK FOREIGN KEY (Metodo_Distribuicao_ID) REFERENCES Metodo_Distribuicao(ID),
    CONSTRAINT REGA_ACCAO_ID_PK PRIMARY KEY (Accao_ID)
);

CREATE TABLE Categoria(
    ID INTEGER,
    Designacao VARCHAR2(255) NOT NULL CONSTRAINT CATEGORIA_DESIGNACAO_UQ UNIQUE,
    PRIMARY KEY(ID)
);


CREATE TABLE Substancia(
    ID INTEGER,
    Nome VARCHAR2(255) NOT NULL CONSTRAINT SUBSTANCIA_NOME_UQ UNIQUE,
    Unidade VARCHAR2(255) NOT NULL,
    Categoria_ID INTEGER NOT NULL,
    CONSTRAINT PRODUTO_CATEGORIA_ID_FK FOREIGN KEY (Categoria_ID) REFERENCES Categoria(ID),
    CONSTRAINT SUBSTANCIA_ID_PK PRIMARY KEY (ID)
);


CREATE TABLE Tipo_Fator_Producao(
    ID INTEGER,
    Designacao VARCHAR2(255) NOT NULL CONSTRAINT TIPO_PRODUTO_DESGNACAO_UQ UNIQUE,
    CONSTRAINT TIPO_PRODUTO_ID_PK PRIMARY KEY (ID)
);


CREATE TABLE Ficha_Tecnica(
    ID INTEGER,
    Fornecedor VARCHAR2(255) NOT NULL,
    Humidade FLOAT,
    pH FLOAT,
    Peso_Especifico FLOAT,
    Formulacao_Peletizada FLOAT,
    CONSTRAINT FICHA_TECNICA_PK PRIMARY KEY(ID)
);

CREATE TABLE Ficha_Tecnica_Substancia(
    Ficha_Tecnica_ID INTEGER,
    Substancia_ID INTEGER,
    QuantidadePerc FLOAT NOT NULL,
    CONSTRAINT FICHA_TECNICA_QUANTIDADEPERC_CK CHECK(QuantidadePerc > 0 and QuantidadePerc < 100),
    CONSTRAINT FICHA_TECNICA_SUBSTANCIA_FICHA_TECNICA_ID_FK FOREIGN KEY (Ficha_Tecnica_ID) REFERENCES Ficha_Tecnica(ID),
    CONSTRAINT FICHA_TECNICA_SUBSTANCIA_ID_FK FOREIGN KEY (Substancia_ID) REFERENCES Substancia(ID)
);

CREATE TABLE Fator_Producao(
    ID INTEGER,
    Tipo_Fator_Producao_ID INTEGER,
    Ficha_Tecnica_ID INTEGER NOT NULL CONSTRAINT FATOR_PRODUCAO_FICHA_TECNICA_ID_UQ UNIQUE,
    Nome VARCHAR2(255) NOT NULL CONSTRAINT FATOR_PRODUCAO_NOME_UQ UNIQUE,
    CONSTRAINT FATOR_PRODUCAO_TIPO_FATOR_PRODUCAO_ID_FK FOREIGN KEY(Tipo_Fator_Producao_ID) REFERENCES Tipo_Fator_Producao(ID),
    CONSTRAINT FATOT_PRODUCAO_FICHA_TECNICA_ID_FK FOREIGN KEY (Ficha_Tecnica_ID) REFERENCES Ficha_Tecnica(ID),
    CONSTRAINT FATOR_PRODUCAO_ID_PK PRIMARY KEY (ID)
);

CREATE TABLE Aplicacao_Fator_Producao(
    Accao_ID INTEGER,
    Fator_Producao_ID INTEGER NOT NULL,
    Metodo_Distribuicao_ID INTEGER NOT NULL,
    Quantidade NUMBER NOT NULL,
    CONSTRAINT APLICACAO_FATOR_PRODUCAO_QUANTIDADE_CK CHECK (Quantidade > 0),
    CONSTRAINT APLICACAO_FATOR_PRODUCAO_ACCAO_ID_FK FOREIGN KEY (Accao_ID) REFERENCES Accao(ID),
    CONSTRAINT APLICACAO_FATOR_PRODUCAO_FATOR_PRODUCAO_ID_FK FOREIGN KEY (Fator_Producao_ID) REFERENCES Fator_Producao(ID),
    CONSTRAINT APLICACAO_FATOR_PRODUCAO_METOFO_DISTRIBUICAO_ID_FK FOREIGN KEY (Metodo_Distribuicao_ID) REFERENCES Metodo_Distribuicao(ID),
    CONSTRAINT APLICACAO_FATOR_PRODUCAO_ACCAO_ID_PK PRIMARY KEY (Accao_ID)  
); 

CREATE TABLE Periodicidade(
    ID INTEGER, 
    Designacao VARCHAR2(255) NOT NULL CONSTRAINT PERIODICIDADE_DESIGNACAO_UQ UNIQUE,
    CONSTRAINT PERIODICIDADE_ID_PK PRIMARY KEY(ID)
);


CREATE TABLE Plano(
    ID INTEGER,
    Periodicidade_ID INTEGER NOT NULL,
    Plantacao_ID INTEGER NOT NULL,
    Quantidade NUMBER NOT NULL,
    CONSTRAINT PLANO_PERIODICIDADE_ID_FK FOREIGN KEY (Periodicidade_ID) REFERENCES Periodicidade(ID),
    CONSTRAINT PLANO_PLANTACAO_ID_FK FOREIGN KEY (Plantacao_ID) REFERENCES Plantacao(ID), 
    CONSTRAINT PLANO_ID_PK PRIMARY KEY (ID)
);

CREATE TABLE Plano_Rega(
    Plano_ID INTEGER,
    CONSTRAINT PLANO_REGA_PLANO_ID_FK FOREIGN KEY (Plano_ID) REFERENCES Plano(ID),
    CONSTRAINT PLANO_REGA_PLANO_ID_PK PRIMARY KEY (Plano_ID)
); 

CREATE TABLE Plano_Aplicacao_Produto(
    Plano_ID INTEGER,
    CONSTRAINT PLANO_APLICACAO_PRODUTO_ID_FK FOREIGN KEY(Plano_ID) REFERENCES Plano(ID),
    CONSTRAINT PLANO_APLICACAO_PRODUTO_ID_PK PRIMARY KEY(Plano_ID)
);

CREATE TABLE Tipo_Sensor(
    ID INTEGER,
    Designacao VARCHAR2(255) NOT NULL CONSTRAINT TIPO_SENSOR_DESIGNACAO_UQ UNIQUE,
    CONSTRAINT TIPO_SENSOR_ID_PK PRIMARY KEY (ID)
);

CREATE TABLE Sensor(
    ID INTEGER,
    Tipo_Sensor_ID INTEGER,
    CONSTRAINT SENSOR_TIPO_SENSOR_ID_FK FOREIGN KEY (Tipo_Sensor_ID) REFERENCES Tipo_Sensor(ID),
    CONSTRAINT SENSOR_ID_PK PRIMARY KEY (ID)
);

CREATE TABLE Leitura(
    Plantacao_ID INTEGER,
    Data_Leitura DATE,
    Sensor_ID INTEGER NOT NULL,
    Valor NUMBER NOT NULL,
    CONSTRAINT LEITURA_PLANTACAO_ID_FK FOREIGN KEY (Plantacao_ID) REFERENCES Plantacao(ID),
    CONSTRAINT LEITURA_SENSOR_ID_FK FOREIGN KEY (Sensor_ID) REFERENCES Sensor(ID),
    CONSTRAINT LEITURA_COMP_PK PRIMARY KEY (Plantacao_ID,Data_Leitura)
);