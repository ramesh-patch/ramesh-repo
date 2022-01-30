CREATE TABLE IF NOT EXISTS image_entity (
	id INT NOT NULL,
	label varchar(50),
	url varchar(250),
	PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS image_object_entity (
	id INT NOT NULL,
	double confidence,
	name varchar(50),
	PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS imageentity_imageobjectentity (
	image_id INT NOT NULL,
	image_object_id INT NOT NULL,
	CONSTRAINT fk_image_object
      FOREIGN KEY(image_object_id)
      REFERENCES image_object_entity(id),
    CONSTRAINT fk_image
      FOREIGN KEY(image_id)
      REFERENCES image_entity(id)
);

CREATE SEQUENCE IF NOT EXISTS image_entity_id_seq;

CREATE SEQUENCE IF NOT EXISTS imageobjectsssequence;
