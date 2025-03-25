INSERT INTO cidade (est_id, cid_nome) VALUES (
	(SELECT id FROM estado WHERE est_sigla = 'SP'),
	'São José dos Campos')
	;
INSERT INTO fazenda (cid_id, faz_nome, faz_tipo_solo, faz_area, faz_prod_anual) VALUES (
	(SELECT id FROM cidade WHERE cid_nome = 'São José dos Campos'),
	'Fazenda Foo',
	'Argiloso',
	20,
	20);