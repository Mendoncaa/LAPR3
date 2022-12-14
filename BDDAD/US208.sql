--procedure que adiciona ou não um novo factor de produção
CREATE OR REPLACE PROCEDURE AddProductionFactor_Proc(v_tipo Tipo_Produto.designacao%type)
 is
    nr_factor INTEGER;
    nr_tipo_produto_id Tipo_Produto.ID%type;
begin
    select count(*) into nr_factor from Tipo_Produto where designacao = v_tipo;

    if nr_factor > 0 then
      DBMS_OUTPUT.PUT_LINE('Factor de produção já existe!');
    else
      select nvl(max(ID), 0)+1 into nr_tipo_produto_id from Tipo_Produto;
      insert into Tipo_Produto (ID, designacao)values (nr_tipo_produto_id,v_tipo);
      DBMS_OUTPUT.PUT_LINE('Novo factor de produção adicionado');
    end if;
end;
/

--bloco anónimo que testa Funcao AddProductionFactor_Proc
begin
  AddProductionFactor_Proc('Estrume natural');
end;


--função que adiciona um novo factor de produção e retorna o id do mesmo
CREATE OR REPLACE FUNCTION AddProductionFactor_Func(v_tipo Tipo_Produto.designacao%type) return tipo_produto.id%type
 is
    nr_factor INTEGER;
    nr_tipo_produto_id Tipo_Produto.ID%type;
begin
    select count(*) into nr_factor from Tipo_Produto where designacao = v_tipo;

    if nr_factor > 0 then
      DBMS_OUTPUT.PUT_LINE('Factor de produção já existe!');
    else
      select nvl(max(ID), 0)+1 into nr_tipo_produto_id from Tipo_Produto;
      insert into Tipo_Produto (ID, designacao)values (nr_tipo_produto_id,v_tipo);
    end if;
    return nr_tipo_produto_id;
end;
/

--bloco anónimo que testa Funcao AddProductionFactor_Func
declare
p_id tipo_produto.id%type;
begin
  p_id := AddProductionFactor_Func('Estrume natural');
  DBMS_OUTPUT.PUT_LINE(p_id);
end;
/


--função que adiciona nova categoria e retora id da mesma
CREATE OR REPLACE Function AddCategory_Func(v_categoria Categoria.designacao%type) return Categoria.id%type 
IS
    nr_tipo_categoria Categoria.ID%type;
begin
      select nvl(max(ID), 0)+1 into nr_tipo_categoria from Categoria;
      insert into Categoria (ID, designacao)
      values (nr_tipo_categoria,v_categoria);
      return nr_tipo_categoria;
end;
/

--bloco anónimo que testa função AddCategory_Func
declare 
id_pesq Categoria.id%type;
begin
id_pesq := AddCategory_Func('Sais minerais');
DBMS_OUTPUT.PUT_LINE(id_pesq);
end;
/


--função que adiciona nova substância e retorna a mesma
CREATE OR REPLACE Function AddSubstancia_Func(v_substancia Substancia.nome%type, v_unit Substancia.unidade%type, v_categoria Categoria.ID%type) return Substancia%rowtype 
IS
    p_substancia Substancia%rowtype;
    nr_substancia_id Substancia.ID%type;
begin
      select nvl(max(ID), 0)+1 into nr_substancia_id from Substancia;
      insert into Substancia (ID, nome, Categoria_ID, unidade) values (nr_substancia_id, v_substancia, v_categoria, v_unit);
      select * into p_substancia from Substancia where id = nr_substancia_id;
      return p_substancia;
end;
/

--bloco anónimo que testa função AddSubstancia_Func
declare 
id_pesq Substancia%rowtype;
begin
id_pesq := AddSubstancia_Func('Cimento', 'kg', 1);
DBMS_OUTPUT.PUT_LINE(id_pesq.id);
end;
/



----------------US208a-------------------

--procedure que adiciona ou não um novo factor de produção
CREATE OR REPLACE PROCEDURE AddProductionFactor_Proc(v_tipo Tipo_Produto.designacao%type)
 is
    nr_factor INTEGER;
    nr_tipo_produto_id Tipo_Produto.ID%type;
begin
    select count(*) into nr_factor from Tipo_Produto where designacao = v_tipo;

    if nr_factor > 0 then
      DBMS_OUTPUT.PUT_LINE('Factor de produção já existe!');
    else
      select nvl(max(ID), 0)+1 into nr_tipo_produto_id from Tipo_Produto;
      insert into Tipo_Produto (ID, designacao)values (nr_tipo_produto_id,v_tipo);
      DBMS_OUTPUT.PUT_LINE('Novo factor de produção adicionado');
    end if;
end;
/

--bloco anónimo que testa Funcao AddProductionFactor_Proc
begin
  AddProductionFactor_Proc('Estrume natural');
end;




---------------------------------------------------------------------------------------------

----------------US208b-------------------

--procedure que adiciona nova ficha técnica e produto
CREATE OR REPLACE PROCEDURE AddFicha_Tecnica_Proc(v_nome Produto.nome%type, v_fornecedor Ficha_Tecnica.Fornecedor%type, v_fator tipo_produto.designacao%type) 
     IS 
    
nr_produtos INTEGER;    
nr_tipo_produto_id Tipo_Produto.id%type;     
tipo_produto_id Tipo_Produto.ID%type;
nr_Tipo_Produto_designacao INTEGER;
nr_ficha_tecnica_id Ficha_Tecnica.id%type;
nr_produto_id Produto.id%type;

begin

select count(*) into nr_produtos from Produto where UPPER(nome) = UPPER(v_nome);

if nr_produtos = 0 then

--tipo de produto--
select count(*) into nr_Tipo_Produto_designacao from Tipo_Produto where UPPER(designacao) = UPPER(v_fator);

if nr_tipo_Produto_designacao > 0 then
  select ID into tipo_produto_id from Tipo_Produto where UPPER(designacao) = UPPER(v_fator);
else
  nr_tipo_produto_id := AddProductionFactor_Func(v_fator);
end if;


  
--nova ficha técnica  
select nvl(max(ID), 0)+1 into nr_ficha_tecnica_id from Ficha_Tecnica; 
select nvl(max(ID), 0)+1 into nr_produto_id from Produto; 
insert into Ficha_Tecnica(ID, tipo_produto_id, Fornecedor) values (nr_ficha_tecnica_id, tipo_produto_id, v_fornecedor);
insert into Produto (ID, Ficha_Tecnica_ID, Nome) values (nr_produto_id, nr_ficha_tecnica_id, v_nome);
DBMS_OUTPUT.PUT_LINE('Nova ficha técnica e produto adicionados');

else

DBMS_OUTPUT.PUT_LINE('Já existe produto com o mesmo nome');

end if;  

exception
  when no_data_found then
    DBMS_OUTPUT.PUT_LINE('No data found');
end;
/

--bloco anónimo que testa a precedure AddFicha_Tecnica_Proc
begin
  AddFicha_Tecnica_Proc('Estrume', 'Quinta', 'Fertilizante');
end;


-------------------------------------------------------------------------------------
----------------US208b-------------------

--procedure que adiciona substancia a uma ficha técnica
CREATE OR REPLACE PROCEDURE AddSubstancia_to_Ficha_Tecnica_Proc(v_nome Produto.nome%type,v_categoria Categoria.designacao%type, v_substancia Substancia.nome%type, v_qtd Ficha_Tecnica_Substancia.QuantidadePerc%type, v_unit Substancia.unidade%type)
is

nr_produtos INTEGER;    
id_ficha_tecnica Produto.ficha_tecnica_id%type;
nr_Categoria_Designacao INTEGER;
nr_Categoria_id Categoria.id%type;
nr_substancia INTEGER;
p_substancia Substancia%rowtype;

begin

select count(*) into nr_produtos from Produto where UPPER(nome) = UPPER(v_nome);

if nr_produtos > 0 then

select ficha_tecnica_id into id_ficha_tecnica from Produto where UPPER(nome) = UPPER(v_nome);

 --categoria--
select count(*) into nr_Categoria_designacao from Categoria where UPPER(designacao) = UPPER(v_categoria);

if nr_Categoria_designacao > 0 then
  select id into nr_categoria_id from Categoria where UPPER(designacao) = UPPER(v_categoria);
else
  nr_Categoria_id := AddCategory_Func(v_categoria);
end if;

--substancia, unidade--
select count(*) into nr_substancia from Substancia where UPPER(nome) = UPPER(v_substancia);

if nr_substancia > 0 then
  select * into p_substancia from Substancia where UPPER(nome) = UPPER(v_substancia);
else
  p_substancia := AddSubstancia_Func(v_substancia, v_unit, nr_Categoria_id);
end if; 

insert into Ficha_Tecnica_Substancia (Ficha_Tecnica_ID, Substancia_ID, QuantidadePerc) values (id_ficha_tecnica, p_substancia.id, v_qtd);
DBMS_OUTPUT.PUT_LINE('Nova substância adicionada à ficha técnica do produto '||v_nome);

else
DBMS_OUTPUT.PUT_LINE('Não existe nenhum produto com esse nome');
end if;  

end;
/

--bloco anónimo que testa a procedure AddSubstancia_to_Ficha_Tecnica_Proc
begin
  AddSubstancia_to_Ficha_Tecnica_Proc('Estrume','substancia organica','agua', 50, 'ml');
end;
/


