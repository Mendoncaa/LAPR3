    ------------acceptance criteria 5----------------

DECLARE

nr_setor_dim INTEGER;
nr_cultura INTEGER;
nr_TIPO_cultura INTEGER;
nr_produto_dim INTEGER;
nr_tempo_dim INTEGER;
nr_cliente_dim INTEGER;
nr_morada INTEGER;
nr_tipo_cliente INTEGER;
nr_producao_FAC INTEGER;
nr_venda_FAC INTEGER;
nr_tipo_hub INTEGER;
nr_hub INTEGER;
nr_local_recolha INTEGER;

BEGIN

DBMS_OUTPUT.PUT_LINE(chr(0));

insert into Setor_DIM(ID, designacao) values (1,'setor 1');
insert into Setor_DIM(ID, designacao) values (2,'setor 2');
insert into Setor_DIM(ID, designacao) values (3,'setor 3');
insert into Setor_DIM(ID, designacao) values (4,'setor 4');
insert into Setor_DIM(ID, designacao) values (5,'setor 5');

SELECT Count(*) INTO nr_Setor_DIM from Setor_DIM;
DBMS_OUTPUT.PUT_LINE('Setores');
DBMS_OUTPUT.PUT_LINE('Expected: 5   Actual: '||nr_Setor_DIM);
DBMS_OUTPUT.PUT_LINE(chr(0));

insert into Cultura(ID, Nome) VALUES (1,'Milho');
insert into Cultura(ID, Nome) VALUES (2,'Tomate');
insert into Cultura(ID, Nome) VALUES (3,'Batata');
insert into Cultura(ID, Nome) VALUES (4,'Cebola');
DBMS_OUTPUT.PUT_LINE('Culturas');
SELECT Count(*) INTO nr_cultura from Cultura;
DBMS_OUTPUT.PUT_LINE('Expected: 4   Actual: '||nr_cultura);
DBMS_OUTPUT.PUT_LINE(chr(0));

insert into Tipo_Cultura(ID, Designacao) VALUES (1,'Permanente');
insert into Tipo_Cultura(ID, Designacao) VALUES (2,'Temporaria');
DBMS_OUTPUT.PUT_LINE('Tipo Culturas');
SELECT Count(*) INTO nr_tipo_cultura from Tipo_Cultura;
DBMS_OUTPUT.PUT_LINE('Expected: 2   Actual: '||nr_tipo_cultura);
DBMS_OUTPUT.PUT_LINE(chr(0));

insert into Produto_DIM(ID, Tipo_Cultura_ID, cultura_id) VALUES (1,1,1);
insert into Produto_DIM(ID, Tipo_Cultura_ID, cultura_id) VALUES (2,2,2);
insert into Produto_DIM(ID, Tipo_Cultura_ID, cultura_id) VALUES (3,2,3);
insert into Produto_DIM(ID, Tipo_Cultura_ID, cultura_id) VALUES (4,1,4);
DBMS_OUTPUT.PUT_LINE('Produtos');
SELECT Count(*) INTO nr_produto_dim from Produto_DIM;
DBMS_OUTPUT.PUT_LINE('Expected: 4   Actual: '||nr_produto_dim);
DBMS_OUTPUT.PUT_LINE(chr(0));

insert into Tempo_DIM(ID, Year, MONTH) VALUES (1,2015,1);
insert into Tempo_DIM(ID, Year, MONTH) VALUES (2,2022,2);
insert into Tempo_DIM(ID, Year, MONTH) VALUES (3,2016,3);
insert into Tempo_DIM(ID, Year, MONTH) VALUES (4,2015,4);
insert into Tempo_DIM(ID, Year, MONTH) VALUES (5,2018,5);
insert into Tempo_DIM(ID, Year, MONTH) VALUES (6,2022,6);
insert into Tempo_DIM(ID, Year, MONTH) VALUES (7,2022,7);
insert into Tempo_DIM(ID, Year, MONTH) VALUES (8,2020,8);
insert into Tempo_DIM(ID, Year, MONTH) VALUES (9,2019,9);
insert into Tempo_DIM(ID, Year, MONTH) VALUES (10,2018,10);
insert into Tempo_DIM(ID, Year, MONTH) VALUES (11,2017,11);
insert into Tempo_DIM(ID, Year, MONTH) VALUES (12,2016,12);
DBMS_OUTPUT.PUT_LINE('Tempo');
SELECT Count(*) INTO nr_tempo_dim from Tempo_DIM;
DBMS_OUTPUT.PUT_LINE('Expected: 12   Actual: '||nr_tempo_dim);
DBMS_OUTPUT.PUT_LINE(chr(0));

insert into Morada(ID, Nome_Rua, Codigo_Postal, Localidade, Pais, numero_porta) VALUES (1,'Rua 1', 1234-1234, 'Localidade 1', 'Portugal', 1);
insert into Morada(ID, Nome_Rua, Codigo_Postal, Localidade, Pais, numero_porta) VALUES (2,'Rua 2', 1234-1234, 'Localidade 2', 'Portugal', 2);
insert into Morada(ID, Nome_Rua, Codigo_Postal, Localidade, Pais, numero_porta) VALUES (3,'Rua 3', 1234-1234, 'Localidade 3', 'Portugal', 3);
insert into Morada(ID, Nome_Rua, Codigo_Postal, Localidade, Pais, numero_porta) VALUES (4,'Rua 4', 1234-1234, 'Localidade 4', 'Portugal', 4);
DBMS_OUTPUT.PUT_LINE('Moradas');
SELECT Count(*) INTO nr_morada from Morada;
DBMS_OUTPUT.PUT_LINE('Expected: 4   Actual: '||nr_morada);
DBMS_OUTPUT.PUT_LINE(chr(0));

insert into Tipo_Cliente(ID, Designacao) VALUES (1,'Tipo 1');
insert into Tipo_Cliente(ID, Designacao) VALUES (2,'Tipo 2');
DBMS_OUTPUT.PUT_LINE('Tipo Clientes');
SELECT Count(*) INTO nr_tipo_cliente from Tipo_Cliente;
DBMS_OUTPUT.PUT_LINE('Expected: 2   Actual: '||nr_tipo_cliente);
DBMS_OUTPUT.PUT_LINE(chr(0));

insert into Cliente_DIM(Codigo_Unico, Nome, Morada_Correspondencia_ID, Tipo_Cliente_ID, Morada_Entrega_ID, Numero_Fiscal, Email, Plafond, Numero_Total_Encomendas, Valor_Total_Encomendas) VALUES (1,'Cliente 1', 1, 1, 1, 123456789, 'c1@gmail.com', 1000, 1, 100);
insert into Cliente_DIM(Codigo_Unico, Nome, Morada_Correspondencia_ID, Tipo_Cliente_ID, Morada_Entrega_ID, Numero_Fiscal, Email, Plafond, Numero_Total_Encomendas, Valor_Total_Encomendas) VALUES (2,'Cliente 2', 2, 2, 2, 123456711, 'c2@gmail.com', 2000, 2, 200);
insert into Cliente_DIM(Codigo_Unico, Nome, Morada_Correspondencia_ID, Tipo_Cliente_ID, Morada_Entrega_ID, Numero_Fiscal, Email, Plafond, Numero_Total_Encomendas, Valor_Total_Encomendas) VALUES (3,'Cliente 3', 3, 1, 3, 123456333, 'c3@gmail.com', 3000, 3, 300);
insert into Cliente_DIM(Codigo_Unico, Nome, Morada_Correspondencia_ID, Tipo_Cliente_ID, Morada_Entrega_ID, Numero_Fiscal, Email, Plafond, Numero_Total_Encomendas, Valor_Total_Encomendas) VALUES (4,'Cliente 4', 4, 2, 4, 123454444, 'c4@gmail.com', 4000, 4, 400);
DBMS_OUTPUT.PUT_LINE('Clientes');
SELECT Count(*) INTO nr_cliente_dim from Cliente_DIM;
DBMS_OUTPUT.PUT_LINE('Expected: 4   Actual: '||nr_cliente_dim);
DBMS_OUTPUT.PUT_LINE(chr(0));

-------------------------------------------------------------
--US216
--acceptance criteria 3

insert into Tipo_Hub(ID, Designacao) VALUES (1,'E');
insert into Tipo_Hub(ID, Designacao) VALUES (2,'P');
DBMS_OUTPUT.PUT_LINE('Tipo Hubs');
SELECT Count(*) INTO nr_tipo_hub from Tipo_Hub;
DBMS_OUTPUT.PUT_LINE('Expected: 2   Actual: '||nr_tipo_hub);
DBMS_OUTPUT.PUT_LINE(chr(0));

insert into Hub (Location_ID, Latitude, Longitude) VALUES ('CT14',38.5243,-8.8926);
insert into Hub (Location_ID, Latitude, Longitude) VALUES ('CT11',39.3167,-7.4167);
insert into Hub (Location_ID, Latitude, Longitude) VALUES ('CT10',39.7444,-8.8072);
DBMS_OUTPUT.PUT_LINE('Hubs');
SELECT Count(*) INTO nr_hub from Hub;
DBMS_OUTPUT.PUT_LINE('Expected: 3   Actual: '||nr_hub);
DBMS_OUTPUT.PUT_LINE(chr(0));

insert into Local_Recolha_DIM(ID, Hub_ID, Tipo_Hub_ID) VALUES (1, 'CT14', 1);
insert into Local_Recolha_DIM(ID, Hub_ID, Tipo_Hub_ID) VALUES (2, 'CT11', 1);
insert into Local_Recolha_DIM(ID, Hub_ID, Tipo_Hub_ID) VALUES (3, 'CT10', 2);
DBMS_OUTPUT.PUT_LINE('Local Recolha');
SELECT Count(*) INTO nr_local_recolha from Local_Recolha_DIM;
DBMS_OUTPUT.PUT_LINE('Expected: 3   Actual: '||nr_local_recolha);
DBMS_OUTPUT.PUT_LINE(chr(0));

-------------------------------------------------------------

insert into Producao_FAC(Produto_ID, Setor_ID, Tempo_ID, Quantidade) VALUES (1,1,1,100);
insert into Producao_FAC(Produto_ID, Setor_ID, Tempo_ID, Quantidade) VALUES (2,2,2,200);
insert into Producao_FAC(Produto_ID, Setor_ID, Tempo_ID, Quantidade) VALUES (3,3,3,300);
insert into Producao_FAC(Produto_ID, Setor_ID, Tempo_ID, Quantidade) VALUES (4,4,4,400);
insert into Producao_FAC(Produto_ID, Setor_ID, Tempo_ID, Quantidade) VALUES (2,4,5,500);
insert into Producao_FAC(Produto_ID, Setor_ID, Tempo_ID, Quantidade) VALUES (3,3,6,600);
insert into Producao_FAC(Produto_ID, Setor_ID, Tempo_ID, Quantidade) VALUES (1,2,7,700);
insert into Producao_FAC(Produto_ID, Setor_ID, Tempo_ID, Quantidade) VALUES (4,1,8,800);
insert into Producao_FAC(Produto_ID, Setor_ID, Tempo_ID, Quantidade) VALUES (3,4,9,900);
insert into Producao_FAC(Produto_ID, Setor_ID, Tempo_ID, Quantidade) VALUES (2,3,10,1000);
insert into Producao_FAC(Produto_ID, Setor_ID, Tempo_ID, Quantidade) VALUES (1,2,11,1100);
insert into Producao_FAC(Produto_ID, Setor_ID, Tempo_ID, Quantidade) VALUES (4,1,12,1200);
DBMS_OUTPUT.PUT_LINE('Producao_FAC');
SELECT Count(*) INTO nr_producao_fac from Producao_FAC;
DBMS_OUTPUT.PUT_LINE('Expected: 12   Actual: '||nr_producao_fac);
DBMS_OUTPUT.PUT_LINE(chr(0));

insert into Venda_FAC(Produto_ID, Cliente_ID, Tempo_ID, Local_Recolha_ID, Quantidade, Preco) VALUES (1,1,1,1,100,100);
insert into Venda_FAC(Produto_ID, Cliente_ID, Tempo_ID, Local_Recolha_ID, Quantidade, Preco) VALUES (2,2,2,1,200,200);
insert into Venda_FAC(Produto_ID, Cliente_ID, Tempo_ID, Local_Recolha_ID, Quantidade, Preco) VALUES (3,3,3,2,300,300);
insert into Venda_FAC(Produto_ID, Cliente_ID, Tempo_ID, Local_Recolha_ID, Quantidade, Preco) VALUES (4,4,4,3,400,400);
insert into Venda_FAC(Produto_ID, Cliente_ID, Tempo_ID, Local_Recolha_ID, Quantidade, Preco) VALUES (2,4,5,2,500,500);
insert into Venda_FAC(Produto_ID, Cliente_ID, Tempo_ID, Local_Recolha_ID, Quantidade, Preco) VALUES (3,3,6,3,600,600);
insert into Venda_FAC(Produto_ID, Cliente_ID, Tempo_ID, Local_Recolha_ID, Quantidade, Preco) VALUES (1,2,7,2,700,700);
insert into Venda_FAC(Produto_ID, Cliente_ID, Tempo_ID, Local_Recolha_ID, Quantidade, Preco) VALUES (4,1,8,2,800,800);
insert into Venda_FAC(Produto_ID, Cliente_ID, Tempo_ID, Local_Recolha_ID, Quantidade, Preco) VALUES (3,4,9,3,900,900);
insert into Venda_FAC(Produto_ID, Cliente_ID, Tempo_ID, Local_Recolha_ID, Quantidade, Preco) VALUES (2,3,10,1,1000,1000);
insert into Venda_FAC(Produto_ID, Cliente_ID, Tempo_ID, Local_Recolha_ID, Quantidade, Preco) VALUES (1,2,11,1,1100,1100);
insert into Venda_FAC(Produto_ID, Cliente_ID, Tempo_ID, Local_Recolha_ID, Quantidade, Preco) VALUES (4,1,12,2,1200,1200);
DBMS_OUTPUT.PUT_LINE('Venda_FAC');
SELECT Count(*) INTO nr_venda_fac from Venda_FAC;
DBMS_OUTPUT.PUT_LINE('Expected: 12   Actual: '||nr_venda_fac);
DBMS_OUTPUT.PUT_LINE(chr(0));

END;
/