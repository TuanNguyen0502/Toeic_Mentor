USE
`toeic-mentor`;

-- 2. Cập nhật bảng "notifications"
ALTER TABLE notifications
    ADD COLUMN action VARCHAR(100);

UPDATE notifications
SET action = 'NEW_REPORT'
WHERE type = 'ERROR_REPORT';

UPDATE notifications
SET action = 'COMPLETE_REPORT'
WHERE type = 'USER';

ALTER TABLE notifications
DROP
COLUMN type;

ALTER TABLE notifications
    ADD CONSTRAINT fk_notification_action
        FOREIGN KEY (action)
            REFERENCES notification_types (action);