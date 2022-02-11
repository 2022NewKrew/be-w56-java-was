create table if not exists member(
    userId varchar(10) primary key,
    password varchar(20) not null,
    name varchar(20) not null,
    email varchar(20) not null
);

create table if not exists post(
    id bigint primary key,
    writer varchar(10) not null,
    content varchar(200) not null,
    written datetime not null
);