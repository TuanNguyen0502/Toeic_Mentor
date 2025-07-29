USE
`toeic-mentor`;

CREATE TABLE streak_achievements
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT NOT NULL,
    milestone   INT      DEFAULT 0,
    achieved_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (user_id, milestone),
    FOREIGN KEY (user_id) REFERENCES users (id)
);