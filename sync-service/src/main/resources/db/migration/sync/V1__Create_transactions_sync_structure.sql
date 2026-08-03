CREATE extension if not exists "pgcrypto";

create table migration_history (
    id uuid not null,
    date timestamp(6) with time zone,
    duration bigint,
    nb_record_migrated bigint,
    status varchar(255),
    primary key (id)
);

create table migration_data (
    id uuid not null,
    from_id varchar(255),
    to_id varchar(255),
    status varchar(255),
    history_id uuid not null,
    primary key (id)
);

alter table if exists migration_data
    add constraint fk_history
    foreign key (history_id)
    references migration_history;

alter table migration_data alter column id set default gen_random_uuid();

alter table migration_history alter column id set default gen_random_uuid();
