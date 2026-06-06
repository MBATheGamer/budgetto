CREATE TABLE shared_budget_members (
    id               INT        AUTO_INCREMENT PRIMARY KEY,
    shared_budget_id INT        NOT NULL,
    user_id          INT        NOT NULL,
    role             ENUM('OWNER','MEMBER') DEFAULT 'MEMBER',
    status           ENUM('PENDING','ACTIVE') DEFAULT 'PENDING',
    joined_at        TIMESTAMP  DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (shared_budget_id)
      REFERENCES shared_budgets(id)
        ON DELETE CASCADE,

    FOREIGN KEY (user_id)
      REFERENCES users(id)
        ON DELETE CASCADE,

    UNIQUE KEY uq_member (shared_budget_id, user_id)
);