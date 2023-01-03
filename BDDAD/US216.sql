--acceptance criteria 4
--Analisar a evolução das vendas mensais por tipo de cultura e hub?

--é usado a função verificacao_ano_FUNC feita na US214 AC6C


CREATE OR REPLACE FUNCTION vendas_mensais_hub_qtd_cultura_PROC(ano Tempo_DIM.year%TYPE, mes Tempo_DIM.month%TYPE, p_id Produto_DIM.Tipo_Cultura_ID%TYPE, p_hub Local_Recolha_DIM.Hub_ID%TYPE) RETURN INTEGER IS
v_qtd INTEGER := 0;
ex_counter EXCEPTION;
BEGIN
    SELECT count(v.quantidade) INTO v_qtd FROM Venda_FAC v 
        INNER JOIN Produto_DIM p ON p.ID = v.Produto_ID
            INNER JOIN Tempo_DIM t ON v.tempo_ID = t.ID
                INNER JOIN Local_Recolha_DIM l ON l.ID = v.Local_Recolha_ID
                    WHERE t.year = ano AND t.month = mes AND p.Tipo_Cultura_ID = p_id AND l.Hub_ID = p_hub;

    IF v_qtd = 0 THEN
        RAISE ex_counter;
    END IF;

    SELECT sum(v.quantidade) INTO v_qtd FROM Venda_FAC v 
        INNER JOIN Produto_DIM p ON p.ID = v.Produto_ID
            INNER JOIN Tempo_DIM t ON v.tempo_ID = t.ID
                INNER JOIN Local_Recolha_DIM l ON l.ID = v.Local_Recolha_ID
                    WHERE t.year = ano AND t.month = mes AND p.Tipo_Cultura_ID = p_id AND l.Hub_ID = p_hub;

    RETURN v_qtd;

EXCEPTION
WHEN no_data_found THEN
    RETURN 0;
WHEN ex_counter THEN
    RETURN 0;
END;
/

CREATE OR REPLACE FUNCTION vendas_mensais_hub_valor_cultura_PROC(ano Tempo_DIM.year%TYPE, mes Tempo_DIM.month%TYPE, p_id Produto_DIM.Tipo_Cultura_ID%TYPE, p_hub Local_Recolha_DIM.Hub_ID%TYPE) RETURN INTEGER IS
v_preco INTEGER := 0;
ex_counter EXCEPTION;
BEGIN
    SELECT count(v.Preco) INTO v_preco FROM Venda_FAC v 
        INNER JOIN Produto_DIM p ON p.ID = v.Produto_ID
            INNER JOIN Tempo_DIM t ON v.tempo_ID = t.ID
                INNER JOIN Local_Recolha_DIM l ON l.ID = v.Local_Recolha_ID
                WHERE t.year = ano AND t.month = mes AND p.Tipo_Cultura_ID = p_id AND l.Hub_ID = p_hub;

    IF v_preco = 0 THEN
        RAISE ex_counter;
    END IF;

    SELECT sum(v.Preco) INTO v_preco FROM Venda_FAC v 
        INNER JOIN Produto_DIM p ON p.ID = v.Produto_ID
            INNER JOIN Tempo_DIM t ON v.tempo_ID = t.ID
                INNER JOIN Local_Recolha_DIM l ON l.ID = v.Local_Recolha_ID
                    WHERE t.year = ano AND t.month = mes AND p.Tipo_Cultura_ID = p_id AND l.Hub_ID = p_hub;

    RETURN v_preco;
EXCEPTION
WHEN no_data_found THEN
    RETURN 0;
WHEN ex_counter THEN
    RETURN 0;
END;
/




CREATE OR REPLACE PROCEDURE vendas_mensais_hub_PROC(ano Tempo_DIM.Year%TYPE, p_hub Local_Recolha_DIM.Hub_ID%TYPE) IS
CURSOR c_hubs IS SELECT DISTINCT p.Tipo_Cultura_ID, tc.designacao FROM Venda_FAC v 
        INNER JOIN Produto_DIM p ON p.ID = v.Produto_ID
            INNER JOIN Tempo_DIM t ON v.tempo_ID = t.ID
                INNER JOIN Local_Recolha_DIM l ON l.ID = v.Local_Recolha_ID
                    INNER JOIN Tipo_Cultura tc ON tc.ID = p.Tipo_Cultura_ID
                        WHERE t.year = ano AND l.Hub_ID = p_hub;
begin
    FOR i IN c_hubs LOOP
        DBMS_OUTPUT.PUT_LINE('Tipo de cultura: '||i.designacao);
        DBMS_OUTPUT.PUT_LINE(chr(0));
        DBMS_OUTPUT.PUT_LINE('Vendas mensais: ');
        FOR j IN 1..12 LOOP
            DBMS_OUTPUT.PUT_LINE('Mês ' || j ||'   Quantidade: '||vendas_mensais_hub_qtd_cultura_PROC(ano, j, i.tipo_cultura_id, p_hub)||' toneladas   Valor: '||vendas_mensais_hub_valor_cultura_PROC(ano, j, i.tipo_cultura_id, p_hub)||' k€');
        END LOOP;
    END LOOP;
end;
/




CREATE OR REPLACE PROCEDURE vendas_mensais_hub_MAIN_PROC(ano Tempo_DIM.Year%TYPE) IS
nr_culturas INTEGER := verificacao_ano_FUNC(ano);
CURSOR c_hubs IS SELECT DISTINCT l.Hub_ID FROM Venda_FAC v 
        INNER JOIN Tempo_DIM t ON v.tempo_ID = t.ID
                INNER JOIN Local_Recolha_DIM l ON l.ID = v.Local_Recolha_ID
                    WHERE t.year = ano;

BEGIN
    FOR i IN c_hubs LOOP
    DBMS_OUTPUT.PUT_LINE(chr(0));
        DBMS_OUTPUT.PUT_LINE('Hub: ' || i.Hub_ID);
        DBMS_OUTPUT.PUT_LINE(chr(0));
        vendas_mensais_hub_PROC(ano, i.Hub_ID);
    END LOOP;
END;
/





BEGIN

vendas_mensais_hub_MAIN_PROC(2017);

END;
/
