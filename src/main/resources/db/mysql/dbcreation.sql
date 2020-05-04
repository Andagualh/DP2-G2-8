drop database if exists `medical`;
create database `medical`;

grant select, insert, update, delete, create, drop, references, index, alter, 
        create temporary tables, lock tables, create view, create routine, 
        alter routine, execute, trigger, show view
    on `medical`.* to 'medical'@'localhost';