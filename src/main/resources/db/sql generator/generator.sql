create table public.utenti
(
    id_utente     uuid default uuid_generate_v4() not null
        primary key,
    email         varchar(320)                    not null,
    password_hash varchar(256)                    not null,
    nome          varchar(20)                     not null,
    cognome       varchar(20)                     not null
);

alter table public.utenti
    owner to postgres;

create table public.pazienti
(
    id_paziente      uuid not null
        primary key
        constraint pazienti_id_utente_fkey
            references public.utenti
            on delete cascade,
    data_nascita     date not null,
    fattori_rischio  text,
    comorbita        text,
    patologie_presse text
);

alter table public.pazienti
    owner to postgres;

create table public.admins
(
    id_admin uuid not null
        primary key
        references public.utenti
);

alter table public.admins
    owner to postgres;

create table public.medici
(
    id_medico uuid not null
        primary key
        references public.utenti
);

alter table public.medici
    owner to postgres;

create table public.farmaci
(
    id_farmaco uuid default uuid_generate_v4() not null
        primary key,
    nome       varchar(20)                     not null,
    bugiardino text
);

alter table public.farmaci
    owner to postgres;

create table public.sintomi
(
    id_sintomo  uuid default uuid_generate_v4() not null
        primary key,
    nome        varchar(20)                     not null,
    descrizione text                            not null
);

alter table public.sintomi
    owner to postgres;

create table public.logs
(
    id_log      uuid default uuid_generate_v4() not null
        primary key,
    descrizione text                            not null,
    timestamp   timestamp                       not null
);

alter table public.logs
    owner to postgres;

create table public.segnalazioni
(
    id_segnalazione uuid default uuid_generate_v4() not null
        primary key,
    id_sintomo      uuid                            not null
        references public.sintomi,
    id_paziente     uuid                            not null
        references public.pazienti,
    intensita       integer                         not null,
    descrizione     text                            not null,
    timestamp       timestamp                       not null
);

alter table public.segnalazioni
    owner to postgres;

create table public.rilevazioni
(
    id_segnalazione uuid default uuid_generate_v4() not null
        primary key,
    id_paziente     uuid                            not null
        references public.pazienti,
    valore          double precision                not null,
    timestamp       timestamp                       not null
);

alter table public.rilevazioni
    owner to postgres;

create table public.assunzioni
(
    id_assunzione uuid default uuid_generate_v4() not null
        primary key,
    id_farmaco    uuid                            not null
        references public.farmaci,
    quantita      double precision                not null,
    timestamp     timestamp                       not null,
    id_paziente   uuid                            not null
        constraint assunzioni_id_paziente_fk
            references public.pazienti
);

alter table public.assunzioni
    owner to postgres;

create table public.terapie
(
    id_terapipa    uuid default uuid_generate_v4() not null
        primary key,
    id_farmaco     uuid                            not null
        references public.farmaci,
    num_assunzioni integer                         not null,
    quantita       double precision                not null,
    indicazioni    text,
    id_paziente    uuid                            not null
        constraint terapie_id_paziente_fk
            references public.pazienti,
    medico_curante uuid                            not null
        constraint terapie_medico_curante_fk
            references public.medici
);

alter table public.terapie
    owner to postgres;

create table public.databasechangelog
(
    id            varchar(255) not null,
    author        varchar(255) not null,
    filename      varchar(255) not null,
    dateexecuted  timestamp    not null,
    orderexecuted integer      not null,
    exectype      varchar(10)  not null,
    md5sum        varchar(35),
    description   varchar(255),
    comments      varchar(255),
    tag           varchar(255),
    liquibase     varchar(20),
    contexts      varchar(255),
    labels        varchar(255),
    deployment_id varchar(10)
);

alter table public.databasechangelog
    owner to postgres;

create table public.databasechangeloglock
(
    id          integer not null
        primary key,
    locked      boolean not null,
    lockgranted timestamp,
    lockedby    varchar(255)
);

alter table public.databasechangeloglock
    owner to postgres;

