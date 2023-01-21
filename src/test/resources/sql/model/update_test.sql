INSERT INTO make (id, name) 
values 
	(1, 'before'),
	(100, 'after');


INSERT INTO model (id, name, make_ref) 
values 
	(9999, 'before', 1);
