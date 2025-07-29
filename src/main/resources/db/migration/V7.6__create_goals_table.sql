USE
`toeic-mentor`;

CREATE TABLE goals
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id      BIGINT       NOT NULL,
    title        VARCHAR(255) NOT NULL,
    type         ENUM('DAILY', 'WEEKLY', 'OTHER') NOT NULL,
    goal_date    DATETIME     NOT NULL,
    target_value INT          NOT NULL,
    actual_value INT      DEFAULT 0,
    unit         ENUM('MINUTES', 'QUESTIONS', 'PARTS', 'TESTS', 'WORDS', 'OTHER') NOT NULL,
    part         INT,
    status       ENUM('IN_PROGRESS', 'COMPLETED', 'FAILED') DEFAULT 'IN_PROGRESS',
    created_at   DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id)
);