--liquibase formatted sql

--changeset migration:202602_01_1
create table app_merch(
    id uuid primary key,
    created timestamp(6),
    modified timestamp(6),
    photos varchar[],
    size varchar,
    band varchar,
    color varchar,
    price numeric(10, 2)
);

--changeset migration:202602_01_2
create index idx_merch_band on app_merch(band);

--changeset migration:202602_01_3
alter table app_user add column email varchar;

--changeset migration:202602_01_4
alter table app_user drop column if exists first_name;

--changeset migration:202602_01_5
create table app_notification(
    id uuid primary key,
    creation timestamp(6),
    modified timestamp(6),
    type varchar not null,
    status varchar,
    recipient varchar not null,
    header varchar,
    body varchar not null
);

--changeset migration:202602_01_6
alter table app_merch add column compound varchar[];