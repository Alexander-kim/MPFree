-- 
-- User
-- 


CREATE USER 'mpfree'@'localhost' IDENTIFIED BY 'mpfree';
GRANT ALL PRIVILEGES ON * . * TO 'mpfree'@'localhost';
FLUSH PRIVILEGES;


-- 
-- Tables
-- 

CREATE DATABASE IF NOT EXISTS mpfree;

USE mpfree;

-- CREATE TABLE IF NOT EXISTS Requests (
--  ID INT NOT NULL AUTO_INCREMENT,
--  Status INT NOT NULL DEFAULT 0,
--  Source INT NOT NULL DEFAULT 1,
--  FileSize INT,
--  RequestingUID INT NOT NULL,
--  YoutubeID VARCHAR(256) NOT NULL,
--  Description VARCHAR(512),
--  PRIMARY KEY(ID)
-- );


CREATE TABLE IF NOT EXISTS VideoDB (
  VideoID VARCHAR(128) NOT NULL, 
  VideoTitle VARCHAR(256), 
  Duration INT, 
--  ViewCount, 
--  AddedOn
  UNIQUE(VideoID)
);


CREATE TABLE IF NOT EXISTS Conversions (
  ID INT NOT NULL AUTO_INCREMENT,
  Status INT NOT NULL DEFAULT 0,
  Timestamp DATETIME,
  RequestingUID VARCHAR(128) NOT NULL, 
  ClientIP VARCHAR(32) NOT NULL,
  VideoID VARCHAR(128) NOT NULL, 
  Description VARCHAR(512) NOT NULL DEFAULT 'No description available',
  DataContainer VARCHAR(128),
  FileSize VARCHAR(16),
  PRIMARY KEY(ID)
);
