    ------------acceptance criteria 6A----------------

--a) Qual é a evolução da produção de uma determinada cultura num determinado setor ao longo
--dos últimos cinco anos?

CREATE OR REPLACE FUNCTION get_setor_ID_FUNC(v_nome Setor_DIM.Designacao%TYPE) RETURN Setor_DIM.ID%TYPE IS
nr_setores INTEGER;
v_ID Setor_DIM.ID%TYPE;
 ex_setor EXCEPTION;
BEGIN

SELECT COUNT(*) into nr_setores from Setor_DIM WHERE UPPER(designacao) = UPPER(v_nome);

IF(nr_setores = 0) THEN 
    RAISE ex_setor;
ELSE
    SELECT ID into v_ID from Setor_DIM WHERE Designacao = v_nome;
    RETURN v_ID;
end if;

EXCEPTION
    WHEN ex_setor THEN
        raise_application_error(-20001, 'Setor não existe!');
    WHEN no_data_found THEN
        raise_application_error(-20002, 'Setor não existe!');
END;
/



CREATE OR REPLACE FUNCTION get_cultura_ID_FUNC(v_nome Cultura.Nome%TYPE) RETURN Cultura.ID%TYPE IS
nr_cultura INTEGER;
v_ID Cultura.ID%TYPE;
ex_cultura EXCEPTION;
BEGIN

SELECT COUNT(*) into nr_cultura from Cultura WHERE UPPER(nome) = UPPER(v_nome);

IF(nr_cultura = 0) THEN 
    RAISE ex_cultura;
ELSE
    SELECT ID into v_ID from Cultura WHERE nome = v_nome;
    RETURN v_ID;
end if;

EXCEPTION
    WHEN no_data_found THEN
        raise_application_error(-20001, 'Cultura não existe!');
    WHEN ex_cultura THEN
        raise_application_error(-20002, 'Cultura não existe!');
END;
/




CREATE OR REPLACE PROCEDURE evolucao_producao_PROC (p_cultura Cultura.Nome%TYPE, p_setor Setor_DIM.Designacao%TYPE) IS  
    nr_cultura INTEGER;
    nr_setor INTEGER;
    ano_atual VARCHAR2(4) := To_char(sysdate,'yyyy');
    ano_1 INTEGER;
    ano_2 INTEGER;
    ano_3 INTEGER;
    ano_4 INTEGER;
    ano_5 INTEGER;
    qtd_ano_1 NUMBER := 0;
    qtd_ano_2 NUMBER := 0;
    qtd_ano_3 NUMBER := 0;
    qtd_ano_4 NUMBER := 0;
    qtd_ano_5 NUMBER := 0;
    v_setor_ID Setor_DIM.ID%TYPE := get_setor_ID_FUNC(p_setor);
    v_cultura_ID Cultura.ID%TYPE := get_cultura_ID_FUNC(p_cultura);

    CURSOR c1 is select t.year, p.quantidade from Producao_FAC p 
        INNER JOIN Tempo_DIM t on t.ID = p.tempo_ID
            WHERE p.Produto_ID = v_cultura_ID AND p.Setor_ID = v_setor_ID
                ORDER BY t.year ASC;

    v_c1 c1%rowtype;

BEGIN
    ano_1 := TO_NUMBER(ano_atual, '9999');
    ano_2 := ano_1 - 1;
    ano_3 := ano_2 - 1;
    ano_4 := ano_3 - 1;
    ano_5 := ano_4 - 1;

OPEN c1;
    loop 
        fetch c1 into v_c1;
        exit when c1%NOTFOUND;
        IF (v_c1.year = ano_1) then
            qtd_ano_1 := qtd_ano_1 + v_c1.quantidade;
        end if;
        IF (v_c1.year = ano_2) then
            qtd_ano_2 := qtd_ano_2 + v_c1.quantidade;
        end if;
        IF (v_c1.year = ano_3) then
            qtd_ano_3 := qtd_ano_3 + v_c1.quantidade;
        end if;
        IF (v_c1.year = ano_4) then
            qtd_ano_4 := qtd_ano_4 + v_c1.quantidade;
        end if;
        IF (v_c1.year = ano_5) then
            qtd_ano_5 := qtd_ano_5 + v_c1.quantidade;
        end if;
    end loop;
    close c1;
    DBMS_OUTPUT.PUT_LINE(chr(0));
    DBMS_OUTPUT.put_line('Produção de ' || p_cultura || ' no setor ' || p_setor || ' nos últimos 5 anos:');
    DBMS_OUTPUT.PUT_LINE(chr(0));
    DBMS_OUTPUT.put_line('Ano ' || ano_5 || ': ' || qtd_ano_5 || ' toneladas');
    DBMS_OUTPUT.put_line('Ano ' || ano_4 || ': ' || qtd_ano_4 || ' toneladas');
    DBMS_OUTPUT.put_line('Ano ' || ano_3 || ': ' || qtd_ano_3 || ' toneladas');
    DBMS_OUTPUT.put_line('Ano ' || ano_2 || ': ' || qtd_ano_2 || ' toneladas');
    DBMS_OUTPUT.put_line('Ano ' || ano_1 || ': ' || qtd_ano_1 || ' toneladas');

END;
/

--bloco anónimo de teste sem erro
DECLARE
v_id INTEGER; 
v_id2 INTEGER;
BEGIN
v_ID := get_setor_ID_FUNC('setor 3');
DBMS_OUTPUT.put_line(v_ID);
END;
/

--bloco anónimo de teste com erro
DECLARE
v_id INTEGER; 
v_id2 INTEGER;
BEGIN
v_ID2 := get_setor_ID_FUNC('Setor 30');
END;
/

--bloco anónimo de teste sem erro
DECLARE
v_id INTEGER; 
BEGIN
v_ID := get_cultura_ID_FUNC('Batata');
DBMS_OUTPUT.put_line(v_ID);
END;
/

--bloco anónimo de teste com erro
DECLARE
v_id INTEGER; 
BEGIN
v_ID := get_cultura_ID_FUNC('Girafa');
END;
/

--bloco anónimo de teste sem erro
BEGIN
evolucao_producao_PROC('Batata', 'setor 3');
END;
/

--bloco anónimo de teste com erro
BEGIN
evolucao_producao_PROC('Girafa', 'setor 3');
END;
/