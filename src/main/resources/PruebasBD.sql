create database prueba_bio_code; -- Creacion de BD 
use prueba_bio_code; -- Usar la base de datos

select * from control_asistencia; -- traer todos los registros de control de asistencia

select * from persona; -- traer todos los registros de persona

insert ficha value (5,2922207,1,4); -- insertar un dato en ficha 

delete from ficha where id_ficha=4; -- borrar un dato en ficha 

drop table control_asistencia; -- Borrar la tabla de control de asistenvia

ALTER TABLE persona MODIFY COLUMN telefono VARCHAR(20); -- Modificacion de una columna de persona

delete from persona; -- borra todos los datos de persona


-- De la linea 20 a la 24 se insetan datos a la tabla persona
INSERT INTO persona (id_persona, nombres, apellidos, telefono, no_documento, correo, id_ficha, foto, huella, id_rol, id_tipo_documento, contrasena)
VALUES
(6, 'ALISSON JHOJANA', 'POLO TORRES', '3213053636', '1000226611', 'alissonjhojanatorres@gmail.com', 5, 1, 1, 1, 1, 1),
(2, 'ANDRES', 'CALDERON', '3213788554', '1007521313', 'andresfelipec840@gmail.com', NULL, NULL, NULL, 1, 1, NULL),
(3, 'SAMYR ESTEBAN', 'LAVERDE OLIVAR', '3223522870', '1003530791', 'samyr1902@gmail.com', NULL, NULL, NULL, 1, 1, NULL);

-- De la linea 27 a la 28 se insertan datos a la tabla control de asistencia
INSERT INTO control_asistencia (fecha_asistencia, num_horas, novedad, documento_excusa, id_persona)
 VALUES ('2025-05-24', 4,'Llegada tarde', NULL,6);
 
 delete from persona where id_persona=5; -- borrar un dato en ficha 
