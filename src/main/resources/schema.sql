create table if not exists users (
    id varchar(255) primary key,
    password varchar(255) not null ,
    name varchar(255) not null,
    email varchar(255) not null
);
