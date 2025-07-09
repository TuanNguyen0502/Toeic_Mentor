USE
`toeic-mentor`;

-- 2. Cập nhật bảng "questions"
-- 2.1 Thêm cột score
ALTER TABLE tests
    ADD COLUMN score INT NOT NULL DEFAULT 0;
-- 2.2 Thêm cột recommendations
ALTER TABLE tests
    ADD COLUMN recommendations TEXT;