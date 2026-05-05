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
values ('ff07701e-0cf8-4cc1-8869-9e33b64eba10', current_timestamp, current_timestamp, 'admin',
        '{noop}admin', true, 'Админ', '{ADMIN}');