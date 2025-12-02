use `niche_ reviews`;
ALTER TABLE business
DROP COLUMN good_for_kids,
DROP COLUMN business_accepts_creditcards;

-- 修改字段类型为LONGTEXT
ALTER TABLE `user` MODIFY COLUMN `friends` LONGTEXT NULL;

ALTER TABLE business MODIFY COLUMN is_open INT NOT NULL DEFAULT 1;
ALTER TABLE business MODIFY COLUMN hours JSON NULL;
ALTER TABLE business
ADD COLUMN postal_code VARCHAR(20) NULL AFTER state,
ADD COLUMN latitude DOUBLE NULL AFTER postal_code,
ADD COLUMN longitude DOUBLE NULL AFTER latitude,
ADD COLUMN attributes JSON NULL AFTER categories,
ADD COLUMN created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP AFTER hours,
ADD COLUMN updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP AFTER created_at;

CREATE TABLE `tips` (
    `tip_id` INT AUTO_INCREMENT PRIMARY KEY, -- 自增ID（Yelp的tips无原生主键）
    `user_id` VARCHAR(50) NOT NULL,         -- 用户ID
    `business_id` VARCHAR(50) NOT NULL,     -- 商户ID（关联business表）
    `text` TEXT,                            -- 贴士内容
    `date` DATE,                            -- 发布日期
    `compliment_count` INT DEFAULT 0,       -- 点赞数
    UNIQUE KEY `unique_tip` (`user_id`, `business_id`, `text`(200)) -- 避免重复贴士
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

drop table photos;

CREATE TABLE `photo` (
    `photo_id` VARCHAR(50) PRIMARY KEY,  -- Yelp原生图片ID
    `business_id` VARCHAR(50) NOT NULL,  -- 关联business表的bid（Yelp商户ID）
    `caption` TEXT NULL,                 -- 图片描述
    `label` VARCHAR(100) NULL         -- 图片标签（如food、inside等）
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;