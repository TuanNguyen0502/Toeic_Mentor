USE
`toeic-mentor`;

ALTER TABLE notification_types
    MODIFY COLUMN action ENUM('NEW_REPORT', 'COMPLETE_REPORT', 'NEW_STREAK_ACHIEVEMENT', 'STREAK_ACHIEVE_REVOKED');

INSERT INTO notification_types (action, description)
VALUES ('STREAK_ACHIEVE_REVOKED', 'User had a milestone revoked');