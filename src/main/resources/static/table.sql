use mytube;

CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       nickname VARCHAR(50) NOT NULL,
                       password VARCHAR(200) NOT NULL,
                       email VARCHAR(50) UNIQUE NOT NULL,
                       role ENUM('USER', 'CREATOR', 'ADMIN') NOT NULL DEFAULT 'USER',
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

ALTER TABLE users
    ADD COLUMN provider VARCHAR(20) DEFAULT 'LOCAL',   -- LOCAL, GOOGLE, GITHUB ...
    ADD COLUMN provider_id VARCHAR(100);              -- 소셜에서 제공하는 user id


CREATE TABLE video (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY ,
                       user_id BIGINT NOT NULL,
                       title VARCHAR(100) NOT NULL,
                       description TEXT,
                       status ENUM('UPLOADING','PROCESSING','AVAILABLE','FAILED') NOT NULL DEFAULT 'UPLOADING',
                       file_url VARCHAR(200) NOT NULL,
                       thumbnail_url VARCHAR(200),
                       views BIGINT DEFAULT 0,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
#     FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE table encoding_jobs (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               video_id BIGINT NOT NULL,
                               resolution VARCHAR(20) NOT NULL ,
                               bitrate VARCHAR(20) NOT NULL,
                               status ENUM('PENDING', 'PROCESSING', 'COMPLETED', 'FAILED') NOT NULL DEFAULT 'PENDING',
                               output_url VARCHAR(200),
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                               FOREIGN KEY (video_id) REFERENCES video(id) ON DELETE CASCADE
);

CREATE TABLE video_comment (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               video_id BIGINT NOT NULL,
                               user_id BIGINT NOT NULL,
                               content TEXT NOT NULL,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
#     FOREIGN KEY (video_id) REFERENCES video(id) ON DELETE CASCADE,
#     FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE video_like (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            video_id BIGINT NOT NULL,
                            user_id BIGINT NOT NULL,
                            is_like BOOLEAN NOT NULL,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            UNIQUE (video_id, user_id)
#     FOREIGN KEY (video_id) REFERENCES video(id) ON DELETE CASCADE,
#     FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE board (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       title VARCHAR(100) NOT NULL,
                       user_id BIGINT NOT NULL,
                       content TEXT NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE board_comment (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               board_id BIGINT NOT NULL,
                               user_id BIGINT NOT NULL,
                               content TEXT NOT NULL,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
#     FOREIGN KEY (board_id) REFERENCES board(id) ON DELETE CASCADE,
#     FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE board_image (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             user_id BIGINT NOT NULL,
                             image_url VARCHAR(200) NOT NULL,
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
#     FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);