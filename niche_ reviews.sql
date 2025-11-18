/*
 Navicat Premium Data Transfer

 Source Server         : 111
 Source Server Type    : MySQL
 Source Server Version : 80031
 Source Host           : localhost:3306
 Source Schema         : niche_ reviews

 Target Server Type    : MySQL
 Target Server Version : 80031
 File Encoding         : 65001

 Date: 23/06/2024 16:30:10
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for access
-- ----------------------------
DROP TABLE IF EXISTS `access`;
CREATE TABLE `access`  (
  `user_id` int NOT NULL,
  `business_id` int NULL DEFAULT NULL,
  `time` datetime NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of access
-- ----------------------------
INSERT INTO `access` VALUES (1, 1, '2024-06-06 21:46:06');
INSERT INTO `access` VALUES (1, 1, '2024-06-07 14:36:53');
INSERT INTO `access` VALUES (1, 1, '2024-06-07 14:39:10');
INSERT INTO `access` VALUES (1, 1, '2024-06-07 14:39:47');
INSERT INTO `access` VALUES (1, 1, '2024-06-07 14:41:23');
INSERT INTO `access` VALUES (21, 1, '2024-06-07 12:37:24');
INSERT INTO `access` VALUES (21, 1, '2024-06-06 12:37:38');
INSERT INTO `access` VALUES (20, 1, '2024-06-12 12:38:01');
INSERT INTO `access` VALUES (20, 1, '2024-06-11 12:38:52');
INSERT INTO `access` VALUES (21, 1, '2024-06-11 12:39:04');

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

-- ----------------------------
-- Records of application
-- ----------------------------
INSERT INTO `application` VALUES (1, 2, 1, 'hahaha', '2024-06-16 11:23:45');

-- ----------------------------
-- Table structure for business
-- ----------------------------
DROP TABLE IF EXISTS `business`;
CREATE TABLE `business`  (
  `business_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `address` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `city` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `stars` double NOT NULL,
  `review_count` int NOT NULL,
  `is_open` int NOT NULL,
  `categories` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `hours` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `bid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `good_for_kids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `business_accepts_creditcards` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`business_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of business
-- ----------------------------
INSERT INTO `business` VALUES (1, '新商铺名称', '123 街道 456 号', 'Affton', '州名', 3.5, 6, 1, '餐馆,咖啡馆', '周一-周日 10:00-22:00', 'unique_bid_string', 'yes', 'true');
INSERT INTO `business` VALUES (2, 'The UPS Store', '87 Grasso Plaza Shopping Center', 'Affton', 'MO', 3, 15, 1, 'Shipping Centers, Local Services, Notaries, Mailbox Centers, Printing Services', '{\"Monday\":\"0:0-0:0\",\"Tuesday\":\"8:0-18:30\",\"Wednesday\":\"8:0-18:30\",\"Thursday\":\"8:0-18:30\",\"Friday\":\"8:0-18:30\",\"Saturday\":\"8:0-14:0\"}', 'yyy', NULL, NULL);
INSERT INTO `business` VALUES (3, 'Target', '5255 E Broadway Blvd', 'Tucson', 'AZ', 3.5, 22, 0, 'Department Stores, Shopping, Fashion, Home & Garden, Electronics, Furniture Stores', '{\"Monday\":\"8:0-22:0\",\"Tuesday\":\"8:0-22:0\",\"Wednesday\":\"8:0-22:0\",\"Thursday\":\"8:0-22:0\",\"Friday\":\"8:0-23:0\",\"Saturday\":\"8:0-23:0\",\"Sunday\":\"8:0-22:0\"}', 'xxx', NULL, NULL);
INSERT INTO `business` VALUES (4, 'The UPS Store', '87 Grasso Plaza Shopping Center', 'Affton', 'MO', 3, 15, 1, '[\"Shipping Centers\", \"Local Services\", \"Notaries\", \"Mailbox Centers\", \"Printing Services\"]', '{\"Monday\":\"0:0-0:0\",\"Tuesday\":\"8:0-18:30\",\"Wednesday\":\"8:0-18:30\",\"Thursday\":\"8:0-18:30\",\"Friday\":\"8:0-18:30\",\"Saturday\":\"8:0-14:0\"}', NULL, NULL, NULL);
INSERT INTO `business` VALUES (5, 'Target', '5255 E Broadway Blvd', 'Tucson', 'AZ', 3.5, 22, 0, '[\"Department Stores\", \"Shopping\", \"Fashion\", \"Home & Garden\", \"Electronics\", \"Furniture Stores\"]', '{\"Monday\":\"8:0-22:0\",\"Tuesday\":\"8:0-22:0\",\"Wednesday\":\"8:0-22:0\",\"Thursday\":\"8:0-22:0\",\"Friday\":\"8:0-23:0\",\"Saturday\":\"8:0-23:0\",\"Sunday\":\"8:0-22:0\"}', NULL, NULL, NULL);
INSERT INTO `business` VALUES (6, 'The UPS Store', '87 Grasso Plaza Shopping Center', 'Affton', 'MO', 3, 15, 1, '[\"Shipping Centers\", \"Local Services\", \"Notaries\", \"Mailbox Centers\", \"Printing Services\"]', '{\"Monday\":\"0:0-0:0\",\"Tuesday\":\"8:0-18:30\",\"Wednesday\":\"8:0-18:30\",\"Thursday\":\"8:0-18:30\",\"Friday\":\"8:0-18:30\",\"Saturday\":\"8:0-14:0\"}', NULL, NULL, NULL);
INSERT INTO `business` VALUES (7, 'Target', '5255 E Broadway Blvd', 'Tucson', 'AZ', 3.5, 22, 0, '[\"Department Stores\", \"Shopping\", \"Fashion\", \"Home & Garden\", \"Electronics\", \"Furniture Stores\"]', '{\"Monday\":\"8:0-22:0\",\"Tuesday\":\"8:0-22:0\",\"Wednesday\":\"8:0-22:0\",\"Thursday\":\"8:0-22:0\",\"Friday\":\"8:0-23:0\",\"Saturday\":\"8:0-23:0\",\"Sunday\":\"8:0-22:0\"}', NULL, NULL, NULL);
INSERT INTO `business` VALUES (8, 'The UPS Store', '87 Grasso Plaza Shopping Center', 'Affton', 'MO', 3, 15, 1, '[\"Shipping Centers\", \"Local Services\", \"Notaries\", \"Mailbox Centers\", \"Printing Services\"]', '{\"Monday\":\"0:0-0:0\",\"Tuesday\":\"8:0-18:30\",\"Wednesday\":\"8:0-18:30\",\"Thursday\":\"8:0-18:30\",\"Friday\":\"8:0-18:30\",\"Saturday\":\"8:0-14:0\"}', NULL, NULL, NULL);
INSERT INTO `business` VALUES (9, 'Sample Business', '123 Main St', 'Sample City', 'Sample State', 4.5, 50, 1, '[Restaurant, Fast Food]', '{Mon-Sun 10:00-22:00}', NULL, NULL, NULL);
INSERT INTO `business` VALUES (19, '解忧杂货铺', '中国', '贵阳', '亚洲', 0, 0, 1, '美食', '周一到周日：10：00-21：30', 'b2936128d0374f', 'yes', 'true');

-- ----------------------------
-- Table structure for collection
-- ----------------------------
DROP TABLE IF EXISTS `collection`;
CREATE TABLE `collection`  (
  `user_id` int NOT NULL,
  `business_id` int NULL DEFAULT NULL,
  `date` datetime NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of collection
-- ----------------------------
INSERT INTO `collection` VALUES (21, 2, '2024-06-11 17:41:40');
INSERT INTO `collection` VALUES (1, 1, '2024-06-12 09:11:12');
INSERT INTO `collection` VALUES (20, 2, '2024-05-17 09:14:19');

-- ----------------------------
-- Table structure for last_message
-- ----------------------------
DROP TABLE IF EXISTS `last_message`;
CREATE TABLE `last_message`  (
  `send_id` int NOT NULL,
  `receive_id` int NOT NULL,
  `time` datetime NOT NULL,
  `context` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of last_message
-- ----------------------------
INSERT INTO `last_message` VALUES (21, 1, '2024-06-15 10:51:27', '哈哈哈');
INSERT INTO `last_message` VALUES (1, 2, '2024-06-15 12:11:40', '还得练');

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
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES (1, '用户身份', 'details', 'system/details', '0', '0', 'user', '#', NULL, NULL, NULL, NULL, 0, NULL);
INSERT INTO `menu` VALUES (2, '商家身份', 'boss', NULL, '0', '0', 'boss', '#', NULL, NULL, NULL, NULL, 0, NULL);
INSERT INTO `menu` VALUES (3, 'NULL', NULL, NULL, '0', '0', 'admin', '#', NULL, NULL, NULL, NULL, 0, NULL);

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
  `send_id` int NOT NULL,
  `receive_id` int NOT NULL,
  `context` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `time` datetime NULL DEFAULT NULL,
  `status` int NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of message
-- ----------------------------
INSERT INTO `message` VALUES (21, 1, 'hahaha,oooh', '2024-06-14 10:42:20', 0);
INSERT INTO `message` VALUES (21, 1, 'hahaha,oooh', '2024-06-14 11:26:12', 0);
INSERT INTO `message` VALUES (21, 1, 'hahaha,oooh', '2024-06-14 11:26:25', 0);
INSERT INTO `message` VALUES (1, 21, '草泥马', '2024-06-14 11:26:57', 0);
INSERT INTO `message` VALUES (1, 21, '草泥马', '2024-06-14 11:35:34', 1);
INSERT INTO `message` VALUES (21, 1, 'hahaha,oooh', '2024-06-14 11:38:04', 1);
INSERT INTO `message` VALUES (21, 1, 'I am good boy', '2024-06-14 13:49:52', 1);
INSERT INTO `message` VALUES (1, 21, '草泥马', '2024-06-14 13:51:02', 1);
INSERT INTO `message` VALUES (1, 21, '草泥马', '2024-06-14 13:54:03', 1);
INSERT INTO `message` VALUES (1, 21, '草泥马', '2024-06-14 13:54:37', 1);
INSERT INTO `message` VALUES (1, 21, '草泥马', '2024-06-14 13:54:58', 1);
INSERT INTO `message` VALUES (21, 1, 'I am good boy', '2024-06-15 10:43:00', 0);
INSERT INTO `message` VALUES (21, 1, 'I am good boy', '2024-06-15 10:45:49', 0);
INSERT INTO `message` VALUES (21, 1, 'you are the beautiest chinese girl I have seen', '2024-06-15 10:47:12', 0);
INSERT INTO `message` VALUES (21, 1, 'you are the beautiest chinese girl I have seen', '2024-06-15 10:49:12', 0);
INSERT INTO `message` VALUES (1, 21, 'you are the beautiest chinese boy I have seen', '2024-06-15 10:51:28', 0);

-- ----------------------------
-- Table structure for news
-- ----------------------------
DROP TABLE IF EXISTS `news`;
CREATE TABLE `news`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `time` datetime NULL DEFAULT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `user_id` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of news
-- ----------------------------
INSERT INTO `news` VALUES (2, '2024-06-23 13:45:08', '你在2018-07-07 22:09:11.0对店铺《Target》做出的评价If you ...违反了社区规定，建议你尽快删除，并且规范自己的发言，否则将面临被封号的风险', 1);
INSERT INTO `news` VALUES (3, '2024-06-23 16:15:19', '你在2018-07-07 22:09:11.0对店铺《Target》做出的评价:If you ...违反了社区规定，建议你尽快删除，并且规范自己的发言，否则将面临被封号的风险', 1);

-- ----------------------------
-- Table structure for photos
-- ----------------------------
DROP TABLE IF EXISTS `photos`;
CREATE TABLE `photos`  (
  `business_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `photo_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of photos
-- ----------------------------
INSERT INTO `photos` VALUES ('yyy', 'dfhkh');
INSERT INTO `photos` VALUES ('unique_bid_string', 'sdjikjn');
INSERT INTO `photos` VALUES ('unique_bid_string', '22222');
INSERT INTO `photos` VALUES ('xxx', '33333');
INSERT INTO `photos` VALUES ('unique_bid_string', '44444');

-- ----------------------------
-- Table structure for reviews
-- ----------------------------
DROP TABLE IF EXISTS `reviews`;
CREATE TABLE `reviews`  (
  `review_id` int NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `business_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `stars` double NOT NULL,
  `useful` int NULL DEFAULT 0,
  `funny` int NULL DEFAULT 0,
  `cool` int NULL DEFAULT 0,
  `text` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `date` datetime NOT NULL,
  `rid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`review_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of reviews
-- ----------------------------
INSERT INTO `reviews` VALUES (1, 'mjWvAkNqoA6Y5P03sDSzfA', 'xxx', 3, 0, 0, 0, 'If you decide to eat here, just be aware it is going to take about 2 hours from beginning to end. We have tried it multiple times, because I want to like it! I have been to it\'s other locations in NJ and never had a bad experience. \nThe food is good, but it takes a very long time to come out. The waitstaff is very young, but usually pleasant. We have just had too many experiences where we spent way too long waiting. We usually opt for another diner or restaurant on the weekends, in order to be done quicker.', '2018-07-07 22:09:11', NULL);
INSERT INTO `reviews` VALUES (3, 'mjWvAkNqoA6Y5P03sDSzfA', 'yyy', 3.5, 0, 0, 0, 'Very good！', '2024-05-31 18:25:51', NULL);
INSERT INTO `reviews` VALUES (4, 'mjWvAkNqoA6Y5P03sDSzfA', 'xxx', 3.5, 0, 0, 0, 'Very bad！', '2024-04-17 18:26:36', NULL);
INSERT INTO `reviews` VALUES (5, 'mjWvAkNqoA6Y5P03sDSzfA', 'xxx', 3.5, 0, 0, 0, 'Very good！', '2024-05-31 18:27:07', NULL);
INSERT INTO `reviews` VALUES (6, 'mjWvAkNqoA6Y5P03sDSzfA', 'xxx', 3.5, 0, 0, 0, 'Very good！', '2024-05-31 18:27:13', NULL);
INSERT INTO `reviews` VALUES (7, 'f16a8dc6-5330-475d-be0', 'unique_bid_string', 3.5, 0, 0, 0, 'Very good！', '2024-05-31 18:27:29', NULL);
INSERT INTO `reviews` VALUES (15, 'mjWvAkNqoA6Y5P03sDSzfA', 'unique_bid_string', 3.5, 0, 0, 0, 'okok', '2024-05-31 18:34:33', NULL);
INSERT INTO `reviews` VALUES (16, 'mjWvAkNqoA6Y5P03sDSzfA', 'unique_bid_string', 0, 0, 0, 0, 'good', '2024-05-31 18:37:34', NULL);
INSERT INTO `reviews` VALUES (23, 'mjWvAkNqoA6Y5P03sDSzfA', '1', 3.7, 0, 0, 0, 'good!', '2024-06-06 17:36:48', NULL);
INSERT INTO `reviews` VALUES (24, 'mjWvAkNqoA6Y5P03sDSzfA', '1', 3.7, 0, 0, 0, '阮恩杰真帅', '2024-06-07 14:45:38', NULL);
INSERT INTO `reviews` VALUES (25, 'f16a8dc6-5330-475d-be0', 'unique_bid_string', 3.7, 0, 0, 0, '阮恩杰真帅', '2024-06-07 14:54:58', NULL);

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
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

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, '普通用户', 'user', '0', 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `role` VALUES (2, '商家', 'boss', '0', 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `role` VALUES (3, '管理员', 'admin', '0', 0, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for role_menu
-- ----------------------------
DROP TABLE IF EXISTS `role_menu`;
CREATE TABLE `role_menu`  (
  `role_id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `menu_id` bigint NOT NULL DEFAULT 0 COMMENT '菜单id',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_menu
-- ----------------------------
INSERT INTO `role_menu` VALUES (1, 1);
INSERT INTO `role_menu` VALUES (2, 2);
INSERT INTO `role_menu` VALUES (3, 3);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `uid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `user_id` int NOT NULL AUTO_INCREMENT,
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

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('mjWvAkNqoA6Y5P03sDSzfA', 1, '周爽', '$2a$10$OJQQrcd9TxdfpECPYi3UPeF0MsEiiCrVxOcOdaBnoc7zQTDqE/37G', '2162063785@qq.com', '女', 0, '阮恩杰的心里', 15, 3, 3, 'null', '2024-05-21 00:00:00', 4, 'f16a8dc6-5330-475d-be0');
INSERT INTO `user` VALUES (NULL, 2, '刘国凯', '$2a$10$OJQQrcd9TxdfpECPYi3UPeF0MsEiiCrVxOcOdaBnoc7zQTDqE/37G', '913957723@qq.com', 'male', 0, '贵州大学', 0, 0, 0, 'null', '2024-06-01 00:00:00', 0, '');
INSERT INTO `user` VALUES (NULL, 3, '阮恩ji', '$2a$10$55qZvzBjaeLo6PgUiCBA1.pZrO0i5u251hQLh/tXPpZT4xDOkxD4O', '3104038044@qq.com', 'female', 0, '湖南省三亚市阳明区', 0, 0, 0, 'null', '2024-06-03 21:21:29', 0, NULL);
INSERT INTO `user` VALUES ('6b71b5f7-8800-4442-a85', 20, '周爽爽', '$2a$10$aSl8/sA9B6KANZNg6jQoCOExTp3JmNK39F8zm61VAAkxWhzvCHEMO', '3104038043@qq.com', 'female', 21, '湖南省三亚市阳明区', 0, 0, 0, NULL, '2024-06-04 17:02:21', 0, '');
INSERT INTO `user` VALUES ('f16a8dc6-5330-475d-be0', 21, '阮恩杰', '$2a$10$3ebOvw2NTK.AJt5wLjhomOG82YAOhHeMiY6XDps/2h6TOS3jL3Yim', '3104038045@qq.com', 'female', 0, 'Affton', 0, 0, 0, NULL, '2024-06-07 13:56:32', 0, 'mjWvAkNqoA6Y5P03sDSzfA');

-- ----------------------------
-- Table structure for user_business
-- ----------------------------
DROP TABLE IF EXISTS `user_business`;
CREATE TABLE `user_business`  (
  `user_id` int NOT NULL,
  `business_id` int NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_business
-- ----------------------------
INSERT INTO `user_business` VALUES (2, 1);
INSERT INTO `user_business` VALUES (21, 19);

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `user_id` bigint NOT NULL COMMENT '用户id',
  `role_id` bigint NOT NULL DEFAULT 0 COMMENT '角色id',
  `status` int NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES (1, 1, 0);
INSERT INTO `user_role` VALUES (2, 2, 0);
INSERT INTO `user_role` VALUES (3, 3, 0);
INSERT INTO `user_role` VALUES (20, 1, 0);
INSERT INTO `user_role` VALUES (21, 2, 0);

SET FOREIGN_KEY_CHECKS = 1;
