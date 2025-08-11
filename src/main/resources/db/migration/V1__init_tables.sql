USE
`toeic-mentor`;

CREATE TABLE roles
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name ENUM('ROLE_ADMIN','ROLE_USER') NOT NULL
);

INSERT INTO roles (name) VALUES
('ROLE_ADMIN'),
('ROLE_USER');

CREATE TABLE users
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    email      VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    full_name  VARCHAR(255) NOT NULL,
    gender     ENUM('FEMALE','MALE','OTHER') NOT NULL,
    avatar_url VARCHAR(255),
    role_id    BIGINT       NOT NULL,
    is_active  BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at DATETIME     NOT NULL,
    updated_at DATETIME     NOT NULL,
    FOREIGN KEY (role_id) REFERENCES roles (id)
);

CREATE TABLE questions
(
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    question_number   INTEGER    NOT NULL,
    question_text     TEXT       NOT NULL,
    correct_answer    VARCHAR(1) NOT NULL,
    passage           TEXT,
    passage_image_url VARCHAR(255)
);

CREATE TABLE question_options
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    option_key  VARCHAR(1) NOT NULL,
    option_text TEXT       NOT NULL,
    question_id BIGINT     NOT NULL,
    FOREIGN KEY (question_id) REFERENCES questions (id)
);

CREATE TABLE question_images
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    image       VARCHAR(255) NOT NULL,
    question_id BIGINT       NOT NULL,
    FOREIGN KEY (question_id) REFERENCES questions (id)
);