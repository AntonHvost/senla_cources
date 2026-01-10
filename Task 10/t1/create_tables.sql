CREATE TABLE product (
	marker varchar(10) NOT NULL,
	model varchar(50) NOT NULL PRIMARY KEY,
	type varchar(50) NOT NULL
);

CREATE TABLE laptop (
	code serial PRIMARY KEY,
	model varchar(50) NOT NULL REFERENCES product(model),
	speed smallint NOT NULL CHECK (speed > 0),
	ram smallint NOT NULL CHECK (ram > 0),
	hd real NOT NULL CHECK (hd > 0),
	price numeric(10,2) CHECK (price > 0),
	screen smallint NOT NULL
);

CREATE TABLE pc (
	code serial PRIMARY KEY,
	model varchar(50) NOT NULL REFERENCES product(model),
	speed smallint NOT NULL CHECK (speed > 0),
	ram smallint NOT NULL CHECK (ram > 0),
	hd real NOT NULL CHECK (hd > 0),
	cd varchar(10) NOT NULL,
	price numeric(10,2) CHECK (price > 0)
);

CREATE TABLE printer (
	code serial PRIMARY KEY,
	model varchar(50) NOT NULL REFERENCES product(model),
	color char(1) NOT NULL,
	type varchar(10) NOT NULL,
	price numeric(10,2)
);