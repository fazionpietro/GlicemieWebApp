create table if not exists utenti
(
    id_utente uuid primary key DEFAULT uuid_generate_v4(),
    email varchar(320) not null,
    password_hash varchar(256) not null,
    nome varchar(20) not null,
    cognome varchar(20) not null
);

create table if not exists pazienti
(
    id_paziente uuid not null
        primary key
        constraint pazienti_id_utente_fkey
            references utenti
            on delete cascade,
    data_nascita date not null,
    fattori_rischio text,
    comorbita text,
    patologie_presse text
);

create table if not exists admins
(
    id_admin uuid not null
        primary key
        references utenti
);

create table if not exists medici
(
    id_medico uuid not null
        primary key
        references utenti
);

create table if not exists farmaci
(
    id_farmaco uuid primary key DEFAULT uuid_generate_v4(),
    nome varchar(20) not null,
    bugiardino text
);

create table if not exists sintomi
(
    id_sintomo uuid primary key DEFAULT uuid_generate_v4(),
    nome varchar(20) not null,
    descrizione text not null
);

create table if not exists logs
(
    id_log uuid primary key DEFAULT uuid_generate_v4(),
    descrizione text not null,
    timestamp timestamp not null
);

create table if not exists segnalazioni
(
    id_segnalazione uuid primary key DEFAULT uuid_generate_v4(),
    id_sintomo uuid not null
        references sintomi,
    id_paziente uuid not null
        references pazienti,
    intensita integer not null,
    descrizione text not null,
    timestamp timestamp not null
);

create table if not exists rilevazioni
(
    id_segnalazione uuid primary key DEFAULT uuid_generate_v4(),
    id_paziente uuid not null
        references pazienti,
    valore double precision not null,
    timestamp timestamp not null
);

create table if not exists assunzioni
(
    id_assunzione uuid primary key DEFAULT uuid_generate_v4(),
    id_farmaco uuid not null
        references farmaci,
    quantita double precision not null,
    timestamp timestamp not null,
    id_paziente uuid not null
        constraint assunzioni_id_paziente_fk
            references pazienti
);

create table if not exists terapie
(
    id_terapipa uuid primary key DEFAULT uuid_generate_v4(),
    id_farmaco uuid not null
        references farmaci,
    num_assunzioni integer not null,
    quantita double precision not null,
    indicazioni text,
    id_paziente uuid not null
        constraint terapie_id_paziente_fk
            references pazienti,
    medico_curante uuid not null
        constraint terapie_medico_curante_fk
            references medici
);

