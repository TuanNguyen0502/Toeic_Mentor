USE
`toeic-mentor`;

CREATE TABLE streak_achievements
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id      BIGINT NOT NULL,
    milestone_id BIGINT NOT NULL,
    achieved_at  DATE     DEFAULT CURRENT_TIMESTAMP,
    created_at   DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (user_id, milestone_id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (milestone_id) REFERENCES streak_milestones (id)
);