USE
`toeic-mentor`;

ALTER TABLE goals
    MODIFY COLUMN goal_date DATE NOT NULL;
