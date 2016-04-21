create table user(
id bigint auto_increment,
user_name varchar(30),
password varchar(50),
salt varchar(50),
status char(1),
role_ids varchar(255),
permission_ids varchar(255),
primary key(id)
);

create table role(
id bigint auto_increment,
name varchar(30),
name_en varchar(30),
name_zh varchar(30),
permission_ids varchar(255),
primary key(id)
);

create table permission(
id bigint auto_increment,
name varchar(30),
name_zh varchar(30),
parent_id bigint,
primary key(id)
);