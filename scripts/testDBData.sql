INSERT INTO cidade (est_id, cid_nome) VALUES (
	(SELECT id FROM estado WHERE est_sigla = 'SP'),
	'São José dos Campos');
INSERT INTO fazenda (cid_id, faz_nome, faz_tipo_solo, faz_area, faz_prod_anual) VALUES (
	(SELECT id FROM cidade WHERE cid_nome = 'São José dos Campos'),
	'Fazenda Foo',
	'Argiloso',
	20,
	20);

INSERT INTO cidade (est_id, cid_nome) VALUES (
	(SELECT id FROM estado WHERE est_sigla = 'AC'),
	'Rio Branco');
INSERT INTO cidade (est_id, cid_nome) VALUES (
	(SELECT id FROM estado WHERE est_sigla = 'AL'),
 	'Maceió');
INSERT INTO cidade (est_id, cid_nome) VALUES (
	(SELECT id FROM estado WHERE est_sigla = 'AP'),
	'Macapá');
INSERT INTO cidade (est_id, cid_nome) VALUES (
	(SELECT id FROM estado WHERE est_sigla = 'AM'),
	'Manaus');
INSERT INTO cidade (est_id, cid_nome) VALUES (
	(SELECT id FROM estado WHERE est_sigla = 'BA'),
	'Salvador');

INSERT INTO fazenda (faz_area, faz_prod_anual, cid_id, faz_nome, faz_tipo_solo)
VALUES (
	150.5,
	2000.0, 
	(SELECT id FROM cidade WHERE cid_nome = 'Rio Branco'),
	'Fazenda Rio Verde',
	'Argiloso');