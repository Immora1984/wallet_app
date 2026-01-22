--liquibase formatted sql

--changeset migration:202601_01_1
create table app_user
(
    id          uuid primary key,
    created     timestamp(6),
    modified    timestamp(6),
    authorities varchar[],
    username    varchar not null,
    password    varchar,
    enabled     boolean not null,
    first_name  varchar
);

--changeset migration:202601_01_2
create index idx_user_username on app_user (username);

--changeset migration:202601_01_3
create table app_auth
(
    id       uuid primary key,
    jti      varchar not null,
    created  timestamp,
    last_use timestamp with time zone,
    user_id  uuid
);

--changeset migration:202601_01_4
create index idx_auth_jti on app_auth (jti);

--changeset migration:202601_01_5
insert into app_user (id, created, modified, username, password, enabled, first_name, authorities)
values ('ff07701e-0cf8-4cc1-8869-9e33b64eba10', current_timestamp, current_timestamp, 'user',
        '{noop}user', true, 'Пользователь', '{USER}');

--changeset migration:202601_01_6
create table app_wallet
(
    id       uuid primary key,
    created  timestamp not null,
    modified timestamp not null,
    owner    uuid,
    balance  numeric(19, 2),
    constraint fk_card_user foreign key (owner) references app_user (id)
);

--changeset migration:202601_01_7
create index idx_wallet_user_id on app_wallet (owner);
-- индекс по user_id

--changeset migration:202601_01_8
create index idx_wallet_number on app_wallet (id);

--changeset migration:202601_01_09
alter table app_wallet
    add column version integer not null default 0;

--changeset migration:202601_01_10
insert into app_wallet (id, created, modified, owner, balance)
values ('a8b3c7d1-1234-5678-9101-abcdef123456', current_timestamp, current_timestamp,
        'ff07701e-0cf8-4cc1-8869-9e33b64eba10', 100000.00)
