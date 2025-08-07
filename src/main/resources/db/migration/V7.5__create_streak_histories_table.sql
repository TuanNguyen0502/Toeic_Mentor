USE
`toeic-mentor`;

CREATE TABLE streak_histories
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id      BIGINT   NOT NULL,
    start_streak DATETIME NOT NULL,
    end_streak   DATETIME,
    created_at   DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

INSERT INTO streak_histories (user_id, start_streak)
SELECT id, '2025-07-28 14:44:36'
FROM users
WHERE id NOT IN (SELECT user_id FROM streak_histories);