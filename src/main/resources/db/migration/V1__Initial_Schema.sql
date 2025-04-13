CREATE EXTENSION postgis;
CREATE EXTENSION postgis_topology;

create table cidade (
    est_id bigint,
    id bigint generated by default as identity,
    cid_nome varchar(50),
    primary key (id)
);

create table estado (
    id bigint generated by default as identity,
    est_nome varchar(60),
    est_sigla varchar(2),
    primary key (id)
);

create table fazenda (
    faz_area float4,
    faz_prod_anual float4,
    cid_id bigint,
    id bigint generated by default as identity,
    faz_nome varchar(60),
    faz_tipo_solo varchar(100),
    primary key (id)
);

create table relatorio (
    percentual_diff float4,
    rel_status smallint check (rel_status between 0 and 3),
    end_review timestamp(6),
    id bigint generated by default as identity,
    start_review timestamp(6),
    use_id bigint,
    primary key (id)
);


create table missao (
    id bigint generated by default as identity,
    primary key (id)
    created_at timestamp(6),
    mis_id bigint,
);

create table resultado (
    id bigint generated by default as identity,
    primary key (id)
    res_source smallint check (res_source between 0 and 1),
    mis_id bigint,
);

create table daninhas (
    id bigint generated by default as identity,
    primary key (id)
    dan_shape geometry(Geometry, 4326),
    res_id bigint,
);

create table talhao (
    tal_area float4,
    faz_id bigint,
    id bigint generated by default as identity,
    tal_cultura varchar(60),
    tal_nome varchar(60),
    rel_shape geometry(Geometry, 4326),
    primary key (id)
);

create table usuario (
    use_role smallint check (use_role between 0 and 2),
    created_at timestamp(6),
    id bigint generated by default as identity,
    use_email varchar(100),
    use_nome varchar(60),
    primary key (id)
);

create table mtm_missoes_talhoes (
    tal_id bigint not null,
    mis_id bigint not null,
    primary key (tal_id, mis_id),
    constraint FK_talhao foreign key (tal_id) references talhao(id),
    constraint FK_missao foreign key (mis_id) references missao(id)
);

alter table if exists cidade 
    add constraint FK3jrphg3ll698ta68xi074uesj 
    foreign key (est_id) 
    references estado;

alter table if exists fazenda 
    add constraint FK1hovweo0237sowrvbo7vxvhkb 
    foreign key (cid_id) 
    references cidade;

alter table if exists relatorio 
    add constraint FK311ms1ya93wcjxc310qa3bv46 
    foreign key (use_id) 
    references usuario;

alter table if exists resultado 
    add constraint FKg9vmapx1koo03j6n8ew5dj4u3 
    foreign key (mis_id) 
    references missao;

alter table if exists talhao 
    add constraint FKnefek7ox53jp06g3bkkhyelkf 
    foreign key (faz_id) 
    references fazenda;

alter table if exists daninhas 
    add constraint FKmissdosijdisojdiajsioajia 
    foreign key (res_id) 
    references resultado;


INSERT INTO estado (est_nome, est_sigla) VALUES ('Acre', 'AC');
INSERT INTO estado (est_nome, est_sigla) VALUES ('Alagoas', 'AL');
INSERT INTO estado (est_nome, est_sigla) VALUES ('Amapá', 'AP');
INSERT INTO estado (est_nome, est_sigla) VALUES ('Amazonas', 'AM');
INSERT INTO estado (est_nome, est_sigla) VALUES ('Bahia', 'BA');
INSERT INTO estado (est_nome, est_sigla) VALUES ('Ceará', 'CE');
INSERT INTO estado (est_nome, est_sigla) VALUES ('Distrito Federal', 'DF');
INSERT INTO estado (est_nome, est_sigla) VALUES ('Espírito Santo', 'ES');
INSERT INTO estado (est_nome, est_sigla) VALUES ('Goiás', 'GO');
INSERT INTO estado (est_nome, est_sigla) VALUES ('Maranhão', 'MA');
INSERT INTO estado (est_nome, est_sigla) VALUES ('Mato Grosso', 'MT');
INSERT INTO estado (est_nome, est_sigla) VALUES ('Mato Grosso do Sul', 'MS');
INSERT INTO estado (est_nome, est_sigla) VALUES ('Minas Gerais', 'MG');
INSERT INTO estado (est_nome, est_sigla) VALUES ('Pará', 'PA');
INSERT INTO estado (est_nome, est_sigla) VALUES ('Paraíba', 'PB');
INSERT INTO estado (est_nome, est_sigla) VALUES ('Paraná', 'PR');
INSERT INTO estado (est_nome, est_sigla) VALUES ('Pernambuco', 'PE');
INSERT INTO estado (est_nome, est_sigla) VALUES ('Piauí', 'PI');
INSERT INTO estado (est_nome, est_sigla) VALUES ('Rio de Janeiro', 'RJ');
INSERT INTO estado (est_nome, est_sigla) VALUES ('Rio Grande do Norte', 'RN');
INSERT INTO estado (est_nome, est_sigla) VALUES ('Rio Grande do Sul', 'RS');
INSERT INTO estado (est_nome, est_sigla) VALUES ('Rondônia', 'RO');
INSERT INTO estado (est_nome, est_sigla) VALUES ('Roraima', 'RR');
INSERT INTO estado (est_nome, est_sigla) VALUES ('Santa Catarina', 'SC');
INSERT INTO estado (est_nome, est_sigla) VALUES ('São Paulo', 'SP');
INSERT INTO estado (est_nome, est_sigla) VALUES ('Sergipe', 'SE');
INSERT INTO estado (est_nome, est_sigla) VALUES ('Tocantins', 'TO');
