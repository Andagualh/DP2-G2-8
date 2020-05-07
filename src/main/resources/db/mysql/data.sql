INSERT INTO authorities VALUES ('pabloMedico','medico');

INSERT INTO medico(id,nombre,apellidos,dni,n_telefono,domicilio,username) 
VALUES (1,'Alvaro','Alferez','78429273D','666666666','Ecija','alvaroMedico');
INSERT INTO medico(id,nombre,apellidos,dni,n_telefono,domicilio,username) 
VALUES (2,'Andres','Alhama','97899162F','666666666','Ecija','andresMedico');
INSERT INTO medico(id,nombre,apellidos,dni,n_telefono,domicilio,username) 
VALUES (3,'Pablo','Moreno','29599162M','666666666','Sevilla','pabloMedico');

INSERT INTO paciente(id,nombre,apellidos,f_nacimiento,dni,n_telefono,domicilio,email,f_alta,medico_id) 
VALUES (1,'Maria Gracia','Castillo Castillo','1983-11-12','66100313S',605708609,'Camino Horno, 29','mariagracia_83@gmail.com','2020-03-20',1);
INSERT INTO paciente(id,nombre,apellidos,f_nacimiento,dni,n_telefono,domicilio,email,f_alta,medico_id) 
VALUES (2,'Oscar','Salas Sala','1986-6-29','53067162K',637664031,'Camino Madrid, 27, Torresandino, Burgos','oscar_86@gmail.com','2020-03-20',1);
INSERT INTO paciente(id,nombre,apellidos,f_nacimiento,dni,n_telefono,domicilio,email,f_alta,medico_id) 
VALUES (3,'Luis Manuel','Chaves Rios','1966-09-13','37149202Q',660645484,'Vía Iglesia, 91, Fonollosa, Barcelona','luismanuel_66@gmail.com','2020-03-20',1);
INSERT INTO paciente(id,nombre,apellidos,f_nacimiento,dni,n_telefono,domicilio,email,f_alta,medico_id) 
VALUES (4,'Maria Soledad','Paniagua Cardenas','1930-07-28','47209793Q',713442852,'Cuesta Pedralbes, 64, Ansó, Huesca','luismanuel_66@gmail.com','2020-03-20',1);
INSERT INTO paciente(id,nombre,apellidos,f_nacimiento,dni,n_telefono,domicilio,email,f_alta,medico_id) 
VALUES (5,'Christian','Ochoa Moran','1956-03-11','35162318X',701219208,'Gran Vía Iglesia, 23, Camarillas, Teruel','christian_56@gmail.com','2020-03-20',2);
INSERT INTO paciente(id,nombre,apellidos,f_nacimiento,dni,n_telefono,domicilio,email,f_alta,medico_id) 
VALUES (6,'Blanca','Leal Pineda','1945-03-29','24921447G',688657684,'Ronda Iglesia, 77, Santa Fe De Mondújar, Almería','blanca_45@gmail.com','2020-03-20',2);
INSERT INTO paciente(id,nombre,apellidos,f_nacimiento,dni,n_telefono,domicilio,email,f_alta,medico_id) 
VALUES (7,'Pablo','Garcia Moreno','1965-04-19','24921887G',610657684,'Ronda Sol, 12, Camas, Sevilla','pablous_99@gmail.com','2020-03-20',3);
INSERT INTO paciente(id,nombre,apellidos,f_nacimiento,dni,n_telefono,domicilio,email,f_alta,medico_id) 
VALUES (8,'Zaida','Viento Valor','1995-01-23','29518993L',662157684,'Calle Freno, 32, Riotinto, Huelva','saidita@gmail.com','2020-03-20',3);

INSERT INTO cita VALUES (1, 'nombre4','2020-03-09','Consulta 1',1);
INSERT INTO cita VALUES (2, 'nombre5','2022-07-09','Consulta 2',1);
INSERT INTO cita VALUES (3, 'nombre6','2021-03-10','Consulta 3',2);
INSERT INTO cita VALUES (4, 'nombre7','2025-04-09','Consulta 4',2);
INSERT INTO cita VALUES (5, 'nombre8','2023-05-09','Consulta 5',3);
INSERT INTO cita VALUES (6, 'nombre9','2023-05-09','Consulta 6',3);
INSERT INTO cita VALUES (7, 'Cita Test Informe','2020-04-18','Consulta Test Informe',1);
INSERT INTO cita VALUES (8, 'Cita Test Edit Informe', '2020-04-20', 'Consulta Test 7', 1);
INSERT INTO cita VALUES (9, 'Cita Test Pasado Informes', '2020-04-20', 'ConsultaTEST', 1);

INSERT INTO historiaclinica(id,descripcion,paciente_id) VALUES (1,'Descripcion',1);
INSERT INTO historiaclinica(id,descripcion,paciente_id) VALUES (2,'Descripcion 2',2);
INSERT INTO historiaclinica(id,descripcion,paciente_id) VALUES (3,'Descripcion 3',3);
INSERT INTO historiaclinica(id,descripcion,paciente_id) VALUES (4,'Descripcion 4',4);
INSERT INTO historiaclinica(id,descripcion,paciente_id) VALUES (5,'Descripcion 5',5);
INSERT INTO historiaclinica(id,descripcion,paciente_id) VALUES (6,'Descripcion 6',6);
INSERT INTO historiaclinica(id,descripcion,paciente_id) VALUES (7,'Descripcion 7',8);

INSERT INTO informe (id,motivo_consulta,diagnostico,cita_id,historia_clinica_id) VALUES (1,'motivo','diagnostico',1,null);
INSERT INTO informe (id,motivo_consulta,diagnostico,cita_id,historia_clinica_id) VALUES (2, 'motivo test', 'diagnostico test', 8, null);
INSERT INTO informe (id,motivo_consulta,diagnostico,cita_id,historia_clinica_id) VALUES (3, 'cita pasada', 'cita past',9,null);

INSERT INTO tratamiento(id,name,medicamento,dosis,f_inicio_tratamiento,f_fin_tratamiento,informe_id) 
VALUES (1, 'nombre1','paracetamol','1 pastilla cada 4 horas','2020-03-09','2020-12-24',1);
INSERT INTO tratamiento(id,name,medicamento,dosis,f_inicio_tratamiento,f_fin_tratamiento,informe_id) 
VALUES (2, 'nombre2','ibuprofeno','1 pastilla cada 8 horas','2020-03-09','2020-12-19',1);
INSERT INTO tratamiento(id,name,medicamento,dosis,f_inicio_tratamiento,f_fin_tratamiento,informe_id) 
VALUES (3, 'nombre3','enantium','1 cada dia','2020-03-09','2020-12-15',1);

