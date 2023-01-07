--Trigger que permite a utilização de inserts--
CREATE OR REPLACE TRIGGER inPista_Auditoria
    AFTER INSERT ON campo
    FOR EACH ROW
    BEGIN
    INSERT INTO Pista_Auditoria(campo_id,horaRealizada,tipo_alteracao) VALUES (utilizador, horaRealizada('now'),SYSTIMESTAMP,'INSERT')
END;

--Trigger que permite a utilização de update--
CREATE OR REPLACE TRIGGER upPista_Auditoria
    AFTER UPDATE ON campo
    FOR EACH ROW
    BEGIN
    INSERT INTO Pista_Auditoria(campo_id,horaRealizada,tipo_alteracao) VALUES (utilizador, horaRealizada('now'),SYSTIMESTAMP,'UPDATE')
END;

--Trigger que permite a utilização de delete--
CREATE OR REPLACE TRIGGER dePistaAuditoria
    AFTER DELETE ON campo
    FOR EACH ROW
    BEGIN
    INSERT INTO Pista_Auditoria(campo_id,horaRealizada,tipo_alteracao) VALUES (utilizador, horaRealizada('now'),SYSTIMESTAMP,'DELETE')
END;


select * from Pista_Auditoria order by horaRealizada

