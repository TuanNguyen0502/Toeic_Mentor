USE
`toeic-mentor`;

-- 1. Tạo bảng "sections"
CREATE TABLE sections
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    title      VARCHAR(255) NOT NULL,
    status     ENUM('DRAFT', 'PENDING_REVIEW', 'APPROVED', 'REJECTED') NOT NULL,
    created_at DATETIME     NOT NULL,
    updated_at DATETIME     NOT NULL
);

-- 2. Tạo dữ liệu mẫu cho bảng "sections"
INSERT INTO sections (title, status, created_at, updated_at)
VALUES ('Section Sample', 'APPROVED', '2025-06-23 10:50:00', '2025-06-23 10:50:00')