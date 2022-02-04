drop table if exists users;
drop table if exists memo;
create table users (id integer primary key auto_increment,user_id varchar(50) not null,name varchar(50) not null,email varchar(50) not null,password varchar(50) not null);
create table memo (id integer primary key auto_increment,author_id integer references users (id),content varchar(100) not null, created_at date);
