declare
nr_campos INTEGER;
nr_esp_vegetais INTEGER;
nr_tipo_cultura INTEGER;
nr_cultura INTEGER;
nr_plantacao INTEGER;
nr_localidade INTEGER;
nr_pais INTEGER;
nr_morada INTEGER;
nr_tipo_cliente INTEGER;
nr_cliente INTEGER;
nr_metodo_distribuicao INTEGER;
nr_accao INTEGER;
nr_Tipo_Fator_Producao INTEGER;
nr_categoria INTEGER;
nr_substancia INTEGER;
nr_ficha_tecnica INTEGER;
nr_ficha_tecnica_substancia INTEGER;
nr_produto INTEGER;
nr_rega INTEGER;
nr_colheita INTEGER;
nr_Aplicacao_Fator_Producao INTEGER;
nr_encomenda INTEGER;
nr_encomenda_Produto INTEGER;
nr_incidentes INTEGER;
nr_tipo_sensores INTEGER;
nr_sensores INTEGER;
nr_Leituras INTEGER;
nr_input_hub INTEGER;

BEGIN   
DBMS_OUTPUT.PUT_LINE(chr(0));

insert into campo(ID, designacao, Hectares) values (1,'setor 1',5);
insert into campo(ID, designacao, Hectares) values (2,'setor 2',1);
insert into campo(ID, designacao, Hectares) values (3,'setor 3',5);
insert into campo(ID, designacao, Hectares) values (4,'setor 4',5);
insert into campo(ID, designacao, Hectares) values (5,'setor 5',2);
SELECT Count(*) INTO nr_campos from campo;
DBMS_OUTPUT.PUT_LINE('Campos');
DBMS_OUTPUT.PUT_LINE('Expected: 5   Actual: '||nr_campos);
DBMS_OUTPUT.PUT_LINE(chr(0));

insert into Produto (ID, NOME) VALUES (1,'Maçãs');
insert into Produto (ID, NOME) VALUES (2,'Azeitonas');
insert into Produto (ID, NOME) VALUES (3,'Laranjas');
insert into Produto (ID, NOME) VALUES (4,'Tomates');
insert into Produto (ID, NOME) VALUES (5,'Peras');
insert into Produto (ID, NOME) VALUES (6,'Kiwi');
SELECT Count(*) INTO nr_esp_vegetais from Produto;
DBMS_OUTPUT.PUT_LINE('Produtos');
DBMS_OUTPUT.PUT_LINE('Expected: 6   Actual: '||nr_esp_vegetais);
DBMS_OUTPUT.PUT_LINE(chr(0));


insert into Cultura(ID, Produto_ID) values (1,1);
insert into Cultura(ID, Produto_ID) values (2,2);
insert into Cultura(ID, Produto_ID) values (3,3);
insert into Cultura(ID, Produto_ID) values (4,4);
SELECT Count(*) INTO nr_cultura from CULTURA;
DBMS_OUTPUT.PUT_LINE('Culturas');
DBMS_OUTPUT.PUT_LINE('Expected: 4   Actual: '||nr_cultura);
DBMS_OUTPUT.PUT_LINE(chr(0));

INSERT INTO plantacao (ID, campo_id, cultura_id, Data_Inicio_Cultura) values (1,1,2,sysdate);
INSERT INTO plantacao (ID, campo_id, cultura_id, Data_Inicio_Cultura) values (2,2,4,sysdate);
INSERT INTO plantacao (ID, campo_id, cultura_id, Data_Inicio_Cultura) values (3,1,3,sysdate);
INSERT INTO plantacao (ID, campo_id, cultura_id, Data_Inicio_Cultura) values (4,5,1,sysdate);
INSERT INTO plantacao (ID, campo_id, cultura_id, Data_Inicio_Cultura) values (5,3,3,sysdate);
SELECT Count(*) INTO nr_plantacao from plantacao;
DBMS_OUTPUT.PUT_LINE('Plantações');
DBMS_OUTPUT.PUT_LINE('Expected: 5   Actual: '||nr_plantacao);
DBMS_OUTPUT.PUT_LINE(chr(0));

insert into Localidade(ID, NOME) VALUES (1,'Porto');
insert into Localidade(ID, NOME) VALUES (2,'Braga');
insert into Localidade(ID, NOME) VALUES (3,'Aveiro');
insert into Localidade(ID, NOME) VALUES (4,'Madrid');
SELECT Count(*) INTO nr_localidade from Localidade;
DBMS_OUTPUT.PUT_LINE('localidades');
DBMS_OUTPUT.PUT_LINE('Expected: 4   Actual: '||nr_localidade);
DBMS_OUTPUT.PUT_LINE(chr(0));

insert into Pais(ID, NOME) VALUES(1,'Portugal');
insert into Pais(ID, NOME) VALUES(2,'Espanha');
SELECT Count(*) INTO nr_pais from pais;
DBMS_OUTPUT.PUT_LINE('Países');
DBMS_OUTPUT.PUT_LINE('Expected: 2   Actual: '||nr_pais);
DBMS_OUTPUT.PUT_LINE(chr(0));

insert into Morada(id, localidade_id,pais_id,nome_rua,numero_porta,codigo_postal) values(1,1,1,'Rua da Cal',82,'4510-507');
insert into Morada(id, localidade_id,pais_id,nome_rua,numero_porta,codigo_postal) values(2,2,1,'Rua do Alvaro',151,'4500-607');
insert into Morada(id, localidade_id,pais_id,nome_rua,numero_porta,codigo_postal) values(3,3,1,'Rua dos Descobrimentos',689,'4102-100');
insert into Morada(id, localidade_id,pais_id,nome_rua,numero_porta,codigo_postal) values(4,4,2,'Atraco a mercadona',101,'4443-207');
SELECT Count(*) INTO nr_morada from morada;
DBMS_OUTPUT.PUT_LINE('Moradas');
DBMS_OUTPUT.PUT_LINE('Expected: 4   Actual: '||nr_morada);
DBMS_OUTPUT.PUT_LINE(chr(0));


insert into tipo_cliente(ID, DESIGNACAO) VALUES (1,'Empresa');
insert into tipo_cliente(ID, DESIGNACAO) VALUES (2,'Particular');
insert into tipo_cliente(ID, DESIGNACAO) VALUES (3,'Consumidor final');
SELECT Count(*) INTO nr_tipo_cliente from Tipo_Cliente;
DBMS_OUTPUT.PUT_LINE('Tipos de cliente');
DBMS_OUTPUT.PUT_LINE('Expected: 3   Actual: '||nr_tipo_cliente);
DBMS_OUTPUT.PUT_LINE(chr(0));

insert into input_hub (input_string) values ('CT1;40.6389;-8.6553;C1');
insert into input_hub (input_string) values ('CT2;38.0333;-7.8833;C2');
insert into input_hub (input_string) values ('CT14;38.5243;-8.8926;E1');
insert into input_hub (input_string) values ('CT11;39.3167;-7.4167;E2');
insert into input_hub (input_string) values ('CT10;39.7444;-8.8072;P3');
SELECT Count(*) INTO nr_input_hub from input_hub;
DBMS_OUTPUT.PUT_LINE('Input Hub');
DBMS_OUTPUT.PUT_LINE('Expected: 5   Actual: '||nr_input_hub);
DBMS_OUTPUT.PUT_LINE(chr(0));   

insert into cliente(codigo_unico,tipo_cliente_id,morada_correspondencia_id,nome,numero_fiscal,email,plafond,numero_total_encomendas,valor_total_encomendas) values(111,1,1,'Joaquim',123456789,'j@gmail.com',100000,0,0);
insert into cliente(codigo_unico,tipo_cliente_id,morada_correspondencia_id,nome,numero_fiscal,email,plafond,hub_location_id,numero_total_encomendas,valor_total_encomendas) values(123,2,2,'Joel',123336789,'joel@gmail.com',300000,'CT14',1,10000);
insert into cliente(codigo_unico,tipo_cliente_id,morada_correspondencia_id,nome,numero_fiscal,email,plafond,hub_location_id,numero_total_encomendas,valor_total_encomendas) values(321,3,3,'Ricardo',113456789,'ric@gmail.com',10000,'CT10',2,5000);
insert into cliente(codigo_unico,tipo_cliente_id,morada_correspondencia_id,nome,numero_fiscal,email,plafond,hub_location_id,numero_total_encomendas,valor_total_encomendas) values(413,2,4,'Tiago',444459999,'tiago@gmail.com',103000,'CT11',0,0);
SELECT Count(*) INTO nr_cliente from Cliente;
DBMS_OUTPUT.PUT_LINE('Clientes');
DBMS_OUTPUT.PUT_LINE('Expected: 4   Actual: '||nr_cliente);
DBMS_OUTPUT.PUT_LINE(chr(0));

insert into Metodo_distribuicao(ID, DESIGNACAO) VALUES (1, 'via foliar');
insert into Metodo_distribuicao(ID, DESIGNACAO) VALUES (2, 'aplicacao direta');
SELECT Count(*) INTO nr_metodo_distribuicao from Metodo_distribuicao;
DBMS_OUTPUT.PUT_LINE('Métodos de distribuição');
DBMS_OUTPUT.PUT_LINE('Expected: 2   Actual: '||nr_metodo_distribuicao);
DBMS_OUTPUT.PUT_LINE(chr(0));

insert into Accao (ID,  Plantacao_ID,DATA_ACCAO, QUANTIDADE) VALUES (1,2,TO_DATE('05/08/2022', 'DD/MM/YYYY'), 23.11);
insert into Accao (ID,  Plantacao_ID,DATA_ACCAO, QUANTIDADE) VALUES (2,4,TO_DATE('17/10/2019', 'DD/MM/YYYY'), 23.11);
insert into Accao (ID,  Plantacao_ID,DATA_ACCAO, QUANTIDADE) VALUES (3,3,TO_DATE('10/12/2021', 'DD/MM/YYYY'), 23.11);
insert into Accao (ID,  Plantacao_ID,DATA_ACCAO, QUANTIDADE) VALUES (4,1,SYSDATE, 23.11);
insert into Accao (ID,  Plantacao_ID,DATA_ACCAO, QUANTIDADE) VALUES (5,3,TO_DATE('10/10/2021', 'DD/MM/YYYY'), 23.11);
insert into Accao (ID,  Plantacao_ID,DATA_ACCAO, QUANTIDADE) VALUES (6,2,TO_DATE('21/07/2017', 'DD/MM/YYYY'), 23.11);
insert into Accao (ID,  Plantacao_ID,DATA_ACCAO, QUANTIDADE) VALUES (7,1,TO_DATE('22/06/2015', 'DD/MM/YYYY'), 23.11);
insert into Accao (ID,  Plantacao_ID,DATA_ACCAO, QUANTIDADE) VALUES (8,1,sysdate, 23.11);
insert into Accao (ID,  Plantacao_ID,DATA_ACCAO, QUANTIDADE) VALUES (9,4,TO_DATE('10/11/2022', 'DD/MM/YYYY'), 23.11);
SELECT Count(*) INTO nr_accao from accao;
DBMS_OUTPUT.PUT_LINE('Ações');
DBMS_OUTPUT.PUT_LINE('Expected: 9   Actual: '||nr_accao);
DBMS_OUTPUT.PUT_LINE(chr(0));

insert into Tipo_Fator_Producao(ID, DESIGNACAO) VALUES(1,'Fertilizante');
insert into Tipo_Fator_Producao(ID, DESIGNACAO) VALUES(2,'Adubo');
insert into Tipo_Fator_Producao(ID, DESIGNACAO) VALUES(3,'Correctivo');
insert into Tipo_Fator_Producao(ID, DESIGNACAO) VALUES(4,'Produto fitofarmaco');
SELECT Count(*) INTO nr_Tipo_Fator_Producao from Tipo_Fator_Producao;
DBMS_OUTPUT.PUT_LINE('Tipos de fatores de produção');
DBMS_OUTPUT.PUT_LINE('Expected: 4   Actual: '||nr_Tipo_Fator_Producao);
DBMS_OUTPUT.PUT_LINE(chr(0));

insert into Categoria(ID, DESIGNACAO) VALUES (1, 'Substancia organica');
insert into Categoria(ID, DESIGNACAO) VALUES (2, 'Elemento nutritivo');
SELECT Count(*) INTO nr_categoria from Categoria;
DBMS_OUTPUT.PUT_LINE('Categorias');
DBMS_OUTPUT.PUT_LINE('Expected: 2   Actual: '||nr_cultura);
DBMS_OUTPUT.PUT_LINE(chr(0));

insert into substancia(id, Categoria_id, nome, unidade) values (1,1, 'azoto', 'mL');
insert into substancia(id, Categoria_id, nome, unidade) values (2,1, 'agua', 'mL');
insert into substancia(id, Categoria_id, nome, unidade) values (3,2, 'oxido de calcio', 'mL');
insert into substancia(id, Categoria_id, nome, unidade) values (4,2, 'magnésio', 'mL');
insert into substancia(id, Categoria_id, nome, unidade) values (5,1, 'potassio', 'mL');
SELECT Count(*) INTO nr_substancia from substancia;
DBMS_OUTPUT.PUT_LINE('Substâncias');
DBMS_OUTPUT.PUT_LINE('Expected: 5   Actual: '||nr_substancia);
DBMS_OUTPUT.PUT_LINE(chr(0));

insert into Ficha_Tecnica(ID, FORNECEDOR, HUMIDADE, PH, PESO_ESPECIFICO, FORMULACAO_PELETIZADA) values(1,'Advanced Nutrients',7,6.5,0.8,3);
insert into Ficha_Tecnica(ID, FORNECEDOR, HUMIDADE, PH, PESO_ESPECIFICO, FORMULACAO_PELETIZADA) values(2,'Siro',4,3.2,0.5,5);
insert into Ficha_Tecnica(ID, FORNECEDOR, HUMIDADE, PH, PESO_ESPECIFICO, FORMULACAO_PELETIZADA) values(3,'Valpec',4,8.3,0.8,2);
insert into Ficha_Tecnica(ID, FORNECEDOR, HUMIDADE, PH, PESO_ESPECIFICO, FORMULACAO_PELETIZADA) values(4,'KB Fungicida',2,5.1,0.1,6);
SELECT Count(*) INTO nr_ficha_tecnica from Ficha_Tecnica;
DBMS_OUTPUT.PUT_LINE('Ficha técnica');
DBMS_OUTPUT.PUT_LINE('Expected: 4   Actual: '||nr_ficha_tecnica);
DBMS_OUTPUT.PUT_LINE(chr(0));

insert into ficha_tecnica_substancia(ficha_tecnica_id, substancia_id, QuantidadePerc) values(1,1,10);
insert into ficha_tecnica_substancia(ficha_tecnica_id, substancia_id, QuantidadePerc) values(1,4,15);
insert into ficha_tecnica_substancia(ficha_tecnica_id, substancia_id, QuantidadePerc) values(1,2,50);
insert into ficha_tecnica_substancia(ficha_tecnica_id, substancia_id, QuantidadePerc) values(2,2,60);
insert into ficha_tecnica_substancia(ficha_tecnica_id, substancia_id, QuantidadePerc) values(3,5,5);
insert into ficha_tecnica_substancia(ficha_tecnica_id, substancia_id, QuantidadePerc) values(3,2,50);
insert into ficha_tecnica_substancia(ficha_tecnica_id, substancia_id, QuantidadePerc) values(4,4,18);
insert into ficha_tecnica_substancia(ficha_tecnica_id, substancia_id, QuantidadePerc) values(4,2,75);
SELECT Count(*) INTO nr_ficha_tecnica_substancia from Ficha_Tecnica_Substancia;
DBMS_OUTPUT.PUT_LINE('Ficha técnica - substância');
DBMS_OUTPUT.PUT_LINE('Expected: 8   Actual: '||nr_ficha_tecnica_substancia);
DBMS_OUTPUT.PUT_LINE(chr(0));

insert into Fator_Producao(ID, Tipo_Fator_Producao_ID ,FICHA_TECNICA_ID, NOME) VALUES(1,1,1,'Grow');
insert into Fator_Producao(ID, Tipo_Fator_Producao_ID ,FICHA_TECNICA_ID, NOME) VALUES(2,2,2,'Mineral Azul');
insert into Fator_Producao(ID, Tipo_Fator_Producao_ID ,FICHA_TECNICA_ID, NOME) VALUES(3,3,3,'Humus');
insert into Fator_Producao(ID, Tipo_Fator_Producao_ID ,FICHA_TECNICA_ID, NOME) VALUES(4,4,4,'Al Pronto');
SELECT Count(*) INTO nr_produto from produto;
DBMS_OUTPUT.PUT_LINE('Fatores de produção');
DBMS_OUTPUT.PUT_LINE('Expected: 4   Actual: '||nr_produto);
DBMS_OUTPUT.PUT_LINE(chr(0));

insert into Rega(accao_id, metodo_distribuicao_ID) values(8, 1);
insert into Rega(accao_id, metodo_distribuicao_ID) values(9, 2);
SELECT Count(*) INTO nr_rega from rega;
DBMS_OUTPUT.PUT_LINE('Regas');
DBMS_OUTPUT.PUT_LINE('Expected: 2   Actual: '||nr_rega);
DBMS_OUTPUT.PUT_LINE(chr(0));

insert into colheita(accao_id, Produto_ID,preco) values (1,1,8000);
insert into colheita(accao_id, Produto_ID,preco) values (2,3,2000);
insert into colheita(accao_id, Produto_ID,preco) values (3,4,2000);
insert into colheita(accao_id, Produto_ID,preco) values (4,1,2000);
SELECT Count(*) INTO nr_colheita from colheita;
DBMS_OUTPUT.PUT_LINE('Colheitas');
DBMS_OUTPUT.PUT_LINE('Expected: 4   Actual: '||nr_colheita);
DBMS_OUTPUT.PUT_LINE(chr(0));

insert into Aplicacao_Fator_Producao(accao_id, fator_Producao_ID, metodo_distribuicao_id) values(5, 1, 1);
insert into Aplicacao_Fator_Producao(accao_id, fator_Producao_ID, metodo_distribuicao_id) values(6, 2, 2);
insert into Aplicacao_Fator_Producao(accao_id, fator_Producao_ID, metodo_distribuicao_id) values(7, 3, 1);
SELECT Count(*) INTO nr_Aplicacao_Fator_Producao from Aplicacao_Fator_Producao;
DBMS_OUTPUT.PUT_LINE('Aplicação de fatores de produção');
DBMS_OUTPUT.PUT_LINE('Expected: 3   Actual: '||nr_Aplicacao_Fator_Producao);
DBMS_OUTPUT.PUT_LINE(chr(0));

insert into encomenda(id,cliente_codigo_unico, hub_location_id, data_encomenda, data_limite_pagamento, data_pagamento) values (1,123,'CT11',sysdate,TO_DATE('17/12/2022', 'DD/MM/YYYY'),sysdate);
insert into encomenda(id,cliente_codigo_unico, hub_location_id, data_encomenda, data_limite_pagamento, data_pagamento) values (2,321,'CT14',TO_DATE('01/12/2022', 'DD/MM/YYYY'),TO_DATE('10/12/2022', 'DD/MM/YYYY'),sysdate);
insert into encomenda(id,cliente_codigo_unico, hub_location_id, data_encomenda, data_limite_pagamento, data_pagamento) values (3,321,'CT10',sysdate,TO_DATE('10/12/2022','DD/MM/YYYY'),sysdate);
SELECT Count(*) INTO nr_encomenda from encomenda;
DBMS_OUTPUT.PUT_LINE('Encomendas');
DBMS_OUTPUT.PUT_LINE('Expected: 3   Actual: '||nr_encomenda);
DBMS_OUTPUT.PUT_LINE(chr(0));

insert into Incidente(encomenda_id, Data_Incumprimento) VALUES(1, sysdate);
insert into Incidente(encomenda_id, Data_Incumprimento) VALUES(2, sysdate);
SELECT Count(*) INTO nr_incidentes from Incidente;
DBMS_OUTPUT.PUT_LINE('Incidente');
DBMS_OUTPUT.PUT_LINE('Expected: 2   Actual: '||nr_incidentes);
DBMS_OUTPUT.PUT_LINE(chr(0));

insert into encomenda_Produto(encomenda_id,Produto_id,quantidade,preco) values(1,1,10000,10000);
insert into encomenda_Produto(encomenda_id,Produto_id,quantidade,preco) values(2,3,750,2500);
insert into encomenda_Produto(encomenda_id,Produto_id,quantidade,preco) values(3,4,1000,2500);
SELECT Count(*) INTO nr_encomenda_Produto from encomenda_Produto;
DBMS_OUTPUT.PUT_LINE('Encomenda de um produto');
DBMS_OUTPUT.PUT_LINE('Expected: 3   Actual: '||nr_encomenda_Produto);
DBMS_OUTPUT.PUT_LINE(chr(0));

insert into Tipo_Sensor(ID, Designacao) VALUES(1, 'Humidade');
insert into Tipo_Sensor(ID, Designacao) VALUES(2, 'Temperatura');
insert into Tipo_Sensor(ID, Designacao) VALUES(3, 'Pressão');
insert into Tipo_Sensor(ID, Designacao) VALUES(4, 'Vento');
SELECT Count(*) INTO nr_tipo_sensores from Tipo_Sensor;
DBMS_OUTPUT.PUT_LINE('Tipos de sensores');
DBMS_OUTPUT.PUT_LINE('Expected: 4   Actual: '||nr_tipo_sensores);
DBMS_OUTPUT.PUT_LINE(chr(0));

insert into Sensor(ID, Tipo_Sensor_ID) VALUES(1, 2);
insert into Sensor(ID, Tipo_Sensor_ID) VALUES(2, 3);
insert into Sensor(ID, Tipo_Sensor_ID) VALUES(3, 4);
insert into Sensor(ID, Tipo_Sensor_ID) VALUES(4, 1);
insert into Sensor(ID, Tipo_Sensor_ID) VALUES(5, 1);
SELECT Count(*) INTO nr_sensores from Sensor;
DBMS_OUTPUT.PUT_LINE('Sensores');
DBMS_OUTPUT.PUT_LINE('Expected: 5   Actual: '||nr_sensores);
DBMS_OUTPUT.PUT_LINE(chr(0));

insert into Leitura(Plantacao_ID , Data_Leitura, Sensor_ID, Valor) VALUES(1,sysdate, 3, 10);
insert into Leitura(Plantacao_ID , Data_Leitura, Sensor_ID, Valor) VALUES(5,sysdate, 2, 1);
insert into Leitura(Plantacao_ID , Data_Leitura, Sensor_ID, Valor) VALUES(3,sysdate, 4, 33);
SELECT Count(*) INTO nr_Leituras from Leitura;
DBMS_OUTPUT.PUT_LINE('Leituras dos sensores');
DBMS_OUTPUT.PUT_LINE('Expected: 3   Actual: '||nr_Leituras);
DBMS_OUTPUT.PUT_LINE(chr(0));

INSERT INTO Restricao VALUES (1, 1, TO_DATE('23/11/2022', 'DD/MM/YYYY'), SYSDATE);
INSERT INTO Restricao VALUES (1, 3, TO_DATE('04/12/2021', 'DD/MM/YYYY'), TO_DATE('16/08/2022', 'DD/MM/YYYY'));
INSERT INTO Restricao VALUES (1, 4, TO_DATE('16/02/2021', 'DD/MM/YYYY'), TO_DATE('18/02/2022', 'DD/MM/YYYY'));
INSERT INTO Restricao VALUES (2, 1, TO_DATE('12/04/2023', 'DD/MM/YYYY'), TO_DATE('01/09/2023', 'DD/MM/YYYY'));
INSERT INTO Restricao VALUES (2, 2, TO_DATE('07/02/2020', 'DD/MM/YYYY'), TO_DATE('16/08/2020', 'DD/MM/YYYY'));
INSERT INTO Restricao VALUES (3, 2, TO_DATE('16/07/2023', 'DD/MM/YYYY'), TO_DATE('25/12/2023', 'DD/MM/YYYY'));
INSERT INTO Restricao VALUES (3, 3, TO_DATE('29/12/2021', 'DD/MM/YYYY'), TO_DATE('01/01/2022', 'DD/MM/YYYY'));
INSERT INTO Restricao VALUES (3, 4, TO_DATE('08/05/2022', 'DD/MM/YYYY'), TO_DATE('06/06/2022', 'DD/MM/YYYY'));

END;
/