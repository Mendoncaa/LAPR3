---------US207 1.---------------

--procedure para obter uma lista de setores/campos ordenados pela quantidade produzida por hectare numa dada safra,
--por ordem decrescente.

create or replace procedure Setores_quantidade_por_hectare_desc_Proc(year_to_look varchar2) is
cursor c_campos is select distinct c.quantidade, k.designacao, k.hectares, e.nome, a.data_accao from accao a 
    inner join colheita c on a.id = c.accao_id 
        inner join plantacao p on p.campo_id = a.campo_id 
            inner join campo k on p.campo_id = k.id 
                inner join cultura cu on p.cultura_id = cu.id
                    inner join especie_vegetal e on e.id = cu.especie_vegetal_id 
    where TO_CHAR(a.data_accao,'YYYY') = year_to_look
        order by c.quantidade/k.hectares desc;
  
nr_setores int;        
v_campos c_campos%rowtype;
begin
select distinct count (*) into nr_setores from accao a inner join colheita c
            on a.id = c.accao_id 
                          where TO_CHAR(a.data_accao,'YYYY') = year_to_look;
        if(nr_setores>0) then
open c_campos;
    dbms_output.put_line(chr(0));
    DBMS_OUTPUT.PUT_LINE('Designacao -- Quantidade/Hectares -- Especie vegetal -- Data da Colheita');
    dbms_output.put_line(chr(0));
    loop 
        fetch c_campos into v_campos;
        exit when c_campos%NOTFOUND;
        DBMS_OUTPUT.PUT_LINE(v_campos.designacao||' -- '||v_campos.quantidade/v_campos.hectares||' -- '||v_campos.nome||' -- '||v_campos.data_accao);
    end loop;
    close c_campos;
        else
        dbms_output.put_line(chr(0));
        DBMS_OUTPUT.PUT_LINE('Nenhum setor');
        end if;
end;
/

--Bloco anónimo para testar procedure Setores_quantidade_por_hectare_desc_Proc.
begin 
Setores_quantidade_por_hectare_desc_Proc ('2022');
end;
/



-----------------------------------------------------------------------------------------------------------------------------------
---------US207 2.---------------


--procedure para obter uma lista de setores/campos ordenados pelo lucro por hectare numa dada safra,
--por ordem decrescente.
create or replace procedure Setores_lucro_por_hectare_desc_Proc(year_to_look varchar2) is
cursor c_campos is select distinct c.preco, k.designacao, k.hectares, e.nome, ec.preco as preco_venda from accao a 
inner join colheita c on a.id = c.accao_id 
    inner join plantacao p on p.campo_id = a.campo_id 
        inner join campo k on p.campo_id = k.id 
            inner join especie_vegetal e on e.id = c.especie_vegetal_id 
                inner join encomenda_especie_vegetal ec on e.id = ec.especie_vegetal_id
    where TO_CHAR(a.data_accao,'YYYY') = year_to_look
        order by (ec.preco-c.preco)/k.hectares desc;
  
nr_setores int;        
v_campos c_campos%rowtype;
begin
select distinct count (*) into nr_setores from accao a inner join colheita c
            on a.id = c.accao_id 
                          where TO_CHAR(a.data_accao,'YYYY') = year_to_look;
        if(nr_setores>0) then
open c_campos;
    dbms_output.put_line(chr(0));
    DBMS_OUTPUT.PUT_LINE('Designacao -- Lucro/Hectare -- Especie vegetal');
    dbms_output.put_line(chr(0));
    loop 
        fetch c_campos into v_campos;
        exit when c_campos%NOTFOUND;
        DBMS_OUTPUT.PUT_LINE(v_campos.designacao||' -- '||(v_campos.preco_venda-v_campos.preco)/v_campos.hectares||' -- '||v_campos.nome);
    end loop;
    close c_campos;
        else
        dbms_output.put_line(chr(0));
        DBMS_OUTPUT.PUT_LINE('Nenhum setor');
        end if;
end;
/

--Bloco anónimo para testar procedure Setores_lucro_por_hectare_desc_Proc.
begin 
Setores_lucro_por_hectare_desc_Proc ('2022');
end;
/
