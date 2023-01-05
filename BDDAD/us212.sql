/*Tabela*/
CREATE TABLE input_sensor(
    id_Sensor  VARCHAR(5) PRIMARY KEY,
    CONSTRAINT Check_id_Sensor_Length CHECK (LENGTH(id_Sensor)=5),
    Tipo_Sensor_ID VARCHAR(2),
    CONSTRAINT Check_Tipo_Sensor_ID_Length CHECK (LENGTH(Tipo_Sensor_ID)=2),
    Valor_lido INT,
    CONSTRAINT Check_Valor_lido  CHECK (Valor_lido >0 and Valor_lido <100 ),
    Designacao INT UNIQUE,
    instante_leitura  date
);

/*1 Criar uma funçao que retorna o enesimo elementp*/

create or replace function func_enesimoelemento(EnesimoNumero IN NUMBER(20, 0)) RETURN VARCHAR2(25) AS
    tempo      VARCHAR2(20);
    resultado   VARCHAR2(20);
    cursor1      SYS_REFCURSOR;
    tempo_contador     NUMBER(20, 0);
    leituras NUMBER(20, 0);
    
BEGIN

resultado := NULL;
    SELECT count(*) into leituras FROM input_sensor;
    if (EnesimoNumero > leituras) THEN

        RAISE_APPLICATION_ERROR(-20000, 'Não existe a ' || EnesimoNumero || ' posição! Existem apenas' ||
                                        leituras || ' elementos!');
    
    end if;
    
    OPEN cursor1 FOR SELECT * from input_sensor;
    LOOP
        FETCH cursor1 INTO tempo;
        EXIT WHEN cursor1%notfound;
        if (tempo_contador = EnesimoNumero) THEN
            resultado := tempo;
        end if;
        tempo_contador := tempo_contador + 1;

    end loop;

    close cursor1;
    return resultado;
end;

CREATE OR ALTER FUNCTION func_ValoresValidos(leitura IN varchar,
                                            Sensor_id OUT VARCHAR2,
                                            Tipo_Sensor_ID OUT VARCHAR2,
                                            valor OUT NUMBER,
                                            Designacao OUT NUMBER,
                                            instante_leitura OUT date) RETURN boolean AS 

    id_Sensor      VARCHAR2(5);
    sensor_tipo    VARCHAR2(2);
    valor2      VARCHAR2(3);
    Designacao2      VARCHAR2(2);
    leitura_instante   VARCHAR2(12);
    flag       BOOLEAN      := TRUE;
    dateformat varchar2(15) := 'DDMMYYYYHHMI';

BEGIN

    id_Sensor := SUBSTR(reading, 0, 5);
    sensor_tipo := SUBSTR(reading, 6, 2);
    valor2 := SUBSTR(reading, 8, 3);
    Designacao2 := SUBSTR(reading, 11, 2);
    leitura_instante := SUBSTR(reading, 13);

    if (id_Sensor is null OR sensor_tipo is null OR valor2 is null OR Designacao2 is null OR leitura_instante is null) then
        flag := FALSE;
    end if;
    Sensor_id := id_Sensor;
    if (NOT (sensor_tipo IN ('HS', 'PL','TS', 'VV', 'TA', 'HA' ,'PA'))) THEN
        flag := false;
    end if;
    Tipo_Sensor_ID := sensor_tipo;
    if (TO_NUMBER(valor2, '999') < 0 or TO_NUMBER(valor2, '999') > 100) THEN
        flag := false;
    end if;
    valor := TO_NUMBER(val, '999');
    Designacao := TO_NUMBER(Designacao2, '99');
    instante_leitura := TO_DATE(leitura_instante, dateformat);
    return flag;
EXCEPTION
    WHEN OTHERS THEN
        return false;
end;



CREATE OR REPLACE PROCEDURE proc_TransferirValores(NumeroValido OUT NUMBER, NumeroInvalido OUT NUMBER) AS
                                                                   
    NumeroValido    NUMBER(20, 0) := 0;
    NumeroInvalido  NUMBER(20, 0) := 0;
    CURSOR1         SYS_REFCURSOR;
    leitura     VARCHAR2(25);
    id_Sensor       VARCHAR2(5);
    sensor_tipo     VARCHAR2(2);
    valor2       NUMBER(3);
    refencia_valor   NUMBER(2);
    leitura_instante date;
    contador     NUMBER(10, 0) := 0;

BEGIN
    open CUR for SELECT * FROM input_sensor;
    LOOP
        FETCH CUR into leitura;
        EXIT WHEN CURSOR1%NOTFOUND;
        if (func_ValoresValidos(leitura, id_Sensor, sensor_tipo, valor, Designacao2, leitura_instante)) then
            NumeroValido := NumeroValido + 1;
            SELECT count(*) into contador FROM SENSOR WHERE SENSOR.ID = id_Sensor;
            if (contador = 0) THEN
                INSERT INTO SENSOR(ID, Tipo_Sensor_ID, Designacao) VALUES (id_Sensor, sensor_tipo, Designacao2);
            end if;

        else
            numInvalid := numInvalid + 1;
        end if;
    end LOOP;
    numberValid := numValid;
    numberInvalid := numInvalid;
end;
