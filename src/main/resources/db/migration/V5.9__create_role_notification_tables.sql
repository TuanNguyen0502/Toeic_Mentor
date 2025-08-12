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
SELECT r.id, nt.id
FROM roles r
         JOIN notification_types nt
              ON nt.action = 'NEW_REPORT'
WHERE r.name = 'ROLE_ADMIN';

INSERT INTO role_notifications (role_id, notification_type_id)
SELECT r.id, nt.id
FROM roles r
         JOIN notification_types nt
              ON nt.action = 'COMPLETE_REPORT'
WHERE r.name = 'ROLE_ADMIN';

INSERT INTO role_notifications (role_id, notification_type_id)
SELECT r.id, nt.id
FROM roles r
         JOIN notification_types nt
              ON nt.action = 'COMPLETE_REPORT'
WHERE r.name = 'ROLE_USER';

