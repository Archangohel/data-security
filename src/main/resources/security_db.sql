

CREATE TABLE UserEntityAccessMapping
(
    user_id INT NOT NULL,
    entity_class VARCHAR(500) NOT NULL,
    entity_id VARCHAR(50) NOT NULL
);
CREATE INDEX userEntityAccessMappingIndex ON UserEntityAccessMapping (user_id, entity_class, entity_id);

-- dummy entries
INSERT INTO security.UserEntityAccessMapping (user_id, entity_class, entity_id) VALUES (1, 'com.dev.target.entity.Person', '1');
INSERT INTO security.UserEntityAccessMapping (user_id, entity_class, entity_id) VALUES (1, 'com.dev.target.entity.Person', '7');
INSERT INTO security.UserEntityAccessMapping (user_id, entity_class, entity_id) VALUES (1, 'com.dev.target.entity.Person', '8');
INSERT INTO security.UserEntityAccessMapping (user_id, entity_class, entity_id) VALUES (1, 'com.dev.target.entity.Person', 'Person2');
INSERT INTO security.UserEntityAccessMapping (user_id, entity_class, entity_id) VALUES (1, 'com.dev.target.entity.Person', 'Person9');
