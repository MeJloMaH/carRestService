INSERT INTO make (id, name)
values	(1, 'find me!1'),
		(2, 'find me!2'),
		(3, 'find me!3'),
		(4, 'find me!4');

INSERT INTO model (id, name, make_ref)
values 	(1, 'find me!1', 1),
		(2, 'find me!2', 2),
		(3, 'find me!3', 3),
		(4, 'find me!4', 4);

INSERT INTO category (id, name)
values	(1, 'find me!1'),
		(2, 'find me!2'),
		(3, 'find me!3'),
		(4, 'find me!4');

INSERT INTO car (id, object_id, year, make_ref, model_ref)
values 	(11000, 'find me!1', 2020, 1, 1),
		(22000, 'find me!2', 2020, 2, 2),
		(33000, 'find me!3', 2020, 3, 3),
		(44000, 'find me!4', 2020, 4, 4);

INSERT INTO car_category (car_id, category_id)
values	(11000, 1),
 		(22000, 2),
 		(33000, 3),
 		(44000, 4);
