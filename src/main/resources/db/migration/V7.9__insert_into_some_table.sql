USE
`toeic-mentor`;

INSERT INTO notification_types (action, description)
VALUES ('NEW_STREAK_ACHIEVEMENT', 'User achieved a new streak milestone');

INSERT INTO notification_settings (user_id, notification_type_id, enabled)
SELECT id AS user_id, 3 AS notification_type_id, 1 AS enabled
FROM users
WHERE NOT EXISTS (
    SELECT 1 FROM notification_settings ns
    WHERE ns.user_id = users.id AND ns.notification_type_id = 3
);