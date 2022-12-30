    ------------acceptance criteria 6B----------------

--b) Comparar as vendas de um ano com outro?

CREATE OR REPLACE PROCEDURE vendas_por_ano_PROC (ano1 Tempo_DIM.Year%TYPE, ano2 Tempo_DIM.Year%TYPE) IS
    
        total1 NUMBER := 0;
        total2 NUMBER := 0;
        nr_vendas1 INTEGER := 0;
        nr_vendas2 INTEGER := 0;
        ex_ano1 EXCEPTION;
        ex_ano2 EXCEPTION;

    BEGIN

        SELECT COUNT (*) INTO nr_vendas1 
    
            FROM Venda_FAC v INNER JOIN Tempo_DIM t 

            ON v.Tempo_ID = t.ID
    
            WHERE t.Year = ano1;

        SELECT COUNT (*) INTO nr_vendas2
        
            FROM Venda_FAC v INNER JOIN Tempo_DIM t 
    
            ON v.Tempo_ID = t.ID
        
            WHERE t.Year = ano2;

        IF nr_vendas1 = 0 THEN
            
                RAISE ex_ano1;
    
            END IF;

        IF nr_vendas2 = 0 THEN
            
                RAISE ex_ano2;
    
            END IF;

        SELECT SUM (v.preco) INTO total1 
    
            FROM Venda_FAC v INNER JOIN Tempo_DIM t 

            ON v.Tempo_ID = t.ID
    
            WHERE t.Year = ano1;

        SELECT SUM (v.preco) INTO total2
    
            FROM Venda_FAC v INNER JOIN Tempo_DIM t 

            ON v.Tempo_ID = t.ID
    
            WHERE t.Year = ano2;

        DBMS_OUTPUT.PUT_LINE(chr(0));
        
        IF  TO_CHAR(total1) = '' THEN

            total1 := 0;

        END IF;

        IF TO_CHAR(total2) = '' THEN

            total2 := 0;

        END IF;

        IF total1 > total2 THEN

            DBMS_OUTPUT.PUT_LINE('O ano ' || ano1 || ' rendeu mais que o ano ' || ano2);

        ELSE
            IF total1 = total2 THEN

                DBMS_OUTPUT.PUT_LINE('O ano ' || ano1 || ' rendeu o mesmo que o ano ' || ano2);

            ELSE

                DBMS_OUTPUT.PUT_LINE('O ano ' || ano2 || ' rendeu mais que o ano ' || ano1);

            END IF;

        END IF;

        DBMS_OUTPUT.PUT_LINE(chr(0));

        DBMS_OUTPUT.PUT_LINE('O ano ' || ano1 || ' teve rendimento de ' || total1 || ' milhares de euros');
        DBMS_OUTPUT.PUT_LINE('O ano ' || ano2 || ' teve rendimento de ' || total2 || ' milhares de euros');


    EXCEPTION
    
            WHEN ex_ano1 THEN
    
                raise_application_error(-20001, 'O ano ' || ano1 || ' não tem vendas registadas');
    
            WHEN ex_ano2 THEN
    
                raise_application_error(-20002, 'O ano ' || ano2 || ' não tem vendas registadas');

            WHEN no_data_found THEN
                DBMS_OUTPUT.put_line('');        
    END;
    /

    --testes
    BEGIN
        vendas_por_ano(2022, 2016);
    END;


    BEGIN
        vendas_por_ano(2022, 2014);
    END;
    
        