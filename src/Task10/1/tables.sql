DROP TABLE IF EXISTS printer;
DROP TABLE IF EXISTS laptop;
DROP TABLE IF EXISTS PC;
DROP TABLE IF EXISTS product;

CREATE TABLE product (
	model VARCHAR(50) NOT NULL PRIMARY KEY,
	maker VARCHAR(10) NOT NULL,
	type VARCHAR(50) NOT NULL CHECK (type IN ('PC', 'Laptop', 'Printer'))
);

CREATE TABLE PC (
	code INT NOT NULL PRIMARY KEY,
	model VARCHAR(50) NOT NULL,
	speed SMALLINT NOT NULL CHECK (speed > 0),
	ram SMALLINT NOT NULL CHECK (ram > 0),
	hd REAL NOT NULL CHECK (hd > 0),
	cd VARCHAR(10) NOT NULL,
	price MONEY,
	FOREIGN KEY (model) REFERENCES product (model)
);

CREATE TABLE laptop (
	code INT NOT NULL PRIMARY KEY,
	model VARCHAR(50) NOT NULL,
	speed SMALLINT NOT NULL CHECK (speed > 0),
	ram SMALLINT NOT NULL CHECK (ram > 0),
	hd REAL NOT NULL CHECK (hd > 0),
	screen SMALLINT NOT NULL CHECK (screen > 0),
	price MONEY,
	FOREIGN KEY (model) REFERENCES product (model)
);

CREATE TABLE printer (
	code INT NOT NULL PRIMARY KEY,
	model VARCHAR(50) NOT NULL,
	color CHAR(1) NOT NULL CHECK (color IN ('y', 'n')),
	type VARCHAR(10) NOT NULL CHECK (type IN ('Laser', 'Jet', 'Matrix')),
	price MONEY,
	FOREIGN KEY (model) REFERENCES product (model)
);