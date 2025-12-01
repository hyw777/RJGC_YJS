create database food_rec;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

use food_rec;

-- ----------------------------
-- Table structure for access
-- ----------------------------
DROP TABLE IF EXISTS `access`;
CREATE TABLE `access`  (
  `user_id` VARCHAR(50) NOT NULL,
  `business_id` VARCHAR(50) NULL DEFAULT NULL,
  `time` datetime NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for application
-- ----------------------------
DROP TABLE IF EXISTS `application`;
CREATE TABLE `application`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `applicant_id` int NOT NULL,
  `recipient_id` int NOT NULL,
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;



DROP TABLE IF EXISTS `business`;
CREATE TABLE `business` (
    `business_id` VARCHAR(50) PRIMARY KEY, -- 商户 ID（来自 Yelp）
    `name` VARCHAR(255) NOT NULL, -- 商户名称
    `address` TEXT NOT NULL, -- 商户地址
    `city` VARCHAR(255) NOT NULL, -- 城市
    `state` VARCHAR(255) NOT NULL, -- 州/省
    `postal_code` VARCHAR(20), -- 邮政编码
    `latitude` DOUBLE, -- 纬度
    `longitude` DOUBLE, -- 经度
    `stars` DOUBLE NOT NULL, -- 星级评分
    `review_count` INT NOT NULL, -- 评论数量
    `is_open` INT DEFAULT 1, -- 是否营业（0=不营业，1=营业）
    `categories` TEXT, -- 商户分类（可以存储一个 JSON 格式的字符串）
    `attributes` JSON, -- 商户属性（JSON 格式）
    `hours` JSON, -- 营业时间（如果有的话）
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 创建时间
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- 更新时间
) ENGINE = InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `gender` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `age` int NULL DEFAULT NULL,
  `city` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `review_count` int NULL DEFAULT NULL,
  `cool` int NULL DEFAULT NULL,
  `useful` int NULL DEFAULT NULL,
  `elite` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `yelping_since` datetime NULL DEFAULT NULL,
  `funny` int NULL DEFAULT NULL,
  `friends` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;



DROP TABLE IF EXISTS `review`;
CREATE TABLE `review` (
    `review_id` INT AUTO_INCREMENT PRIMARY KEY, -- 评论 ID
    `user_id` VARCHAR(50) NOT NULL, -- 用户 ID（来自 Yelp）
    `business_id` VARCHAR(50) NOT NULL, -- 商户 ID（来自 business 表）
    `stars` INT NOT NULL, -- 评论星级
    `useful` INT DEFAULT 0, -- 有用的评分
    `funny` INT DEFAULT 0, -- 有趣的评分
    `cool` INT DEFAULT 0, -- 酷的评分
    `text` TEXT NOT NULL, -- 评论内容
    `date` DATETIME NOT NULL, -- 评论时间
    FOREIGN KEY (`business_id`) REFERENCES `business`(`business_id`) -- 外键关联到 business 表
) ENGINE = InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `tips` (
    `tip_id` INT AUTO_INCREMENT PRIMARY KEY, -- 自增ID（Yelp的tips无原生主键）
    `user_id` VARCHAR(50) NOT NULL,         -- 用户ID
    `business_id` VARCHAR(50) NOT NULL,     -- 商户ID（关联business表）
    `text` TEXT,                            -- 贴士内容
    `date` DATE,                            -- 发布日期
    `compliment_count` INT DEFAULT 0,       -- 点赞数
    FOREIGN KEY (`business_id`) REFERENCES `business`(`business_id`) ON DELETE CASCADE,
    UNIQUE KEY `unique_tip` (`user_id`, `business_id`, `text`(200)) -- 避免重复贴士
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for user_business
-- ----------------------------
DROP TABLE IF EXISTS `user_business`;
CREATE TABLE `user_business`  (
  `user_id` VARCHAR(50) NOT NULL,
  `business_id` VARCHAR(50) NOT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;



-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `user_id` VARCHAR(50) NOT NULL COMMENT '用户id',
  `role_id` bigint NOT NULL DEFAULT 0 COMMENT '角色id',
  `status` int NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for role_menu
-- ----------------------------
DROP TABLE IF EXISTS `role_menu`;
CREATE TABLE `role_menu`  (
  `role_id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `menu_id` bigint NOT NULL DEFAULT 0 COMMENT '菜单id',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;


CREATE TABLE `role`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `role_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色权限字符串',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '角色状态（0正常 1停用）',
  `del_flag` int NULL DEFAULT 0 COMMENT 'del_flag',
  `create_by` bigint NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` bigint NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `photos`;
CREATE TABLE `photos`  (
  `business_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `photo_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for news
-- ----------------------------
DROP TABLE IF EXISTS `news`;
CREATE TABLE `news`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `time` datetime NULL DEFAULT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `user_id` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
  `send_id` VARCHAR(50) NOT NULL,
  `receive_id` VARCHAR(50) NOT NULL,
  `context` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `time` datetime NULL DEFAULT NULL,
  `status` int NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `menu_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'NULL' COMMENT '菜单名',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组件路径',
  `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '#' COMMENT '菜单图标',
  `create_by` bigint NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` bigint NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `del_flag` int NULL DEFAULT 0 COMMENT '是否删除（0未删除 1已删除）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for last_message
-- ----------------------------
DROP TABLE IF EXISTS `last_message`;
CREATE TABLE `last_message`  (
  `send_id` VARCHAR(50) NOT NULL,
  `receive_id` VARCHAR(50) NOT NULL,
  `time` datetime NOT NULL,
  `context` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for collection
-- ----------------------------
DROP TABLE IF EXISTS `collection`;
CREATE TABLE `collection`  (
  `user_id` VARCHAR(50) NOT NULL,
  `business_id` VARCHAR(50) NULL DEFAULT NULL,
  `date` datetime NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;
