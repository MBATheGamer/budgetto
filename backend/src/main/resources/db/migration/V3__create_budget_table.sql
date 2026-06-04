CREATE TABLE budgets (
    id              INT           AUTO_INCREMENT PRIMARY KEY,
    user_id         INT           NOT NULL,
    category_id     INT           NOT NULL,
    amount_limit    DECIMAL(10,2) NOT NULL,
    period          ENUM('MONTHLY','WEEKLY','CUSTOM') DEFAULT 'MONTHLY',
    start_date      DATE          NOT NULL,
    end_date        DATE          NULL,
    alert_threshold TINYINT       DEFAULT 80 COMMENT '0-100 %',
    created_at      TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id)
      REFERENCES users(id)
        ON DELETE CASCADE,

    FOREIGN KEY (category_id)
      REFERENCES categories(id)
        ON DELETE CASCADE
);
