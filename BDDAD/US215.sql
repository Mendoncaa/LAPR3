
CREATE TABLE input_hub (
    Location_ID VARCHAR2(5),
    Latitude NUMBER,
    Longitude NUMBER,
    Code VARCHAR2(3),
    CONSTRAINT INPUT_HUB_COMP_PK PRIMARY KEY (Location_ID, Code) 
);
/

CREATE TABLE Hub (
    Location_ID VARCHAR2(5),
    Latitude NUMBER,
    Longitude NUMBER,
    CONSTRAINT HUB_COMP_PK PRIMARY KEY (Location_ID) 
);
/

--criar trigger para inserir na tabela hub, os dados da tabela input_hub se a primeira letra do código for diferente a C 

CREATE OR REPLACE TRIGGER Insert_Hub_Trigger
AFTER INSERT ON Input_Hub
FOR EACH ROW
DECLARE
BEGIN
    if substr(:new.Code,1,1) != 'C' then
        INSERT INTO Hub (Location_ID, Latitude, Longitude) VALUES (:new.Location_ID, :new.Latitude, :new.Longitude);
    end if; 
END;
/



begin 
    insert into input_hub (Location_ID, Latitude, Longitude, Code) values ('CT1', 40.6389, -8.6553, 'C1');
    insert into input_hub (Location_ID, Latitude, Longitude, Code) values ('CT2', 38.0333, -7.8833, 'C2');
    insert into input_hub (Location_ID, Latitude, Longitude, Code) values ('CT14', 38.5243, -8.8926, 'E1');
    insert into input_hub (Location_ID, Latitude, Longitude, Code) values ('CT11', 39.3167, -7.4167, 'E2');
    insert into input_hub (Location_ID, Latitude, Longitude, Code) values ('CT10', 39.7444, -8.8072, 'P3');
end;
/

drop trigger Insert_Hub_Trigger;
drop TABLE input_hub;
drop TABLE Hub;
