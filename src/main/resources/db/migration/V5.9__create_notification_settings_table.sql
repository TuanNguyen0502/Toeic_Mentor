USE
`toeic-mentor`;

-- 1. Tạo bảng "notification_settings"
CREATE TABLE notification_settings
(
    user_id              BIGINT NOT NULL,
    notification_type_id BIGINT NOT NULL,
    enabled              BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (user_id, notification_type_id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (notification_type_id) REFERENCES notification_types (id)
);