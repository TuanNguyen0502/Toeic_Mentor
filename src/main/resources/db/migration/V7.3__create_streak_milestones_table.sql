USE
`toeic-mentor`;

CREATE TABLE streak_milestones
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    day_target  INT          NOT NULL UNIQUE, -- Số ngày liên tục (VD: 3, 7, 14)
    title       VARCHAR(255) NOT NULL,        -- Tiêu đề hiển thị (VD: "Chăm chỉ 7 ngày")
    description TEXT,                         -- Mô tả, phần thưởng,...
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME DEFAULT CURRENT_TIMESTAMP
);