CREATE TABLE users (
  id                  INT           AUTO_INCREMENT PRIMARY KEY,
  first_name          VARCHAR(32)   NOT NULL,
  last_name           VARCHAR(32)   NOT NULL,
  email               VARCHAR(128)  NOT NULL UNIQUE,
  password            VARCHAR(255)  NOT NULL,
  role                ENUM('USER', 'ADMIN') DEFAULT 'USER',
  status              ENUM('PENDING', 'ACTIVE', 'BLOCKED') DEFAULT 'PENDING',
  failed_attempts     TINYINT       DEFAULT 0,
  locked_until        DATETIME      NULL,
  deletion_requested  TINYINT(1)    NOT NULL DEFAULT 0,
  created_at          TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
  last_login          DATETIME      NULL
);