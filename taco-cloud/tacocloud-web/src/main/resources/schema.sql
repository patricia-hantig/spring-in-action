create table if not exists Users (
    id identity,
    username varchar(25) not null,
    password varchar(255) not null,
    enabled boolean
);

create table if not exists Authorities (
    id identity,
    authority varchar(25) not null,
    username varchar(25) not null
);

create table if not exists Groups (
    id identity,
    group_name varchar(25) not null
);

create table if not exists Group_Members (
    id identity,
    username varchar(25) not null,
    group_id bigint not null
);

create table if not exists Group_Authorities (
    id identity,
    authority varchar(25) not null,
    group_id bigint not null
);

alter table Authorities add foreign key (username) references Users(username);
alter table Group_Members add foreign key (username) references Users(username);
alter table Group_Members add foreign key (group_id) references Groups(id);
alter table Group_Authorities add foreign key (group_id) references Groups(id);