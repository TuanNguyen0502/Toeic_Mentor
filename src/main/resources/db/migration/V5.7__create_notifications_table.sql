USE
`toeic-mentor`;

-- 1. Tạo bảng "notifications"
CREATE TABLE notifications
(
    id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    receiver_id          BIGINT       NOT NULL,
    report_id            BIGINT,
    notification_type_id BIGINT       NOT NULL,
    title                VARCHAR(255) NOT NULL,
    message              TEXT,
    is_read              BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at           DATETIME     NOT NULL,
    updated_at           DATETIME     NOT NULL,
    FOREIGN KEY (receiver_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (report_id) REFERENCES reports (id) ON DELETE CASCADE,
    FOREIGN KEY (notification_type_id) REFERENCES notification_types (id) ON DELETE CASCADE
);