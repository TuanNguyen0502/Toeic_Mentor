USE
`toeic-mentor`;

ALTER TABLE notification_types
    MODIFY COLUMN action ENUM('NEW_REPORT', 'COMPLETE_REPORT', 'NEW_STREAK_ACHIEVEMENT', 'STREAK_ACHIEVE_REVOKED');

INSERT INTO notification_types (action, description)
VALUES ('STREAK_ACHIEVE_REVOKED', 'User had a milestone revoked');

INSERT INTO role_notifications (role_id, notification_type_id)
SELECT r.id, nt.id
FROM roles r
         JOIN notification_types nt
              ON nt.action = 'STREAK_ACHIEVE_REVOKED'
WHERE r.name = 'ROLE_USER';

INSERT INTO role_notifications (role_id, notification_type_id)
SELECT r.id, nt.id
FROM roles r
         JOIN notification_types nt
              ON nt.action = 'STREAK_ACHIEVE_REVOKED'
WHERE r.name = 'ROLE_ADMIN';