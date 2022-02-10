DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users(
    id INT AUTO_INCREMENT,
    userId VARCHAR(32),
    password VARCHAR(32),
    userName VARCHAR(32),
    email VARCHAR(64),
    PRIMARY KEY (id),
    UNIQUE KEY (userId)
                                );

