/*--- Funções Utilitárias ---*/

CREATE OR REPLACE FUNCTION Plantacao_Existe_FUNC(var_plantacao_ID Plantacao.ID%TYPE) RETURN BOOLEAN
AS
    var_cnt INT;
BEGIN
    SELECT COUNT(*) INTO var_cnt
        FROM Plantacao plant
        WHERE plant.ID = var_plantacao_ID;

    RETURN var_cnt > 0;
END;
/

CREATE OR REPLACE FUNCTION Metodo_Distribuicao_Existe_FUNC(var_metodo_dist_ID Metodo_Distribuicao.ID%TYPE) RETURN BOOLEAN
AS
    var_cnt INT;
BEGIN
    SELECT COUNT(*) INTO var_cnt
        FROM Metodo_Distribuicao metodo_dist
        WHERE metodo_dist.ID = var_metodo_dist_ID;

    RETURN var_cnt > 0;
END;
/

CREATE OR REPLACE FUNCTION Gen_Accao_ID_FUNC RETURN Accao.ID%TYPE
AS
    var_cnt INT;
    var_ID Accao.ID%TYPE;
BEGIN
    SELECT COUNT(*) INTO var_cnt
        FROM Accao accao;

    IF var_cnt = 0 THEN
        RETURN 1;
    END IF;

    SELECT MAX(ID) INTO var_ID
        FROM Accao accao;

    RETURN var_ID + 1;
END;
/

CREATE OR REPLACE FUNCTION Fator_Producao_Existe_FUNC(var_fator_prod_ID Fator_Producao.ID%TYPE) RETURN BOOLEAN
AS
    var_cnt INT;
BEGIN
    SELECT COUNT(*) INTO var_cnt
        FROM Fator_Producao fator_prod
        WHERE fator_prod.ID = var_fator_prod_ID;

    RETURN var_cnt > 0;
END;
/

CREATE OR REPLACE FUNCTION Restricao_Prod_Campo_Existe1_FUNC(var_plantacao_ID Plantacao.ID%TYPE, var_fator_prod_ID Fator_Producao.ID%TYPE) RETURN BOOLEAN
AS
    var_campo_ID Campo.ID%TYPE;
    var_cnt INT;
BEGIN
    SELECT campo.ID INTO var_campo_ID
        FROM Campo campo
        INNER JOIN Plantacao plant
            ON plant.Campo_ID = campo.ID
        WHERE plant.ID = var_plantacao_ID;
    
    SELECT COUNT(*) INTO var_cnt
        FROM Restricao restricao
        WHERE restricao.Campo_ID = var_campo_ID
            AND restricao.Fator_Producao_ID = var_fator_prod_ID;

    RETURN var_cnt > 0;
END;
/

CREATE OR REPLACE FUNCTION Restricao_Prod_Campo_Existe2_FUNC(var_accao_ID Accao.ID%TYPE, var_fator_prod_ID Fator_Producao.ID%TYPE) RETURN BOOLEAN
AS
    var_campo_ID Campo.ID%TYPE;
    var_cnt INT;
BEGIN
    SELECT campo.ID INTO var_campo_ID
        FROM Campo campo
        INNER JOIN Plantacao plant
            ON plant.Campo_ID = campo.ID
        INNER JOIN Accao acc
            ON acc.Plantacao_ID = plant.ID
        WHERE acc.ID = var_accao_ID;
    
    SELECT COUNT(*) INTO var_cnt
        FROM Restricao restricao
        WHERE restricao.Campo_ID = var_campo_ID
            AND restricao.Fator_Producao_ID = var_fator_prod_ID;

    RETURN var_cnt > 0;
END;
/


CREATE OR REPLACE FUNCTION Produto_Existe_FUNC(var_prod_ID Produto.ID%TYPE) RETURN BOOLEAN
AS
    var_cnt INT;
BEGIN
    SELECT COUNT(*) INTO var_cnt
        FROM Produto prod
        WHERE prod.ID = var_prod_ID;

    RETURN var_cnt > 0;
END;
/

CREATE OR REPLACE FUNCTION Accao_Ass_Adubacao_FUNC(var_accao_ID Accao.ID%TYPE) RETURN BOOLEAN
AS
    var_cnt INT;
BEGIN
    SELECT COUNT(*) INTO var_cnt
        FROM Adubacao adb
        WHERE adb.Accao_ID = var_accao_ID;

    RETURN var_cnt > 0;
END;
/

CREATE OR REPLACE FUNCTION Accao_Ass_Apli_Fator_Prod_FUNC(var_accao_ID Accao.ID%TYPE) RETURN BOOLEAN
AS
    var_cnt INT;
BEGIN
    SELECT COUNT(*) INTO var_cnt
        FROM Aplicacao_Fator_Producao apli_fator_prod
        WHERE apli_fator_prod.Accao_ID = var_accao_ID;

    RETURN var_cnt > 0;
END;
/

CREATE OR REPLACE FUNCTION Accao_Ass_Rega_FUNC(var_accao_ID Accao.ID%TYPE) RETURN BOOLEAN
AS
    var_cnt INT;
BEGIN
    SELECT COUNT(*) INTO var_cnt
        FROM Rega rega
        WHERE rega.Accao_ID = var_accao_ID;

    RETURN var_cnt > 0;
END;
/

CREATE OR REPLACE FUNCTION Accao_Ass_Colheita_FUNC(var_accao_ID Accao.ID%TYPE) RETURN BOOLEAN
AS
    var_cnt INT;
BEGIN
    SELECT COUNT(*) INTO var_cnt
        FROM Colheita colheita
        WHERE colheita.Accao_ID = var_accao_ID;

    RETURN var_cnt > 0;
END;
/

/*--- Triggers ---*/
CREATE OR REPLACE TRIGGER Registar_Accao_TRIG BEFORE INSERT ON Accao FOR EACH ROW
DECLARE 
    var_data_inicio_cultura Plantacao.Data_Inicio_Cultura%TYPE;
    var_data_fim_cultura Plantacao.Data_Fim_Cultura%TYPE;

    var_dtaacc_cond1_exc EXCEPTION;
    var_dtaacc_cond2_exc EXCEPTION;
BEGIN
    SELECT Data_Inicio_Cultura, Data_Fim_Cultura INTO var_data_inicio_cultura, var_data_fim_cultura
    FROM Plantacao plant
    WHERE plant.ID = :NEW.Plantacao_ID;

    IF :NEW.Data_Accao < var_data_inicio_cultura THEN
        RAISE var_dtaacc_cond1_exc;
    END IF;

    IF NOT var_data_fim_cultura IS NULL AND :NEW.Data_Accao > var_data_fim_cultura THEN
        RAISE var_dtaacc_cond2_exc;
    END IF;
EXCEPTION
    WHEN var_dtaacc_cond1_exc THEN
        RAISE_APPLICATION_ERROR(-20001, 'Data de Realização da Rega antecede o Início da Plantação;');
    WHEN var_dtaacc_cond2_exc THEN
        RAISE_APPLICATION_ERROR(-20002, 'Data de Realização da Rega precede o Fim da Plantação;');
END Registar_Accao_TRIG;
/

/*--- US210.A.A - Registar Rega ---*/

CREATE OR REPLACE PROCEDURE Registar_Rega_PROC(var_plantacao_ID Accao.Plantacao_ID%TYPE, var_data_accao Accao.Data_Accao%TYPE, var_quantidade Accao.Quantidade%TYPE, var_metodo_dist_ID Rega.Metodo_Distribuicao_ID%TYPE)
AS
    var_plant_null_exc EXCEPTION;
    var_dtaacc_null_exc EXCEPTION;
    var_qnt_null_exc EXCEPTION;
    var_metdist_null_exc EXCEPTION;
    var_plant_ext_exc EXCEPTION;
    var_metdist_ext_exc EXCEPTION;
    var_dtaacc_cond1_exc EXCEPTION;
    var_dtaacc_cond2_exc EXCEPTION;
    var_qnt_cond_exc EXCEPTION;

    var_data_inicio_cultura Plantacao.Data_Inicio_Cultura%TYPE;
    var_data_fim_cultura Plantacao.Data_Fim_Cultura%TYPE;
    var_accao_ID Accao.ID%TYPE;
BEGIN
    IF var_plantacao_ID IS NULL THEN
        RAISE var_plant_null_exc;
    END IF;

    IF var_data_accao IS NULL THEN
        RAISE var_dtaacc_null_exc;
    END IF;

    IF var_quantidade IS NULL THEN
        RAISE var_qnt_null_exc;
    END IF;

    IF var_metodo_dist_ID IS NULL THEN
        RAISE var_metdist_null_exc;
    END IF;

    IF NOT Plantacao_Existe_FUNC(var_plantacao_ID) THEN
        RAISE var_plant_ext_exc;
    END IF;

    IF NOT Metodo_Distribuicao_Existe_FUNC(var_metodo_dist_ID) THEN
        RAISE var_metdist_ext_exc;
    END IF;

    SELECT Data_Inicio_Cultura INTO var_data_inicio_cultura
        FROM Plantacao plant
        WHERE plant.ID = var_plantacao_ID;

    IF var_data_inicio_cultura > var_data_accao THEN
        RAISE var_dtaacc_cond1_exc;
    END IF;

    SELECT Data_Fim_Cultura INTO var_data_fim_cultura
        FROM Plantacao plant
        WHERE plant.ID = var_plantacao_ID;

    IF NOT var_data_fim_cultura IS NULL AND var_data_accao > var_data_fim_cultura THEN
        RAISE var_dtaacc_cond2_exc;
    END IF;

    IF var_quantidade <= 0 THEN
        RAISE var_qnt_cond_exc;
    END IF;
    
    var_accao_ID := Gen_Accao_ID_FUNC;

    INSERT INTO Accao VALUES (var_accao_ID, var_plantacao_ID, var_data_accao, var_quantidade);
    INSERT INTO Rega VALUES (var_accao_ID, var_metodo_dist_ID);

    DBMS_OUTPUT.PUT_LINE('Rega inserida com sucesso;');

EXCEPTION
    WHEN var_plant_null_exc THEN
        RAISE_APPLICATION_ERROR(-20001, 'Não foi indicada uma Plantação;');
    WHEN var_dtaacc_null_exc THEN
        RAISE_APPLICATION_ERROR(-20002, 'Não foi indicada a Data de Realização da Rega;');
    WHEN var_qnt_null_exc THEN
        RAISE_APPLICATION_ERROR(-20003, 'Não foi indicada a Quantidade de Água utilizada na Rega;');
    WHEN var_metdist_null_exc THEN
        RAISE_APPLICATION_ERROR(-20004, 'Não foi indicada o Método de Distribuição utilizado na Rega;');
    WHEN var_plant_ext_exc THEN
        RAISE_APPLICATION_ERROR(-20005, 'Plantação indicada não existe;');
    WHEN var_metdist_ext_exc THEN
        RAISE_APPLICATION_ERROR(-20006, 'Método Distribuição indicada não existe;');
    WHEN var_dtaacc_cond1_exc THEN
        RAISE_APPLICATION_ERROR(-20007, 'Data de Realização da Rega antecede o Início da Plantação;');
    WHEN var_dtaacc_cond2_exc THEN
        RAISE_APPLICATION_ERROR(-20008, 'Data de Realização da Rega precede o Fim da Plantação;');
    WHEN var_qnt_cond_exc THEN
        RAISE_APPLICATION_ERROR(-20009, 'Quantidade de Água utilizada na Rega deve ser superior a 0;');
END;
/

CREATE OR REPLACE TRIGGER Registar_Rega_TRIG BEFORE INSERT ON Rega FOR EACH ROW
DECLARE
    var_accndis1_exc EXCEPTION;
    var_accndis2_exc EXCEPTION;
    var_accndis3_exc EXCEPTION;
BEGIN
    IF Accao_Ass_Adubacao_FUNC(:NEW.Accao_ID) THEN
        RAISE var_accndis1_exc;
    END IF;

    IF Accao_Ass_Apli_Fator_Prod_FUNC(:NEW.Accao_ID) THEN
        RAISE var_accndis2_exc;
    END IF;

    IF Accao_Ass_Colheita_FUNC(:NEW.Accao_ID) THEN
        RAISE var_accndis3_exc;
    END IF;
EXCEPTION
    WHEN var_accndis1_exc THEN
        RAISE_APPLICATION_ERROR(-20001, 'Acção previamente associada a Operação de Adubação;');
    WHEN var_accndis2_exc THEN
        RAISE_APPLICATION_ERROR(-20002, 'Acção previamente associada a Operação de Aplicação de Fator de Produção;');
    WHEN var_accndis3_exc THEN
        RAISE_APPLICATION_ERROR(-20002, 'Acção previamente associada a Operação de Colheita;');
END Registar_Rega_TRIG;
/

/*--- US210.A.B - Registar Adubação ---*/

CREATE OR REPLACE PROCEDURE Registar_Adubacao_PROC(var_plantacao_ID Accao.Plantacao_ID%TYPE, var_data_accao Accao.Data_Accao%TYPE, var_quantidade Accao.Quantidade%TYPE, var_metodo_dist_ID Adubacao.Metodo_Distribuicao_ID%TYPE)
AS
    var_plant_null_exc EXCEPTION;
    var_dtaacc_null_exc EXCEPTION;
    var_qnt_null_exc EXCEPTION;
    var_metdist_null_exc EXCEPTION;
    var_plant_ext_exc EXCEPTION;
    var_metdist_ext_exc EXCEPTION;
    var_dtaacc_cond1_exc EXCEPTION;
    var_dtaacc_cond2_exc EXCEPTION;
    var_qnt_cond_exc EXCEPTION;

    var_data_inicio_cultura Plantacao.Data_Inicio_Cultura%TYPE;
    var_data_fim_cultura Plantacao.Data_Fim_Cultura%TYPE;
    var_accao_ID Accao.ID%TYPE;
BEGIN
    IF var_plantacao_ID IS NULL THEN
        RAISE var_plant_null_exc;
    END IF;

    IF var_data_accao IS NULL THEN
        RAISE var_dtaacc_null_exc;
    END IF;

    IF var_quantidade IS NULL THEN
        RAISE var_qnt_null_exc;
    END IF;

    IF var_metodo_dist_ID IS NULL THEN
        RAISE var_metdist_null_exc;
    END IF;

    IF NOT Plantacao_Existe_FUNC(var_plantacao_ID) THEN
        RAISE var_plant_ext_exc;
    END IF;

    IF NOT Metodo_Distribuicao_Existe_FUNC(var_metodo_dist_ID) THEN
        RAISE var_metdist_ext_exc;
    END IF;

    SELECT Data_Inicio_Cultura INTO var_data_inicio_cultura
        FROM Plantacao plant
        WHERE plant.ID = var_plantacao_ID;

    IF var_data_inicio_cultura > var_data_accao THEN
        RAISE var_dtaacc_cond1_exc;
    END IF;

    SELECT Data_Fim_Cultura INTO var_data_fim_cultura
        FROM Plantacao plant
        WHERE plant.ID = var_plantacao_ID;

    IF NOT var_data_fim_cultura IS NULL AND var_data_accao > var_data_fim_cultura THEN
        RAISE var_dtaacc_cond2_exc;
    END IF;

    IF var_quantidade <= 0 THEN
        RAISE var_qnt_cond_exc;
    END IF;
    
    var_accao_ID := Gen_Accao_ID_FUNC;

    INSERT INTO Accao VALUES (var_accao_ID, var_plantacao_ID, var_data_accao, var_quantidade);
    INSERT INTO Adubacao VALUES (var_accao_ID, var_metodo_dist_ID);

    DBMS_OUTPUT.PUT_LINE('Adubação inserida com sucesso;');

EXCEPTION
    WHEN var_plant_null_exc THEN
        RAISE_APPLICATION_ERROR(-20001, 'Não foi indicada uma Plantação;');
    WHEN var_dtaacc_null_exc THEN
        RAISE_APPLICATION_ERROR(-20002, 'Não foi indicada a Data de Realização da Adubação;');
    WHEN var_qnt_null_exc THEN
        RAISE_APPLICATION_ERROR(-20003, 'Não foi indicada a Quantidade de Adubo utilizado na Adubação;');
    WHEN var_metdist_null_exc THEN
        RAISE_APPLICATION_ERROR(-20004, 'Não foi indicada o Método de Distribuição utilizado na Adubação;');
    WHEN var_plant_ext_exc THEN
        RAISE_APPLICATION_ERROR(-20005, 'Plantação indicada não existe;');
    WHEN var_metdist_ext_exc THEN
        RAISE_APPLICATION_ERROR(-20006, 'Método Distribuição indicada não existe;');
    WHEN var_dtaacc_cond1_exc THEN
        RAISE_APPLICATION_ERROR(-20007, 'Data de Realização da Adubação antecede o Início da Plantação;');
    WHEN var_dtaacc_cond2_exc THEN
        RAISE_APPLICATION_ERROR(-20008, 'Data de Realização da Adubação precede o Fim da Plantação;');
    WHEN var_qnt_cond_exc THEN
        RAISE_APPLICATION_ERROR(-20009, 'Quantidade de Adubo utilizado na Adubação deve ser superior a 0;');
END;
/

CREATE OR REPLACE TRIGGER Registar_Adubacao_TRIG BEFORE INSERT ON Adubacao FOR EACH ROW
DECLARE
    var_accndis1_exc EXCEPTION;
    var_accndis2_exc EXCEPTION;
    var_accndis3_exc EXCEPTION;
BEGIN
    IF Accao_Ass_Rega_FUNC(:NEW.Accao_ID) THEN
        RAISE var_accndis1_exc;
    END IF;

    IF Accao_Ass_Apli_Fator_Prod_FUNC(:NEW.Accao_ID) THEN
        RAISE var_accndis2_exc;
    END IF;

    IF Accao_Ass_Colheita_FUNC(:NEW.Accao_ID) THEN
        RAISE var_accndis3_exc;
    END IF;
EXCEPTION
    WHEN var_accndis1_exc THEN
        RAISE_APPLICATION_ERROR(-20001, 'Acção previamente associada a Operação de Rega;');
    WHEN var_accndis2_exc THEN
        RAISE_APPLICATION_ERROR(-20002, 'Acção previamente associada a Operação de Aplicação de Fator de Produção;');
    WHEN var_accndis3_exc THEN
        RAISE_APPLICATION_ERROR(-20002, 'Acção previamente associada a Operação de Colheita;');
END Registar_Adubacao_TRIG;
/

/*--- US210.A.C - Registar Aplicação Fator Produção ---*/
CREATE OR REPLACE PROCEDURE Registar_Aplicacao_Fator_Producao_PROC(var_plantacao_ID Accao.Plantacao_ID%TYPE, var_data_accao Accao.Data_Accao%TYPE, var_quantidade Accao.Quantidade%TYPE, var_fator_producao_ID Aplicacao_Fator_Producao.Fator_Producao_ID%TYPE, var_metodo_dist_ID Aplicacao_Fator_Producao.Metodo_Distribuicao_ID%TYPE)
AS
    var_plant_null_exc EXCEPTION;
    var_dtaacc_null_exc EXCEPTION;
    var_qnt_null_exc EXCEPTION;
    var_ftprod_null_exc EXCEPTION;
    var_metdist_null_exc EXCEPTION;
    var_plant_ext_exc EXCEPTION;
    var_ftprod_ext_exc EXCEPTION;
    var_metdist_ext_exc EXCEPTION;
    var_dtaacc_cond1_exc EXCEPTION;
    var_dtaacc_cond2_exc EXCEPTION;
    var_dtaacc_cond3_exc EXCEPTION;
    var_qnt_cond_exc EXCEPTION;

    var_inicio_restricao Restricao.Inicio_Restricao%TYPE;
    var_fim_restricao Restricao.Fim_Restricao%TYPE;
    var_data_inicio_cultura Plantacao.Data_Inicio_Cultura%TYPE;
    var_data_fim_cultura Plantacao.Data_Fim_Cultura%TYPE;
    var_accao_ID Accao.ID%TYPE;

    CURSOR cur_restricao(var_plant_ID Plantacao.ID%TYPE, var_ft_prod_ID Fator_Producao.ID%TYPE) IS
        SELECT Inicio_Restricao, Fim_Restricao
        FROM Restricao restricao
        INNER JOIN Plantacao plant
            ON plant.Campo_ID = restricao.Campo_ID
        WHERE plant.ID = var_plant_ID
        AND restricao.Fator_Producao_ID = var_ft_prod_ID;
BEGIN
    IF var_plantacao_ID IS NULL THEN
        RAISE var_plant_null_exc;
    END IF;

    IF var_data_accao IS NULL THEN
        RAISE var_dtaacc_null_exc;
    END IF;

    IF var_quantidade IS NULL THEN
        RAISE var_qnt_null_exc;
    END IF;

    IF var_fator_producao_ID IS NULL THEN
        RAISE var_ftprod_null_exc;
    END IF;

    IF var_metodo_dist_ID IS NULL THEN
        RAISE var_metdist_null_exc;
    END IF;

    IF NOT Plantacao_Existe_FUNC(var_plantacao_ID) THEN
        RAISE var_plant_ext_exc;
    END IF;

    IF NOT Fator_Producao_Existe_FUNC(var_fator_producao_ID) THEN
        RAISE var_ftprod_ext_exc;
    END IF;

    IF NOT Metodo_Distribuicao_Existe_FUNC(var_metodo_dist_ID) THEN
        RAISE var_metdist_ext_exc;
    END IF;

    IF Restricao_Prod_Campo_Existe1_FUNC(var_plantacao_ID, var_fator_producao_ID) THEN
        OPEN cur_restricao(var_plantacao_ID, var_fator_producao_ID);

        LOOP
            FETCH cur_restricao INTO var_inicio_restricao, var_fim_restricao;
            EXIT WHEN cur_restricao%NOTFOUND;

            IF var_data_accao >= var_inicio_restricao AND var_data_accao <= var_fim_restricao THEN
                RAISE var_dtaacc_cond1_exc;
            END IF;
        END LOOP;
        CLOSE cur_restricao;

    END IF;

    SELECT Data_Inicio_Cultura INTO var_data_inicio_cultura
        FROM Plantacao plant
        WHERE plant.ID = var_plantacao_ID;

    IF var_data_inicio_cultura > var_data_accao THEN
        RAISE var_dtaacc_cond2_exc;
    END IF;

    SELECT Data_Fim_Cultura INTO var_data_fim_cultura
        FROM Plantacao plant
        WHERE plant.ID = var_plantacao_ID;

    IF NOT var_data_fim_cultura IS NULL AND var_data_accao > var_data_fim_cultura THEN
        RAISE var_dtaacc_cond3_exc;
    END IF;

    IF var_quantidade <= 0 THEN
        RAISE var_qnt_cond_exc;
    END IF;
    
    var_accao_ID := Gen_Accao_ID_FUNC;

    INSERT INTO Accao VALUES (var_accao_ID, var_plantacao_ID, var_data_accao, var_quantidade);
    INSERT INTO Aplicacao_Fator_Producao VALUES (var_accao_ID, var_fator_producao_ID, var_metodo_dist_ID);

    DBMS_OUTPUT.PUT_LINE('Aplicação Fator de Produção inserida com sucesso;');

EXCEPTION
    WHEN var_plant_null_exc THEN
        RAISE_APPLICATION_ERROR(-20001, 'Não foi indicada uma Plantação;');
    WHEN var_dtaacc_null_exc THEN
        RAISE_APPLICATION_ERROR(-20002, 'Não foi indicada a Data de Realização da Aplicação do Fator de Produção;');
    WHEN var_qnt_null_exc THEN
        RAISE_APPLICATION_ERROR(-20003, 'Não foi indicada a Quantidade do Fator de Produção utilizado na Aplicação do Fator de Produção;');
    WHEN var_ftprod_null_exc THEN
        RAISE_APPLICATION_ERROR(-20004, 'Não foi indicada um Fator Produção;');
    WHEN var_metdist_null_exc THEN
        RAISE_APPLICATION_ERROR(-20005, 'Não foi indicada o Método de Distribuição utilizado na Aplicação do Fator de Produção;');
    WHEN var_plant_ext_exc THEN
        RAISE_APPLICATION_ERROR(-20006, 'Plantação indicada não existe;');
    WHEN var_ftprod_ext_exc THEN
        RAISE_APPLICATION_ERROR(-20007, 'Fator de Produção indicada não existe;');
    WHEN var_metdist_ext_exc THEN
        RAISE_APPLICATION_ERROR(-20008, 'Método Distribuição indicada não existe;');
    WHEN var_dtaacc_cond1_exc THEN
        RAISE_APPLICATION_ERROR(-20009, 'Data de Realização da Aplicação do Fator de Produção decorre durante o Período de Aplicação de uma Restrição;');
    WHEN var_dtaacc_cond2_exc THEN
        RAISE_APPLICATION_ERROR(-20010, 'Data de Realização da Aplicação do Fator de Produção antecede o Início da Plantação;');
    WHEN var_dtaacc_cond3_exc THEN
        RAISE_APPLICATION_ERROR(-20011, 'Data de Realização da Aplicação do Fator de Produção precede o Fim da Plantação;');
    WHEN var_qnt_cond_exc THEN
        RAISE_APPLICATION_ERROR(-20012, 'Quantidade de Fator de Produção utilizado na Aplicação do Fator de Produção deve ser superior a 0;');
END;
/

CREATE OR REPLACE TRIGGER Registar_Apli_Fator_Prod_TRIG BEFORE INSERT ON Aplicacao_Fator_Producao FOR EACH ROW
DECLARE
    var_accndis1_exc EXCEPTION;
    var_accndis2_exc EXCEPTION;
    var_accndis3_exc EXCEPTION;
    var_dtaacc_cond1_exc EXCEPTION;

    var_inicio_restricao Restricao.Inicio_Restricao%TYPE;
    var_fim_restricao Restricao.Fim_Restricao%TYPE;
    var_data_accao Accao.Data_Accao%TYPE;

    CURSOR cur_restricao(var_acc_ID Aplicacao_Fator_Producao.Accao_ID%TYPE, var_ft_prod_ID Aplicacao_Fator_Producao.Fator_Producao_ID%TYPE) IS
        SELECT Inicio_Restricao, Fim_Restricao
        FROM Restricao restricao
        INNER JOIN Plantacao plant
            ON plant.Campo_ID = restricao.Campo_ID
        INNER JOIN Accao acc
            ON acc.Plantacao_ID = plant.ID
        WHERE restricao.Fator_Producao_ID = var_ft_prod_ID
        AND acc.ID = var_acc_ID;
BEGIN
    IF Accao_Ass_Adubacao_FUNC(:NEW.Accao_ID) THEN
        RAISE var_accndis1_exc;
    END IF;

    IF Accao_Ass_Rega_FUNC(:NEW.Accao_ID) THEN
        RAISE var_accndis2_exc;
    END IF;

    IF Accao_Ass_Colheita_FUNC(:NEW.Accao_ID) THEN
        RAISE var_accndis3_exc;
    END IF;

    IF Restricao_Prod_Campo_Existe2_FUNC(:NEW.Accao_ID, :NEW.Fator_Producao_ID) THEN
        OPEN cur_restricao(:NEW.Accao_ID, :NEW.Fator_Producao_ID);

        LOOP
            FETCH cur_restricao INTO var_inicio_restricao, var_fim_restricao;
            EXIT WHEN cur_restricao%NOTFOUND;

            SELECT Data_Accao INTO var_data_accao
            FROM Accao acc
            WHERE acc.ID = :NEW.Accao_ID;

            IF var_data_accao >= var_inicio_restricao AND var_data_accao <= var_fim_restricao THEN
                RAISE var_dtaacc_cond1_exc;
            END IF;
        END LOOP;
        CLOSE cur_restricao;

    END IF;
EXCEPTION
    WHEN var_accndis1_exc THEN
        RAISE_APPLICATION_ERROR(-20001, 'Acção previamente associada a Operação de Adubação;');
    WHEN var_accndis2_exc THEN
        RAISE_APPLICATION_ERROR(-20002, 'Acção previamente associada a Operação de Aplicação de Fator de Produção;');
    WHEN var_accndis3_exc THEN
        RAISE_APPLICATION_ERROR(-20003, 'Acção previamente associada a Operação de Colheita;');
    WHEN var_dtaacc_cond1_exc THEN
        RAISE_APPLICATION_ERROR(-20004, 'Data de Realização da Aplicação do Fator de Produção decorre durante o Período de Aplicação de uma Restrição;');
END Registar_Apli_Fator_Prod_TRIG;
/

/*--- US210.A.D - Registar Colheita ---*/
CREATE OR REPLACE PROCEDURE Registar_Colheita_PROC(var_plantacao_ID Accao.Plantacao_ID%TYPE, var_data_accao Accao.Data_Accao%TYPE, var_quantidade Accao.Quantidade%TYPE, var_preco Colheita.Preco%TYPE, var_produto_ID Colheita.Produto_ID%TYPE)
AS
    var_plant_null_exc EXCEPTION;
    var_dtaacc_null_exc EXCEPTION;
    var_qnt_null_exc EXCEPTION;
    var_prc_null_exc EXCEPTION;
    var_prod_null_exc EXCEPTION;
    var_plant_ext_exc EXCEPTION;
    var_prod_ext_exc EXCEPTION;
    var_dtaacc_cond1_exc EXCEPTION;
    var_dtaacc_cond2_exc EXCEPTION;
    var_qnt_cond_exc EXCEPTION;
    var_prc_cond_exc EXCEPTION;

    var_data_inicio_cultura Plantacao.Data_Inicio_Cultura%TYPE;
    var_data_fim_cultura Plantacao.Data_Fim_Cultura%TYPE;
    var_accao_ID Accao.ID%TYPE;
BEGIN
    IF var_plantacao_ID IS NULL THEN
        RAISE var_plant_null_exc;
    END IF;

    IF var_data_accao IS NULL THEN
        RAISE var_dtaacc_null_exc;
    END IF;

    IF var_quantidade IS NULL THEN
        RAISE var_qnt_null_exc;
    END IF;

    IF var_preco IS NULL THEN
        RAISE var_prc_null_exc;
    END IF;

    IF var_produto_ID IS NULL THEN
        RAISE var_prod_null_exc;
    END IF;

    IF NOT Plantacao_Existe_FUNC(var_plantacao_ID) THEN
        RAISE var_plant_ext_exc;
    END IF;

    IF NOT Produto_Existe_FUNC(var_produto_ID) THEN
        RAISE var_prod_ext_exc;
    END IF;

    SELECT Data_Inicio_Cultura INTO var_data_inicio_cultura
        FROM Plantacao plant
        WHERE plant.ID = var_plantacao_ID;

    IF var_data_inicio_cultura > var_data_accao THEN
        RAISE var_dtaacc_cond1_exc;
    END IF;

    SELECT Data_Fim_Cultura INTO var_data_fim_cultura
        FROM Plantacao plant
        WHERE plant.ID = var_plantacao_ID;

    IF NOT var_data_fim_cultura IS NULL AND var_data_accao > var_data_fim_cultura THEN
        RAISE var_dtaacc_cond2_exc;
    END IF;

    IF var_quantidade <= 0 THEN
        RAISE var_qnt_cond_exc;
    END IF;

    IF var_preco <= 0 THEN
        RAISE var_prc_cond_exc;
    END IF;
    
    var_accao_ID := Gen_Accao_ID_FUNC;

    INSERT INTO Accao VALUES (var_accao_ID, var_plantacao_ID, var_data_accao, var_quantidade);
    INSERT INTO Colheita VALUES (var_accao_ID, var_produto_ID, var_preco);

    DBMS_OUTPUT.PUT_LINE('Colheita inserida com sucesso;');

EXCEPTION
    WHEN var_plant_null_exc THEN
        RAISE_APPLICATION_ERROR(-20001, 'Não foi indicada uma Plantação;');
    WHEN var_dtaacc_null_exc THEN
        RAISE_APPLICATION_ERROR(-20002, 'Não foi indicada a Data de Realização da Colheita;');
    WHEN var_qnt_null_exc THEN
        RAISE_APPLICATION_ERROR(-20003, 'Não foi indicada a Quantidade de Produtos colhidos na Colheita;');
    WHEN var_prc_null_exc THEN
        RAISE_APPLICATION_ERROR(-20004, 'Não foi indicada o Preço dos Produtos colhidos na Colheita;');
    WHEN var_prod_null_exc THEN
        RAISE_APPLICATION_ERROR(-20005, 'Não foi indicada o Produto colhido na Colheita;');
    WHEN var_plant_ext_exc THEN
        RAISE_APPLICATION_ERROR(-20006, 'Plantação indicada não existe;');
    WHEN var_prod_ext_exc THEN
        RAISE_APPLICATION_ERROR(-20007, 'Produto indicado não existe;');
    WHEN var_dtaacc_cond1_exc THEN
        RAISE_APPLICATION_ERROR(-20008, 'Data de Realização da Colheita antecede o Início da Plantação;');
    WHEN var_dtaacc_cond2_exc THEN
        RAISE_APPLICATION_ERROR(-20009, 'Data de Realização da Colheita precede o Fim da Plantação;');
    WHEN var_qnt_cond_exc THEN
        RAISE_APPLICATION_ERROR(-20010, 'Quantidade de Produtos colhidos na Colheita deve ser superior a 0;');
    WHEN var_prc_cond_exc THEN
        RAISE_APPLICATION_ERROR(-20011, 'Preço dos Produtos colhidos na Colheita deve ser superior a 0;');
END;
/

CREATE OR REPLACE TRIGGER Registar_Colheita_TRIG BEFORE INSERT ON Colheita FOR EACH ROW
DECLARE
    var_accndis1_exc EXCEPTION;
    var_accndis2_exc EXCEPTION;
    var_accndis3_exc EXCEPTION;
BEGIN
    IF Accao_Ass_Rega_FUNC(:NEW.Accao_ID) THEN
        RAISE var_accndis1_exc;
    END IF;

    IF Accao_Ass_Apli_Fator_Prod_FUNC(:NEW.Accao_ID) THEN
        RAISE var_accndis2_exc;
    END IF;

    IF Accao_Ass_Adubacao_FUNC(:NEW.Accao_ID) THEN
        RAISE var_accndis3_exc;
    END IF;
EXCEPTION
    WHEN var_accndis1_exc THEN
        RAISE_APPLICATION_ERROR(-20001, 'Acção previamente associada a Operação de Rega;');
    WHEN var_accndis2_exc THEN
        RAISE_APPLICATION_ERROR(-20002, 'Acção previamente associada a Operação de Aplicação de Fator de Produção;');
    WHEN var_accndis3_exc THEN
        RAISE_APPLICATION_ERROR(-20003, 'Acção previamente associada a Operação de Adubação;');
END Registar_Colheita_TRIG;
/

/*SUCCESS*/
EXEC Registar_Rega_PROC(1, TO_DATE('23/11/2023', 'DD/MM/YYYY'), 23.11, 1);
/*FAIL INSERTS*/
EXEC Registar_Rega_PROC(2311, TO_DATE('23/11/2023', 'DD/MM/YYYY'), 23.11, 1);
EXEC Registar_Rega_PROC(1, TO_DATE('23/11/1967', 'DD/MM/YYYY'), 23.11, 1);
EXEC Registar_Rega_PROC(1, TO_DATE('23/11/2023', 'DD/MM/YYYY'), -23.11, 1);
EXEC Registar_Rega_PROC(1, TO_DATE('23/11/2023', 'DD/MM/YYYY'), 23.11, 1123);

/*SUCCESS*/
EXEC Registar_Adubacao_PROC(2, TO_DATE('4/12/2023', 'DD/MM/YYYY'), 12.4, 2);
/*FAIL INSERTS*/
EXEC Registar_Adubacao_PROC(412, TO_DATE('4/12/2023', 'DD/MM/YYYY'), 12.4, 2);
EXEC Registar_Adubacao_PROC(2, TO_DATE('4/12/1967', 'DD/MM/YYYY'), 12.4, 2);
EXEC Registar_Adubacao_PROC(2, TO_DATE('4/12/2023', 'DD/MM/YYYY'), -12.4, 2);
EXEC Registar_Adubacao_PROC(2, TO_DATE('4/12/2023', 'DD/MM/YYYY'), 12.4, 162);

/*SUCCESS*/
EXEC Registar_Aplicacao_Fator_Producao_PROC(1, TO_DATE('02/02/2023', 'DD/MM/YYYY'), 16.2, 1, 2);
/*FAIL INSERTS*/
EXEC Registar_Aplicacao_Fator_Producao_PROC(168, TO_DATE('02/02/2023', 'DD/MM/YYYY'), 16.2, 1, 2);
EXEC Registar_Aplicacao_Fator_Producao_PROC(1, TO_DATE('02/02/1967', 'DD/MM/YYYY'), 16.2, 1, 2);
EXEC Registar_Aplicacao_Fator_Producao_PROC(1, TO_DATE('02/02/2023', 'DD/MM/YYYY'), -16.2, 1, 2);
EXEC Registar_Aplicacao_Fator_Producao_PROC(1, TO_DATE('02/02/2023', 'DD/MM/YYYY'), 16.2, -1, 2);
EXEC Registar_Aplicacao_Fator_Producao_PROC(1, TO_DATE('02/02/2023', 'DD/MM/YYYY'), 16.2, 1, 2267);

/*SUCCESS*/
EXEC Registar_Colheita_PROC(1, TO_DATE('16/8/2023', 'DD/MM/YYYY'), 16.8, 2.12, 2);
/*FAIL INSERTS*/
EXEC Registar_Colheita_PROC(124, TO_DATE('16/8/2022', 'DD/MM/YYYY'), 16.8, 2.12, 2);
EXEC Registar_Colheita_PROC(1, TO_DATE('16/8/1967', 'DD/MM/YYYY'), 16.8, 2.12, 2);
EXEC Registar_Colheita_PROC(1, TO_DATE('16/8/2022', 'DD/MM/YYYY'), -16.8, 2.12, 2);
EXEC Registar_Colheita_PROC(1, TO_DATE('16/8/2022', 'DD/MM/YYYY'), 16.8, -2.12, 2);
EXEC Registar_Colheita_PROC(1, TO_DATE('16/8/2022', 'DD/MM/YYYY'), 16.8, 2.12, 672);