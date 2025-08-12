USE
`toeic-mentor`;

ALTER TABLE notification_types
    MODIFY COLUMN action ENUM('NEW_REPORT', 'COMPLETE_REPORT', 'NEW_STREAK_ACHIEVEMENT');

INSERT INTO notification_types (action, description)
VALUES ('NEW_STREAK_ACHIEVEMENT', 'User achieved a new streak milestone');

INSERT INTO role_notifications (role_id, notification_type_id)
SELECT r.id, nt.id
FROM roles r
         JOIN notification_types nt
              ON nt.action = 'NEW_STREAK_ACHIEVEMENT'
WHERE r.name = 'ROLE_USER';
