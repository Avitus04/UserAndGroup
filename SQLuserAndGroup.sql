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


CREATE TABLE groupModel (
	groupID VARCHAR(250) PRIMARY KEY,
	descriptionGroup VARCHAR(500),
	ACCESS_LEVEL ENUM('1', '2', '3') DEFAULT '3'
);

CREATE TABLE tokenModel (
    tokenID VARCHAR(250) NOT NULL PRIMARY KEY,
    creationDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    expirationDate DATETIME NOT NULL,
    statusToken BOOL NOT NULL
);
 
 CREATE TABLE AddressModel (
    uuid VARCHAR(36) NOT NULL PRIMARY KEY,
    unit_number INT,     
    street_name VARCHAR(255),
    postal_code VARCHAR(20),
    city VARCHAR(100),
    country VARCHAR(100),
    additional_information TEXT,
    creation_date DATE NOT NULL,
    update_date DATE,
    deleted BOOLEAN NOT NULL DEFAULT 0
);
 
 CREATE TABLE userModel (
	userID VARCHAR(250) PRIMARY KEY,
    addressID VARCHAR(36) DEFAULT NULL,
    groupID VARCHAR(250) DEFAULT NULL,
    tokenID VARCHAR(250) DEFAULT NULL,
    nameUser VARCHAR(50) NOT NULL,
    passwordUser VARCHAR(255) NOT NULL,
    FOREIGN KEY (addressID) REFERENCES AddressModel(uuid) ON DELETE CASCADE,
    FOREIGN KEY (groupID) REFERENCES groupModel(groupID) ON DELETE CASCADE,
    FOREIGN KEY (tokenID) REFERENCES tokenModel(tokenID) ON DELETE CASCADE,
    creationDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    lastConnection DATETIME,
    statusUser BOOL NOT NULL
);



INSERT INTO tokenModel (tokenID, creationDate, expirationDate, statusToken) VALUES
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

INSERT INTO AddressModel (uuid, unit_number, street_name, postal_code, city, country, additional_information, creation_date, update_date, deleted) VALUES
('21f677b9-208f-4267-aea6-fd8a0a1a130b', 484, 'Main Street', '10001', 'Berlin', 'Canada', 'Behind the mall', '2022-09-08', '2023-12-05', 1),
('98a93e53-af4d-4e73-b134-918d84b105f6', 174, 'Main Street', '10115', 'New York', 'Germany', 'Behind the mall', '2021-06-01', '2021-07-28', 0),
('c9da74b4-866e-4887-9ef5-328ab7eb0cdd', 920, 'Elm Street', '10001', 'Toronto', 'Japan', 'Next to the river', '2022-04-26', '2024-10-18', 1),
('30ed2873-6403-4a96-bf78-b871c819ae33', 747, 'Maple Avenue', 'M5H1T1', 'Tokyo', 'Germany', 'Next to the river', '2020-12-06', '2020-10-01', 0),
('d1a3cca1-2d64-4e2f-a42c-317d7e1fd639', 109, 'Sunset Boulevard', '10001', 'Paris', 'Japan', 'Next to the river', '2020-01-12', '2020-01-09', 0),
('0cfb4217-1c14-4c0c-807e-b2dfa5dfbbbb', 16, 'Elm Street', '100-0001', 'Berlin', 'Germany', 'Next to the river', '2020-11-07', '2022-10-24', 0),
('4054becb-441d-4d6a-b7d1-f9e9bca69f6e', 674, 'Broadway', '10115', 'Berlin', 'France', 'Close to the subway', '2024-11-12', '2021-09-22', 0),
('91ec6421-a949-4ab3-8067-3411822bec8b', 742, 'Sunset Boulevard', '10001', 'Toronto', 'USA', 'Behind the mall', '2023-02-25', '2021-04-25', 1),
('9a39e3bc-67c4-4c6a-b963-17999082327a', 253, 'Elm Street', 'M5H1T1', 'Paris', 'Canada', 'Near the park', '2020-09-17', '2024-04-23', 1),
('21ad8e6b-f146-43ea-a2bb-2d728adef000', 586, 'Broadway', '10001', 'Berlin', 'Canada', 'Near the park', '2021-03-11', '2022-01-20', 1);

INSERT INTO groupModel (groupID, descriptionGroup, ACCESS_LEVEL) 
VALUES 
    ('admin', 'Administrators with full access', '1'),
    ('CE', 'Comité d\'entreprise with limited management rights', '2'),
    ('member', 'Regular members with basic access rights', '3');


INSERT INTO userModel (userID, addressID, groupID, tokenID, nameUser, passwordUser, creationDate, lastConnection, statusUser) VALUES
('U1', '21f677b9-208f-4267-aea6-fd8a0a1a130b', 'admin', 'T1', 'JohnDoe', 'password123', '2025-01-01 10:00:00', '2025-01-05 12:00:00', TRUE),
('U2', '98a93e53-af4d-4e73-b134-918d84b105f6', 'CE', 'T2', 'JaneSmith', 'securePwd!', '2025-01-02 11:00:00', '2025-01-06 13:00:00', FALSE),
('U3', 'c9da74b4-866e-4887-9ef5-328ab7eb0cdd', 'member', 'T3', 'AliceBrown', 'mypassword', '2025-01-03 12:00:00', '2025-01-07 14:00:00', TRUE),
('U4', '30ed2873-6403-4a96-bf78-b871c819ae33', 'member', 'T4', 'BobJohnson', '12345secure', '2025-01-04 13:00:00', '2025-01-08 15:00:00', FALSE),
('U5', 'd1a3cca1-2d64-4e2f-a42c-317d7e1fd639', 'admin', 'T5', 'CharlieGreen', 'topSecret', '2025-01-05 14:00:00', '2025-01-09 16:00:00', TRUE),
('U6', '0cfb4217-1c14-4c0c-807e-b2dfa5dfbbbb', 'CE', 'T6', 'DianaWhite', 'password!', '2025-01-06 15:00:00', '2025-01-10 17:00:00', FALSE),
('U7', '4054becb-441d-4d6a-b7d1-f9e9bca69f6e', 'member',  'T7', 'EvanBlue', '123Secure!', '2025-01-07 16:00:00', '2025-01-11 18:00:00', TRUE),
('U8', '91ec6421-a949-4ab3-8067-3411822bec8b', 'CE','T8', 'FionaRed', 'mySuperPass', '2025-01-08 17:00:00', '2025-01-12 19:00:00', FALSE),
('U9', '9a39e3bc-67c4-4c6a-b963-17999082327a', 'member', 'T9', 'GeorgeBlack', 'password#1', '2025-01-09 18:00:00', '2025-01-13 20:00:00', TRUE),
('U10', '21ad8e6b-f146-43ea-a2bb-2d728adef000', 'member', 'T10', 'HannahYellow', 'Yellow@123', '2025-01-10 19:00:00', '2025-01-14 21:00:00', TRUE);