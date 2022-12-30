        ------------acceptance criteria 6C----------------

--c) Analisar a evolução das vendas mensais por tipo de cultura?

CREATE OR REPLACE FUNCTION verificacao_ano_FUNC(ano Tempo_DIM.Year%TYPE) RETURN INTEGER IS
nr_culturas INTEGER;
ex_cultura EXCEPTION;
BEGIN
SELECT COUNT(DISTINCT p.Cultura_ID) INTO nr_culturas FROM Venda_FAC v 
    INNER JOIN Produto_DIM p ON p.ID = v.Produto_ID
        INNER JOIN Tempo_DIM t ON v.tempo_ID = t.ID
            WHERE t.year = ano;

IF nr_culturas = 0 THEN
    RAISE ex_cultura;
END IF;

RETURN nr_culturas;

EXCEPTION
WHEN ex_cultura THEN
    RAISE_APPLICATION_ERROR(-20001, 'O ano ' || ano || ' não tem vendas registadas');
WHEN no_data_found THEN
    RAISE_APPLICATION_ERROR(-20002, 'O ano ' || ano || ' não tem vendas registadas');

END;
/


CREATE OR REPLACE FUNCTION vendas_mensais_qtd_cultura_PROC(ano Tempo_DIM.year%TYPE, mes Tempo_DIM.month%TYPE, p_id Produto_DIM.Cultura_ID%TYPE) RETURN INTEGER IS
v_qtd INTEGER := 0;
ex_counter EXCEPTION;
BEGIN
    SELECT count(v.quantidade) INTO v_qtd FROM Venda_FAC v 
        INNER JOIN Produto_DIM p ON p.ID = v.Produto_ID
            INNER JOIN Tempo_DIM t ON v.tempo_ID = t.ID
                WHERE t.year = ano AND t.month = mes AND p.Cultura_ID = p_id;

    IF v_qtd = 0 THEN
        RAISE ex_counter;
    END IF;

    SELECT sum(v.quantidade) INTO v_qtd FROM Venda_FAC v 
        INNER JOIN Produto_DIM p ON p.ID = v.Produto_ID
            INNER JOIN Tempo_DIM t ON v.tempo_ID = t.ID
                WHERE t.year = ano AND t.month = mes AND p.Cultura_ID = p_id;

    RETURN v_qtd;

EXCEPTION
WHEN no_data_found THEN
    RETURN 0;
WHEN ex_counter THEN
    RETURN 0;
END;
/

CREATE OR REPLACE FUNCTION vendas_mensais_valor_cultura_PROC(ano Tempo_DIM.year%TYPE, mes Tempo_DIM.month%TYPE, p_id Produto_DIM.Cultura_ID%TYPE) RETURN INTEGER IS
v_preco INTEGER := 0;
ex_counter EXCEPTION;
BEGIN
    SELECT count(v.Preco) INTO v_preco FROM Venda_FAC v 
        INNER JOIN Produto_DIM p ON p.ID = v.Produto_ID
            INNER JOIN Tempo_DIM t ON v.tempo_ID = t.ID
                WHERE t.year = ano AND t.month = mes AND p.Cultura_ID = p_id;

    IF v_preco = 0 THEN
        RAISE ex_counter;
    END IF;

    SELECT sum(v.Preco) INTO v_preco FROM Venda_FAC v 
        INNER JOIN Produto_DIM p ON p.ID = v.Produto_ID
            INNER JOIN Tempo_DIM t ON v.tempo_ID = t.ID
                WHERE t.year = ano AND t.month = mes AND p.Cultura_ID = p_id;

    RETURN v_preco;
EXCEPTION
WHEN no_data_found THEN
    RETURN 0;
WHEN ex_counter THEN
    RETURN 0;
END;
/


CREATE OR REPLACE PROCEDURE vendas_mensais_tipo_cultura_PROC(ano Tempo_DIM.Year%TYPE) IS
nr_culturas INTEGER := verificacao_ano_FUNC(ano);
CURSOR c_culturas IS SELECT DISTINCT c.nome, p.cultura_id FROM Venda_FAC v 
    INNER JOIN Produto_DIM p ON p.ID = v.Produto_ID
        INNER JOIN Tempo_DIM t ON v.tempo_ID = t.ID
            INNER JOIN Cultura c ON c.ID = p.Cultura_ID
                WHERE t.year = ano;

BEGIN
    FOR i IN c_culturas LOOP
    DBMS_OUTPUT.PUT_LINE(chr(0));
        DBMS_OUTPUT.PUT_LINE('Cultura: ' || i.nome);
        DBMS_OUTPUT.PUT_LINE(chr(0));
        DBMS_OUTPUT.PUT_LINE('Vendas mensais: ');
        FOR j IN 1..12 LOOP
            DBMS_OUTPUT.PUT_LINE('Mês ' || j ||'   Quantidade: '||vendas_mensais_qtd_cultura_PROC(ano, j, i.cultura_id)||' toneladas   Valor: '||vendas_mensais_valor_cultura_PROC(ano, j, i.cultura_id)||' k€');
        END LOOP;
    END LOOP;
END;
/

BEGIN

vendas_mensais_tipo_cultura_PROC(2016);

END;
/
