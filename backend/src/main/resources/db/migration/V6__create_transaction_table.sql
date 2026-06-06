CREATE TABLE transactions (
    id               INT            AUTO_INCREMENT PRIMARY KEY,
    user_id          INT            NOT NULL,
    category_id      INT            NULL,
    shared_budget_id INT            NULL,
    type             ENUM('EXPENSE','INCOME') NOT NULL,
    description      VARCHAR(255)   NOT NULL,
    amount           DECIMAL(10,2)  NOT NULL,
    transaction_date DATE           NOT NULL,
    comment          TEXT           NULL,
    created_at       TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id)
      REFERENCES users(id)
        ON DELETE CASCADE,

    FOREIGN KEY (category_id)
      REFERENCES categories(id)
        ON DELETE SET NULL,

    FOREIGN KEY (shared_budget_id)
      REFERENCES shared_budgets(id)
        ON DELETE SET NULL
);