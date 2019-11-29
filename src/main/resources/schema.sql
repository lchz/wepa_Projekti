DROP TABLE IF EXISTS Account;

CREATE TABLE Account (
    id Long PRIMARY KEY AUTO_INCREMENT,
    firstname VARCHAR(250) NOT NULL,
    familyname VARCHAR(250) NOT NULL,
    username VARCHAR(250) NOT NULL,
    password VARCHAR(250) NOT NULL

);
