<h2 style="text-align: center"><b>READ ME - Documentação Base Dados</b></h2>
<br>
<br>

<h3><b>1. Nomenclatura</b></h3>
<br>

<p>Para facilitar a coesão e compreensão dos Modelos criados e da elaboração das funcionalidades exigidas, foi convencionado várias nomenclaturas, que iremos apresentar na tabela que se segue;</p>
<br>

<table align="center">
    <tr>
        <th>Objetos de Base Dados</th>
        <th>Nomenclatura</th>
        <th>Exemplos</th>
        <th>Exceções</th>
    </tr>
    <tr>
        <td>Tabelas</td>
        <td><p>Consiste no nome da Entidade identificada no Universo de Discurso (UoD);</p><p>Espaços e caráteres especiais devem ser substítuidos por "_" ou pelos caratéres simplificados correspondentes (i.e. o "ç" deve ser substítuido por "c") respetivamente;</p></td>
        <td><p>"Especie_Vegetal";</p><p>"Campo";</p><p>"Plantacao";</p></td>
        <td><p>Tabelas que são resultado do processo de Normalização e que não foram identificados no UoD devem ter o nome que é resultado da união das Tabelas envolventes (exemplo: "Encomenda_Especie_Vegetal");</p></td>
    </tr>
    <tr>
        <td>Atributos</td>
        <td><p>Consiste em termo(s) utilizados para descrever a Entidade ao qual corresponde;</p><p>Espaços e caráteres especiais devem ser substítuidos por "_" ou pelos caratéres simplificados correspondentes (i.e. o "ç" deve ser substítuido por "c") respetivamente;</p></td>
        <td><p>"Quantidade";</p><p>"Data_Incumprimento";</p></td>
        <td><p>Atributos que resultam de uma segunda Tabela, devem conter no início o nome da Tabela de origem separado por um "_"; </p></td>
    </tr>
    <tr>
        <td>Restrições</td>
        <td><p>Consiste na união dos nomes da Tabela juntamente com o nome do Atributo em causa separado por um "_";</p><p>Os nomes das Restrições devem terminar com o código associado ao tipo da Restrição (exemplo: a Restrições "PRIMARY KEY" possuem o código "PK");</p></td>
        <td><p>"CULTURA_ID_PK";</p><p>"COLHEITA_QUANTIDADE_CK";</p></td>
        <td><p>Restrições associadas a múltiplos Atributos em vez de conterem os nomes dos Atributos simplesmente contem "COMP";</p></td>
    </tr>
    <tr>
        <td>Chaves Primárias</td>
        <td><p>Os nomes de Chaves Primárias obedecem às mesmas regras definidas para os nomes dos Atributos;</p></td>
        <td><p>"ID";</p><p>"Campo_ID";</p></td>
        <td>N/A</td>
    </tr>
    <tr>
        <td>Chaves Estrangeiras</td>
        <td><p>Os nomes de Chaves Estrangeiras obedecem às mesmas regras definidas para os nomes dos Atributos;</p></td>
        <td><p>"Especie_Vegetal_ID";</p><p>"Campo_ID";</p></td>
        <td>N/A</td>
    </tr>
    <tr>
        <td>Procedimentos</td>
        <td>Consiste no nome indicativo da objetivo da funcionalidade juntamente com o código "PROC" no final;</td>
        <td></td>
        <td>N/A</td>
    </tr>
    <tr>
        <td>Funções</td>
        <td>Consiste no nome indicativo da objetivo da funcionalidade juntamente com o código "FUNC" no final;</td>
        <td></td>
        <td>N/A</td>
    </tr>
</table>