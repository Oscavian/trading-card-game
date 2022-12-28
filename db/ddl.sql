CREATE database swe1db;
GRANT ALL PRIVILEGES ON DATABASE swe1db TO swe1user;
create extension if not exists "uuid-ossp";

drop table if exists users;
drop table if exists cards;
drop table if exists stacks;
drop table if exists decks;
drop table if exists tradings;
drop table if exists packages;
drop table if exists battle_logs;
drop table if exists packages_cards;



create table if not exists users
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

create table if not exists cards
(
    uuid   uuid default uuid_generate_v4() primary key,
    name   varchar(255) not null,
    damage float        not null
);

create table if not exists decks
(
    user_uuid uuid,
    card_uuid uuid,
    primary key (user_uuid, card_uuid),
    constraint fk_decks_users
        foreign key (user_uuid)
            references users (uuid)
            on delete cascade,
    constraint fk_decks_cards
        foreign key (card_uuid)
            references cards (uuid)
            on delete cascade
);

create table if not exists stacks
(
    user_uuid uuid,
    card_uuid uuid,
    primary key (user_uuid, card_uuid),
    constraint fk_decks_users
        foreign key (user_uuid)
            references users (uuid)
            on delete cascade,
    constraint fk_decks_cards
        foreign key (card_uuid)
            references cards (uuid)
            on delete cascade
);

create table if not exists tradings
(
    uuid uuid,
    user_offering uuid,
    card_offered uuid,
    type varchar(255),
    minimum_damage int,
    constraint fk_tradings_users
        foreign key (user_offering)
            references users(uuid),
    constraint fk_tradings_cards
        foreign key (card_offered)
            references cards(uuid)
);

create table if not exists packages
(
    uuid  uuid,
    owner uuid,
    primary key (uuid),
    constraint fk_packages_users
        foreign key (owner)
            references users (uuid)
            on delete set null
);

create table if not exists packages_cards
(
    package uuid,
    card    uuid,
    primary key (package, card),
    constraint fk_pc_packages
        foreign key (package)
            references packages (uuid)
            on delete cascade,
    constraint fk_pc_cards
        foreign key (card)
            references cards (uuid)
            on delete cascade
);

create table if not exists battle_logs
(
    battle_uuid uuid,
    player1     uuid not null,
    player2     uuid not null,
    actions     text,
    primary key (battle_uuid),
    constraint fk_logs_player1
        foreign key (player1)
            references users (uuid)
            on delete no action,
    constraint fk_logs_player2
        foreign key (player1)
            references users (uuid)
            on delete no action
);