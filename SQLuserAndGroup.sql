CREATE DATABASE IF NOT EXISTS projet;
USE projet;
 
 
drop user if exists admin@localhost;
flush privileges;
 
-- Créer un utilisateur avec un mot de passe
CREATE USER 'admin'@'localhost' IDENTIFIED BY '1234';
 
-- Accorder tous les privilèges à cet utilisateur sur la base de données
GRANT ALL PRIVILEGES ON projet.* TO 'admin'@'localhost';
 
-- Appliquer les changements
FLUSH PRIVILEGES;

CREATE TABLE IF NOT EXISTS user_profile (
	profile_id VARCHAR(16) PRIMARY KEY,
	`description` VARCHAR(500),
	access_level TINYINT DEFAULT '3'
);

CREATE TABLE IF NOT EXISTS token (
    token_id VARCHAR(64) NOT NULL PRIMARY KEY,
    date_created DATETIME DEFAULT CURRENT_TIMESTAMP,
    date_expiration DATETIME NOT NULL,
    status_token BOOL NOT NULL
);
 
 CREATE TABLE IF NOT EXISTS membership (
	uuid_user VARCHAR(36) PRIMARY KEY,
    uuid_address VARCHAR(36) DEFAULT NULL,
    profile_id VARCHAR(16) DEFAULT NULL,
    token_id VARCHAR(64) DEFAULT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    passwd VARCHAR(255) NOT NULL,
    date_created DATETIME DEFAULT CURRENT_TIMESTAMP,
    date_last_connection DATETIME,
    status_user BOOL NOT NULL
);

INSERT INTO user_profile (profile_id, `description`, access_level) 
VALUES 
    ('admin', 'Administrators with full access', '1'),
    ('CE', 'Comité d\'entreprise with limited management rights', '2'),
    ('member', 'Regular members with basic access rights', '3');
