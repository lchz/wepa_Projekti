DROP TABLE IF EXISTS User;
-- DROP TABLE IF EXISTS Message;

CREATE TABLE User (
    id Long PRIMARY KEY AUTO_INCREMENT,
    firstname VARCHAR(250) NOT NULL,
    familyname VARCHAR(250) NOT NULL,
    username VARCHAR(250) NOT NULL,
    password VARCHAR(250) NOT NULL

);

-- CREATE TABLE Message (
--     id Long PRIMARY KEY AUTO_INCREMENT,
--     date LocalDate,
--     time LocalTime,
--     likes Long
-- );
