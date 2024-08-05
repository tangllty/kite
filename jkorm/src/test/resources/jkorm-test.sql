drop database if exists `jkorm-test`;

create database `jkorm-test` default character set utf8mb4 collate utf8mb4_unicode_ci;

set names utf8mb4;

use `jkorm-test`;

drop table if exists account;
create table account
(
    id bigint auto_increment primary key comment 'ID',
    username varchar(32) default '' comment 'Username',
    password varchar(32) default '' comment 'Password'
);

insert into account (username, password) values
('admin', 'admin'),
('user', 'user');
