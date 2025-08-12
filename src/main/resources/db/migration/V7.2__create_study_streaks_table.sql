USE
`toeic-mentor`;

CREATE TABLE study_streaks
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id         BIGINT NOT NULL,
    current_streak  INT      DEFAULT 0,
    max_streak      INT      DEFAULT 0,
    last_study_date DATE,
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id)
);
