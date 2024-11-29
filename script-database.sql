CREATE TABLE role (
    role_id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,
    PRIMARY KEY (role_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;


CREATE SCHEMA `security_db`;

use `security_db`;
-- Insert default roles
INSERT INTO role (name) VALUES ('ROLE_REALTOR');
INSERT INTO role (name) VALUES ('ROLE_ADMIN');