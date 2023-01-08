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

CREATE OR REPLACE FUNCTION Accao_Existe_FUNC(var_accao_ID Accao.ID%TYPE) RETURN BOOLEAN
AS
    var_cnt INT;
BEGIN
    SELECT COUNT(*) INTO var_cnt
        FROM Accao acc
        WHERE acc.ID = var_accao_ID;

    RETURN var_cnt > 0;
END;
/

CREATE OR REPLACE TRIGGER Accao_TRIG BEFORE DELETE OR UPDATE ON Accao FOR EACH ROW
DECLARE
    var_dtaacc_cond1_exc EXCEPTION;
    var_dtaacc_cond2_exc EXCEPTION;
    var_dtaacc_cond3_exc EXCEPTION;
BEGIN
    IF DELETING THEN
        IF :OLD.Data_Accao < SYSDATE THEN
            RAISE var_dtaacc_cond1_exc;
        END IF;

        IF Accao_Ass_Adubacao_FUNC(:OLD.ID) THEN
            DELETE FROM Adubacao adub
            WHERE adub.Accao_ID = :OLD.ID;
        ELSIF Accao_Ass_Apli_Fator_Prod_FUNC(:OLD.ID) THEN
            DELETE FROM Aplicacao_Fator_Producao apli_fator_prod
            WHERE apli_fator_prod.Accao_ID = :OLD.ID;
        ELSIF Accao_Ass_Colheita_FUNC(:OLD.ID) THEN
            DELETE FROM Colheita colheita
            WHERE colheita.Accao_ID = :OLD.ID;
        ELSE
            DELETE FROM Rega rega
            WHERE rega.Accao_ID = :OLD.ID;
        END IF;
    END IF;

    IF UPDATING THEN
        IF :OLD.Data_Accao < SYSDATE THEN
            RAISE var_dtaacc_cond2_exc;
        END IF;

        IF :NEW.Data_Accao < SYSDATE THEN
            RAISE var_dtaacc_cond3_exc;
        END IF;
    END IF;
EXCEPTION
    WHEN var_dtaacc_cond1_exc THEN
        RAISE_APPLICATION_ERROR(-20001, 'Não é possível eliminar o Registo da Acção em causa, visto que a Operação já ocorreu;');
    WHEN var_dtaacc_cond2_exc THEN
        RAISE_APPLICATION_ERROR(-20001, 'Não é possível atualizar o Registo da Acção em causa, visto que a Operação já ocorreu;');
    WHEN var_dtaacc_cond3_exc THEN
        RAISE_APPLICATION_ERROR(-20001, 'Não é possível atualizar o Registo da Acção em causa, visto que a Data inserida já passou;');
END Accao_TRIG;
/

CREATE OR REPLACE TRIGGER Atualizar_Rega_TRIG BEFORE UPDATE ON Rega FOR EACH ROW
DECLARE
    var_data_accao Accao.Data_Accao%TYPE;

    var_dtaacc_cond1_exc EXCEPTION;
BEGIN
    SELECT Data_Accao INTO var_data_accao
    FROM Accao acc
    WHERE :OLD.Accao_ID = acc.ID;

    IF var_data_accao < SYSDATE THEN
        RAISE var_dtaacc_cond1_exc;
    END IF;
EXCEPTION
    WHEN var_dtaacc_cond1_exc THEN
        RAISE_APPLICATION_ERROR(-20001, 'Não é possível atualizar o Método de Distribuição associado ao Registo Rega em causa, visto que a Operação já ocorreu;');
END Atualizar_Rega_TRIG;
/

CREATE OR REPLACE TRIGGER Atualizar_Adubacao_TRIG BEFORE UPDATE ON Adubacao FOR EACH ROW
DECLARE
    var_data_accao Accao.Data_Accao%TYPE;

    var_dtaacc_cond1_exc EXCEPTION;
BEGIN
    SELECT Data_Accao INTO var_data_accao
    FROM Accao acc
    WHERE :OLD.Accao_ID = acc.ID;

    IF var_data_accao < SYSDATE THEN
        RAISE var_dtaacc_cond1_exc;
    END IF;
EXCEPTION
    WHEN var_dtaacc_cond1_exc THEN
        RAISE_APPLICATION_ERROR(-20001, 'Não é possível atualizar o Método de Distribuição associado ao Registo Adubação em causa, visto que a Operação já ocorreu;');
END Atualizar_Adubacao_TRIG;
/

CREATE OR REPLACE TRIGGER Atualizar_Aplicacao_Fator_Producao_TRIG BEFORE UPDATE ON Aplicacao_Fator_Producao FOR EACH ROW
DECLARE
    var_data_accao Accao.Data_Accao%TYPE;

    var_dtaacc_cond1_exc EXCEPTION;
BEGIN
    SELECT Data_Accao INTO var_data_accao
    FROM Accao acc
    WHERE :OLD.Accao_ID = acc.ID;

    IF var_data_accao < SYSDATE THEN
        RAISE var_dtaacc_cond1_exc;
    END IF;
EXCEPTION
    WHEN var_dtaacc_cond1_exc THEN
        RAISE_APPLICATION_ERROR(-20001, 'Não é possível atualizar o Registo Aplicação Fator de Produção em causa, visto que a Operação já ocorreu;');
END Atualizar_Aplicacao_Fator_Producao_TRIG;
/

CREATE OR REPLACE TRIGGER Atualizar_Colheita_TRIG BEFORE UPDATE ON Colheita FOR EACH ROW
DECLARE
    var_data_accao Accao.Data_Accao%TYPE;

    var_dtaacc_cond1_exc EXCEPTION;
BEGIN
    SELECT Data_Accao INTO var_data_accao
    FROM Accao acc
    WHERE :OLD.Accao_ID = acc.ID;

    IF var_data_accao < SYSDATE THEN
        RAISE var_dtaacc_cond1_exc;
    END IF;
EXCEPTION
    WHEN var_dtaacc_cond1_exc THEN
        RAISE_APPLICATION_ERROR(-20001, 'Não é possível atualizar o Registo Colheita em causa, visto que a Operação já ocorreu;');
END Atualizar_Colheita_TRIG;
/