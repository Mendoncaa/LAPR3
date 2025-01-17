--Trigger que permite a utilização de inserts--
CREATE OR REPLACE TRIGGER inPista_Auditoria
    AFTER INSERT ON campo
    FOR EACH ROW
    BEGIN
    INSERT INTO Pista_Auditoria(utilizador,campo_id,horaRealizada,tipo_alteracao) 
    VALUES (:new.utilizador,:new.campo_id,SYSTIMESTAMP,'INSERT')
END;

--Trigger que permite a utilização de update--
CREATE OR REPLACE TRIGGER upPista_Auditoria
    AFTER UPDATE ON campo
    FOR EACH ROW
    BEGIN
    INSERT INTO Pista_Auditoria(utilizador,campo_id,horaRealizada,tipo_alteracao) 
    VALUES (:new.utilizador,:new.campo_id,SYSTIMESTAMP,'UPDATE')
END;

--Trigger que permite a utilização de delete--
CREATE OR REPLACE TRIGGER dePista_Auditoria
    AFTER DELETE ON campo
    FOR EACH ROW
    BEGIN
    INSERT INTO Pista_Auditoria(utilizador,campo_id,horaRealizada,tipo_alteracao)
    VALUES (:new.utilizador,:new.campo_id,SYSTIMESTAMP,'DELETE')
END;


select * from Pista_Auditoria order by horaRealizada