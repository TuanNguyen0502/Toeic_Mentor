USE
`toeic-mentor`;

ALTER TABLE streak_achievements
DROP
FOREIGN KEY streak_achievements_ibfk_2;

ALTER TABLE streak_achievements
    ADD CONSTRAINT streak_achievements_ibfk_2
        FOREIGN KEY (milestone_id) REFERENCES streak_milestones (id)
            ON DELETE CASCADE;
