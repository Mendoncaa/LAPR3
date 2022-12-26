CREATE OR REPLACE FUNCTION Add_Localidade_Func(v_localidade Localidade.nome%TYPE) RETURN Localidade.id%TYPE
IS
nr_id INTEGER;
BEGIN
select nvl(max(ID), 0)+1 into nr_id from Localidade;
INSERT into Localidade (id, nome) VALUES(nr_id, v_localidade);
RETURN nr_id;
END;
/

CREATE OR REPLACE FUNCTION Add_Pais_Func(v_nome Pais.nome%type) RETURN Pais.ID%type
is 
nr_id INTEGER;
BEGIN
select nvl(max(ID), 0)+1 into nr_id from Pais;
INSERT into Pais (id, nome) VALUES(nr_id, v_nome);
RETURN nr_id;
end;
/


CREATE OR REPLACE FUNCTION Add_Morada_Func(v_id_localidade Morada.Localidade_ID%type, v_id_pais Morada.Pais_id%TYPE, v_nome_rua Morada.nome_rua%TYPE, v_numero_porta Morada.Numero_Porta%TYPE, v_codigo_postal Morada.codigo_postal%TYPE) RETURN cliente.morada_correspondencia_id%TYPE
is
nr_id_morada INTEGER;
BEGIN
select nvl(max(ID), 0)+1 into nr_id_morada from Morada;
INSERT into Morada(id, localidade_id,pais_id,nome_rua,numero_porta,codigo_postal)
VALUES(nr_id_morada,v_id_localidade,v_id_pais,v_nome_rua, v_numero_porta, v_codigo_postal);
RETURN nr_id_morada;
END;
/


CREATE OR REPLACE FUNCTION Add_Tipo_Cliente_Func(v_designacao Tipo_Cliente.designacao%TYPE) RETURN Tipo_Cliente.designacao%TYPE
is
nr_id_tipo_cliente INTEGER;
BEGIN
select nvl(max(ID), 0)+1 into nr_id_tipo_cliente from Tipo_Cliente;
INSERT into Tipo_Cliente(id, designacao) VALUES(nr_id_tipo_cliente,v_designacao);
RETURN nr_id_tipo_cliente;
END;
/



-----------US205 1./2.--------------------

CREATE OR REPLACE PROCEDURE Add_Client_Proc(v_nome Cliente.nome%TYPE,v_tipo_cliente tipo_cliente.designacao%TYPE, v_numero_fiscal cliente.numero_fiscal%TYPE,
v_email cliente.email%TYPE, v_plafond cliente.plafond%TYPE,v_nome_localidade Localidade.nome%TYPE,v_pais Pais.nome%type,v_nome_rua Morada.nome_rua%TYPE,
v_num_porta Morada.numero_porta%TYPE,v_codigo_postal Morada.codigo_postal%TYPE)
IS
nr_email INTEGER;
ex_email EXCEPTION;
nr_nif INTEGER;
ex_nif EXCEPTION;
nr_pais INTEGER;
nr_id_pais Pais.id%TYPE;
nr_localidade INTEGER;
nr_id_localidade Localidade.id%TYPE;
ex_plafond EXCEPTION;
nr_id_morada Cliente.morada_correspondencia_id%TYPE;
codigo_unico_gerado cliente.codigo_unico%TYPE;
nr_tipo_cliente INTEGER;
id_tipo_cliente Tipo_Cliente.id%TYPE;
BEGIN
SElect count(*) into nr_email from Cliente where email = v_email;

IF nr_email = 0 AND v_email LIKE '%@%.%' THEN

SELECT count(*) into nr_nif from Cliente where numero_FISCAL = v_numero_fiscal;
IF nr_nif > 0 or (v_Numero_Fiscal < 99999999 AND v_Numero_Fiscal > 1000000000) THEN
RAISE ex_nif;
ELSE

SELECT count(*) into nr_pais from Pais where UPPER(nome) = UPPER(v_pais);
IF nr_pais = 0 THEN
nr_id_pais := Add_Pais_Func(v_pais);
ELSE
select id into nr_id_pais from Pais WHERE UPPER(nome) = UPPER(v_pais);
end if;

SELECT count(*) into nr_localidade from Localidade where UPPER(nome) = UPPER(v_nome_localidade);
IF nr_localidade = 0 THEN
nr_id_localidade := Add_Localidade_Func(v_nome_localidade);
ELSE
select id into nr_id_localidade from Localidade WHERE UPPER(nome) = UPPER(v_nome_localidade);
end if;

SELECT count(*) into nr_tipo_cliente from Tipo_Cliente where UPPER(designacao) = UPPER(v_tipo_cliente);
IF nr_tipo_cliente = 0 THEN
id_tipo_cliente := Add_Tipo_Cliente_Func(v_tipo_cliente);
ELSE
select id into id_tipo_cliente from Tipo_Cliente WHERE UPPER(designacao) = UPPER(v_tipo_cliente);
end if;

IF v_plafond < 0 THEN
RAISE ex_plafond;
ELSE

nr_id_morada := Add_Morada_Func(nr_id_localidade, nr_id_pais, v_nome_rua, v_num_porta, v_codigo_postal);
select nvl(max(codigo_unico), 99)+1 into codigo_unico_gerado from cliente;
INSERT into cliente(codigo_unico,tipo_cliente_id,morada_correspondencia_id,morada_entrega_id,nome,numero_fiscal,email,plafond,numero_total_encomendas,valor_total_encomendas)
VALUES(codigo_unico_gerado, id_tipo_cliente,nr_id_morada,nr_id_morada,v_nome,v_numero_fiscal,v_email,v_plafond,0,0);

end if;
END IF;

ELSE
RAISE ex_email;
end if;

EXCEPTION
when ex_email then
    raise_application_error(-20001, 'Email já existe ou é inválido');
WHEN ex_nif THEN
    raise_application_error(-20002, 'Numero fiscal já existe ou é inválido');
WHEN ex_plafond THEN
    raise_application_error(-20003, 'Plafond negativo');
END;
/


BEGIN
Add_Client_Proc('Pedro','empresa',123454356,'pedro@gmail.com',10000,'Paris','França','Baguette',231,'80202');
end;
