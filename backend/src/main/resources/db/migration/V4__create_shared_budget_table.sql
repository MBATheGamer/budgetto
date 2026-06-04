
CREATE TABLE shared_budgets (
  id           INT            AUTO_INCREMENT PRIMARY KEY,
  name         VARCHAR(150)   NOT NULL,
  created_by   INT            NOT NULL,
  amount_limit DECIMAL(10,2)  NOT NULL,
  period_type  ENUM('MONTHLY','ONE_TIME') DEFAULT 'MONTHLY',
  start_date   DATE           NOT NULL,
  end_date     DATE           NULL,
  created_at   TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,

  FOREIGN KEY (created_by)
    REFERENCES users(id)
      ON DELETE CASCADE
);
