USE
`toeic-mentor`;

-- 1. Tạo bảng "role_notifications"
CREATE TABLE role_notifications
(
    role_id           BIGINT       NOT NULL,
    notification_type VARCHAR(100) NOT NULL,
    PRIMARY KEY (role_id, notification_type),
    FOREIGN KEY (role_id) REFERENCES roles (id),
    FOREIGN KEY (notification_type) REFERENCES notification_types (action)
);

-- 2. Thêm dữ liệu mẫu vào bảng "role_notifications"
INSERT INTO role_notifications (role_id, notification_type) VALUES
(1, 'NEW_REPORT'),
(2, 'COMPLETE_REPORT')

