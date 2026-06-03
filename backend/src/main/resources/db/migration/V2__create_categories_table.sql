CREATE TABLE categories (
  id         INT          AUTO_INCREMENT PRIMARY KEY,
  user_id    INT          NULL COMMENT 'NULL = system default',
  name       VARCHAR(100) NOT NULL,
  icon       VARCHAR(20)  DEFAULT '📁',
  type       ENUM('EXPENSE','INCOME') NOT NULL,
  is_default BOOLEAN      DEFAULT FALSE,
  created_at TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,

  FOREIGN KEY (user_id)
    REFERENCES users(id)
      ON DELETE CASCADE
);