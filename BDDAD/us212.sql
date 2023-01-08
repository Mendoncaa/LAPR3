CREATE OR REPLACE TRIGGER Insert_Sensor_Trigger
    AFTER INSERT ON input_sensor
    FOR EACH ROW
    DECLARE
    conta_sensor INTEGER,
    Tipo_Sensor Char(2),
    PlantacaoID INTEGER,
    DataLeitura DATE,
    SensorID INTEGER NOT NULL,
    Horas INTEGER,
    Minutos INTEGER,
    Valor NUMBER NOT NULL,
    ex EXCEPTION,
    ex1 EXCEPTION
    ex2 EXCEPTION,
    ex3 EXCEPTION;
    BEGIN

    if lenght(:NEW.input_string) != 25 THEN
        RAISE ex;
    end if;
    Tipo_Sensor := SUBSTR(:NEW.input_string,5, 2);
    SensorID := TO_Number(SUBSTR(:NEW.input_string,0, 5));
    SELECT count(*) INTO conta_sensor FROM Sensor inner join Tipo_Sensor on Tipo_Sensor_ID = Tipo_Sensor.ID
    WHERE Sensor.ID = SensorID and Designacao = Tipo_Sensor;

    if conta_sensor = 0 THEN
        RAISE ex1;
    end if;

    Valor := TO_Number(SUBSTR(:NEW.input_string,7, 3));

    SELECT Plantacao_ID INTO PlantacaoID FROM Sensor
    WHERE Sensor.ID = SensorID;

    Horas := TO_Number(SUBSTR(:NEW.input_string,21, 2));
    if Horas > 23 THEN
        RAISE ex3;
    end if;
    Minutos := TO_Number(SUBSTR(:NEW.input_string,23, 2));
    if Minutos > 59 THEN
        RAISE ex4;
    end if;
   
   Insert INTO leitura (Plantacao_ID, Data_Leitura, Sensor_ID, Valor) values (PlantacaoID, Concat(Horas, ":", Minutos), SensorID, Valor);
   Delete FROM input_sensor where input_string = new.input_string;


    EXCEPTION
    
        WHEN ex THEN
            RAISE_APPLICATION_ERROR(-20001, 'ERROR: Formato inválido');

        WHEN ex1 THEN
            RAISE_APPLICATION_ERROR(-20001, 'ERROR: Tipo de sensor errado');

        WHEN ex2 THEN
            RAISE_APPLICATION_ERROR(-20001, 'ERROR: Valor da hora errado');
        
        WHEN ex3 THEN
            RAISE_APPLICATION_ERROR(-20001, 'ERROR: Valor dos minutos errado');
    END;

/*CREATE OR REPLACE FUNCTION devolver_elemento_n(n INT) RETURNS VARCHAR2(25) AS

    resultado  VARCHAR2(25);
    cursor1    SYS_REFCURSOR;

    BEGIN
    resultado := NULL;
    SELECT count(*) into leitura FROM input_sensor;

    OPEN cursor1 FOR SELECT * from input_sensor;
    LOOP

    
END;