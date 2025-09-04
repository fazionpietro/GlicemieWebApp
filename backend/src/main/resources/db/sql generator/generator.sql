create table if not exists public.databasechangelog
(
    id varchar(255) not null,
    author varchar(255) not null,
    filename varchar(255) not null,
    dateexecuted timestamp not null,
    orderexecuted integer not null,
    exectype varchar(10) not null,
    md5sum varchar(35),
    description varchar(255),
    comments varchar(255),
    tag varchar(255),
    liquibase varchar(20),
    contexts varchar(255),
    labels varchar(255),
    deployment_id varchar(10)
);

create table if not exists public.databasechangeloglock
(
    id integer not null
        primary key,
    locked boolean not null,
    lockgranted timestamp,
    lockedby varchar(255)
);

create table if not exists public.utenti
(
    id_utente uuid default uuid_generate_v4() not null
        primary key,
    email varchar(320) not null
        unique,
    password_hash varchar(256) not null,
    nome varchar(20) not null,
    cognome varchar(20) not null,
    ruolo varchar(20) not null
        constraint utenti_ruolo_check
            check ((ruolo)::text = ANY (ARRAY[('ROLE_ADMIN'::character varying)::text, ('ROLE_MEDICO'::character varying)::text, ('ROLE_PAZIENTE'::character varying)::text])),
    data_nascita date not null
);

create trigger trigger_check_utente_ruolo_change
    before update
    on public.utenti
    for each row
execute procedure public.check_utente_ruolo_change();

create table if not exists public.pazienti
(
    id_paziente uuid not null
        primary key
        constraint pazienti_id_utente_fkey
            references public.utenti
            on delete cascade,
    fattori_rischio text,
    comorbita text,
    patologie_pregresse text,
    id_medico uuid
        constraint fk_pazienti_on_id_medico
            references public.utenti
);

create trigger trigger_check_paziente_medico_ruolo
    before insert or update
    on public.pazienti
    for each row
execute procedure public.check_paziente_medico_ruolo();

create table if not exists public.farmaci
(
    id_farmaco uuid default uuid_generate_v4() not null
        primary key,
    nome varchar(20) not null,
    bugiardino text
);

create table if not exists public.sintomi
(
    id_sintomo uuid default uuid_generate_v4() not null
        primary key,
    nome varchar(20) not null,
    descrizione text not null
);

create table if not exists public.logs
(
    id_log uuid default uuid_generate_v4() not null
        primary key,
    descrizione text not null,
    timestamp timestamp not null
);

create table if not exists public.segnalazioni
(
    id_segnalazione uuid default uuid_generate_v4() not null
        primary key,
    id_sintomo uuid not null
        references public.sintomi,
    id_paziente uuid not null
        references public.pazienti,
    intensita integer not null
        constraint check_intensita
            check ((intensita >= 1) AND (intensita <= 10)),
    descrizione text not null,
    timestamp timestamp not null
);

create table if not exists public.rilevazioni
(
    id_paziente uuid not null
        references public.pazienti,
    valore double precision not null,
    timestamp timestamp not null,
    id_rilevazione uuid not null
        constraint pk_rilevazioni
            primary key
);

create table if not exists public.assunzioni
(
    id_assunzione uuid default uuid_generate_v4() not null
        primary key,
    id_farmaco uuid not null
        references public.farmaci,
    quantita double precision not null
        constraint check_quantita_positiva
            check (quantita > (0)::double precision),
    timestamp timestamp not null,
    id_paziente uuid not null
        constraint assunzioni_id_paziente_fk
            references public.pazienti
);

create table if not exists public.terapie
(
    id_farmaco uuid not null
        references public.farmaci,
    num_assunzioni integer not null
        constraint check_num_assunzioni_positivo
            check (num_assunzioni > 0),
    quantita double precision not null
        constraint check_quantita_terapie_positiva
            check (quantita > (0)::double precision),
    indicazioni text,
    id_paziente uuid not null
        constraint terapie_id_paziente_fk
            references public.pazienti,
    medico_curante uuid not null
        constraint terapie_medico_curante_fk
            references public.utenti,
    id_terapia uuid not null
        constraint pk_terapie
            primary key
);

create trigger trigger_check_terapia_medico_ruolo
    before insert or update
    on public.terapie
    for each row
execute procedure public.check_terapia_medico_ruolo();

