INSERT INTO make (id, name)
values (1, 'first'), (2, 'second');

INSERT INTO model (id, name, make_ref)
values (1, 'first', 1), (2, 'second', 2);

INSERT INTO category (id, name)
values (1, 'first'), (2, 'second');

INSERT INTO car (id, object_id, year, make_ref, model_ref)
values 	(99, 'first', 2020, 1, 1), 
		(11000, 'second', 2020, 2, 2);

INSERT INTO car_category (car_id, category_id)
values (99, 1), (11000, 2);