
CREATE TABLE Setor_DIM (
    ID INTEGER,
    Designacao VARCHAR(255) NOT NULL CONSTRAINT SETOR_DIM_DESIGNACAO_UQ UNIQUE,
    CONSTRAINT SETOR_DIM_ID_PK PRIMARY KEY (ID)
);

CREATE TABLE Cultura_H(
    ID INTEGER,
    Nome VARCHAR(255) NOT NULL CONSTRAINT CULTURA_NOME_UQ UNIQUE,
    CONSTRAINT CULTURA_ID_PK PRIMARY KEY (ID)
);

CREATE TABLE Tipo_Cultura_H(
    ID INTEGER,
    Designacao VARCHAR(255) NOT NULL CONSTRAINT TIPO_CULTURA_DESIGNACAO_UQ UNIQUE,
    CONSTRAINT TIPO_CULTURA_ID_PK PRIMARY KEY (ID)
);

CREATE TABLE Produto_DIM(
    ID INTEGER,
    Tipo_Cultura_ID INTEGER NOT NULL,
    Cultura_ID INTEGER NOT NULL,
    CONSTRAINT PRODUTO_DIM_ID_PK PRIMARY KEY (ID),
    CONSTRAINT PRODUTO_DIM_TIPO_CULTURA_ID_FK FOREIGN KEY (Tipo_Cultura_ID) REFERENCES Tipo_Cultura_H(ID),
    CONSTRAINT PRODUTO_DIM_CULTURA_ID_FK FOREIGN KEY (Cultura_ID) REFERENCES Cultura_H(ID)
);

CREATE TABLE Tempo_DIM(
    ID INTEGER,
    Year INTEGER NOT NULL,
    Month INTEGER NOT NULL,
    CONSTRAINT TEMPO_DIM_ID_PK PRIMARY KEY (ID)
);

CREATE TABLE Morada_H(
    ID INTEGER,
    Localidade VARCHAR(255) NOT NULL,
    Pais VARCHAR(255) NOT NULL,
    Nome_rua VARCHAR(255) NOT NULL,
    Numero_porta INTEGER NOT NULL,
    Codigo_Postal VARCHAR(9) NOT NULL,
    CONSTRAINT MORADA_NUMERO_PORTA_CK CHECK (Numero_Porta > 0),
    --CONSTRAINT MORADA_CODIGO_POSTAL_CK CHECK (Codigo_Postal LIKE '[0-9][0-9][0-9][0-9]-[0-9][0-9][0-9]'),
    CONSTRAINT MORADA_ID_PK PRIMARY KEY (ID)
);

CREATE TABLE Tipo_Cliente_H(
    ID INTEGER,
    Designacao VARCHAR(255) NOT NULL CONSTRAINT TIPO_CLIENTE_DESIGNACAO_UQ UNIQUE,
    CONSTRAINT TIPO_CLIENTE_ID_PK PRIMARY KEY (ID)
);


--------------------------------------------------------------
--US216
--acceptance criteria 1/2

CREATE TABLE Tipo_Hub_H(
    ID INTEGER,
    Designacao VARCHAR(1) NOT NULL CONSTRAINT TIPO_HUB_DESIGNACAO_UQ UNIQUE,
    CONSTRAINT TIPO_HUB_ID_PK PRIMARY KEY (ID)
);

CREATE TABLE Hub_H(
    Location_ID VARCHAR2(5) NOT NULL,
    Latitude NUMBER NOT NULL,
    Longitude NUMBER NOT NULL,
    CONSTRAINT HUB_ID_PK PRIMARY KEY (Location_ID)
);

CREATE TABLE Local_Recolha_DIM(
    ID INTEGER,
    Hub_ID VARCHAR2(5) NOT NULL,
    Tipo_Hub_ID INTEGER NOT NULL,
    CONSTRAINT LOCAL_RECOLHA_ID_PK PRIMARY KEY (ID),
    CONSTRAINT LOCAL_RECOLHA_HUB_ID_FK FOREIGN KEY (Hub_ID) REFERENCES Hub_H(Location_ID),
    CONSTRAINT LOCAL_RECOLHA_TIPO_HUB_ID_FK FOREIGN KEY (Tipo_Hub_ID) REFERENCES Tipo_Hub_H(ID)
);

--------------------------------------------------------------

CREATE TABLE Cliente_DIM(
    Codigo_Unico INTEGER,
    Tipo_Cliente_ID INTEGER NOT NULL,
    Morada_Correspondencia_ID INTEGER NOT NULL,
    Nome VARCHAR2(255) NOT NULL,
    Numero_Fiscal VARCHAR(9) NOT NULL UNIQUE,
    Email VARCHAR2(255) NOT NULL UNIQUE,
    Plafond NUMBER NOT NULL,
    Numero_Total_Encomendas INTEGER NOT NULL,
    Valor_Total_Encomendas NUMBER NOT NULL,
    CONSTRAINT CLIENTE_EMAIL_CK CHECK (Email LIKE '%@%.%'),
    CONSTRAINT CLIENTE_NUMERO_FISCAL_CK CHECK (Numero_Fiscal > 99999999 AND Numero_Fiscal < 1000000000),
    CONSTRAINT CLIENTE_CODIGO_UNICO_PK PRIMARY KEY (Codigo_Unico),
    CONSTRAINT CLIENTE_TIPO_CLIENTE_ID_FK FOREIGN KEY (Tipo_Cliente_ID) REFERENCES Tipo_Cliente_H(ID),
    CONSTRAINT CLIENTE_MORADA_CORRESPONDENCIA_ID_FK FOREIGN KEY (Morada_Correspondencia_ID) REFERENCES Morada_H(ID),
    CONSTRAINT CLIENTE_PLAFOND_CK CHECK (Plafond >= 0),
    CONSTRAINT CLIENTE_NUMERO_TOTAL_ENCOMENDAS_CK CHECK (Numero_Total_Encomendas >= 0),
    CONSTRAINT CLIENTE_VALOR_TOTAL_ENCOMENDAS_CK CHECK (Valor_Total_Encomendas >= 0)
);


CREATE TABLE Venda_FAC(
    Cliente_ID INTEGER NOT NULL,
    Produto_ID INTEGER NOT NULL,
    Tempo_ID INTEGER NOT NULL,
    Local_Recolha_ID INTEGER NOT NULL,
    Quantidade INTEGER NOT NULL,
    Preco NUMBER NOT NULL,
    CONSTRAINT VENDA_FAC_CLIENTE_ID_FK FOREIGN KEY (Cliente_ID) REFERENCES Cliente_DIM(Codigo_Unico),
    CONSTRAINT VENDA_FAC_PRODUTO_ID_FK FOREIGN KEY (Produto_ID) REFERENCES Produto_DIM(ID),
    CONSTRAINT VENDA_FAC_TEMPO_ID_FK FOREIGN KEY (Tempo_ID) REFERENCES Tempo_DIM(ID),
    CONSTRAINT VENDA_FAC_LOCAL_RECOLHA_ID_FK FOREIGN KEY (Local_Recolha_ID) REFERENCES Local_Recolha_DIM(ID),
    CONSTRAINT VENDA_FAC_Preco_CK CHECK (Preco > 0),
    CONSTRAINT VENDA_FAC_Quantidade_CK CHECK (Quantidade > 0),
    CONSTRAINT VENDA_FAC_PK_COMP PRIMARY KEY (Cliente_ID, Produto_ID, Tempo_ID, Local_Recolha_ID)
);

CREATE TABLE Producao_FAC(
    Produto_ID INTEGER NOT NULL,
    Setor_ID INTEGER NOT NULL,
    Tempo_ID INTEGER NOT NULL,
    Quantidade NUMBER NOT NULL,
    CONSTRAINT PRODUCAO_FAC_SETOR_ID_FK FOREIGN KEY (Setor_ID) REFERENCES Setor_DIM(ID),
    CONSTRAINT PRODUCAO_FAC_PRODUTO_ID_FK FOREIGN KEY (Produto_ID) REFERENCES Produto_DIM(ID),
    CONSTRAINT PRODUCAO_FAC_TEMPO_ID_FK FOREIGN KEY (Tempo_ID) REFERENCES Tempo_DIM(ID),
    CONSTRAINT PRODUCAO_FAC_Quantidade_CK CHECK (Quantidade > 0),
    CONSTRAINT PRODUCAO_FAC_PK_COMP PRIMARY KEY (Produto_ID, Setor_ID, Tempo_ID)
);  
