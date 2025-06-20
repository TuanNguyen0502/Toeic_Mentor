USE
`toeic-mentor`;

-- 1. Create "parts" table with ENUM column "name"
CREATE TABLE parts
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name ENUM('PART_1','PART_2','PART_3','PART_4','PART_5','PART_6','PART_7') NOT NULL UNIQUE
);

-- 2. Insert default parts: PART_5, PART_6, PART_7
INSERT INTO parts (name)
VALUES ('PART_1'),
       ('PART_2'),
       ('PART_3'),
       ('PART_4'),
       ('PART_5'),
       ('PART_6'),
       ('PART_7');