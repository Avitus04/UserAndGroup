CREATE DATABASE IF NOT EXISTS user_and_group;
USE user_and_group;
 
 
drop user if exists admin@localhost;
flush privileges;
 
-- Créer un utilisateur avec un mot de passe
CREATE USER 'admin'@'localhost' IDENTIFIED BY '1234';
 
-- Accorder tous les privilèges à cet utilisateur sur la base de données
GRANT ALL PRIVILEGES ON user_and_group.* TO 'admin'@'localhost';
 
-- Appliquer les changements
FLUSH PRIVILEGES;


CREATE TABLE user_profile (
	profile_id VARCHAR(16) PRIMARY KEY,
	`description` VARCHAR(500),
	access_level TINYINT DEFAULT '3'
);

CREATE TABLE token (
    token_id VARCHAR(16) NOT NULL PRIMARY KEY,
    date_created DATETIME DEFAULT CURRENT_TIMESTAMP,
    date_expiration DATETIME NOT NULL,
    status_token BOOL NOT NULL
);
 
 
 CREATE TABLE membership (
	membership_id VARCHAR(16) PRIMARY KEY,
    address_id VARCHAR(16) DEFAULT NULL,
    profile_id VARCHAR(16) DEFAULT NULL,
    token_id VARCHAR(16) DEFAULT NULL,
    username VARCHAR(50) NOT NULL,
    passwd VARCHAR(255) NOT NULL,
    date_created DATETIME DEFAULT CURRENT_TIMESTAMP,
    date_last_connection DATETIME,
    status_user BOOL NOT NULL
);



INSERT INTO token (token_id, date_created, date_expiration, status_token) VALUES
('T1', '2025-01-01 10:00:00', '2025-01-15 10:00:00', TRUE),
('T2', '2025-01-02 11:00:00', '2025-01-16 11:00:00', FALSE),
('T3', '2025-01-03 12:00:00', '2025-01-17 12:00:00', TRUE),
('T4', '2025-01-04 13:00:00', '2025-01-18 13:00:00', FALSE),
('T5', '2025-01-05 14:00:00', '2025-01-19 14:00:00', TRUE),
('T6', '2025-01-06 15:00:00', '2025-01-20 15:00:00', FALSE),
('T7', '2025-01-07 16:00:00', '2025-01-21 16:00:00', TRUE),
('T8', '2025-01-08 17:00:00', '2025-01-22 17:00:00', FALSE),
('T9', '2025-01-09 18:00:00', '2025-01-23 18:00:00', TRUE),
('T10', '2025-01-10 19:00:00', '2025-01-24 19:00:00', TRUE);


INSERT INTO user_profile (profile_id, `description`, access_level) 
VALUES 
    ('admin', 'Administrators with full access', '1'),
    ('CE', 'Comité d\'entreprise with limited management rights', '2'),
    ('member', 'Regular members with basic access rights', '3');


INSERT INTO membership (membership_id, address_id, profile_id, token_id, username, passwd, date_created, date_last_connection, status_user) VALUES
('U1', '21f677b9208f4267', 'admin', 'T1', 'JohnDoe', 'password123', '2025-01-01 10:00:00', '2025-01-05 12:00:00', TRUE),
('U2', '98a93e53af4d4e73', 'CE', 'T2', 'JaneSmith', 'securePwd!', '2025-01-02 11:00:00', '2025-01-06 13:00:00', FALSE),
('U3', 'c9da74b4866e4887', 'member', 'T3', 'AliceBrown', 'mypassword', '2025-01-03 12:00:00', '2025-01-07 14:00:00', TRUE),
('U4', '30ed287364034a96', 'member', 'T4', 'BobJohnson', '12345secure', '2025-01-04 13:00:00', '2025-01-08 15:00:00', FALSE),
('U5', 'd1a3cca12d644e2f', 'admin', 'T5', 'CharlieGreen', 'topSecret', '2025-01-05 14:00:00', '2025-01-09 16:00:00', TRUE),
('U6', '0cfb42171c144c0c', 'CE', 'T6', 'DianaWhite', 'password!', '2025-01-06 15:00:00', '2025-01-10 17:00:00', FALSE),
('U7', '4054becb441d4d6a', 'member',  'T7', 'EvanBlue', '123Secure!', '2025-01-07 16:00:00', '2025-01-11 18:00:00', TRUE),
('U8', '91ec6421a9494ab3', 'CE','T8', 'FionaRed', 'mySuperPass', '2025-01-08 17:00:00', '2025-01-12 19:00:00', FALSE),
('U9', '9a39e3bc67c44c6a', 'member', 'T9', 'GeorgeBlack', 'password#1', '2025-01-09 18:00:00', '2025-01-13 20:00:00', TRUE),
('U10', '21ad8e6bf14643ea', 'member', 'T10', 'HannahYellow', 'Yellow@123', '2025-01-10 19:00:00', '2025-01-14 21:00:00', TRUE);