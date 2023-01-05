CREATE OR REPLACE PACKAGE Restricao_PACK AS
    TYPE Restricao_Data_REC IS RECORD (
        Campo_ID Campo.ID%TYPE,
        Campo_Designacao Campo.Designacao%TYPE,
        Campo_Hectares Campo.Hectares%TYPE,
        Fator_Producao_ID Fator_Producao.ID%TYPE,
        Fator_Producao_Nome Fator_Producao.Nome%TYPE,
        Tipo_Fator_Producao_ID Tipo_Fator_Producao.ID%TYPE,
        Tipo_Fator_Producao_Designacao Tipo_Fator_Producao.Designacao%TYPE,
        Inicio_Restricao Restricao.Inicio_Restricao%TYPE,
        Fim_Restricao Restricao.Fim_Restricao%TYPE
    );

    TYPE Restricao_Data_TAB IS TABLE OF Restricao_Data_REC;

    FUNCTION Restricao_Curto_Prazo_FUNC(var_data DATE) RETURN Restricao_Data_TAB PIPELINED;
    FUNCTION Campo_Existe_FUNC(var_campo_ID Campo.ID%TYPE) RETURN BOOLEAN;
    FUNCTION Restricao_Campo_Data_FUNC (var_data DATE, var_campo_ID Campo.ID%TYPE) RETURN Restricao_Data_TAB PIPELINED;
    FUNCTION Restricao_Periodo_Ord_FUNC(var_inicio_periodo DATE, var_fim_periodo DATE) RETURN Restricao_Data_TAB PIPELINED;
END Restricao_PACK;
/

CREATE OR REPLACE PACKAGE BODY Restricao_PACK AS
    /*--- Funções Utilitárias ---*/
    FUNCTION Campo_Existe_FUNC (var_campo_ID Campo.ID%TYPE) RETURN BOOLEAN
    AS
        var_cnt INT;
    BEGIN
        SELECT COUNT(*) INTO var_cnt
            FROM Campo campo
            WHERE campo.ID = var_campo_ID;

        RETURN var_cnt > 0;
    END Campo_Existe_FUNC;

    /*--- Critério Aceitação #3 ---*/
    FUNCTION Restricao_Curto_Prazo_FUNC(var_data DATE) RETURN Restricao_Data_TAB PIPELINED
    AS
        CURSOR cur_restricao(var_data DATE) IS
            SELECT campo.ID AS Campo_ID, campo.Designacao AS Campo_Designacao, campo.Hectares AS Campo_Hectares, fator_prod.ID AS Fator_Producao_ID, fator_prod.Nome AS Fator_Producao_Nome, tipo_fator_prod.ID AS Tipo_Fator_Producao_ID, tipo_fator_prod.Designacao AS Tipo_Fator_Producao_Designacao, restricao.Inicio_Restricao AS Inicio_Restricao, restricao.Fim_Restricao AS Fim_Restricao
            FROM Restricao restricao
            INNER JOIN Campo campo
                ON campo.ID = restricao.Campo_ID
            INNER JOIN Fator_Producao fator_prod
                ON fator_prod.ID = restricao.Fator_Producao_ID
            INNER JOIN Tipo_Fator_Producao tipo_fator_prod
                ON tipo_fator_prod.ID = fator_prod.Tipo_Fator_Producao_ID
            WHERE restricao.Inicio_Restricao = var_data + 7;
    BEGIN
        FOR rest IN cur_restricao(var_data)
        LOOP
            PIPE ROW(Restricao_Data_REC(rest.Campo_ID, rest.Campo_Designacao, rest.Campo_Hectares, rest.Fator_Producao_ID, rest.Fator_Producao_Nome, rest.Tipo_Fator_Producao_ID, rest.Tipo_Fator_Producao_Designacao, rest.Inicio_Restricao, rest.Fim_Restricao));
            EXIT WHEN cur_restricao%NOTFOUND;
        END LOOP;

        RETURN;
    END Restricao_Curto_Prazo_FUNC;

    /*--- Critério Aceitação #4 ---*/
    FUNCTION Restricao_Campo_Data_FUNC (var_data DATE, var_campo_ID Campo.ID%TYPE) RETURN Restricao_Data_TAB PIPELINED
    AS
        var_cmp_null_exc EXCEPTION;

        var_rest_campo_ID Campo.ID%TYPE;
        var_campo_area Campo.Hectares%TYPE;
        var_campo_desig Campo.Designacao%TYPE;
        var_fator_prod_ID Fator_Producao.ID%TYPE;
        var_fator_prod_nome Fator_Producao.Nome%TYPE;
        var_tipo_fator_prod_ID Tipo_Fator_Producao.ID%TYPE;
        var_tipo_fator_prod_desig Tipo_Fator_Producao.Designacao%TYPE;
        var_inicio_restricao Restricao.Inicio_Restricao%TYPE;
        var_fim_restricao Restricao.Fim_Restricao%TYPE;

        CURSOR cur_restricao(var_data DATE, var_campo_ID Campo.ID%TYPE) IS
            SELECT campo.ID AS Campo_ID, campo.Designacao AS Campo_Designacao, campo.Hectares AS Campo_Hectares, fator_prod.ID AS Fator_Producao_ID, fator_prod.Nome AS Fator_Producao_Nome, tipo_fator_prod.ID AS Tipo_Fator_Producao_ID, tipo_fator_prod.Designacao AS Tipo_Fator_Producao_Designacao, rest.Inicio_Restricao AS Inicio_Restricao, rest.Fim_Restricao AS Fim_Restricao
            FROM Restricao rest
            INNER JOIN Campo campo
                ON campo.ID = rest.Campo_ID
            INNER JOIN Fator_Producao fator_prod
                ON fator_prod.ID = rest.Fator_Producao_ID
            INNER JOIN Tipo_Fator_Producao tipo_fator_prod
                ON fator_prod.Tipo_Fator_Producao_ID = tipo_fator_prod.ID
            WHERE rest.Campo_ID = var_campo_ID
            AND rest.Inicio_Restricao <= var_data
            AND rest.Fim_Restricao >= var_data;
    BEGIN
        IF NOT Campo_Existe_FUNC(var_campo_ID) THEN
            RAISE var_cmp_null_exc;
        END IF;

        FOR rest IN cur_restricao(var_data, var_campo_ID)
        LOOP
            PIPE ROW(Restricao_Data_REC(rest.Campo_ID, rest.Campo_Designacao, rest.Campo_Hectares, rest.Fator_Producao_ID, rest.Fator_Producao_Nome, rest.Tipo_Fator_Producao_ID, rest.Tipo_Fator_Producao_Designacao, rest.Inicio_Restricao, rest.Fim_Restricao));
            EXIT WHEN cur_restricao%NOTFOUND;
        END LOOP;

        RETURN;
    END Restricao_Campo_Data_FUNC;

    /*--- Critério Aceitação #5 ---*/
    FUNCTION Restricao_Periodo_Ord_FUNC(var_inicio_periodo DATE, var_fim_periodo DATE) RETURN Restricao_Data_TAB PIPELINED
    AS
        CURSOR cur_restricao(var_inicio_periodo DATE, var_fim_periodo DATE) IS
            SELECT campo.ID AS Campo_ID, campo.Designacao AS Campo_Designacao, campo.Hectares AS Campo_Hectares, fator_prod.ID AS Fator_Producao_ID, fator_prod.Nome AS Fator_Producao_Nome, tipo_fator_prod.ID AS Tipo_Fator_Producao_ID, tipo_fator_prod.Designacao AS Tipo_Fator_Producao_Designacao, rest.Inicio_Restricao AS Inicio_Restricao, rest.Fim_Restricao AS Fim_Restricao
            FROM Restricao rest
            INNER JOIN Campo campo
                ON campo.ID = rest.Campo_ID
            INNER JOIN Fator_Producao fator_prod
                ON fator_prod.ID = rest.Fator_Producao_ID
            INNER JOIN Tipo_Fator_Producao tipo_fator_prod
                ON tipo_fator_prod.ID = fator_prod.Tipo_Fator_Producao_ID
            WHERE rest.Inicio_Restricao >= var_inicio_periodo
            AND rest.Fim_Restricao <= var_fim_periodo
            ORDER BY rest.Inicio_Restricao;

    BEGIN
        FOR rest IN cur_restricao(var_inicio_periodo, var_fim_periodo)
        LOOP
            PIPE ROW(Restricao_Data_REC(rest.Campo_ID, rest.Campo_Designacao, rest.Campo_Hectares, rest.Fator_Producao_ID, rest.Fator_Producao_Nome, rest.Tipo_Fator_Producao_ID, rest.Tipo_Fator_Producao_Designacao, rest.Inicio_Restricao, rest.Fim_Restricao));
            EXIT WHEN cur_restricao%NOTFOUND;
        END LOOP;

        RETURN;
    END Restricao_Periodo_Ord_FUNC;
END;
/