--Trigger que permite a utilização de inserts--
CREATE OR REPLACE TRIGGER inPistaAuditoria
    AFTER INSERT ON campo
    FOR EACH ROW
    BEGIN
    INSERT INTO PistaAuditoria(campo_id,horaRealizada,tabela,tipo_alteracao) VALUES (utilizador, horaRealizada('now'),SYSTIMESTAMP,'INSERT')
END;

--Trigger que permite a utilização de update--
CREATE OR REPLACE TRIGGER upPistaAuditoria
    AFTER UPDATE ON campo
    FOR EACH ROW
    BEGIN
    INSERT INTO PistaAuditoria(campo_id,horaRealizada,tabela,tipo_alteracao) VALUES (utilizador, horaRealizada('now'),SYSTIMESTAMP,'UPDATE')
END;

--Trigger que permite a utilização de delete--
CREATE OR REPLACE TRIGGER dePistaAuditoria
    AFTER DELETE ON campo
    FOR EACH ROW
    BEGIN
    INSERT INTO PistaAuditoria(campo_id,horaRealizada,tabela,tipo_alteracao) VALUES (utilizador, horaRealizada('now'),SYSTIMESTAMP,'DELETE')
END;


select * from PistaAuditoria order by horaRealizada

