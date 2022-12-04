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
nr_tipo_produto INTEGER;
nr_categoria INTEGER;
nr_substancia INTEGER;
nr_ficha_tecnica INTEGER;
nr_ficha_tecnica_substancia INTEGER;
nr_produto INTEGER;
nr_rega INTEGER;
nr_colheita INTEGER;
nr_aplicacao_produto INTEGER;
nr_encomenda INTEGER;
nr_encomenda_especie_vegetal INTEGER;
nr_incidentes INTEGER;
nr_tipo_sensores INTEGER;
nr_sensores INTEGER;
nr_Leituras INTEGER;

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

insert into especie_vegetal (ID, NOME) VALUES (1,'Maçãs');
insert into especie_vegetal (ID, NOME) VALUES (2,'Azeitonas');
insert into especie_vegetal (ID, NOME) VALUES (3,'Laranjas');
insert into especie_vegetal (ID, NOME) VALUES (4,'Tomates');
insert into especie_vegetal (ID, NOME) VALUES (5,'Peras');
insert into especie_vegetal (ID, NOME) VALUES (6,'Kiwi');
SELECT Count(*) INTO nr_esp_vegetais from especie_vegetal;
DBMS_OUTPUT.PUT_LINE('Especies vegetais');
DBMS_OUTPUT.PUT_LINE('Expected: 6   Actual: '||nr_esp_vegetais);


insert into tipo_cultura(ID, DESIGNACAO) values (1,'Moderna');
insert into tipo_cultura(ID, DESIGNACAO) values (2,'Intensiva');
insert into tipo_cultura(ID, DESIGNACAO) values (3,'patronal');
SELECT Count(*) INTO nr_tipo_cultura from tipo_cultura;
DBMS_OUTPUT.PUT_LINE('Tipos de cultura');
DBMS_OUTPUT.PUT_LINE('Expected: 3   Actual: '||nr_tipo_cultura);

insert into Cultura(ID, especie_vegetal_ID, tipo_cultura_ID) values (1,1,1);
insert into Cultura(ID, especie_vegetal_ID, tipo_cultura_ID) values (2,2,2);
insert into Cultura(ID, especie_vegetal_ID, tipo_cultura_ID) values (3,3,3);
insert into Cultura(ID, especie_vegetal_ID, tipo_cultura_ID) values (4,4,1);
SELECT Count(*) INTO nr_cultura from CULTURA;
DBMS_OUTPUT.PUT_LINE('Culturas');
DBMS_OUTPUT.PUT_LINE('Expected: 4   Actual: '||nr_cultura);


INSERT INTO plantacao (campo_id, cultura_id,data_inicio_cultura) values (1,2,sysdate);
INSERT INTO plantacao (campo_id, cultura_id,data_inicio_cultura) values (2,4,sysdate);
INSERT INTO plantacao (campo_id, cultura_id,data_inicio_cultura) values (1,3,sysdate);
INSERT INTO plantacao (campo_id, cultura_id,data_inicio_cultura) values (5,1,sysdate);
INSERT INTO plantacao (campo_id, cultura_id,data_inicio_cultura) values (3,3,sysdate);
SELECT Count(*) INTO nr_plantacao from plantacao;
DBMS_OUTPUT.PUT_LINE('Plantações');
DBMS_OUTPUT.PUT_LINE('Expected: 5   Actual: '||nr_plantacao);


insert into Localidade(ID, NOME) VALUES (1,'Porto');
insert into Localidade(ID, NOME) VALUES (2,'Braga');
insert into Localidade(ID, NOME) VALUES (3,'Aveiro');
insert into Localidade(ID, NOME) VALUES (4,'Madrid');
SELECT Count(*) INTO nr_localidade from Localidade;
DBMS_OUTPUT.PUT_LINE('localidades');
DBMS_OUTPUT.PUT_LINE('Expected: 4   Actual: '||nr_localidade);

insert into Pais(ID, NOME) VALUES(1,'Portugal');
insert into Pais(ID, NOME) VALUES(2,'Espanha');
SELECT Count(*) INTO nr_pais from pais;
DBMS_OUTPUT.PUT_LINE('Países');
DBMS_OUTPUT.PUT_LINE('Expected: 2   Actual: '||nr_pais);

insert into Morada(id, localidade_id,pais_id,nome_rua,numero_porta,codigo_postal) values(1,1,1,'Rua da Cal',82,'4510-507');
insert into Morada(id, localidade_id,pais_id,nome_rua,numero_porta,codigo_postal) values(2,2,1,'Rua do Alvaro',151,'4500-607');
insert into Morada(id, localidade_id,pais_id,nome_rua,numero_porta,codigo_postal) values(3,3,1,'Rua dos Descobrimentos',689,'4102-100');
insert into Morada(id, localidade_id,pais_id,nome_rua,numero_porta,codigo_postal) values(4,4,2,'Atraco a mercadona',101,'4443-207');
SELECT Count(*) INTO nr_morada from morada;
DBMS_OUTPUT.PUT_LINE('Moradas');
DBMS_OUTPUT.PUT_LINE('Expected: 4   Actual: '||nr_morada);


insert into tipo_cliente(ID, DESIGNACAO) VALUES (1,'Empresa');
insert into tipo_cliente(ID, DESIGNACAO) VALUES (2,'Particular');
insert into tipo_cliente(ID, DESIGNACAO) VALUES (3,'Consumidor final');
SELECT Count(*) INTO nr_tipo_cliente from Tipo_Cliente;
DBMS_OUTPUT.PUT_LINE('Tipos de cliente');
DBMS_OUTPUT.PUT_LINE('Expected: 3   Actual: '||nr_tipo_cliente);

insert into cliente(codigo_unico,tipo_cliente_id,morada_correspondencia_id,morada_entrega_id,nome,numero_fiscal,email,plafond,numero_total_encomendas,valor_total_encomendas) values(111,1,1,1,'Joaquim',123456789,'j@gmail.com',100000,0,0);
insert into cliente(codigo_unico,tipo_cliente_id,morada_correspondencia_id,morada_entrega_id,nome,numero_fiscal,email,plafond,numero_total_encomendas,valor_total_encomendas) values(123,2,2,2,'Joel',123336789,'joel@gmail.com',300000,1,10000);
insert into cliente(codigo_unico,tipo_cliente_id,morada_correspondencia_id,morada_entrega_id,nome,numero_fiscal,email,plafond,numero_total_encomendas,valor_total_encomendas) values(321,3,3,3,'Ricardo',113456789,'ric@gmail.com',10000,2,5000);
insert into cliente(codigo_unico,tipo_cliente_id,morada_correspondencia_id,morada_entrega_id,nome,numero_fiscal,email,plafond,numero_total_encomendas,valor_total_encomendas) values(413,2,4,4,'Tiago',444459999,'tiago@gmail.com',103000,0,0);
SELECT Count(*) INTO nr_cliente from Cliente;
DBMS_OUTPUT.PUT_LINE('Clientes');
DBMS_OUTPUT.PUT_LINE('Expected: 4   Actual: '||nr_cliente);

insert into Metodo_distribuicao(ID, DESIGNACAO) VALUES (1, 'via foliar');
insert into Metodo_distribuicao(ID, DESIGNACAO) VALUES (2, 'aplicacao direta');
SELECT Count(*) INTO nr_metodo_distribuicao from Metodo_distribuicao;
DBMS_OUTPUT.PUT_LINE('Métodos de distribuição');
DBMS_OUTPUT.PUT_LINE('Expected: 2   Actual: '||nr_metodo_distribuicao);

insert into Accao (ID, CAMPO_ID, CULTURA_ID,DATA_ACCAO) VALUES (1,1,2,TO_DATE('05/08/2022', 'DD/MM/YYYY'));
insert into Accao (ID, CAMPO_ID, CULTURA_ID,DATA_ACCAO) VALUES (2,2,4,TO_DATE('17/10/2019', 'DD/MM/YYYY'));
insert into Accao (ID, CAMPO_ID, CULTURA_ID,DATA_ACCAO) VALUES (3,3,3,TO_DATE('10/12/2021', 'DD/MM/YYYY'));
insert into Accao (ID, CAMPO_ID, CULTURA_ID,DATA_ACCAO) VALUES (4,5,1,SYSDATE);
insert into Accao (ID, CAMPO_ID, CULTURA_ID,DATA_ACCAO) VALUES (5,3,3,TO_DATE('10/10/2021', 'DD/MM/YYYY'));
insert into Accao (ID, CAMPO_ID, CULTURA_ID,DATA_ACCAO) VALUES (6,1,2,TO_DATE('21/07/2017', 'DD/MM/YYYY'));
insert into Accao (ID, CAMPO_ID, CULTURA_ID,DATA_ACCAO) VALUES (7,5,1,TO_DATE('22/06/2015', 'DD/MM/YYYY'));
insert into Accao (ID, CAMPO_ID, CULTURA_ID,DATA_ACCAO) VALUES (8,5,1,sysdate);
insert into Accao (ID, CAMPO_ID, CULTURA_ID,DATA_ACCAO) VALUES (9,2,4,TO_DATE('10/11/2022', 'DD/MM/YYYY'));
SELECT Count(*) INTO nr_accao from accao;
DBMS_OUTPUT.PUT_LINE('Ações');
DBMS_OUTPUT.PUT_LINE('Expected: 9   Actual: '||nr_accao);


insert into Tipo_Produto(ID, DESIGNACAO) VALUES(1,'Fertilizante');
insert into Tipo_Produto(ID, DESIGNACAO) VALUES(2,'Adubo');
insert into Tipo_Produto(ID, DESIGNACAO) VALUES(3,'Correctivo');
insert into Tipo_Produto(ID, DESIGNACAO) VALUES(4,'Produto fitofarmaco');
SELECT Count(*) INTO nr_tipo_produto from Tipo_Produto;
DBMS_OUTPUT.PUT_LINE('Tipos de produto');
DBMS_OUTPUT.PUT_LINE('Expected: 4   Actual: '||nr_tipo_produto);

insert into Categoria(ID, DESIGNACAO) VALUES (1, 'Substancia organica');
insert into Categoria(ID, DESIGNACAO) VALUES (2, 'Elemento nutritivo');
SELECT Count(*) INTO nr_categoria from Categoria;
DBMS_OUTPUT.PUT_LINE('Categorias');
DBMS_OUTPUT.PUT_LINE('Expected: 2   Actual: '||nr_cultura);


insert into substancia(id, Categoria_id, nome, unidade) values (1,1, 'azoto', 'mL');
insert into substancia(id, Categoria_id, nome, unidade) values (2,1, 'agua', 'mL');
insert into substancia(id, Categoria_id, nome, unidade) values (3,2, 'oxido de calcio', 'mL');
insert into substancia(id, Categoria_id, nome, unidade) values (4,2, 'magnésio', 'mL');
insert into substancia(id, Categoria_id, nome, unidade) values (5,1, 'potassio', 'mL');
SELECT Count(*) INTO nr_substancia from substancia;
DBMS_OUTPUT.PUT_LINE('Substâncias');
DBMS_OUTPUT.PUT_LINE('Expected: 5   Actual: '||nr_substancia);

insert into Ficha_Tecnica(ID, TIPO_PRODUTO_ID,  FORNECEDOR, HUMIDADE, PH, PESO_ESPECIFICO, FORMULACAO_PELETIZADA) values(1,1,'Advanced Nutrients',7,6.5,0.8,3);
insert into Ficha_Tecnica(ID, TIPO_PRODUTO_ID,  FORNECEDOR, HUMIDADE, PH, PESO_ESPECIFICO, FORMULACAO_PELETIZADA) values(2,1,'Siro',4,3.2,0.5,5);
insert into Ficha_Tecnica(ID, TIPO_PRODUTO_ID,  FORNECEDOR, HUMIDADE, PH, PESO_ESPECIFICO, FORMULACAO_PELETIZADA) values(3,3,'Valpec',4,8.3,0.8,2);
insert into Ficha_Tecnica(ID, TIPO_PRODUTO_ID,  FORNECEDOR, HUMIDADE, PH, PESO_ESPECIFICO, FORMULACAO_PELETIZADA) values(4,4,'KB Fungicida',2,5.1,0.1,6);
SELECT Count(*) INTO nr_ficha_tecnica from Ficha_Tecnica;
DBMS_OUTPUT.PUT_LINE('Ficha técnica');
DBMS_OUTPUT.PUT_LINE('Expected: 4   Actual: '||nr_ficha_tecnica);

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

insert into Produto(ID, FICHA_TECNICA_ID, NOME) VALUES(1,1,'Grow');
insert into Produto(ID, FICHA_TECNICA_ID, NOME) VALUES(2,2,'Mineral Azul');
insert into Produto(ID, FICHA_TECNICA_ID, NOME) VALUES(3,3,'Humus');
insert into Produto(ID, FICHA_TECNICA_ID, NOME) VALUES(4,4,'Al Pronto');
SELECT Count(*) INTO nr_produto from produto;
DBMS_OUTPUT.PUT_LINE('Produtos');
DBMS_OUTPUT.PUT_LINE('Expected: 4   Actual: '||nr_produto);

insert into Rega(accao_id, metodo_distribuicao_ID, quantidade) values(8, 1, 100000);
insert into Rega(accao_id, metodo_distribuicao_ID, quantidade) values(9, 2, 550000);
SELECT Count(*) INTO nr_rega from rega;
DBMS_OUTPUT.PUT_LINE('Regas');
DBMS_OUTPUT.PUT_LINE('Expected: 2   Actual: '||nr_rega);

insert into colheita(accao_id, especie_vegetal_id,preco,QUANTIDADE) values (1,1,8000, 10000);
insert into colheita(accao_id, especie_vegetal_id,preco,QUANTIDADE) values (2,3,2000, 750);
insert into colheita(accao_id, especie_vegetal_id,preco,QUANTIDADE) values (3,4,2000, 1000);
insert into colheita(accao_id, especie_vegetal_id,preco,QUANTIDADE) values (4,1,2000, 2500);
SELECT Count(*) INTO nr_colheita from colheita;
DBMS_OUTPUT.PUT_LINE('Colheitas');
DBMS_OUTPUT.PUT_LINE('Expected: 4   Actual: '||nr_colheita);

insert into Aplicacao_Produto(accao_id, produto_id, metodo_distribuicao_id,quantidade) values(5, 1, 1, 10);
insert into Aplicacao_Produto(accao_id, produto_id, metodo_distribuicao_id,quantidade) values(6, 2, 2, 15);
insert into Aplicacao_Produto(accao_id, produto_id, metodo_distribuicao_id,quantidade) values(7, 3, 1, 20);
SELECT Count(*) INTO nr_aplicacao_produto from Aplicacao_Produto;
DBMS_OUTPUT.PUT_LINE('Aplicação de produto');
DBMS_OUTPUT.PUT_LINE('Expected: 3   Actual: '||nr_aplicacao_produto);

insert into encomenda(id,cliente_codigo_unico, data_encomenda, data_limite_pagamento, data_pagamento) values (1,123,sysdate,TO_DATE('17/12/2022', 'DD/MM/YYYY'),sysdate);
insert into encomenda(id,cliente_codigo_unico, data_encomenda, data_limite_pagamento, data_pagamento) values (2,321,TO_DATE('01/12/2022', 'DD/MM/YYYY'),TO_DATE('10/12/2022', 'DD/MM/YYYY'),sysdate);
insert into encomenda(id,cliente_codigo_unico, data_encomenda, data_limite_pagamento, data_pagamento) values (3,321,sysdate,TO_DATE('10/12/2022','DD/MM/YYYY'),sysdate);
SELECT Count(*) INTO nr_encomenda from encomenda;
DBMS_OUTPUT.PUT_LINE('Encomendas');
DBMS_OUTPUT.PUT_LINE('Expected: 3   Actual: '||nr_encomenda);

insert into Incidente(cliente_codigo_unico, encomenda_id, Data_Incumprimento) VALUES(111, 1, sysdate);
insert into Incidente(cliente_codigo_unico, encomenda_id, Data_Incumprimento) VALUES(123, 2, sysdate);
SELECT Count(*) INTO nr_incidentes from Incidente;
DBMS_OUTPUT.PUT_LINE('Incidente');
DBMS_OUTPUT.PUT_LINE('Expected: 2   Actual: '||nr_incidentes);


insert into encomenda_especie_vegetal(encomenda_id,especie_vegetal_id,quantidade,preco) values(1,1,10000,10000);
insert into encomenda_especie_vegetal(encomenda_id,especie_vegetal_id,quantidade,preco) values(2,3,750,2500);
insert into encomenda_especie_vegetal(encomenda_id,especie_vegetal_id,quantidade,preco) values(3,4,1000,2500);
SELECT Count(*) INTO nr_encomenda_especie_vegetal from encomenda_especie_vegetal;
DBMS_OUTPUT.PUT_LINE('Encomenda de uma especie vegetal');
DBMS_OUTPUT.PUT_LINE('Expected: 3   Actual: '||nr_encomenda_especie_vegetal);

insert into Tipo_Sensor(ID, Designacao) VALUES(1, 'Humidade');
insert into Tipo_Sensor(ID, Designacao) VALUES(2, 'Temperatura');
insert into Tipo_Sensor(ID, Designacao) VALUES(3, 'Pressão');
insert into Tipo_Sensor(ID, Designacao) VALUES(4, 'Vento');
SELECT Count(*) INTO nr_tipo_sensores from Tipo_Sensor;
DBMS_OUTPUT.PUT_LINE('Tipos de sensores');
DBMS_OUTPUT.PUT_LINE('Expected: 4   Actual: '||nr_tipo_sensores);


insert into Sensor(ID, Tipo_Sensor_ID) VALUES(1, 2);
insert into Sensor(ID, Tipo_Sensor_ID) VALUES(2, 3);
insert into Sensor(ID, Tipo_Sensor_ID) VALUES(3, 4);
insert into Sensor(ID, Tipo_Sensor_ID) VALUES(4, 1);
insert into Sensor(ID, Tipo_Sensor_ID) VALUES(5, 1);
SELECT Count(*) INTO nr_sensores from Sensor;
DBMS_OUTPUT.PUT_LINE('Sensores');
DBMS_OUTPUT.PUT_LINE('Expected: 5   Actual: '||nr_sensores);


insert into Leitura(campo_id, cultura_id, Data_Leitura, Sensor_ID, Valor) VALUES(1, 2,sysdate, 3, 10);
insert into Leitura(campo_id, cultura_id, Data_Leitura, Sensor_ID, Valor) VALUES(5, 1,sysdate, 2, 1);
insert into Leitura(campo_id, cultura_id, Data_Leitura, Sensor_ID, Valor) VALUES(3, 3,sysdate, 4, 33);
SELECT Count(*) INTO nr_Leituras from Leitura;
DBMS_OUTPUT.PUT_LINE('Leituras dos sensores');
DBMS_OUTPUT.PUT_LINE('Expected: 3   Actual: '||nr_Leituras);


END;
/