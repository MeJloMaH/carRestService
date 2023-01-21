INSERT INTO make (id, name) 
values 
	(1, 'test');

INSERT INTO model (id, name, make_ref) 
values 
	(1, 'test', '1');

INSERT INTO category (id, name) 
values 
	(1, 'test');


INSERT INTO car (id, object_id, year, make_ref, model_ref)	
values (11000, 'test', 2020, 1, 1);

INSERT INTO car_category (car_id, category_id)
values (11000, 1);