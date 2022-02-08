drop table user;
drop table memo;

create table if not exists user
(
    user_id  varchar(100) not null primary key,
    password varchar(100) not null,
    name     varchar(100) not null,
    email    varchar(100) not null
);

create table if not exists memo
(
    memo_id   bigint        not null auto_increment primary key,
    writer_id varchar(100)  not null,
    content   varchar(1500) not null,
    reg_date  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
