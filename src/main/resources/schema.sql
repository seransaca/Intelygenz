drop table if exists safebox;
create table safebox (
	id 		INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	uuid 	VARCHAR(36) NOT NULL UNIQUE,
	name 	VARCHAR(30) NOT NULL,
	password 	VARCHAR(32) NOT NULL,
	blocked INT NOT NULL DEFAULT 0
);

drop table if exists items;
create table items (
	id 		INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	uuid 	VARCHAR(36) NOT NULL,
	item 	VARCHAR(50) NOT NULL,
	FOREIGN KEY (uuid) REFERENCES safebox(uuid)
);