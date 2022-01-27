INSERT INTO IMAGE_ENTITY(id,label,url) values(1,'Image1','url1');
INSERT INTO IMAGE_OBJECT_ENTITY(id,confidence,name) values(11,100.0,'dog');
INSERT INTO IMAGE_OBJECT_ENTITY(id,confidence,name) values(12,90.0,'cat');
INSERT INTO IMAGEENTITY_IMAGEOBJECTENTITY(image_id,image_object_id) values(1,11);
INSERT INTO IMAGEENTITY_IMAGEOBJECTENTITY(image_id,image_object_id) values(1,2);

INSERT INTO IMAGE_ENTITY(id,label,url) values(2,'Image2','url2');
INSERT INTO IMAGE_OBJECT_ENTITY(id,confidence,name) values(21,100.0,'dog');
INSERT INTO IMAGE_OBJECT_ENTITY(id,confidence,name) values(22,90.0,'cow');
INSERT INTO IMAGEENTITY_IMAGEOBJECTENTITY(image_id,image_object_id) values(2,21);
INSERT INTO IMAGEENTITY_IMAGEOBJECTENTITY(image_id,image_object_id) values(2,22);