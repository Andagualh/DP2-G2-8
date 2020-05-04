drop database if exists `medical`;
create database `medical`;

drop user if exists 'medical'@'localhost';
CREATE USER 'medical'@'localhost' IDENTIFIED BY 'medical';


grant select, insert, update, delete, create, drop, references, index, alter, 
        create temporary tables, lock tables, create view, create routine, 
        alter routine, execute, trigger, show view
    on `medical`.* to 'medical'@'localhost';