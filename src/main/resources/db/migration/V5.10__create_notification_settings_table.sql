USE
`toeic-mentor`;

-- 1. Tạo bảng "notification_settings"
CREATE TABLE notification_settings
(
    user_id BIGINT NOT NULL,
    notification_type VARCHAR(100) NOT NULL,
    enabled BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (user_id, notification_type),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (notification_type) REFERENCES notification_types (action)
);

-- 2. Thêm dữ liệu mẫu vào bảng "notification_settings"
INSERT INTO notification_settings (user_id, notification_type, enabled) VALUES
(1, 'NEW_REPORT', TRUE),
(1, 'COMPLETE_REPORT', TRUE),
(2, 'NEW_REPORT', TRUE),
(2, 'COMPLETE_REPORT', TRUE),
(3, 'NEW_REPORT', TRUE),
(3, 'COMPLETE_REPORT', TRUE);