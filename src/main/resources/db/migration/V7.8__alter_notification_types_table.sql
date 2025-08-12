USE
`toeic-mentor`;

ALTER TABLE notification_types
    MODIFY COLUMN action ENUM('NEW_REPORT', 'COMPLETE_REPORT', 'NEW_STREAK_ACHIEVEMENT');

INSERT INTO notification_types (action, description)
VALUES ('NEW_STREAK_ACHIEVEMENT', 'User achieved a new streak milestone');
