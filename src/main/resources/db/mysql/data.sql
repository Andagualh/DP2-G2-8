-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO users(username,password,enabled) VALUES ('admin1','4dm1n',TRUE);
INSERT INTO authorities VALUES ('admin1','admin');
-- One owner user, named owner1 with passwor 0wn3r
INSERT INTO users(username,password,enabled) VALUES ('owner1','0wn3r',TRUE);
INSERT INTO authorities VALUES ('owner1','owner');
-- One vet user, named vet1 with passwor v3t
INSERT INTO users(username,password,enabled) VALUES ('vet1','v3t',TRUE);
INSERT INTO authorities VALUES ('vet1','veterinarian');

INSERT INTO vets VALUES (1, 'James', 'Carter');
INSERT INTO vets VALUES (2, 'Helen', 'Leary');
INSERT INTO vets VALUES (3, 'Linda', 'Douglas');
INSERT INTO vets VALUES (4, 'Rafael', 'Ortega');
INSERT INTO vets VALUES (5, 'Henry', 'Stevens');
INSERT INTO vets VALUES (6, 'Sharon', 'Jenkins');

INSERT INTO specialties VALUES (1, 'radiology');
INSERT INTO specialties VALUES (2, 'surgery');
INSERT INTO specialties VALUES (3, 'dentistry');

INSERT INTO vet_specialties VALUES (2, 1);
INSERT INTO vet_specialties VALUES (3, 2);
INSERT INTO vet_specialties VALUES (3, 3);
INSERT INTO vet_specialties VALUES (4, 2);
INSERT INTO vet_specialties VALUES (5, 1);

INSERT INTO types VALUES (1, 'cat');
INSERT INTO types VALUES (2, 'dog');
INSERT INTO types VALUES (3, 'lizard');
INSERT INTO types VALUES (4, 'snake');
INSERT INTO types VALUES (5, 'bird');
INSERT INTO types VALUES (6, 'hamster');

INSERT INTO owners VALUES (1, 'George', 'Franklin', '110 W. Liberty St.', 'Madison', '6085551023', 'owner1');
INSERT INTO owners VALUES (2, 'Betty', 'Davis', '638 Cardinal Ave.', 'Sun Prairie', '6085551749', 'owner1');
INSERT INTO owners VALUES (3, 'Eduardo', 'Rodriquez', '2693 Commerce St.', 'McFarland', '6085558763', 'owner1');
INSERT INTO owners VALUES (4, 'Harold', 'Davis', '563 Friendly St.', 'Windsor', '6085553198', 'owner1');
INSERT INTO owners VALUES (5, 'Peter', 'McTavish', '2387 S. Fair Way', 'Madison', '6085552765', 'owner1');
INSERT INTO owners VALUES (6, 'Jean', 'Coleman', '105 N. Lake St.', 'Monona', '6085552654', 'owner1');
INSERT INTO owners VALUES (7, 'Jeff', 'Black', '1450 Oak Blvd.', 'Monona', '6085555387', 'owner1');
INSERT INTO owners VALUES (8, 'Maria', 'Escobito', '345 Maple St.', 'Madison', '6085557683', 'owner1');
INSERT INTO owners VALUES (9, 'David', 'Schroeder', '2749 Blackhawk Trail', 'Madison', '6085559435', 'owner1');
INSERT INTO owners VALUES (10, 'Carlos', 'Estaban', '2335 Independence La.', 'Waunakee', '6085555487', 'owner1');

INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (1, 'Leo', '2010-09-07', 1, 1);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (2, 'Basil', '2012-08-06', 6, 2);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (3, 'Rosy', '2011-04-17', 2, 3);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (4, 'Jewel', '2010-03-07', 2, 3);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (5, 'Iggy', '2010-11-30', 3, 4);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (6, 'George', '2010-01-20', 4, 5);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (7, 'Samantha', '2012-09-04', 1, 6);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (8, 'Max', '2012-09-04', 1, 6);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (9, 'Lucky', '2011-08-06', 5, 7);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (10, 'Mulligan', '2007-02-24', 2, 8);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (11, 'Freddy', '2010-03-09', 5, 9);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (12, 'Lucky', '2010-06-24', 2, 10);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (13, 'Sly', '2012-06-08', 1, 10);

INSERT INTO visits(id,pet_id,visit_date,description) VALUES (1, 7, '2013-01-01', 'rabies shot');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (2, 8, '2013-01-02', 'rabies shot');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (3, 8, '2013-01-03', 'neutered');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (4, 7, '2013-01-04', 'spayed');

/* OUR BBDD */

INSERT INTO users(username,password,enabled) VALUES ('alvaro','entrar',TRUE);
INSERT INTO authorities VALUES ('alvaro','admin');
INSERT INTO users(username,password,enabled) VALUES ('alvaroMedico','entrar',TRUE);
INSERT INTO authorities VALUES ('alvaroMedico','medico');
INSERT INTO users(username,password,enabled) VALUES ('andresMedico','entrar',TRUE);
INSERT INTO authorities VALUES ('andresMedico','medico');
INSERT INTO users(username,password,enabled) VALUES ('pabloMedico','entrar',TRUE);
INSERT INTO authorities VALUES ('pabloMedico','medico');
INSERT INTO users(username,password,enabled) VALUES ('pedroMedico','entrar',TRUE);
INSERT INTO authorities VALUES ('pedroMedico','medico');

INSERT INTO medico(id,nombre,apellidos,dni,n_telefono,domicilio,username) 
VALUES (1,'Alvaro','Alferez','78429273D','666666666','Ecija','alvaroMedico');
INSERT INTO medico(id,nombre,apellidos,dni,n_telefono,domicilio,username) 
VALUES (2,'Andres','Alhama','97899162F','666666666','Ecija','andresMedico');
INSERT INTO medico(id,nombre,apellidos,dni,n_telefono,domicilio,username) 
VALUES (3,'Pablo','Moreno','29599162M','666666666','Sevilla','pabloMedico');
INSERT INTO medico(id,nombre,apellidos,dni,n_telefono,domicilio,username) 
VALUES (4,'Pedro','Coza','53279183M','666666666','Sevilla','pedroMedico');


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
INSERT INTO paciente(id,nombre,apellidos,f_nacimiento,dni,n_telefono,domicilio,email,f_alta,medico_id) 
VALUES (9,'Borrar','Borrar Borrar','1983-11-12','66100313S',605708609,'Borrar Borrar, Borrar','borrar@gmail.com','2020-03-20',1);

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
