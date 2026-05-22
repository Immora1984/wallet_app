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