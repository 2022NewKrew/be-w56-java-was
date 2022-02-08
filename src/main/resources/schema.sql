create table if not exists users (
    id varchar(255) primary key,
    password varchar(255) not null ,
    name varchar(255) not null,
    email varchar(255) not null
);

create table if not exists memos (
    id bigint primary key auto_increment,
    user_id varchar(255) not null,
    memo text not null,
    created_at datetime not null,
    foreign key (user_id) references users (id)
)
