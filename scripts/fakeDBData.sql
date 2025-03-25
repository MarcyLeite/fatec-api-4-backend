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
INSERT INTO talhao (tal_area, faz_id, tal_cultura, tal_nome, rel_shape)
VALUES 
	(50.0,
	(SELECT id FROM fazenda WHERE faz_nome = 'Fazenda Foo'),
	'Soja',
	'Talhão A',
	ST_GeomFromText('POLYGON((-47.9 -15.8, -47.8 -15.8, -47.8 -15.9, -47.9 -15.9, -47.9 -15.8))',4326));