create table safebox (
	id 		INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	uuid 	VARCHAR(36) NOT NULL,
	name 	VARCHAR(30) NOT NULL,
	password 	VARCHAR(32) NOT NULL
);

create table safebox (
	id 		INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	uuid 	VARCHAR(36) NOT NULL,
	item 	VARCHAR(50) NOT NULL
	FOREIGN KEY (uuid) REFERENCES safebox(uuid)
);