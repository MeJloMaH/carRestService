drop table if exists car_category;
drop table if exists car;
drop table if exists category;
drop table if exists model;
drop table if exists make;

create table make
(
	id   		BIGSERIAL PRIMARY KEY,
	name		varchar(255) UNIQUE not null
);

create table model
(
	id   		BIGSERIAL PRIMARY KEY,
	make_ref	BIGINT REFERENCES make (id) on delete cascade not null,
	name		varchar(255) UNIQUE not null
);

create table category
(
	id   		BIGSERIAL PRIMARY KEY,
	name		varchar(255) UNIQUE not null
);

create table car
(
    id   		BIGSERIAL PRIMARY KEY,
    object_id	varchar(255) UNIQUE not null,
    year 		INT not null,
    make_ref	BIGINT REFERENCES make (id) on delete cascade not null,
    model_ref	BIGINT REFERENCES model (id) on delete cascade not null
);

create table car_category
(
	car_id		BIGINT REFERENCES car (id) on delete cascade not null,
	category_id	BIGINT REFERENCES category (id) on delete cascade not null,
	UNIQUE		(car_id, category_id)
);

	
	
	


