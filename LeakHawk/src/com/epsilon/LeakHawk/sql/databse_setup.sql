CREATE DATABASE epsilondb;

USE epsilondb;

CREATE USER 'epsilontest'@'localhost' IDENTIFIED BY 'epsilontest';

GRANT ALL PRIVILEGES ON epsilondb.* TO 'epsilontest'@'localhost';


CREATE TABLE feed_entry(
   entry_id INT NOT NULL AUTO_INCREMENT,
   entry_key VARCHAR(50) NOT NULL,
   entry_url VARCHAR(100) NOT NULL,
   entry_title VARCHAR(500) ,
   entry_file LONGBLOB,
   entry_user VARCHAR(100) ,   
   PRIMARY KEY ( entry_id )
);