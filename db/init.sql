DROP DATABASE IF EXISTS simpleDb__test;
CREATE DATABASE simpleDb__test;
USE simpleDb__test;

DROP DATABASE IF EXISTS simpleDb;
CREATE DATABASE simpleDb;
USE simpleDb;

CREATE TABLE quotation (
     id INT UNSIGNED NOT NULL AUTO_INCREMENT,
     PRIMARY KEY(id),
     createdDate DATETIME NOT NULL,
     modifiedDate DATETIME NOT NULL,
     content VARCHAR(100) NOT NULL,
     authorName CHAR(100) NOT NULL
);