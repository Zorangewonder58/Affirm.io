create database Affirm;
use Affirm;

create table User (
    UserID primary key auto_increment,
    username varchar(255) not null,
    password varchar (255) not null);