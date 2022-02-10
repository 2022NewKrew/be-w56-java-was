DROP TABLE IF EXISTS memo;
DROP TABLE IF EXISTS session;
DROP TABLE IF EXISTS user;

CREATE TABLE user
(
    userId  VARCHAR(32) PRIMARY KEY,
    password VARCHAR(32),
    name VARCHAR(32),
    email VARCHAR(32)
);

CREATE TABLE memo
(
    id BIGINT PRIMARY KEY,
    writer VARCHAR(32),
    content VARCHAR(300),
    createdAt datetime
);

CREATE TABLE session
(
    sessionId BIGINT PRIMARY KEY,
    userId VARCHAR(32),
    expire datetime
);
