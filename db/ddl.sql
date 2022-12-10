CREATE database swe1db;

GRANT ALL PRIVILEGES ON DATABASE swe1db TO swe1user;

create extension if not exists "uuid-ossp";

create table users
(
    uuid     uuid default uuid_generate_v4() primary key,
    username varchar(255) not null unique,
    password varchar(512) not null,
    bio      text,
    image    text,
    elo      int,
    wins     int,
    losses   int
);