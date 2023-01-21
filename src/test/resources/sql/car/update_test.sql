INSERT INTO make (id, name) 
values 
	(1, 'before'),
	(100, 'after');


INSERT INTO model (id, name, make_ref) 
values 
	(1, 'before', '1'),
	(100, 'after', '100');


INSERT INTO category (id, name) 
values 
	(1, 'before'),
	(100, 'after');


INSERT INTO car (id, object_id, year, make_ref, model_ref)	
values (11000, 'before', 1000, 1, 1);

INSERT INTO car_category (car_id, category_id)
values (11000, 1);
