DROP TABLE IF EXISTS USER;

CREATE TABLE USER (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    userId VARCHAR(32),
    password VARCHAR(32),
    name VARCHAR(32),
    email VARCHAR(32)
)
