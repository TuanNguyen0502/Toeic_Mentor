USE
`toeic-mentor`;

-- 1. Tạo bảng "role_notifications"
CREATE TABLE role_notifications
(
    role_id              BIGINT NOT NULL,
    notification_type_id BIGINT NOT NULL,
    PRIMARY KEY (role_id, notification_type_id),
    FOREIGN KEY (role_id) REFERENCES roles (id),
    FOREIGN KEY (notification_type_id) REFERENCES notification_types (id)
);

-- 2. Thêm dữ liệu mẫu vào bảng "role_notifications"
INSERT INTO role_notifications (role_id, notification_type_id)
SELECT 1, id
FROM notification_types
WHERE action ='NEW_REPORT'
UNION ALL
SELECT 1, id
FROM notification_types
WHERE action ='COMPLETE_REPORT'
UNION ALL
SELECT 2, id
FROM notification_types
WHERE action ='COMPLETE_REPORT';

