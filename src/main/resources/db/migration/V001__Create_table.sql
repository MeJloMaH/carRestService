drop table if exists car_category;
drop table if exists car;
drop table if exists category;
drop table if exists model;
drop table if exists make;

create table make
(
	id   		SERIAL PRIMARY KEY,
	name		varchar(255) UNIQUE not null
);

create table model
(
	id   		SERIAL PRIMARY KEY,
	make_ref	INT REFERENCES make (id) on delete cascade not null,
	name		varchar(255) UNIQUE not null
);

create table category
(
	id   		SERIAL PRIMARY KEY,
	name		varchar(255) UNIQUE not null
);

create table car
(
    id   		SERIAL PRIMARY KEY,
    object_id	varchar(255) UNIQUE not null,
    year 		INT not null,
    make_ref	INT REFERENCES make (id) on delete cascade not null,
    model_ref	INT REFERENCES model (id) on delete cascade not null
);

create table car_category
(
	car_id		INT REFERENCES car (id) on delete cascade not null,
	category_id	INT REFERENCES category (id) on delete cascade not null,
	UNIQUE		(car_id, category_id)
);

	
	
	


