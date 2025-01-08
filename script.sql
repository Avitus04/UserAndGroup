CREATE DATABASE IF NOT EXISTS  projet;
USE projet;


-- Apartments & Availability

CREATE TABLE IF NOT EXISTS apartment(
    id VARCHAR(36) PRIMARY KEY,
    nb_piece INT NOT NULL,
    price INT NOT NULL,
    area INT NOT NULL,
    nb_people INT NOT NULL,
    adress VARCHAR(255),
    owner_id INT,
    availability_id INT,
    INDEX idx_owner_id (owner_id),
    INDEX idx_availability_id (availability_id)
);


CREATE TABLE IF NOT EXISTS owner(
    id INT AUTO_INCREMENT PRIMARY KEY,
    lastname VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL,
    contact VARCHAR(255),
    adress VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS apartmentavailability(
    id INT AUTO_INCREMENT PRIMARY KEY,
    availability ENUM('free','occupied','underConstruction'),
    week INT NOT NULL,
    year INT NOT NULL
);

INSERT INTO apartment (id, nb_piece, price, area, nb_people, adress, owner_id, availability_id) VALUES
('e6c8d15a-3cda-4ef3-99d4-bbb7e0e1e23f', 3, 1200, 75, 4, '123 Main Street', 1, 2),
('88a10707-3d37-4f2b-b57b-5d93db7d3073', 2, 900, 50, 2, '456 Elm Street', 2, 3),
('5f0be6be-56a1-4d96-8bbd-05354a9d84c2', 4, 1500, 90, 5, '789 Oak Avenue', 3, 1),
('dcd079ea-9a3e-41f5-9a8d-1200c24a0149', 1, 600, 30, 1, '321 Pine Road', 4, 4),
('0777b23b-2e2a-44fc-a953-dac8d06f400a', 5, 2000, 120, 6, '654 Maple Boulevard', 5, 5);

INSERT INTO owner (lastname, surname, contact, adress) VALUES
('Smith', 'John', 'smith@example.com', '123 Main Street'),
('Johnson', 'Emily', 'johnson@example.com', '456 Elm Street'),
('Williams', 'Michael', 'williams@example.com', '789 Oak Avenue'),
('Brown', 'Sarah', 'brown@example.com', '321 Pine Road'),
('Jones', 'Robert', 'jones@example.com', '654 Maple Boulevard');

INSERT INTO apartmentavailability (availability, week, year) VALUES
('free', 3, 2024),
('occupied', 19, 2024),
('underConstruction', 5, 2024),
('free', 12, 2024),
('occupied', 46, 2024);


-- Products & Availability
CREATE TABLE IF NOT EXISTS `category` (
  `id_category` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `description` varchar(250) DEFAULT NULL,
  `date_created` datetime DEFAULT NULL,
  `bDelete` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id_category`)
);

INSERT INTO `category` (`id_category`, `name`, `description`, `date_created`, `bDelete`) VALUES
	(1, 'Électronique', 'Produits électroniques tels que téléphones, ordina', '2024-01-02 10:30:00', 0),
	(2, 'Vêtements', 'Articles vestimentaires pour hommes, femmes et enf', '2024-01-05 12:15:00', 0),
	(3, 'Mobilier', 'Mobilier pour la maison et le bureau, y compris ch', '2024-01-10 08:45:00', 0),
	(4, 'Jouets', 'Jouets pour enfants, y compris jeux éducatifs et d', '2024-01-12 09:00:00', 0),
	(5, 'Cuisine', 'Ustensiles et appareils électroménagers pour la cu', '2024-01-15 11:20:00', 0),
	(6, 'Livres', 'Livres de fiction, manuels éducatifs, et ouvrages ', '2024-01-20 14:05:00', 0),
	(7, 'Audio', 'Équipements audio tels que casques, enceintes et m', '2024-02-01 16:50:00', 0),
	(8, 'Jardinage', 'Outils et accessoires pour entretenir les jardins ', '2024-02-05 10:25:00', 0),
	(9, 'Sport', 'Équipements et vêtements pour diverses activités s', '2024-02-10 15:40:00', 0),
	(10, 'Beauté', 'Produits cosmétiques, soins corporels et accessoir', '2024-02-12 13:10:00', 0);

CREATE TABLE IF NOT EXISTS `product` (
  `id_product` varchar(16) NOT NULL DEFAULT 'AUTO_INCREMENT',
  `name` varchar(50) DEFAULT NULL,
  `unit_price` float DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `id_subcategory` int(11) DEFAULT NULL,
  `stock` int(11) DEFAULT NULL,
  `id_provider` varchar(30) DEFAULT NULL,
  `date_updated` datetime DEFAULT NULL,
  `date_created` datetime DEFAULT NULL,
  `bDelete` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id_product`)
);

INSERT INTO `product` (`id_product`, `name`, `unit_price`, `status`, `id_subcategory`, `stock`, `id_provider`, `date_updated`, `date_created`, `bDelete`) VALUES
	('P001', 'iPhone 14', 999.99, 'Disponible', 1, 50, 'PR001', '2024-01-15 14:30:00', '2024-01-01 10:00:00', 0),
	('P002', 'MacBook Pro 16"', 2499.99, 'Disponible', 2, 20, 'PR002', '2024-01-16 11:20:00', '2024-01-02 11:00:00', 0),
	('P003', 'Samsung Galaxy S23', 799.99, 'Disponible', 1, 35, 'PR003', '2024-01-20 13:45:00', '2024-01-05 09:30:00', 0),
	('P004', 'Casque Sony WH-1000XM5', 399.99, 'Disponible', 1, 40, 'PR004', '2024-01-22 15:00:00', '2024-01-10 10:15:00', 0),
	('P005', 'Chaise ergonomique', 199.99, 'Disponible', 7, 60, 'PR005', '2024-01-25 10:45:00', '2024-01-12 14:00:00', 0),
	('P006', 'Livre "1984" de George Orwell', 14.99, 'Disponible', 6, 100, 'PR006', '2024-02-01 12:30:00', '2024-01-15 16:30:00', 0),
	('P007', 'Jeu Monopoly', 29.99, 'Disponible', 8, 75, 'PR007', '2024-02-05 14:50:00', '2024-01-20 09:15:00', 0),
	('P008', 'Mixeur Philips', 49.99, 'En rupture', 5, 0, 'PR008', '2024-02-10 11:10:00', '2024-01-22 13:40:00', 0),
	('P009', 'Haut-parleurs JBL Charge 5', 149.99, 'Disponible', 10, 30, 'PR009', '2024-02-12 17:25:00', '2024-01-25 08:00:00', 0),
	('P010', 'Tondeuse électrique Bosch', 299.99, 'Disponible', 9, 15, 'PR010', '2024-02-15 19:00:00', '2024-01-30 12:00:00', 0);


CREATE TABLE IF NOT EXISTS `subcategory` (
  `id_subcategory` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `active` tinyint(4) DEFAULT NULL,
  `id_category` int(11) NOT NULL DEFAULT 0,
  `date_created` datetime DEFAULT NULL,
  PRIMARY KEY (`id_subcategory`)
);

INSERT INTO `subcategory` (`id_subcategory`, `name`, `active`, `id_category`, `date_created`) VALUES
	(1, 'Smartphones', 1, 1, '2024-01-02 10:45:00'),
	(2, 'Ordinateurs portables', 1, 1, '2024-01-05 11:30:00'),
	(3, 'Téléviseurs', 1, 1, '2024-01-10 14:20:00'),
	(4, 'Vêtements décontractés', 1, 2, '2024-01-12 09:15:00'),
	(5, 'Vêtements de sport', 1, 2, '2024-01-15 13:45:00'),
	(6, 'Canapés', 1, 3, '2024-01-20 10:00:00'),
	(7, 'Chaises de bureau', 1, 3, '2024-01-25 14:30:00'),
	(8, 'Jeux de société', 1, 4, '2024-02-01 16:10:00'),
	(9, 'Ustensiles de cuisine', 1, 5, '2024-02-10 15:20:00'),
	(10, 'Outils de jardin', 1, 8, '2024-02-15 17:40:00');


-- Contacts & Adresses
CREATE TABLE IF NOT EXISTS `contact_model` (
  `uuid` VARCHAR(1024),
  `id_address` VARCHAR(1024),
  `name` VARCHAR(1024),
  `first_name` VARCHAR(1024),
  `email` VARCHAR(1024),
  `personal_phone` BIGINT,
  `function` VARCHAR(1024),
  `work_phone` BIGINT
);

INSERT INTO `contact_model` (`uuid`,`id_address`,`name`,`first_name`,`email`,`personal_phone`,`function`,`work_phone`)
VALUES
('a8dff5c7-8d88-4fcb-jklo-e4a09de05e36','a8dff5c7-8d88-4fcb-8d24-e4a09de05e36','Dupont','Jean','jean.dupont@example.com',601020304,'Développeur',140234567),
('ecb9d879-9a9b-4d85-jklo-5867e827efb9','ecb9d879-9a9b-4d85-b0b2-5867e827efb9','Martin','Sophie','sophie.martin@example.com',612345678,'Responsable RH',148765432),
('c3f207fa-6327-4d62-jklo-7ac621af68f9','c3f207fa-6327-4d62-9d56-7ac621af68f9','Lemoine','Pierre','pierre.lemoine@example.com',634567890,'Chef de projet',132456789),
('b4c8a9c2-e524-4f59-jklo-054b73057465','b4c8a9c2-e524-4f59-9f91-054b73057465','Leblanc','Claire','claire.leblanc@example.com',622334455,'Marketing Manager',145667788),
('a1b08f23-84b9-4eac-jklo-97c7cd1fd0c1','a1b08f23-84b9-4eac-9483-97c7cd1fd0c1','Pires','Lucas','lucas.pires@example.com',701020304,'Développeur Backend',150234567),
('b4f9a7da-0b09-4e42-jklo-06be4327f1e4','b4f9a7da-0b09-4e42-81c4-06be4327f1e4','Tremblay','Alice','alice.tremblay@example.com',712345678,'Chef de produit',178765432),
('c8bb4f84-d394-40c9-jklo-9b292f27f4ff','c8bb4f84-d394-40c9-b215-9b292f27f4ff','Fournier','Éric','eric.fournier@example.com',722334455,'Directeur technique',132345678);

CREATE TABLE IF NOT EXISTS `address_model` (
  `uuid` VARCHAR(1024),
  `number` BIGINT,
  `street` VARCHAR(1024),
  `postal_code` BIGINT,
  `city` VARCHAR(1024),
  `country` VARCHAR(1024),
  `address_complement` VARCHAR(1024),
  `additional_information` VARCHAR(1024)
);

INSERT INTO `address_model` (`uuid`,`number`,`street`,`postal_code`,`city`,`country`,`address_complement`,`additional_information`)
VALUES
('a8dff5c7-8d88-4fcb-8d24-e4a09de05e36',10,'Rue des Acacias',75001,'Paris','France','Appartement 12','Résidence sécurisée'),
('ecb9d879-9a9b-4d85-b0b2-5867e827efb9',25,'Avenue de la République',69001,'Lyon','France','Bâtiment B','Près du parc'),
('c3f207fa-6327-4d62-9d56-7ac621af68f9',45,'Boulevard de la Liberté',13001,'Marseille','France','Escalier A','Immeuble ancien'),
('b4c8a9c2-e524-4f59-9f91-054b73057465',3,'Place des Vosges',75003,'Paris','France','Rez-de-chaussée','Boutique en façade'),
('a1b08f23-84b9-4eac-9483-97c7cd1fd0c1',15,'Avenue des Champs-Élysées',75008,'Paris','France','Appartement 5','Immeuble moderne'),
('b4f9a7da-0b09-4e42-81c4-06be4327f1e4',100,'Rue de la Paix',69002,'Lyon','France','Bâtiment A','Près du centre commercial'),
('c8bb4f84-d394-40c9-b215-9b292f27f4ff',75,'Boulevard Haussmann',75009,'Paris','France','3ème étage','Immeuble bien situé');


-- User & Group
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


-- CRM
CREATE TABLE IF NOT EXISTS `tickets` (
  `uuid_ticket` varchar(255) NOT NULL,
  `uuid_client` varchar(255) NOT NULL,
  `title` varchar(255) NOT NULL,
  `description` text DEFAULT NULL,
  `date_created` datetime NOT NULL,
  `date_update` datetime DEFAULT NULL,
  `status` enum('UNSTARTED','IN_PROGRESS','ENDED','CANCELLED') NOT NULL,
  `priority` enum('LOW','NORMAL','HIGH') NOT NULL,
  `request_type` enum('SUPPORT','FEATURE_REQUEST','BUG_REPORT') NOT NULL,
  `source` enum('EMAIL','PHONE','VISIT','WEB_FORM') NOT NULL,
  `comments` varchar(255) NOT NULL
);

INSERT INTO `tickets` (`uuid_ticket`, `uuid_client`, `title`, `description`, `date_created`, `date_update`, `status`, `priority`, `request_type`, `source`, `comments`) VALUES
('0f8289bc-b47f-45ba-90cc-ea65c88ea537', 'adff7b9d-b45f-450d-9245-e70c943d238b', 'Feature Request: Multi-Language Support', 'Add multi-language support to the platform for better accessibility across regions.', '2025-01-07 12:00:00', '2025-01-07 12:10:00', 'UNSTARTED', 'NORMAL', 'FEATURE_REQUEST', 'WEB_FORM', 'Clients from different regions requesting language support.'),
('6c6d5e7e-40a9-456d-b8a1-bd6b11525d32', 'a132d27e-0df9-4268-9cb6-c798df55f9c4', 'Bug Report: Form Submission Error', 'There is a bug when submitting forms on the website, it throws a 500 error.', '2025-01-07 11:00:00', '2025-01-07 11:05:00', 'IN_PROGRESS', 'NORMAL', 'BUG_REPORT', 'WEB_FORM', 'Issue reported after form submission, blocking users from completing their actions.'),
('7da01685-c09b-4ec5-a37e-07b773748f19', 'd02c10fd-3cd1-4d93-bef1-520f14a983ac', 'Bug Report: Login Failure', 'Unable to log in to the platform after resetting the password, it still says invalid credentials.', '2025-01-07 12:30:00', '2025-01-07 12:40:00', 'IN_PROGRESS', 'HIGH', 'BUG_REPORT', 'PHONE', 'Critical issue, preventing users from accessing their accounts.'),
('a7f0b0d0-b16f-40e5-888f-bfbe52ff6298', '4fd627aa-07f9-48c2-a477-d56b2513b8bb', 'Technical Support Request', 'I am unable to connect to the internet through the company network.', '2025-01-07 08:15:00', '2025-01-07 08:20:00', 'IN_PROGRESS', 'HIGH', 'SUPPORT', 'PHONE', 'Urgent issue due to network downtime.'),
('b6ad6b56-6747-4f6f-8cc4-1b80c77c8391', '7f4f0eac-d8d1-47ef-9bda-531052303d39', 'Bug Report: Login Page Issue', 'There seems to be an issue with logging in via the mobile app, it crashes the app every time.', '2025-01-07 09:30:00', '2025-01-07 09:45:00', 'IN_PROGRESS', 'LOW', 'BUG_REPORT', 'PHONE', 'User experiencing crashes during login on mobile app.'),
('cdb95ab1-6125-4a0a-8b1e-9ea5575c76f5', 'e5877f57-2be0-4e5d-bc30-fb7422c3c6ac', 'Support: Account Verification Issue', 'I am unable to verify my account using the provided code.', '2025-01-07 11:30:00', '2025-01-07 11:40:00', 'UNSTARTED', 'LOW', 'SUPPORT', 'PHONE', 'Customer unable to verify account due to incorrect code.'),
('e2f25d5b-d5a9-4664-9789-b2d383fc9d72', '2f5f10be-3777-44f5-91c3-e1d6720a59bc', 'Feature Request for New Dashboard', 'Please add a customizable dashboard for users to manage their tasks more efficiently.', '2025-01-07 09:00:00', '2025-01-07 09:15:00', 'UNSTARTED', 'NORMAL', 'FEATURE_REQUEST', 'WEB_FORM', 'Client is requesting a feature to improve workflow.'),
('e3a2a9d3-b081-4714-b287-d3545b059697', 'f38c40f8-1d47-4c3b-b6d5-37d7d82d4163', 'Feature Request: Custom Reports', 'A request for a custom reporting feature that allows users to generate reports based on various criteria.', '2025-01-07 10:15:00', '2025-01-07 10:30:00', 'IN_PROGRESS', 'HIGH', 'FEATURE_REQUEST', 'WEB_FORM', 'High demand from users for advanced reporting functionality.'),
('fbc2de19-73b5-44f2-8ad7-7f42453eaee8', 'd2dced8d-5f56-4e5d-bd8f-209a61ac0100', 'Support Request: Password Reset', 'I forgot my password and need to reset it to access my account.', '2025-01-07 10:00:00', '2025-01-07 10:05:00', 'UNSTARTED', 'NORMAL', 'SUPPORT', 'EMAIL', 'Client is unable to access their account due to a forgotten password.');

CREATE TABLE IF NOT EXISTS `ticket_actions` (
  `uuid_action` varchar(255) NOT NULL,
  `ticket` varchar(255) NOT NULL,
  `date_action` datetime DEFAULT NULL,
  `action_type` enum('CREATION','UPDATE','DELETION') NOT NULL,
  `description` text DEFAULT NULL,
  `uuid_user` varchar(255) NOT NULL
);

INSERT INTO `ticket_actions` (`uuid_action`, `ticket`, `date_action`, `action_type`, `description`, `uuid_user`) VALUES
('14cdda53-c4de-4b14-8e52-f346afc9d9ea', 'b6ad6b56-6747-4f6f-8cc4-1b80c77c8391', '2025-01-07 10:00:00', 'DELETION', 'Ticket deleted due to duplicate entry.', 'user-003'),
('30de9c7d-9dfd-4f8b-bc03-0dc8b6e8e2b5', 'e3a2a9d3-b081-4714-b287-d3545b059697', '2025-01-07 11:45:00', 'CREATION', 'Created feature request for advanced reporting functionality.', 'user-005'),
('7ab907d6-f38b-45f4-8c12-0736b0c4b1fc', 'e2f25d5b-d5a9-4664-9789-b2d383fc9d72', '2025-01-07 11:15:00', 'UPDATE', 'Added a comment: Client requested faster response time.', 'user-004'),
('8de56f1e-b0e6-4a32-9b7a-c79d1f42c5e4', 'cdb95ab1-6125-4a0a-8b1e-9ea5575c76f5', '2025-01-07 13:00:00', 'DELETION', 'Deleted ticket due to customer request.', 'user-008'),
('ac8d3f1a-23e4-4829-99b4-650db5db4cb1', 'a7f0b0d0-b16f-40e5-888f-bfbe52ff6298', '2025-01-07 08:15:00', 'CREATION', 'Ticket created for network issue resolution.', 'user-001'),
('d6c43b1c-9e2a-4e62-859f-fd4b45c7d8f9', '0f8289bc-b47f-45ba-90cc-ea65c88ea537', '2025-01-07 14:00:00', 'UPDATE', 'Status changed from UNSTARTED to IN_PROGRESS.', 'user-009'),
('e4d6f92c-98c3-4dfb-85b3-7c83b2e7c85d', '7da01685-c09b-4ec5-a37e-07b773748f19', '2025-01-07 12:30:00', 'CREATION', 'Bug reported: Unable to log in with reset password.', 'user-007'),
('e9d6f7c1-8835-4411-8b6d-7c435a7c7913', 'a7f0b0d0-b16f-40e5-888f-bfbe52ff6298', '2025-01-07 09:00:00', 'UPDATE', 'Priority updated to HIGH due to user escalation.', 'user-002'),
('f5d45b1b-e237-49c4-8db4-17c5f70dc2fa', 'b6ad6b56-6747-4f6f-8cc4-1b80c77c8391', '2025-01-07 12:00:00', 'UPDATE', 'Corrected description: Removed duplicate information.', 'user-006'),
('f9d84e6d-8b1a-4cfc-91c3-d82f9b3c7e54', 'fbc2de19-73b5-44f2-8ad7-7f42453eaee8', '2025-01-07 14:30:00', 'CREATION', 'Created support ticket for password reset.', 'user-010');

CREATE TABLE IF NOT EXISTS `ticket_tasks` (
  `uuid_task` varchar(255) NOT NULL,
  `ticket` varchar(255) NOT NULL,
  `date_reminder` datetime DEFAULT NULL,
  `description` text DEFAULT NULL,
  `uuid_user` varchar(255) NOT NULL
);

INSERT INTO `ticket_tasks` (`uuid_task`, `ticket`, `date_reminder`, `description`, `uuid_user`) VALUES
('task-001', 'ticket-001', '2025-01-08 10:00:00', 'Check the status of the ticket and update the client.', 'user-001'),
('task-002', 'ticket-002', '2025-01-08 14:00:00', 'Call the client to gather additional information on the issue.', 'user-002'),
('task-003', 'ticket-003', '2025-01-09 09:30:00', 'Test the fix applied to the reported bug.', 'user-003'),
('task-004', 'ticket-004', '2025-01-09 11:00:00', 'Schedule a team meeting to discuss the feature request.', 'user-004'),
('task-005', 'ticket-005', '2025-01-09 16:00:00', 'Document the resolution steps taken for future reference.', 'user-005'),
('task-006', 'ticket-006', '2025-01-10 10:00:00', 'Send a follow-up email to the client for feedback on the resolved ticket.', 'user-006'),
('task-007', 'ticket-007', '2025-01-10 15:00:00', 'Check the server logs for additional details related to the reported issue.', 'user-007'),
('task-008', 'ticket-008', '2025-01-11 08:30:00', 'Reassign the ticket to a more experienced support agent.', 'user-008'),
('task-009', 'ticket-009', '2025-01-11 13:00:00', 'Prepare a root cause analysis report for the incident.', 'user-009'),
('task-010', 'ticket-010', '2025-01-11 17:00:00', 'Update the client on the progress of their feature request.', 'user-010');


ALTER TABLE `tickets`
  ADD PRIMARY KEY (`uuid_ticket`);
  
ALTER TABLE `ticket_actions`
  ADD PRIMARY KEY (`uuid_action`);

ALTER TABLE `ticket_tasks`
  ADD PRIMARY KEY (`uuid_task`);
COMMIT;


-- Order & Transaction
CREATE TABLE IF NOT EXISTS `order` (
  `id_order` int NOT NULL AUTO_INCREMENT,
  `uuid_order` varchar(255) DEFAULT NULL,
  `date_created` datetime DEFAULT NULL,
  `date_update` datetime DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `total_amount` float DEFAULT NULL,
  `id_item` int DEFAULT NULL,
  `uuid_item` varchar(255) DEFAULT NULL,
  `item_type` varchar(50) DEFAULT NULL,
  `id_user` int DEFAULT NULL,
  `uuid_user` varchar(255) DEFAULT NULL,
  `description` text,
  PRIMARY KEY (`id_order`)
);

INSERT INTO `order` VALUES (1,'ef4f7b2e-cce3-11ef-b916-b48c9d6df3cd','2025-01-01 00:00:00','2025-01-02 00:00:00','PENDING',100.5,1,'4b357396-cce4-11ef-b916-b48c9d6df3cd','PRODUCT',1,'45e2ef5e-cce4-11ef-b916-b48c9d6df3cd','First order description'),(2,'ef4f8184-cce3-11ef-b916-b48c9d6df3cd','2025-01-03 00:00:00','2025-01-04 00:00:00','FINISHED',250,2,'4b3579db-cce4-11ef-b916-b48c9d6df3cd','APPARTMENT',2,'45e2f5ae-cce4-11ef-b916-b48c9d6df3cd','Second order description'),(3,'ef4f8326-cce3-11ef-b916-b48c9d6df3cd','2025-01-05 00:00:00','2025-01-06 00:00:00','CANCELLED',75.75,3,'4b357b93-cce4-11ef-b916-b48c9d6df3cd','PRODUCT',3,'45e2f864-cce4-11ef-b916-b48c9d6df3cd','Third order description');

CREATE TABLE IF NOT EXISTS `transaction` (
  `id_transaction` int NOT NULL AUTO_INCREMENT,
  `uuid_transaction` varchar(255) DEFAULT NULL,
  `amount` float DEFAULT NULL,
  `payment_method` varchar(50) DEFAULT NULL,
  `id_order` int DEFAULT NULL,
  `transaction_status` varchar(50) DEFAULT NULL,
  `date_created` datetime DEFAULT NULL,
  `date_update` datetime DEFAULT NULL,
  `description` text,
  PRIMARY KEY (`id_transaction`)
);

INSERT INTO `transaction` VALUES (1,'f1714d93-cce3-11ef-b916-b48c9d6df3cd',100.5,'CARD',1,'SUCCESS','2025-01-01 00:00:00','2025-01-02 00:00:00','Transaction for the first order'),(2,'f17152ea-cce3-11ef-b916-b48c9d6df3cd',250,'PAYPAL',2,'FAILED','2025-01-03 00:00:00','2025-01-04 00:00:00','Transaction for the second order'),(3,'f1715454-cce3-11ef-b916-b48c9d6df3cd',75.75,'CASH',3,'PENDING','2025-01-05 00:00:00','2025-01-06 00:00:00','Transaction for the third order');


-- Experience Management
drop user if exists admin@localhost;
flush privileges;

-- Créer un utilisateur avec un mot de passe
CREATE USER 'admin'@'localhost' IDENTIFIED BY '1234';
-- Accorder tous les privilèges à cet utilisateur sur la base de données
GRANT ALL PRIVILEGES ON projet.* TO 'admin'@'localhost';

-- Appliquer les changements
FLUSH PRIVILEGES;

-- Table pour les catégories de feedback
CREATE TABLE FEEDBACKCATEGORY (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

-- Table pour les catégories de mémoire
CREATE TABLE MEMORYCATEGORY (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

-- Table pour les types de partage
CREATE TABLE SHARE (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

-- Table pour les retours utilisateurs (FeedbackModel)
CREATE TABLE FeedbackModel (
    idFeedback VARCHAR(36) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    dateCreated DATE NOT NULL,
    review TEXT,
    image VARCHAR(255),
    category INT,
    views INT DEFAULT 0,
    dateUpdated DATE,
    idUser VARCHAR(36) NOT NULL,
    idOrder VARCHAR(36),
    bDelete BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (category) REFERENCES FEEDBACKCATEGORY(id)
);

-- Table pour les mémoires (MemoryModel)
CREATE TABLE MemoryModel (
    idMemory VARCHAR(36) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    publicationDate DATE NOT NULL,
    category INT,
    views INT DEFAULT 0,
    image VARCHAR(255),
    description TEXT,
    place VARCHAR(255),
    hashtag VARCHAR(255),
    share INT,
    tag VARCHAR(255),
    idOrder VARCHAR(36),
    idUser VARCHAR(36) NOT NULL,
    bDelete BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (category) REFERENCES MEMORYCATEGORY(id),
    FOREIGN KEY (share) REFERENCES SHARE(id)
);


-- Ajout de catégories de feedback
INSERT INTO FEEDBACKCATEGORY (name) VALUES
('Product Feedback'),
('Service Feedback'),
('Website Feedback'),
('Other');

-- Ajout de catégories de mémoire
INSERT INTO MEMORYCATEGORY (name) VALUES
('Personal'),
('Work'),
('Travel'),
('Event');

-- Ajout de types de partage
INSERT INTO SHARE (name) VALUES
('Public'),
('Private'),
('Friends Only');

-- Ajout d'exemples de retours utilisateurs (FeedbackModel)
INSERT INTO FeedbackModel (idFeedback, title, dateCreated, review, image, category, views, dateUpdated, idUser, idOrder, bDelete) VALUES
(UUID(), 'Great Service', '2025-01-01', 'The service was excellent.', 'image1.jpg', 1, 100, '2025-01-02', 'user1', 'order1', FALSE),
(UUID(), 'Website Issue', '2025-01-03', 'The website is slow.', 'image2.jpg', 3, 50, '2025-01-04', 'user2', 'order2', FALSE),
(UUID(), 'Product Feedback', '2025-01-05', 'The product quality is great.', 'image3.jpg', 1, 30, '2025-01-06', 'user3', 'order3', FALSE),
(UUID(), 'Delivery Delay', '2025-01-07', 'Delivery took longer than expected.', 'image4.jpg', 2, 80, '2025-01-08', 'user4', 'order4', FALSE),
(UUID(), 'Amazing Support', '2025-01-09', 'Customer support was very helpful.', 'image5.jpg', 1, 120, '2025-01-10', 'user5', 'order5', FALSE),
(UUID(), 'Poor Packaging', '2025-01-11', 'The packaging was damaged.', 'image6.jpg', 2, 40, '2025-01-12', 'user6', 'order6', FALSE),
(UUID(), 'Excellent App', '2025-01-13', 'The app is user-friendly and intuitive.', 'image7.jpg', 3, 200, '2025-01-14', 'user7', 'order7', FALSE),
(UUID(), 'Refund Issue', '2025-01-15', 'Refund process was complicated.', 'image8.jpg', 4, 30, '2025-01-16', 'user8', 'order8', FALSE),
(UUID(), 'Quick Delivery', '2025-01-17', 'The order was delivered quickly.', 'image9.jpg', 1, 150, '2025-01-18', 'user9', 'order9', FALSE),
(UUID(), 'Misleading Product', '2025-01-19', 'The product was not as described.', 'image10.jpg', 2, 25, '2025-01-20', 'user10', 'order10', FALSE);

-- Ajout d'exemples de mémoires (MemoryModel)
INSERT INTO MemoryModel (idMemory, title, publicationDate, category, views, image, description, place, hashtag, share, tag, idOrder, idUser, bDelete) VALUES
(UUID(), 'Vacation in Paris', '2024-12-25', 3, 200, 'paris.jpg', 'Visited the Eiffel Tower and Louvre.', 'Paris', '#travel', 1, 'vacation,paris', 'order4', 'user1', FALSE),
(UUID(), 'Office Party', '2024-12-20', 2, 150, 'office.jpg', 'End-of-year celebration at the office.', 'Office', '#work', 2, 'party,office', 'order5', 'user2', FALSE),
(UUID(), 'Wedding Event', '2024-12-15', 4, 300, 'wedding.jpg', 'My best friend’s wedding.', 'Venue XYZ', '#wedding', 1, 'event,wedding', 'order6', 'user3', FALSE),
(UUID(), 'Mountain Hiking', '2024-12-10', 3, 180, 'hiking.jpg', 'Reached the summit after a long hike.', 'Mount Everest', '#adventure', 1, 'hiking,mountain', 'order7', 'user4', FALSE),
(UUID(), 'Family Reunion', '2024-12-05', 1, 100, 'family.jpg', 'A wonderful time with family.', 'Home', '#family', 2, 'reunion,family', 'order8', 'user5', FALSE),
(UUID(), 'Beach Party', '2024-11-30', 3, 220, 'beach.jpg', 'Enjoyed the sunshine and waves.', 'Malibu', '#beach', 1, 'party,beach', 'order9', 'user6', FALSE),
(UUID(), 'Conference Talk', '2024-11-25', 2, 160, 'conference.jpg', 'Gave a talk on technology trends.', 'Convention Center', '#conference', 2, 'talk,conference', 'order10', 'user7', FALSE),
(UUID(), 'Birthday Celebration', '2024-11-20', 4, 140, 'birthday.jpg', 'Celebrated my 30th birthday.', 'Banquet Hall', '#birthday', 1, 'celebration,birthday', 'order11', 'user8', FALSE),
(UUID(), 'Camping Trip', '2024-11-15', 3, 190, 'camping.jpg', 'Stayed under the stars.', 'National Park', '#camping', 1, 'trip,camping', 'order12', 'user9', FALSE),
(UUID(), 'Art Exhibition', '2024-11-10', 4, 110, 'art.jpg', 'Visited an inspiring art exhibition.', 'City Gallery', '#art', 2, 'exhibition,art', 'order13', 'user10', FALSE);

-- Learning & Availability
CREATE TABLE location (
    locationId CHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    addressId VARCHAR(255),
    availability INTEGER
);

-- Table for formationModel
CREATE TABLE formation (
    formationId CHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    durationInHours INTEGER,
    startDate DATE,
    endDate DATE,
    locationId CHAR(36),
    state ENUM('Planned', 'InProgress', 'Cancelled', 'Completed') NOT NULL,
    FOREIGN KEY (locationId) REFERENCES location(locationId) ON DELETE CASCADE
);

-- Table for availabilityModel
CREATE TABLE formationavailability (
    availabilityId CHAR(36) PRIMARY KEY,
    formationId CHAR(36),
    year INTEGER NOT NULL,
    timeline VARCHAR(366) NOT NULL,
    FOREIGN KEY (formationId) REFERENCES formation(formationId) ON DELETE CASCADE,
    CONSTRAINT chk_timeline CHECK (timeline REGEXP '^[01]{365}$' OR timeline REGEXP '^[01]{366}$')

);

-- Enum for FORMATIONSTATE is embedded directly into the formation table as an ENUM type

-- Insert test data for locationModel
INSERT INTO location (locationId, name, addressId, availability) VALUES
('9f98e66a-0494-4c07-8cee-73c0122738a8', 'Location A', 'addr-001', 50),
('4bfcb9f9-9a93-4936-9862-67b400a66119', 'Location B', 'addr-002', 30);

-- Insert test data for formationModel
INSERT INTO formation (formationId, name, durationInHours, startDate, endDate, locationId, state) VALUES
('a09b751b-bd71-4d1a-ab36-9b1a62f79bcd', 'Formation 1', 40, '2025-01-01', '2025-01-05', '9f98e66a-0494-4c07-8cee-73c0122738a8', 'Planned'),
('88052d74-505a-4369-b696-1ff1acbe8da5', 'Formation 2', 20, '2025-02-01', '2025-02-03', '4bfcb9f9-9a93-4936-9862-67b400a66119', 'InProgress');

-- Insert test data for availabilityModel
INSERT INTO formationavailability (availabilityId, formationId, year, timeline) VALUES
('7611ece3-92d8-4f84-acd5-7cf479afd514', 'a09b751b-bd71-4d1a-ab36-9b1a62f79bcd', 2024, 
 '101100101011100101101111010100110010101001101001001001110100101011110101001001100001100001010011110001000001111110101111011110001010001011000111111101110110011101110010100111000110110010101000100001111011111001010000110001100010110111011111100111010001110101101011101100110101111001100010010110110010111111011110010001000001001011010010010100110111100011110010000110'),  -- 366 days for leap year
('20c92ebe-a7dc-4a2a-954a-e6712ad9f75b', '88052d74-505a-4369-b696-1ff1acbe8da5', 2025, 
 '00001110000011011110101100000000011011100110001000100001011000100100101011101010011011111101101010100100010001000110000010000010011110101100111001000011001101101000111110100001101010111001000111100010011100001011101100100100010101011101010010100011100000001001110101111111001110101000011011100100100011100111100111001000000100101011010101000110000100001111001110101');  -- 365 days for non-leap year


 -- Purchase & Provider
 CREATE TABLE IF NOT EXISTS `provider` (
  `id` varchar(16) NOT NULL,
  `name` varchar(255) NOT NULL,
  `service` varchar(255) NOT NULL,
  `siren` varchar(9) NOT NULL,
  `status` varchar(50) NOT NULL,
  `id_contact` varchar(16) NOT NULL,
  `registration_date` date NOT NULL,
  `region` varchar(100) NOT NULL,
  `legal_informations` text NOT NULL,
  `category` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
);

INSERT INTO `provider` VALUES ('1a7e9b4f3c2d5c8e','Gonzalez','embrace bleeding-edge mindshare','469212479','Inactive','343','2024-06-25','Bretagne','L\'assurance d\'avancer de mani�re efficace','Retail'),('2d4f9a8e7c1b3d6f','Roux','grow web-enabled mindshare','524027091','Active','111','2024-03-03','Bretagne','L\'avantage d\'�voluer sans soucis','Healthcare'),('3e5d9c7a4f1b2c6d','Dufour','deploy seamless users','665149097','Suspended','220','2023-04-05','Occitanie','La libert� d\'avancer plus rapidement','IT Services'),('4a5c9b8d3e2f1d4c','Grenier','cultivate end-to-end infrastructures','984179959','Inactive','129','2015-04-24','Occitanie','Le confort d\'�voluer plus rapidement','Agriculture'),('6d8c7e1b2f9a3d4f','Schneider SARL','benchmark turn-key infrastructures','522132365','Suspended','405','2019-07-12','Nouvelle-Aquitaine','Le pouvoir de rouler avant-tout','IT Services'),('7c1d5f3b4e2a9d8f','Duhamel','innovate front-end web services','779346520','Active','79','2018-06-05','Bourgogne-Franche-Comt�','Le droit de rouler autrement','Education'),('7d3f5e9b4c1d3a8f','Olivier Normand et Fils','reinvent holistic technologies','896059353','Pending','7','2017-01-22','Grand Est','L\'avantage d\'atteindre vos buts de mani�re efficace','Healthcare'),('9b4c3d5e6f7a8c1d','Lebon','strategize vertical e-business','981208650','Active','460','2024-07-05','Normandie','L\'assurance de changer � la pointe','IT Services'),('b2f4e7d3c1a8b9c5','Guilbert','matrix granular applications','476309348','Pending','322','2024-06-01','Bourgogne-Franche-Comt�','Le plaisir de changer sans soucis','Education'),('f91a3e4b2d7f9c7b','Courtois et Fils','scale revolutionary infrastructures','309218916','Suspended','337','2017-10-04','Bretagne','Le plaisir d\'�voluer de mani�re s�re','Construction');

CREATE TABLE `purchase` (
  `id` varchar(16) NOT NULL,
  `buyDate` date NOT NULL,
  `price` float NOT NULL,
  `quantity` int NOT NULL,
  `state` enum('pending','cancelled','delivered','in_delivery','confirmed') NOT NULL,
  `idProduct` varchar(36) NOT NULL,
  PRIMARY KEY (`id`)
);

INSERT INTO `purchase` VALUES ('0ee4800f-e69f-4','2024-05-29',92.6,17,'pending','77cf4a22-2de7-49d5-9f70-a539994ead33'),('1947c848-8122-4','2024-02-17',427.25,41,'confirmed','8a3ffa99-f53a-4778-91e2-feedc1b40416'),('1fd50eb8-b8c4-4','2024-10-14',78.21,30,'delivered','b147be12-c903-45f5-be5f-2406dfa670b6'),('23838f15-1676-4e','2024-06-21',436.51,37,'in_delivery','dfae690f-1e50-4763-9225-a2634454b216'),('38bf3233-9028-4','2024-02-20',433.44,4,'cancelled','0b188c3e-c9d2-464b-9efa-c00c59073faf'),('3ab47ad6-044d-4','2024-10-26',57.46,14,'pending','53f83c9c-40df-4f71-97a4-563204a2ff01'),('650599a6-8b5e-4','2024-01-19',32.68,48,'in_delivery','69d47814-9742-4bae-b3da-8515b4d2e5f4'),('7a538497-81df-42','2024-06-02',147.78,11,'confirmed','58d6cd08-c674-45c3-a3b0-1f96398f43fa'),('bd7a9f8c-9a4c-4','2024-06-11',246.57,30,'pending','36f88fb6-275e-45eb-a1b8-ef0f4adcf7c6'),('ea2ad64f-8f2e-4','2024-06-20',365.07,48,'confirmed','abb64834-2cfd-4080-9b45-c22b8adb6ab8');

