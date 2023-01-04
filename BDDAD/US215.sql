--já adicionado no banco de dados
/*
CREATE TABLE input_hub (
    input_string VARCHAR2(25),
    CONSTRAINT INPUT_HUB_INPUT_STRING_PK PRIMARY KEY (input_string)
);

CREATE TABLE Hub (
    Location_ID VARCHAR2(5),
    Latitude NUMBER,
    Longitude NUMBER,
    CONSTRAINT HUB_COMP_PK PRIMARY KEY (Location_ID) 
);
*/


--criar trigger para inserir na tabela hub, os dados da tabela input_hub se a primeira letra do código for diferente a C 
/*
    CREATE OR REPLACE TRIGGER Insert_Hub_Trigger
    AFTER INSERT ON Input_Hub
    FOR EACH ROW
    DECLARE
    Cursor c1 is SELECT regexp_substr(:new.input_string, '[^;]+', 1, LEVEL) as split_string
    FROM dual
    CONNECT BY regexp_substr(:new.input_string, '[^;]+', 1, LEVEL) IS NOT NULL;
    v_str c1%rowtype;
    v_locID varchar2(5);
    v_lat number;
    v_long number;
    v_code varchar2(3);
    v_counter INTEGER := 0;
    ex_lines EXCEPTION;
    BEGIN
        OPEN c1;
        LOOP
            FETCH c1 INTO v_str;
            EXIT WHEN c1%NOTFOUND;
            v_counter := v_counter + 1;
        END LOOP;
        CLOSE c1;
        if v_counter != 4 then
            RAISE ex_lines;
        end if;
        OPEN c1;
        LOOP
            FETCH c1 INTO v_str;
            EXIT WHEN c1%NOTFOUND;
            v_locID := v_str.split_string;
            FETCH c1 INTO v_str;
            v_lat := to_number(v_str.split_string, '9999999.9999', 'NLS_NUMERIC_CHARACTERS = ''.,''');
            FETCH c1 INTO v_str;
            v_long := to_number(v_str.split_string, '9999999.9999', 'NLS_NUMERIC_CHARACTERS = ''.,''');
            FETCH c1 INTO v_str;
            v_code := v_str.split_string;
            if SUBSTR(v_code, 1, 1) != 'C' then
                insert into Hub (Location_ID, Latitude, Longitude) values (v_locID, v_lat, v_long);
            end if;
        END LOOP;
        CLOSE c1;
    EXCEPTION
        WHEN ex_lines THEN
            RAISE_APPLICATION_ERROR(-20001, 'ERROR: Invalid number of fields');
    END;
    /
    */

--ja na US204
/*    
begin 
    insert into input_hub (input_string) values ('CT1;40.6389;-8.6553;C1');
    insert into input_hub (input_string) values ('CT2;38.0333;-7.8833;C2');
    insert into input_hub (input_string) values ('CT14;38.5243;-8.8926;E1');
    insert into input_hub (input_string) values ('CT11;39.3167;-7.4167;E2');
    insert into input_hub (input_string) values ('CT10;39.7444;-8.8072;P3');
end;
/
*/


--acceptance criteria 2

--É possível atribuir e alterar o hub por defeito de um cliente.

CREATE OR REPLACE PROCEDURE CHANGE_DEFAULT_HUB_PROC (p_clientID IN VARCHAR2, p_hubID IN VARCHAR2) IS
nr_clientes INTEGER;
cliente_ex EXCEPTION;
nr_hubs INTEGER;
hub_ex EXCEPTION;
BEGIN
    SELECT COUNT(*) INTO nr_clientes FROM Cliente 
    WHERE Codigo_Unico = p_clientID;
    IF nr_clientes = 0 THEN
        RAISE cliente_ex;
    END IF;
    SELECT COUNT(*) INTO nr_hubs FROM Hub 
    WHERE Location_ID = p_hubID;
    IF nr_hubs = 0 THEN
        RAISE hub_ex;
    END IF;
    UPDATE Cliente SET hub_location_id = p_hubID WHERE Codigo_Unico = p_clientID;

    EXCEPTION
        WHEN cliente_ex THEN
            RAISE_APPLICATION_ERROR(-20011, 'ERROR: Client does not exist');
        WHEN hub_ex THEN
            RAISE_APPLICATION_ERROR(-20012, 'ERROR: Hub does not exist');
END;
/

--teste change_default_hub
DECLARE 
v_cliente Cliente%rowtype;
BEGIN
CHANGE_DEFAULT_HUB_PROC('111', 'CT1');
SELECT * into v_cliente FROM Cliente WHERE Codigo_Unico = '111';
DBMS_OUTPUT.PUT_LINE(v_cliente.codigo_unico||' '||v_cliente.Hub_Location_ID);
END;
/


--acceptance criteria 3
/*Quando o cliente efetua uma nota de encomenda pode indicar um hub, distinto do seu hub por
defeito, para proceder à recolha dos produtos encomendados. O hub indicado deve ser válido,
i.e., deve existir na tabela hub.
*/


CREATE OR REPLACE PROCEDURE Update_order_client_PROC(p_clientID cliente.Codigo_Unico%TYPE, p_valor cliente.valor_total_encomendas%TYPE) IS
nr_clientes INTEGER;
cliente_ex EXCEPTION;
BEGIN
    SELECT COUNT(*) INTO nr_clientes FROM Cliente 
    WHERE Codigo_Unico = p_clientID;
    IF nr_clientes = 0 THEN
        RAISE cliente_ex;
    END IF;

    UPDATE Cliente SET numero_total_encomendas = numero_total_encomendas + 1 WHERE Codigo_Unico = p_clientID;
    UPDATE Cliente SET valor_total_encomendas = valor_total_encomendas + p_valor WHERE Codigo_Unico = p_clientID;

    EXCEPTION
        WHEN cliente_ex THEN
            RAISE_APPLICATION_ERROR(-20020, 'ERROR: Client does not exist');
END;
/



CREATE OR REPLACE PROCEDURE Order_With_Diferent_hub_PROC (p_clientID Cliente.Codigo_Unico%TYPE, p_produto Produto.nome%TYPE, p_qtd Encomenda_Produto.Quantidade%TYPE ,p_hubID Hub.Location_ID%TYPE) IS
nr_clientes INTEGER;
cliente_ex EXCEPTION;
nr_produtos INTEGER;
produto_ex EXCEPTION;
nr_hubs INTEGER;
hub_ex EXCEPTION;
nr_id_encomenda INTEGER;
encomenda_ex EXCEPTION;
v_produto_ID Produto.ID%TYPE; 
v_accID colheita.accao_id%type;
v_data DATE;
v_colheita INTEGER;
colheita_ex EXCEPTION;
v_quantidade Accao.Quantidade%TYPE;
v_preco Colheita.preco%TYPE;
v_preco_unidade NUMBER;

BEGIN
    SELECT COUNT(*) INTO nr_clientes FROM Cliente 
    WHERE Codigo_Unico = p_clientID;
    IF nr_clientes = 0 THEN
        RAISE cliente_ex;
    END IF;
    SELECT COUNT(*) INTO nr_produtos FROM Produto 
    WHERE nome = p_produto;
    IF nr_produtos = 0 THEN
        RAISE produto_ex;
    END IF;
    SELECT COUNT(*) INTO nr_hubs FROM Hub 
    WHERE Location_ID = p_hubID;
    IF nr_hubs = 0 THEN
        RAISE hub_ex;
    END IF;

    select nvl(max(ID), 0)+1 into nr_id_encomenda from Encomenda;
    select ID into v_produto_ID from Produto where nome = p_produto;
    --descobrir ultima data de colheita do produto em causa
    select count(*) into v_colheita from Colheita c 
        INNER JOIN Accao ac on c.accao_id = ac.ID
            WHERE c.Produto_ID = v_produto_ID;
    if v_colheita = 0 then
        RAISE colheita_ex;
    end if;
    select max(ac.Data_Accao) into v_data from Colheita c 
        INNER JOIN Accao ac on c.accao_id = ac.ID
            WHERE c.Produto_ID = v_produto_ID;
    --descobrir o id de accao  
    select c.accao_ID into v_accID FROM Colheita c 
        INNER JOIN Accao ac on ac.ID = c.accao_ID
            WHERE ac.Data_Accao = v_data and c.Produto_ID = v_produto_ID; 

    SELECT Preco into v_preco FROM Colheita WHERE accao_ID = v_accID;
    select quantidade into v_quantidade FROM Accao WHERE ID = v_accID;

    v_preco_unidade := v_preco/v_quantidade;  

    INSERT INTO Encomenda (ID, Cliente_Codigo_Unico, Hub_Location_ID, Data_Encomenda, Data_Limite_Pagamento, Data_Pagamento) VALUES (nr_id_encomenda, p_clientID, p_hubID, SYSDATE, SYSDATE, SYSDATE);
    INSERT INTO Encomenda_Produto (Encomenda_ID, Produto_ID, Quantidade, Preco) VALUES (nr_id_encomenda, v_produto_ID, p_qtd, p_qtd*v_preco_unidade);
    Update_order_client_PROC(p_clientID, p_qtd*v_preco_unidade);
EXCEPTION
        WHEN cliente_ex THEN
            RAISE_APPLICATION_ERROR(-20013, 'ERROR: Client does not exist');
        WHEN produto_ex THEN
            RAISE_APPLICATION_ERROR(-20014, 'ERROR: Product does not exist');
        WHEN hub_ex THEN
            RAISE_APPLICATION_ERROR(-20015, 'ERROR: Hub does not exist');
        WHEN colheita_ex THEN
            RAISE_APPLICATION_ERROR(-20016, 'ERROR: There is no harvest for this product');
END;
/

--teste
BEGIN
Order_With_Diferent_hub_PROC('111', 'Laranjas', 10, 'CT11');
END;
/



CREATE OR REPLACE PROCEDURE Order_PROC (p_clientID Cliente.Codigo_Unico%TYPE, p_produto Produto.nome%TYPE, p_qtd Encomenda_Produto.Quantidade%TYPE) IS
nr_clientes INTEGER;
cliente_ex EXCEPTION;
nr_produtos INTEGER;
produto_ex EXCEPTION;
nr_id_encomenda INTEGER;
encomenda_ex EXCEPTION;
v_hub Encomenda.hub_location_id%TYPE;
hub_ex EXCEPTION;
v_produto_ID Produto.ID%TYPE; 
v_accID colheita.accao_id%type;
v_data DATE;
v_colheita INTEGER;
colheita_ex EXCEPTION;
v_quantidade Accao.Quantidade%TYPE;
v_preco Colheita.preco%TYPE;
v_preco_unidade NUMBER;

BEGIN
    SELECT COUNT(*) INTO nr_clientes FROM Cliente 
    WHERE Codigo_Unico = p_clientID;
    IF nr_clientes = 0 THEN
        RAISE cliente_ex;
    END IF;
    SELECT COUNT(*) INTO nr_produtos FROM Produto 
    WHERE nome = p_produto;
    IF nr_produtos = 0 THEN
        RAISE produto_ex;
    END IF;

    SELECT hub_location_id INTO v_hub FROM Cliente WHERE Codigo_Unico = p_clientID;
    IF v_hub IS NULL THEN
        RAISE hub_ex;
    END IF;
    select nvl(max(ID), 0)+1 into nr_id_encomenda from Encomenda;
    select ID into v_produto_ID from Produto where nome = p_produto;

    --descobrir ultima data de colheita do produto em causa
    select count(*) into v_colheita from Colheita c 
        INNER JOIN Accao ac on c.accao_id = ac.ID
            WHERE c.Produto_ID = v_produto_ID;
    if v_colheita = 0 then
        RAISE colheita_ex;
    end if;
    select max(ac.Data_Accao) into v_data from Colheita c 
        INNER JOIN Accao ac on c.accao_id = ac.ID
            WHERE c.Produto_ID = v_produto_ID;
    --descobrir o id de accao  
    select c.accao_ID into v_accID FROM Colheita c 
        INNER JOIN Accao ac on ac.ID = c.accao_ID
            WHERE ac.Data_Accao = v_data and c.Produto_ID = v_produto_ID; 

    SELECT Preco into v_preco FROM Colheita WHERE accao_ID = v_accID;
    select quantidade into v_quantidade FROM Accao WHERE ID = v_accID;

    v_preco_unidade := v_preco/v_quantidade;  

    INSERT INTO Encomenda (ID, Cliente_Codigo_Unico, Hub_Location_ID, Data_Encomenda, Data_Limite_Pagamento, Data_Pagamento) VALUES (nr_id_encomenda, p_clientID, v_hub, SYSDATE, SYSDATE, SYSDATE);
    INSERT INTO Encomenda_Produto (Encomenda_ID, Produto_ID, Quantidade, Preco) VALUES (nr_id_encomenda, v_produto_ID, p_qtd, p_qtd*v_preco_unidade);
    Update_order_client_PROC(p_clientID, p_qtd*v_preco_unidade);
EXCEPTION
        WHEN hub_ex THEN
            RAISE_APPLICATION_ERROR(-20015, 'ERROR: Client does not have a default hub');
        WHEN cliente_ex THEN
            RAISE_APPLICATION_ERROR(-20013, 'ERROR: Client does not exist');
        WHEN produto_ex THEN
            RAISE_APPLICATION_ERROR(-20014, 'ERROR: Product does not exist');
        WHEN colheita_ex THEN
            RAISE_APPLICATION_ERROR(-20016, 'ERROR: There is no harvest for this product');
END;
/

BEGIN
Order_PROC('111', 'Laranjas', 10);
END;
/
