/*
 Navicat Premium Data Transfer

 Source Server         : hadoop102
 Source Server Type    : MySQL
 Source Server Version : 50716
 Source Host           : hadoop102:3306
 Source Schema         : user_profile_manager

 Target Server Type    : MySQL
 Target Server Version : 50716
 File Encoding         : 65001

 Date: 29/06/2024 15:17:46
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for file_info
-- ----------------------------
DROP TABLE IF EXISTS `file_info`;
CREATE TABLE `file_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `file_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件名',
  `file_ex_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展名',
  `file_path` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件路径',
  `file_system` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件系统',
  `file_status` bigint(20) NULL DEFAULT NULL COMMENT '文件状态 1 正常 2 弃用',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 39 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of file_info
-- ----------------------------
INSERT INTO `file_info` VALUES (1, 'task-sql-1.0-SNAPSHOT-jar-with-dependencies.jar', 'jar', 'hdfs://hadoop102:8020/user_profile/task_customer/jar/cbd5a9c3-c9a9-49b7-9202-84a8f6c6452e/task-sql-1.0-SNAPSHOT-jar-with-dependencies.jar', 'HDFS', 1, '2024-05-08 18:13:34');
INSERT INTO `file_info` VALUES (2, 'TaskTagMergeApp.scala', 'scala', 'hdfs://hadoop102:8020/user_profile/task_customer/jar/7e05565b-db39-4ecd-ad35-90b3e939693f/TaskTagMergeApp.scala', 'HDFS', 1, '2024-05-12 20:46:12');
INSERT INTO `file_info` VALUES (3, 'task-tag-merge-1.0-SNAPSHOT-jar-with-dependencies.jar', 'jar', 'hdfs://hadoop102:8020/user_profile/task_customer/jar/f6a78973-7b0b-475b-b9f2-088ded81b843/task-tag-merge-1.0-SNAPSHOT-jar-with-dependencies.jar', 'HDFS', 1, '2024-05-12 21:07:04');
INSERT INTO `file_info` VALUES (4, 'task-ck-to-bitmap-1.0-SNAPSHOT-jar-with-dependencies.jar', 'jar', 'hdfs://hadoop102:8020/user_profile/task_customer/jar/f28eeecd-975e-4122-9db2-ce58bedd9d01/task-ck-to-bitmap-1.0-SNAPSHOT-jar-with-dependencies.jar', 'HDFS', 1, '2024-05-13 18:36:15');
INSERT INTO `file_info` VALUES (5, 'task-ck-to-bitmap-1.0-SNAPSHOT-jar-with-dependencies.jar', 'jar', 'hdfs://hadoop102:8020/user_profile/task_customer/jar/5b02d72e-2938-4f90-a11b-a418f1fce1de/task-ck-to-bitmap-1.0-SNAPSHOT-jar-with-dependencies.jar', 'HDFS', 1, '2024-05-13 19:27:11');
INSERT INTO `file_info` VALUES (6, 'task-hive-export-ck-1.0-SNAPSHOT-jar-with-dependencies.jar', 'jar', 'hdfs://hadoop102:8020/user_profile/task_customer/jar/a48fc573-273a-49f6-b3ef-d433ca722422/task-hive-export-ck-1.0-SNAPSHOT-jar-with-dependencies.jar', 'HDFS', 1, '2024-05-13 19:31:08');
INSERT INTO `file_info` VALUES (7, 'task-hive-export-ck-1.0-SNAPSHOT-jar-with-dependencies.jar', 'jar', 'hdfs://hadoop102:8020/user_profile/task_customer/jar/25a2ebd4-2daf-45d5-a7ac-68910326ec67/task-hive-export-ck-1.0-SNAPSHOT-jar-with-dependencies.jar', 'HDFS', 1, '2024-05-13 19:45:09');
INSERT INTO `file_info` VALUES (8, 'task-hive-export-ck-1.0-SNAPSHOT-jar-with-dependencies.jar', 'jar', 'hdfs://hadoop102:8020/user_profile/task_customer/jar/eac4c28f-b779-4f99-80db-bcdff0b48514/task-hive-export-ck-1.0-SNAPSHOT-jar-with-dependencies.jar', 'HDFS', 1, '2024-05-13 19:59:23');
INSERT INTO `file_info` VALUES (9, 'task-ck-to-bitmap-1.0-SNAPSHOT-jar-with-dependencies.jar', 'jar', 'hdfs://hadoop102:8020/user_profile/task_customer/jar/ad2eee67-55b9-4e21-8035-4fb0cd693a6e/task-ck-to-bitmap-1.0-SNAPSHOT-jar-with-dependencies.jar', 'HDFS', 1, '2024-05-13 19:59:41');
INSERT INTO `file_info` VALUES (10, 'task-hive-export-ck-1.0-SNAPSHOT-jar-with-dependencies.jar', 'jar', 'hdfs://hadoop102:8020/user_profile/task_customer/jar/e8d68756-3031-4dbf-8a1f-9f3ef5d2e563/task-hive-export-ck-1.0-SNAPSHOT-jar-with-dependencies.jar', 'HDFS', 1, '2024-05-13 20:18:05');
INSERT INTO `file_info` VALUES (11, 'task-ck-to-bitmap-1.0-SNAPSHOT-jar-with-dependencies.jar', 'jar', 'hdfs://hadoop102:8020/user_profile/task_customer/jar/53d42063-9f71-40fe-8d8d-0813816c05ef/task-ck-to-bitmap-1.0-SNAPSHOT-jar-with-dependencies.jar', 'HDFS', 1, '2024-05-13 20:18:22');
INSERT INTO `file_info` VALUES (12, 'task-ck-to-bitmap-1.0-SNAPSHOT-jar-with-dependencies.jar', 'jar', 'hdfs://hadoop102:8020/user_profile/task_customer/jar/c1406369-df64-4db7-a04f-3eec7e5f8069/task-ck-to-bitmap-1.0-SNAPSHOT-jar-with-dependencies.jar', 'HDFS', 1, '2024-05-13 21:13:28');
INSERT INTO `file_info` VALUES (13, 'task-ck-to-bitmap-1.0-SNAPSHOT-jar-with-dependencies.jar', 'jar', 'hdfs://hadoop102:8020/user_profile/task_customer/jar/127394ac-977d-4398-a40c-5c277dac60b2/task-ck-to-bitmap-1.0-SNAPSHOT-jar-with-dependencies.jar', 'HDFS', 1, '2024-05-13 21:32:35');
INSERT INTO `file_info` VALUES (14, 'task-ck-to-bitmap-1.0-SNAPSHOT-jar-with-dependencies.jar', 'jar', 'hdfs://hadoop102:8020/user_profile/task_customer/jar/9210b9ee-a671-4b61-9af0-baf3bbe81956/task-ck-to-bitmap-1.0-SNAPSHOT-jar-with-dependencies.jar', 'HDFS', 1, '2024-05-13 21:44:22');
INSERT INTO `file_info` VALUES (15, 'task-sql-1.1-SNAPSHOT-jar-with-dependencies.jar', 'jar', 'hdfs://hadoop102:8020/user_profile/task_customer/jar/979698de-24b0-4759-aa2d-3d4c70a02975/task-sql-1.1-SNAPSHOT-jar-with-dependencies.jar', 'HDFS', 1, '2024-06-22 21:23:08');
INSERT INTO `file_info` VALUES (16, 'task-sql-1.1-SNAPSHOT-jar-with-dependencies.jar', 'jar', 'hdfs://hadoop102:8020/user_profile/task_customer/jar/94e52758-4be3-490a-9085-d5838e965b43/task-sql-1.1-SNAPSHOT-jar-with-dependencies.jar', 'HDFS', 1, '2024-06-22 21:25:31');
INSERT INTO `file_info` VALUES (17, 'task-sql-1.1-SNAPSHOT-jar-with-dependencies.jar', 'jar', 'hdfs://hadoop102:8020/user_profile/task_customer/jar/f45100d5-7eb7-48f0-95b2-f4f4f20619a9/task-sql-1.1-SNAPSHOT-jar-with-dependencies.jar', 'HDFS', 1, '2024-06-25 19:41:51');
INSERT INTO `file_info` VALUES (18, 'task-sql-1.1-SNAPSHOT-jar-with-dependencies.jar', 'jar', 'hdfs://hadoop102:8020/user_profile/task_customer/jar/e69f4b64-8944-42a2-8180-303c36bbd040/task-sql-1.1-SNAPSHOT-jar-with-dependencies.jar', 'HDFS', 1, '2024-06-25 20:10:27');
INSERT INTO `file_info` VALUES (19, 'task-sql-1.1-SNAPSHOT-jar-with-dependencies.jar', 'jar', 'hdfs://hadoop102:8020/user_profile/task_customer/jar/e79e1a1a-5e88-4ca1-811a-f8289d6fcfeb/task-sql-1.1-SNAPSHOT-jar-with-dependencies.jar', 'HDFS', 1, '2024-06-25 20:17:45');
INSERT INTO `file_info` VALUES (20, 'task-sql-1.1-SNAPSHOT-jar-with-dependencies.jar', 'jar', 'hdfs://hadoop102:8020/user_profile/task_customer/jar/7adab6a3-e7fc-44fb-9cb5-10f354c453e7/task-sql-1.1-SNAPSHOT-jar-with-dependencies.jar', 'HDFS', 1, '2024-06-25 20:51:57');
INSERT INTO `file_info` VALUES (21, 'task-tag-merge-1.0-SNAPSHOT-jar-with-dependencies.jar', 'jar', 'hdfs://hadoop102:8020/user_profile/task_customer/jar/4f39ff39-1750-4ffc-bdbd-239030336776/task-tag-merge-1.0-SNAPSHOT-jar-with-dependencies.jar', 'HDFS', 1, '2024-06-26 16:29:31');
INSERT INTO `file_info` VALUES (22, 'task-hive-export-ck-1.0-SNAPSHOT-jar-with-dependencies.jar', 'jar', 'hdfs://hadoop102:8020/user_profile/task_customer/jar/314bf085-fa67-483f-9934-c465bd918b29/task-hive-export-ck-1.0-SNAPSHOT-jar-with-dependencies.jar', 'HDFS', 1, '2024-06-26 17:11:05');
INSERT INTO `file_info` VALUES (23, 'task-ck-to-bitmap-1.0-SNAPSHOT-jar-with-dependencies.jar', 'jar', 'hdfs://hadoop102:8020/user_profile/task_customer/jar/b8932adf-a679-4d63-8473-1fdcc7684086/task-ck-to-bitmap-1.0-SNAPSHOT-jar-with-dependencies.jar', 'HDFS', 1, '2024-06-26 21:06:10');
INSERT INTO `file_info` VALUES (24, 'task-ck-to-bitmap-1.0-SNAPSHOT-jar-with-dependencies.jar', 'jar', 'hdfs://hadoop102:8020/user_profile/task_customer/jar/d806133f-36c6-4a64-97a9-8969b09bb40f/task-ck-to-bitmap-1.0-SNAPSHOT-jar-with-dependencies.jar', 'HDFS', 1, '2024-06-26 21:25:41');
INSERT INTO `file_info` VALUES (25, 'task-spark-logistic-1.0-SNAPSHOT-jar-with-dependencies.jar', 'jar', 'hdfs://hadoop102:8020/user_profile/task_customer/jar/865d2f81-2f42-4c32-b588-3e5e5c72ec46/task-spark-logistic-1.0-SNAPSHOT-jar-with-dependencies.jar', 'HDFS', 1, '2024-06-28 15:22:31');
INSERT INTO `file_info` VALUES (26, 'task-spark-logistic-1.0-SNAPSHOT-jar-with-dependencies.jar', 'jar', 'hdfs://hadoop102:8020/user_profile/task_customer/jar/8d2fd952-8135-4b84-8008-cb7fabf528fa/task-spark-logistic-1.0-SNAPSHOT-jar-with-dependencies.jar', 'HDFS', 1, '2024-06-28 15:24:10');
INSERT INTO `file_info` VALUES (27, 'task-spark-logistic-1.0-SNAPSHOT-jar-with-dependencies.jar', 'jar', 'hdfs://hadoop102:8020/user_profile/task_customer/jar/e501e83a-28c8-4209-9018-f55b13c1adc2/task-spark-logistic-1.0-SNAPSHOT-jar-with-dependencies.jar', 'HDFS', 1, '2024-06-28 20:35:46');
INSERT INTO `file_info` VALUES (28, 'task-spark-logistic-1.0-SNAPSHOT-jar-with-dependencies.jar', 'jar', 'hdfs://hadoop102:8020/user_profile/task_customer/jar/c9dd4960-a7f0-40f5-a6af-0bf5f6d812dc/task-spark-logistic-1.0-SNAPSHOT-jar-with-dependencies.jar', 'HDFS', 1, '2024-06-28 20:36:01');
INSERT INTO `file_info` VALUES (29, 'task-spark-logistic-1.0-SNAPSHOT-jar-with-dependencies.jar', 'jar', 'hdfs://hadoop102:8020/user_profile/task_customer/jar/65bae0e6-bb62-405e-ad6b-ca861f4ce3e9/task-spark-logistic-1.0-SNAPSHOT-jar-with-dependencies.jar', 'HDFS', 1, '2024-06-28 20:41:09');
INSERT INTO `file_info` VALUES (30, 'task-spark-logistic-1.0-SNAPSHOT-jar-with-dependencies.jar', 'jar', 'hdfs://hadoop102:8020/user_profile/task_customer/jar/b4e782e2-1971-4b86-9f1a-fe60500029b1/task-spark-logistic-1.0-SNAPSHOT-jar-with-dependencies.jar', 'HDFS', 1, '2024-06-28 20:41:21');
INSERT INTO `file_info` VALUES (31, 'task-spark-logistic-1.0-SNAPSHOT-jar-with-dependencies.jar', 'jar', 'hdfs://hadoop102:8020/user_profile/task_customer/jar/4d635065-188d-4336-a8f2-26e7d5116902/task-spark-logistic-1.0-SNAPSHOT-jar-with-dependencies.jar', 'HDFS', 1, '2024-06-28 22:15:12');
INSERT INTO `file_info` VALUES (32, 'task-spark-logistic-1.0-SNAPSHOT-jar-with-dependencies.jar', 'jar', 'hdfs://hadoop102:8020/user_profile/task_customer/jar/a8606a19-3102-4bdb-bf33-cb2f0a4e177d/task-spark-logistic-1.0-SNAPSHOT-jar-with-dependencies.jar', 'HDFS', 1, '2024-06-28 22:15:26');
INSERT INTO `file_info` VALUES (33, 'task-tag-merge-1.0-SNAPSHOT-jar-with-dependencies.jar', 'jar', 'hdfs://hadoop102:8020/user_profile/task_customer/jar/90a00a1a-fb0f-4b03-8fa5-6b9786d72e4f/task-tag-merge-1.0-SNAPSHOT-jar-with-dependencies.jar', 'HDFS', 1, '2024-06-28 22:49:53');
INSERT INTO `file_info` VALUES (34, 'task-tag-merge-1.0-SNAPSHOT-jar-with-dependencies.jar', 'jar', 'hdfs://hadoop102:8020/user_profile/task_customer/jar/8838a8bf-50e8-4552-b539-ffd73196366a/task-tag-merge-1.0-SNAPSHOT-jar-with-dependencies.jar', 'HDFS', 1, '2024-06-29 11:28:00');
INSERT INTO `file_info` VALUES (35, 'task-spark-logistic-1.0-SNAPSHOT-jar-with-dependencies.jar', 'jar', 'hdfs://hadoop102:8020/user_profile/task_customer/jar/33f676b0-18b6-4ade-b93e-4fd10ceeb5ca/task-spark-logistic-1.0-SNAPSHOT-jar-with-dependencies.jar', 'HDFS', 1, '2024-06-29 12:20:13');
INSERT INTO `file_info` VALUES (36, 'task-spark-logistic-1.0-SNAPSHOT-jar-with-dependencies.jar', 'jar', 'hdfs://hadoop102:8020/user_profile/task_customer/jar/6677b4ff-8256-4b2d-a1c0-88e596db4392/task-spark-logistic-1.0-SNAPSHOT-jar-with-dependencies.jar', 'HDFS', 1, '2024-06-29 12:21:12');
INSERT INTO `file_info` VALUES (37, 'task-spark-logistic-1.0-SNAPSHOT-jar-with-dependencies.jar', 'jar', 'hdfs://hadoop102:8020/user_profile/task_customer/jar/bb32a1d1-e473-445f-99f7-834ede3d1c42/task-spark-logistic-1.0-SNAPSHOT-jar-with-dependencies.jar', 'HDFS', 1, '2024-06-29 12:53:42');
INSERT INTO `file_info` VALUES (38, 'task-spark-logistic-1.0-SNAPSHOT-jar-with-dependencies.jar', 'jar', 'hdfs://hadoop102:8020/user_profile/task_customer/jar/f60feff0-bceb-4641-8864-df952c57aaef/task-spark-logistic-1.0-SNAPSHOT-jar-with-dependencies.jar', 'HDFS', 1, '2024-06-29 12:54:16');

-- ----------------------------
-- Table structure for tag_common_task
-- ----------------------------
DROP TABLE IF EXISTS `tag_common_task`;
CREATE TABLE `tag_common_task`  (
  `id` bigint(20) NOT NULL,
  `task_file_id` bigint(20) NULL DEFAULT NULL,
  `main_class` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tag_common_task
-- ----------------------------
INSERT INTO `tag_common_task` VALUES (1, 20, 'com.hkbigdata.userprofile.app.TaskTagSqlApp', '2024-06-25 20:51:59');

-- ----------------------------
-- Table structure for tag_info
-- ----------------------------
DROP TABLE IF EXISTS `tag_info`;
CREATE TABLE `tag_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tag_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `tag_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `tag_level` bigint(20) NULL DEFAULT NULL,
  `parent_tag_id` bigint(20) NULL DEFAULT NULL,
  `tag_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `tag_value_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '1 整数 2 浮点 3 文本 4 日期',
  `tag_value_limit` decimal(16, 2) NULL DEFAULT NULL COMMENT '数值预估上限 数字型填写',
  `tag_value_step` bigint(20) NULL DEFAULT NULL COMMENT '1,10,100,1000',
  `tag_task_id` bigint(20) NULL DEFAULT NULL,
  `tag_comment` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tag_level_id`(`tag_level`, `id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 58 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tag_info
-- ----------------------------
INSERT INTO `tag_info` VALUES (2, 'TG_PERSON', '人口属性', 1, NULL, '99', '', NULL, NULL, NULL, NULL, NULL, '2024-05-08 15:28:30');
INSERT INTO `tag_info` VALUES (3, 'TG_PERSON_BASE', '自然属性', 2, 2, '99', '', NULL, NULL, NULL, NULL, NULL, '2024-05-08 15:29:10');
INSERT INTO `tag_info` VALUES (4, 'TG_PERSON_BASE_GENDER', '性别', 3, 3, '1', '3', NULL, NULL, 1, NULL, NULL, '2024-05-08 15:29:56');
INSERT INTO `tag_info` VALUES (5, 'TG_PERSON_BASE_AGEGROUP', '年龄段', 3, 3, '1', '3', NULL, NULL, 3, NULL, NULL, '2024-05-08 15:33:44');
INSERT INTO `tag_info` VALUES (6, 'PERSON_BASE_GENDER_MAN', '男', 4, 4, '1', '3', NULL, NULL, NULL, NULL, NULL, '2024-05-08 15:34:59');
INSERT INTO `tag_info` VALUES (7, 'N_BASE_GENDER_WOMAN', '女', 4, 4, '1', '3', NULL, NULL, NULL, NULL, NULL, '2024-05-08 15:35:35');
INSERT INTO `tag_info` VALUES (8, 'N_BASE_GENDER_UNKONW', '未知', 4, 4, '1', '3', NULL, NULL, NULL, NULL, NULL, '2024-05-08 15:36:05');
INSERT INTO `tag_info` VALUES (9, 'TG_PERSON_BASE_AGEGROUP_50', '50后', 4, 5, '1', '3', NULL, NULL, NULL, NULL, NULL, '2024-05-08 15:36:30');
INSERT INTO `tag_info` VALUES (10, 'TG_PERSON_BASE_AGEGROUP_60', '60后', 4, 5, '1', '3', NULL, NULL, NULL, NULL, NULL, '2024-05-08 18:49:48');
INSERT INTO `tag_info` VALUES (11, 'TG_PERSON_BASE_AGEGROUP_70', '70后', 4, 5, '1', '3', NULL, NULL, NULL, NULL, NULL, '2024-05-08 18:50:01');
INSERT INTO `tag_info` VALUES (12, 'TG_PERSON_BASE_AGEGROUP_80', '80后', 4, 5, '1', '3', NULL, NULL, NULL, NULL, NULL, '2024-05-08 18:50:13');
INSERT INTO `tag_info` VALUES (13, 'TG_PERSON_BASE_AGEGROUP_90', '90后', 4, 5, '1', '3', NULL, NULL, NULL, NULL, NULL, '2024-05-08 18:50:30');
INSERT INTO `tag_info` VALUES (14, 'TG_PERSON_BASE_AGEGROUP_00', '00后', 4, 5, '1', '3', NULL, NULL, NULL, NULL, NULL, '2024-05-08 18:50:40');
INSERT INTO `tag_info` VALUES (15, 'BUSINESS_TAG', '商店属性', 1, NULL, '99', NULL, NULL, NULL, NULL, NULL, NULL, '2024-06-22 21:13:31');
INSERT INTO `tag_info` VALUES (16, 'BUSINESS_TAG_BASE', '自然属性', 2, 15, '99', '', NULL, NULL, NULL, NULL, NULL, '2024-06-22 21:14:54');
INSERT INTO `tag_info` VALUES (17, 'BUSINESS_GOOD_FOR_KIDS_TAG', 'good_for_kids', 3, 16, '1', '3', NULL, NULL, 8, NULL, NULL, '2024-06-22 21:17:08');
INSERT INTO `tag_info` VALUES (18, 'BUSINESS_GOOD_FOR_KIDS', '是', 4, 17, '1', '3', NULL, NULL, NULL, NULL, NULL, '2024-06-22 21:18:12');
INSERT INTO `tag_info` VALUES (19, 'BUSINESS_NOT_GOOD_FOR_KIDS', '否', 4, 17, '1', '3', NULL, NULL, NULL, NULL, NULL, '2024-06-22 21:18:39');
INSERT INTO `tag_info` VALUES (20, 'BUSINESS_UNKNOW_GOOD_FOR_KIDS', '未知', 4, 17, '1', '3', NULL, NULL, NULL, NULL, NULL, '2024-06-22 21:19:11');
INSERT INTO `tag_info` VALUES (22, 'TG_PERSON_BASE_AGEGROUP_OTHER', '其他', 4, 5, '1', '3', NULL, NULL, NULL, NULL, NULL, '2024-06-26 16:02:21');
INSERT INTO `tag_info` VALUES (26, 'TG_PERSON_BASE_FRIENDS', '社交', 3, 3, '1', '3', NULL, NULL, NULL, NULL, NULL, '2024-06-28 15:27:52');
INSERT INTO `tag_info` VALUES (27, 'TG_PERSON_BASE_FRIENDS_NUM', '好友数', 4, 26, '1', '1', NULL, NULL, 11, NULL, NULL, '2024-06-28 15:29:25');
INSERT INTO `tag_info` VALUES (28, 'TG_PERSON_BASE_POPULAR', '网红', 3, 3, '1', '3', NULL, NULL, 12, NULL, NULL, '2024-06-28 16:12:54');
INSERT INTO `tag_info` VALUES (29, 'TG_PERSON_BASE_POPULAR_YES', '是', 4, 28, '1', '3', NULL, NULL, NULL, NULL, NULL, '2024-06-28 16:13:22');
INSERT INTO `tag_info` VALUES (30, 'TG_PERSON_BASE_POPULAR_NO', '否', 4, 28, '1', '3', NULL, NULL, NULL, NULL, NULL, '2024-06-28 16:13:39');
INSERT INTO `tag_info` VALUES (31, 'TG_PERSON_BASE_TYPE', '类型', 3, 3, '1', '3', NULL, NULL, 13, NULL, NULL, '2024-06-28 16:20:28');
INSERT INTO `tag_info` VALUES (32, 'TG_PERSON_BASE_TYPE_USEFUL', '科普博主', 4, 31, '1', '3', NULL, NULL, NULL, NULL, NULL, '2024-06-28 16:21:04');
INSERT INTO `tag_info` VALUES (33, 'TG_PERSON_BASE_TYPE_COOL', '个性博主', 4, 31, '1', '3', NULL, NULL, NULL, NULL, NULL, '2024-06-28 16:22:03');
INSERT INTO `tag_info` VALUES (34, 'TG_PERSON_BASE_TYPE_FUNNY', '搞笑博主', 4, 31, '1', '3', NULL, NULL, NULL, NULL, NULL, '2024-06-28 16:22:41');
INSERT INTO `tag_info` VALUES (35, 'TG_PERSON_BASE_ELITE', '优质用户', 3, 3, '1', '1', NULL, NULL, NULL, NULL, NULL, '2024-06-28 16:27:39');
INSERT INTO `tag_info` VALUES (36, 'TG_PERSON_BASE_ELITE_NUM', '优质用户次数', 4, 35, '1', '1', NULL, NULL, 14, NULL, NULL, '2024-06-28 16:28:04');
INSERT INTO `tag_info` VALUES (37, 'BUSINESS_TAG_BASE_GOOD_FOR_CREDIT', 'good_for_credit', 3, 16, '1', '3', NULL, NULL, 15, NULL, NULL, '2024-06-28 16:32:31');
INSERT INTO `tag_info` VALUES (38, 'BUSINESS_TAG_BASE_IS_GOOD_FOR_CREDIT', '是', 4, 37, '1', '3', NULL, NULL, NULL, NULL, NULL, '2024-06-28 16:32:54');
INSERT INTO `tag_info` VALUES (39, 'BUSINESS_TAG_BASE_IS_NOT_GOOD_FOR_CREDIT', '否', 4, 37, '1', '3', NULL, NULL, NULL, NULL, NULL, '2024-06-28 16:33:18');
INSERT INTO `tag_info` VALUES (40, 'BUSINESS_TAG_BASE_OPEN', '营业', 3, 16, '1', '3', NULL, NULL, 16, NULL, NULL, '2024-06-28 16:37:58');
INSERT INTO `tag_info` VALUES (41, 'BUSINESS_TAG_BASE_IS_OPEN', '是', 4, 40, '1', '3', NULL, NULL, NULL, NULL, NULL, '2024-06-28 16:38:13');
INSERT INTO `tag_info` VALUES (42, 'BUSINESS_TAG_BASE_IS_NOT_OPEN', '否', 4, 40, '1', '3', NULL, NULL, NULL, NULL, NULL, '2024-06-28 16:38:33');
INSERT INTO `tag_info` VALUES (43, 'BUSINESS_TAG_BASE_STARS', '评分', 3, 16, '1', '3', NULL, NULL, 17, NULL, NULL, '2024-06-28 16:40:04');
INSERT INTO `tag_info` VALUES (44, 'BUSINESS_TAG_BASE_STARS_BETTER', '优质商户', 4, 43, '1', '3', NULL, NULL, NULL, NULL, NULL, '2024-06-28 16:40:28');
INSERT INTO `tag_info` VALUES (45, 'BUSINESS_TAG_BASE_STARS_GOOD', '良好商户', 4, 43, '1', '3', NULL, NULL, NULL, NULL, NULL, '2024-06-28 16:40:46');
INSERT INTO `tag_info` VALUES (46, 'BUSINESS_TAG_BASE_STARS_MEDIA', '中等商户', 4, 43, '1', '3', NULL, NULL, NULL, NULL, NULL, '2024-06-28 16:41:08');
INSERT INTO `tag_info` VALUES (47, 'BUSINESS_TAG_BASE_STARS_WORSE', '劣质商户', 4, 43, '1', '3', NULL, NULL, NULL, NULL, NULL, '2024-06-28 16:41:37');
INSERT INTO `tag_info` VALUES (48, 'BUSINESS_TAG_BASE_REVIEW_GOODNUM', '好评数量', 3, 16, '3', '1', NULL, NULL, 19, NULL, NULL, '2024-06-29 12:15:46');
INSERT INTO `tag_info` VALUES (49, 'BUSINESS_TAG_BASE_REVIEW_GOODNUM_10', '10', 4, 48, '1', '1', NULL, NULL, NULL, NULL, NULL, '2024-06-29 12:16:45');
INSERT INTO `tag_info` VALUES (50, 'BUSINESS_TAG_BASE_REVIEW_GOODNUM_50', '50', 4, 48, '1', '1', NULL, NULL, NULL, NULL, NULL, '2024-06-29 12:17:20');
INSERT INTO `tag_info` VALUES (51, 'BUSINESS_TAG_BASE_REVIEW_GOODNUM_100', '100', 4, 48, '1', '1', NULL, NULL, NULL, NULL, NULL, '2024-06-29 12:17:41');
INSERT INTO `tag_info` VALUES (52, 'BUSINESS_TAG_BASE_REVIEW_GOODNUM_1000', '1000', 4, 48, '1', '1', NULL, NULL, NULL, NULL, NULL, '2024-06-29 12:17:56');
INSERT INTO `tag_info` VALUES (53, 'BUSINESS_TAG_BASE_REVIEW_BADNUM', '差评数量', 3, 16, '3', '1', NULL, NULL, 18, NULL, NULL, '2024-06-29 12:18:36');
INSERT INTO `tag_info` VALUES (54, 'BUSINESS_TAG_BASE_REVIEW_BADNUM_10', '10', 4, 53, '1', '1', NULL, NULL, NULL, NULL, NULL, '2024-06-29 12:18:55');
INSERT INTO `tag_info` VALUES (55, 'BUSINESS_TAG_BASE_REVIEW_BADNUM_50', '50', 4, 53, '1', '1', NULL, NULL, NULL, NULL, NULL, '2024-06-29 12:19:10');
INSERT INTO `tag_info` VALUES (56, 'BUSINESS_TAG_BASE_REVIEW_BADNUM_100', '100', 4, 53, '1', '1', NULL, NULL, NULL, NULL, NULL, '2024-06-29 12:19:23');
INSERT INTO `tag_info` VALUES (57, 'BUSINESS_TAG_BASE_REVIEW_BADNUM_1000', '1000', 4, 53, '1', '1', NULL, NULL, NULL, NULL, NULL, '2024-06-29 12:19:33');

-- ----------------------------
-- Table structure for task_info
-- ----------------------------
DROP TABLE IF EXISTS `task_info`;
CREATE TABLE `task_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `task_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务名称',
  `task_status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务状态',
  `task_comment` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务说明',
  `task_time` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务作业时间(小时分)',
  `task_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务类型(标签,流程)',
  `exec_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '执行方式(jar,sparksql)',
  `main_class` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '启动执行的主类',
  `file_id` bigint(200) NULL DEFAULT NULL COMMENT '程序jar文件id',
  `task_args` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '启动任务的参数',
  `task_sql` varchar(5000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '启动的执行的sql',
  `task_exec_level` bigint(20) NULL DEFAULT NULL COMMENT '执行层级',
  `create_time` date NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_task_time`(`task_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of task_info
-- ----------------------------
INSERT INTO `task_info` VALUES (1, '标签计算:性别', '1', 'user_profile user user_id', '00:00', 'TAG', 'SQL', NULL, NULL, '', 'select user_id, gender as query_value from user', 100, NULL);
INSERT INTO `task_info` VALUES (3, '年龄段', '1', 'user_profile user user_id', '02:40', 'TAG', 'SQL', NULL, NULL, '', 'select user_id, floor(((2024 - age) % 100) / 10) as query_value from user', 100, NULL);
INSERT INTO `task_info` VALUES (4, '合并标签宽表', '1', '合并标签宽表', '01:00', 'FLOW', 'JAR', 'com.hkbigdata.userprofile.app.TaskTagMergeApp', 34, '', NULL, 200, NULL);
INSERT INTO `task_info` VALUES (6, 'hive-to-ck', '1', 'hive-to-ck', '01:00', 'FLOW', 'JAR', 'com.hkbigdata.app.TaskHiveExportCk', 22, '', NULL, 300, NULL);
INSERT INTO `task_info` VALUES (7, 'ck-to-bitmap', '1', 'ck-to-bitmap', '01:00', 'FLOW', 'JAR', 'com.hkbigdata.userprofile.app.TaskCkToBitmap', 24, '', NULL, 400, NULL);
INSERT INTO `task_info` VALUES (8, '标签计算:good_for_kids', '1', 'business_profile business business_id', '00:00', 'TAG', 'SQL', NULL, NULL, '', 'select business_id, good_for_kids as query_value from business', 100, NULL);
INSERT INTO `task_info` VALUES (9, '标签计算:好评数', '2', 'business_profile business business_id', '00:00', 'TAG', 'JAR', 'com.ywhu.business_review_goodNum', 31, '', 'select user_id, gender as query_value from user', 150, NULL);
INSERT INTO `task_info` VALUES (10, '标签计算:差评数', '2', 'business_profile business business_id', '00:00', 'TAG', 'JAR', 'com.ywhu.business_review_badNum', 32, '', 'select user_id, gender as query_value from user', 175, NULL);
INSERT INTO `task_info` VALUES (11, '标签计算:好友数', '1', 'user_profile user user_id', '02:40', 'TAG', 'SQL', NULL, NULL, '', 'select user_id, size(split(friends, \',\')) as query_value from user', 100, NULL);
INSERT INTO `task_info` VALUES (12, '标签计算:网红', '1', 'user_profile user user_id', '00:00', 'TAG', 'SQL', 'com.ywhu.business_review_badNum', NULL, '', 'select user_id, CASE WHEN fans>100 then 1 ELSE 0 END as query_value from user', 100, NULL);
INSERT INTO `task_info` VALUES (13, '标签计算:类型', '1', 'user_profile user user_id', '00:00', 'TAG', 'SQL', 'com.ywhu.business_review_badNum', NULL, '', 'select user_id, CASE WHEN useful>=cool and useful>=funny then 1 WHEN funny>=cool and funny>=useful then 2 ELSE 3 END as query_value from user', 100, NULL);
INSERT INTO `task_info` VALUES (14, '标签计算:优质用户次数', '1', 'user_profile user user_id', '00:00', 'TAG', 'SQL', 'com.ywhu.business_review_badNum', NULL, '', 'select user_id, size(split(elite, \',\')) as query_value from user', 100, NULL);
INSERT INTO `task_info` VALUES (15, '标签计算:good_for_credit', '1', 'business_profile business business_id', '00:00', 'TAG', 'SQL', 'com.ywhu.business_review_badNum', NULL, '', 'select business_id, business_accepts_creditcards as query_value from business', 100, NULL);
INSERT INTO `task_info` VALUES (16, '标签计算:营业', '1', 'business_profile business business_id', '00:00', 'TAG', 'SQL', NULL, NULL, '', 'select business_id, is_open as query_value from business', 100, NULL);
INSERT INTO `task_info` VALUES (17, '标签计算:评分', '1', 'business_profile business business_id', '00:00', 'TAG', 'SQL', NULL, NULL, '', 'select business_id,case when stars>=4 then 1 when stars>=3 and stars<4 then 2 when stars>=2 and stars<3 then 3 else 4 end as query_value from business', 100, NULL);
INSERT INTO `task_info` VALUES (18, '标签计算:差评数量', '1', 'business_profile business business_id', '02:40', 'TAG', 'JAR', 'com.ywhu.business_review_badNum', 38, '', NULL, 100, NULL);
INSERT INTO `task_info` VALUES (19, '标签计算:好评数量', '1', 'business_profile business business_id', '02:40', 'TAG', 'JAR', 'com.ywhu.business_review_goodNum', 37, '', NULL, 100, NULL);

-- ----------------------------
-- Table structure for task_process
-- ----------------------------
DROP TABLE IF EXISTS `task_process`;
CREATE TABLE `task_process`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `task_id` bigint(20) NULL DEFAULT NULL COMMENT '任务id',
  `task_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务名称',
  `task_exec_time` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务触发时间',
  `task_busi_date` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务执行日期',
  `task_exec_status` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务阶段 TODO ,START,SUBMITTED,RUNNING,FAILED,FINISHED',
  `task_exec_level` bigint(20) NULL DEFAULT NULL COMMENT '任务执行层级',
  `yarn_app_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'yarn的application_id',
  `batch_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '批次id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `start_time` datetime NULL DEFAULT NULL COMMENT '启动时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间(包括完成和失败)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 174 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of task_process
-- ----------------------------
INSERT INTO `task_process` VALUES (7, 1, '标签计算:性别', '00:00', '2024-05-08', 'START', 100, NULL, 'ccad6b06-8541-403a-9b68-c088c8de3b18', '2024-05-08 22:09:02', '2024-05-08 22:09:15', NULL);
INSERT INTO `task_process` VALUES (8, 3, '年龄段', '02:40', '2024-05-08', 'START', 100, NULL, 'ccad6b06-8541-403a-9b68-c088c8de3b18', '2024-05-08 22:09:02', '2024-05-08 22:09:15', NULL);
INSERT INTO `task_process` VALUES (9, 1, '标签计算:性别', '00:00', '2024-05-08', 'SUBMITTED', 100, 'application_1714556470202_0009', '840cf10f-0fe9-45d0-88db-a04dd0a1e9b3', '2024-05-08 22:10:18', '2024-05-08 22:10:30', NULL);
INSERT INTO `task_process` VALUES (10, 3, '年龄段', '02:40', '2024-05-08', 'SUBMITTED', 100, 'application_1714556470202_0008', '840cf10f-0fe9-45d0-88db-a04dd0a1e9b3', '2024-05-08 22:10:18', '2024-05-08 22:10:30', NULL);
INSERT INTO `task_process` VALUES (11, 1, '标签计算:性别', '00:00', '2024-05-08', 'FINISHED', 100, 'application_1715178741568_0001', '896697db-697f-46f4-94e3-b6d90b63c4de', '2024-05-08 22:34:11', '2024-05-08 22:34:15', '2024-05-08 22:36:01');
INSERT INTO `task_process` VALUES (12, 3, '年龄段', '02:40', '2024-05-08', 'FINISHED', 100, 'application_1715178741568_0002', '896697db-697f-46f4-94e3-b6d90b63c4de', '2024-05-08 22:34:11', '2024-05-08 22:34:15', '2024-05-08 22:35:28');
INSERT INTO `task_process` VALUES (13, 1, '标签计算:性别', '00:00', '2024-05-12', 'FINISHED', 100, 'application_1715178741568_0007', 'e9cdd3db-2705-4032-96cf-0d493f1970c7', '2024-05-12 20:49:24', '2024-05-12 20:49:30', '2024-05-12 20:50:47');
INSERT INTO `task_process` VALUES (14, 3, '年龄段', '02:40', '2024-05-12', 'FINISHED', 100, 'application_1715178741568_0008', 'e9cdd3db-2705-4032-96cf-0d493f1970c7', '2024-05-12 20:49:24', '2024-05-12 20:49:30', '2024-05-12 20:51:18');
INSERT INTO `task_process` VALUES (15, 4, '合并标签宽表', '01:00', '2024-05-12', 'FAILED', 200, 'application_1715178741568_0009', 'e9cdd3db-2705-4032-96cf-0d493f1970c7', '2024-05-12 20:49:24', '2024-05-12 20:51:30', '2024-05-12 20:51:50');
INSERT INTO `task_process` VALUES (16, 1, '标签计算:性别', '00:00', '2024-05-11', 'FINISHED', 100, 'application_1715178741568_0010', '5728a376-e30e-42b4-9661-3ba29e5ee95f', '2024-05-12 20:56:37', '2024-05-12 20:56:45', '2024-05-12 20:58:30');
INSERT INTO `task_process` VALUES (17, 3, '年龄段', '02:40', '2024-05-11', 'FINISHED', 100, 'application_1715178741568_0011', '5728a376-e30e-42b4-9661-3ba29e5ee95f', '2024-05-12 20:56:37', '2024-05-12 20:56:45', '2024-05-12 20:58:01');
INSERT INTO `task_process` VALUES (18, 4, '合并标签宽表', '01:00', '2024-05-11', 'FAILED', 200, 'application_1715178741568_0012', '5728a376-e30e-42b4-9661-3ba29e5ee95f', '2024-05-12 20:56:37', '2024-05-12 20:58:30', '2024-05-12 20:58:45');
INSERT INTO `task_process` VALUES (19, 1, '标签计算:性别', '00:00', '2024-05-12', 'FINISHED', 100, 'application_1715178741568_0013', '231c4ef2-2ff3-4e5b-b8c6-52a78cce60ab', '2024-05-12 21:07:14', '2024-05-12 21:07:15', '2024-05-12 21:08:18');
INSERT INTO `task_process` VALUES (20, 3, '年龄段', '02:40', '2024-05-12', 'FINISHED', 100, 'application_1715178741568_0014', '231c4ef2-2ff3-4e5b-b8c6-52a78cce60ab', '2024-05-12 21:07:14', '2024-05-12 21:07:15', '2024-05-12 21:08:45');
INSERT INTO `task_process` VALUES (21, 4, '合并标签宽表', '01:00', '2024-05-12', 'FINISHED', 200, 'application_1715178741568_0015', '231c4ef2-2ff3-4e5b-b8c6-52a78cce60ab', '2024-05-12 21:07:14', '2024-05-12 21:09:00', '2024-05-12 21:10:00');
INSERT INTO `task_process` VALUES (22, 1, '标签计算:性别', '00:00', '2024-05-13', 'FINISHED', 100, 'application_1715585476774_0002', 'd44ab971-bc53-4f5d-ae32-ea66cc9d28de', '2024-05-13 18:37:39', '2024-05-13 18:37:45', '2024-05-13 18:39:04');
INSERT INTO `task_process` VALUES (23, 3, '年龄段', '02:40', '2024-05-13', 'FINISHED', 100, 'application_1715585476774_0001', 'd44ab971-bc53-4f5d-ae32-ea66cc9d28de', '2024-05-13 18:37:39', '2024-05-13 18:37:45', '2024-05-13 18:39:32');
INSERT INTO `task_process` VALUES (24, 4, '合并标签宽表', '01:00', '2024-05-13', 'FINISHED', 200, 'application_1715585476774_0003', 'd44ab971-bc53-4f5d-ae32-ea66cc9d28de', '2024-05-13 18:37:39', '2024-05-13 18:39:45', '2024-05-13 18:40:50');
INSERT INTO `task_process` VALUES (25, 5, 'ckToBitmap', '01:00', '2024-05-13', 'FAILED', 300, 'application_1715585476774_0004', 'd44ab971-bc53-4f5d-ae32-ea66cc9d28de', '2024-05-13 18:37:39', '2024-05-13 18:41:00', '2024-05-13 18:41:55');
INSERT INTO `task_process` VALUES (26, 1, '标签计算:性别', '00:00', '2024-05-13', 'FINISHED', 100, 'application_1715585476774_0006', 'f6fd8aa3-7e8d-412b-9e72-8a1e2054867d', '2024-05-13 19:27:18', '2024-05-13 19:27:30', '2024-05-13 19:28:59');
INSERT INTO `task_process` VALUES (27, 3, '年龄段', '02:40', '2024-05-13', 'FINISHED', 100, 'application_1715585476774_0005', 'f6fd8aa3-7e8d-412b-9e72-8a1e2054867d', '2024-05-13 19:27:18', '2024-05-13 19:27:30', '2024-05-13 19:28:30');
INSERT INTO `task_process` VALUES (28, 4, '合并标签宽表', '01:00', '2024-05-13', 'FINISHED', 200, 'application_1715585476774_0007', 'f6fd8aa3-7e8d-412b-9e72-8a1e2054867d', '2024-05-13 19:27:18', '2024-05-13 19:29:00', '2024-05-13 19:30:07');
INSERT INTO `task_process` VALUES (29, 5, 'ckToBitmap', '01:00', '2024-05-13', 'FAILED', 300, 'application_1715585476774_0008', 'f6fd8aa3-7e8d-412b-9e72-8a1e2054867d', '2024-05-13 19:27:18', '2024-05-13 19:30:15', '2024-05-13 19:31:13');
INSERT INTO `task_process` VALUES (30, 1, '标签计算:性别', '00:00', '2024-05-13', 'FINISHED', 100, 'application_1715585476774_0009', '337794ad-e896-4ec6-b787-37c38288836a', '2024-05-13 19:33:04', '2024-05-13 19:33:15', '2024-05-13 19:34:02');
INSERT INTO `task_process` VALUES (31, 3, '年龄段', '02:40', '2024-05-13', 'FINISHED', 100, 'application_1715585476774_0010', '337794ad-e896-4ec6-b787-37c38288836a', '2024-05-13 19:33:04', '2024-05-13 19:33:15', '2024-05-13 19:34:32');
INSERT INTO `task_process` VALUES (32, 4, '合并标签宽表', '01:00', '2024-05-13', 'FINISHED', 200, 'application_1715585476774_0011', '337794ad-e896-4ec6-b787-37c38288836a', '2024-05-13 19:33:04', '2024-05-13 19:34:45', '2024-05-13 19:35:47');
INSERT INTO `task_process` VALUES (33, 5, 'ckToBitmap', '01:00', '2024-05-13', 'FAILED', 300, 'application_1715585476774_0012', '337794ad-e896-4ec6-b787-37c38288836a', '2024-05-13 19:33:04', '2024-05-13 19:36:00', '2024-05-13 19:36:53');
INSERT INTO `task_process` VALUES (34, 1, '标签计算:性别', '00:00', '2024-05-13', 'FINISHED', 100, 'application_1715585476774_0013', '3bbd7f2e-62b7-4a4f-8181-3a644df2fc60', '2024-05-13 19:37:08', '2024-05-13 19:37:15', '2024-05-13 19:38:00');
INSERT INTO `task_process` VALUES (35, 3, '年龄段', '02:40', '2024-05-13', 'FINISHED', 100, 'application_1715585476774_0014', '3bbd7f2e-62b7-4a4f-8181-3a644df2fc60', '2024-05-13 19:37:08', '2024-05-13 19:37:15', '2024-05-13 19:38:26');
INSERT INTO `task_process` VALUES (36, 4, '合并标签宽表', '01:00', '2024-05-13', 'FINISHED', 200, 'application_1715585476774_0015', '3bbd7f2e-62b7-4a4f-8181-3a644df2fc60', '2024-05-13 19:37:08', '2024-05-13 19:38:30', '2024-05-13 19:39:32');
INSERT INTO `task_process` VALUES (37, 5, 'ckToBitmap', '01:00', '2024-05-13', 'FAILED', 300, 'application_1715585476774_0017', '3bbd7f2e-62b7-4a4f-8181-3a644df2fc60', '2024-05-13 19:37:08', '2024-05-13 19:40:30', '2024-05-13 19:41:22');
INSERT INTO `task_process` VALUES (38, 6, 'hive-to-ck', '01:00', '2024-05-13', 'FAILED', 250, 'application_1715585476774_0016', '3bbd7f2e-62b7-4a4f-8181-3a644df2fc60', '2024-05-13 19:37:08', '2024-05-13 19:39:45', '2024-05-13 19:40:27');
INSERT INTO `task_process` VALUES (39, 1, '标签计算:性别', '00:00', '2024-05-13', 'FINISHED', 100, 'application_1715585476774_0018', '0ac21aaa-f613-47de-b261-cea36ceca38b', '2024-05-13 19:45:15', '2024-05-13 19:45:30', '2024-05-13 19:46:11');
INSERT INTO `task_process` VALUES (40, 3, '年龄段', '02:40', '2024-05-13', 'FINISHED', 100, 'application_1715585476774_0019', '0ac21aaa-f613-47de-b261-cea36ceca38b', '2024-05-13 19:45:15', '2024-05-13 19:45:30', '2024-05-13 19:46:39');
INSERT INTO `task_process` VALUES (41, 4, '合并标签宽表', '01:00', '2024-05-13', 'FINISHED', 200, 'application_1715585476774_0020', '0ac21aaa-f613-47de-b261-cea36ceca38b', '2024-05-13 19:45:15', '2024-05-13 19:46:45', '2024-05-13 19:47:43');
INSERT INTO `task_process` VALUES (42, 5, 'ckToBitmap', '01:00', '2024-05-13', 'FAILED', 300, 'application_1715585476774_0022', '0ac21aaa-f613-47de-b261-cea36ceca38b', '2024-05-13 19:45:15', '2024-05-13 19:48:45', '2024-05-13 19:49:40');
INSERT INTO `task_process` VALUES (43, 6, 'hive-to-ck', '01:00', '2024-05-13', 'FAILED', 250, 'application_1715585476774_0021', '0ac21aaa-f613-47de-b261-cea36ceca38b', '2024-05-13 19:45:15', '2024-05-13 19:47:45', '2024-05-13 19:48:38');
INSERT INTO `task_process` VALUES (44, 1, '标签计算:性别', '00:00', '2024-05-13', 'FINISHED', 100, 'application_1715585476774_0024', '4f0d8f13-416f-4903-a8cd-bbf94e0020aa', '2024-05-13 19:59:48', '2024-05-13 20:00:00', '2024-05-13 20:01:12');
INSERT INTO `task_process` VALUES (45, 3, '年龄段', '02:40', '2024-05-13', 'FINISHED', 100, 'application_1715585476774_0023', '4f0d8f13-416f-4903-a8cd-bbf94e0020aa', '2024-05-13 19:59:48', '2024-05-13 20:00:00', '2024-05-13 20:00:43');
INSERT INTO `task_process` VALUES (46, 4, '合并标签宽表', '01:00', '2024-05-13', 'FINISHED', 200, 'application_1715585476774_0025', '4f0d8f13-416f-4903-a8cd-bbf94e0020aa', '2024-05-13 19:59:48', '2024-05-13 20:01:15', '2024-05-13 20:02:16');
INSERT INTO `task_process` VALUES (47, 5, 'ckToBitmap', '01:00', '2024-05-13', 'FAILED', 300, 'application_1715585476774_0027', '4f0d8f13-416f-4903-a8cd-bbf94e0020aa', '2024-05-13 19:59:48', '2024-05-13 20:03:30', '2024-05-13 20:04:25');
INSERT INTO `task_process` VALUES (48, 6, 'hive-to-ck', '01:00', '2024-05-13', 'FAILED', 250, 'application_1715585476774_0026', '4f0d8f13-416f-4903-a8cd-bbf94e0020aa', '2024-05-13 19:59:48', '2024-05-13 20:02:30', '2024-05-13 20:03:21');
INSERT INTO `task_process` VALUES (49, 1, '标签计算:性别', '00:00', '2024-05-13', 'FINISHED', 100, 'application_1715585476774_0028', '6a15f7b3-2b8e-4722-9b64-1924173b950d', '2024-05-13 20:18:30', '2024-05-13 20:18:45', '2024-05-13 20:19:32');
INSERT INTO `task_process` VALUES (50, 3, '年龄段', '02:40', '2024-05-13', 'FINISHED', 100, 'application_1715585476774_0029', '6a15f7b3-2b8e-4722-9b64-1924173b950d', '2024-05-13 20:18:30', '2024-05-13 20:18:45', '2024-05-13 20:20:01');
INSERT INTO `task_process` VALUES (51, 4, '合并标签宽表', '01:00', '2024-05-13', 'FINISHED', 200, 'application_1715585476774_0030', '6a15f7b3-2b8e-4722-9b64-1924173b950d', '2024-05-13 20:18:30', '2024-05-13 20:20:15', '2024-05-13 20:21:14');
INSERT INTO `task_process` VALUES (52, 5, 'ckToBitmap', '01:00', '2024-05-13', 'FAILED', 300, 'application_1715585476774_0032', '6a15f7b3-2b8e-4722-9b64-1924173b950d', '2024-05-13 20:18:30', '2024-05-13 20:22:00', '2024-05-13 20:22:32');
INSERT INTO `task_process` VALUES (53, 6, 'hive-to-ck', '01:00', '2024-05-13', 'FINISHED', 250, 'application_1715585476774_0031', '6a15f7b3-2b8e-4722-9b64-1924173b950d', '2024-05-13 20:18:30', '2024-05-13 20:21:15', '2024-05-13 20:21:59');
INSERT INTO `task_process` VALUES (54, 1, '标签计算:性别', '00:00', '2024-05-13', 'FINISHED', 100, 'application_1715585476774_0034', '5d745614-c6f5-458a-b077-558ec5e52db6', '2024-05-13 20:41:38', '2024-05-13 20:41:45', '2024-05-13 20:42:30');
INSERT INTO `task_process` VALUES (55, 3, '年龄段', '02:40', '2024-05-13', 'FINISHED', 100, 'application_1715585476774_0033', '5d745614-c6f5-458a-b077-558ec5e52db6', '2024-05-13 20:41:38', '2024-05-13 20:41:45', '2024-05-13 20:42:58');
INSERT INTO `task_process` VALUES (56, 4, '合并标签宽表', '01:00', '2024-05-13', 'FINISHED', 200, 'application_1715585476774_0035', '5d745614-c6f5-458a-b077-558ec5e52db6', '2024-05-13 20:41:38', '2024-05-13 20:43:00', '2024-05-13 20:44:04');
INSERT INTO `task_process` VALUES (57, 5, 'ckToBitmap', '01:00', '2024-05-13', 'FAILED', 300, 'application_1715585476774_0039', '5d745614-c6f5-458a-b077-558ec5e52db6', '2024-05-13 20:41:38', '2024-05-13 21:04:45', '2024-05-13 21:05:19');
INSERT INTO `task_process` VALUES (58, 6, 'hive-to-ck', '01:00', '2024-05-13', 'FINISHED', 250, 'application_1715585476774_0036', '5d745614-c6f5-458a-b077-558ec5e52db6', '2024-05-13 20:41:38', '2024-05-13 20:44:15', '2024-05-13 20:45:03');
INSERT INTO `task_process` VALUES (59, 1, '标签计算:性别', '00:00', '2024-05-11', 'FINISHED', 100, 'application_1715585476774_0041', '9b940588-84bb-4439-96c7-b4f88a99b82b', '2024-05-13 21:06:21', '2024-05-13 21:06:30', '2024-05-13 21:07:12');
INSERT INTO `task_process` VALUES (60, 3, '年龄段', '02:40', '2024-05-11', 'FINISHED', 100, 'application_1715585476774_0040', '9b940588-84bb-4439-96c7-b4f88a99b82b', '2024-05-13 21:06:21', '2024-05-13 21:06:30', '2024-05-13 21:07:38');
INSERT INTO `task_process` VALUES (61, 4, '合并标签宽表', '01:00', '2024-05-11', 'FINISHED', 200, 'application_1715585476774_0042', '9b940588-84bb-4439-96c7-b4f88a99b82b', '2024-05-13 21:06:21', '2024-05-13 21:07:45', '2024-05-13 21:08:45');
INSERT INTO `task_process` VALUES (62, 5, 'ckToBitmap', '01:00', '2024-05-11', 'FAILED', 300, 'application_1715585476774_0044', '9b940588-84bb-4439-96c7-b4f88a99b82b', '2024-05-13 21:06:21', '2024-05-13 21:09:45', '2024-05-13 21:10:17');
INSERT INTO `task_process` VALUES (63, 6, 'hive-to-ck', '01:00', '2024-05-11', 'FINISHED', 250, 'application_1715585476774_0043', '9b940588-84bb-4439-96c7-b4f88a99b82b', '2024-05-13 21:06:21', '2024-05-13 21:08:45', '2024-05-13 21:09:30');
INSERT INTO `task_process` VALUES (64, 1, '标签计算:性别', '00:00', '2024-05-13', 'FINISHED', 100, 'application_1715585476774_0046', '2f274e5b-82d2-463d-a4d0-a41004423d53', '2024-05-13 21:13:37', '2024-05-13 21:13:45', '2024-05-13 21:14:30');
INSERT INTO `task_process` VALUES (65, 3, '年龄段', '02:40', '2024-05-13', 'FINISHED', 100, 'application_1715585476774_0045', '2f274e5b-82d2-463d-a4d0-a41004423d53', '2024-05-13 21:13:37', '2024-05-13 21:13:45', '2024-05-13 21:14:56');
INSERT INTO `task_process` VALUES (66, 4, '合并标签宽表', '01:00', '2024-05-13', 'FINISHED', 200, 'application_1715585476774_0047', '2f274e5b-82d2-463d-a4d0-a41004423d53', '2024-05-13 21:13:37', '2024-05-13 21:15:00', '2024-05-13 21:15:57');
INSERT INTO `task_process` VALUES (68, 6, 'hive-to-ck', '01:00', '2024-05-13', 'FINISHED', 250, 'application_1715585476774_0048', '2f274e5b-82d2-463d-a4d0-a41004423d53', '2024-05-13 21:13:37', '2024-05-13 21:16:00', '2024-05-13 21:16:41');
INSERT INTO `task_process` VALUES (69, 1, '标签计算:性别', '00:00', '2024-05-13', 'FINISHED', 100, 'application_1715585476774_0051', '021394df-b839-4237-9922-e45cb7f99228', '2024-05-13 21:26:06', '2024-05-13 21:26:15', '2024-05-13 21:26:57');
INSERT INTO `task_process` VALUES (70, 3, '年龄段', '02:40', '2024-05-13', 'FINISHED', 100, 'application_1715585476774_0050', '021394df-b839-4237-9922-e45cb7f99228', '2024-05-13 21:26:06', '2024-05-13 21:26:15', '2024-05-13 21:27:25');
INSERT INTO `task_process` VALUES (71, 4, '合并标签宽表', '01:00', '2024-05-13', 'FINISHED', 200, 'application_1715585476774_0052', '021394df-b839-4237-9922-e45cb7f99228', '2024-05-13 21:26:06', '2024-05-13 21:27:30', '2024-05-13 21:28:32');
INSERT INTO `task_process` VALUES (73, 6, 'hive-to-ck', '01:00', '2024-05-13', 'FINISHED', 250, 'application_1715585476774_0053', '021394df-b839-4237-9922-e45cb7f99228', '2024-05-13 21:26:06', '2024-05-13 21:28:45', '2024-05-13 21:29:26');
INSERT INTO `task_process` VALUES (74, 1, '标签计算:性别', '00:00', '2024-05-13', 'FINISHED', 100, 'application_1715585476774_0054', '3cdb6345-c5fc-4f7b-a7d7-07ff6646076a', '2024-05-13 21:33:26', '2024-05-13 21:33:30', '2024-05-13 21:34:38');
INSERT INTO `task_process` VALUES (75, 3, '年龄段', '02:40', '2024-05-13', 'FINISHED', 100, 'application_1715585476774_0055', '3cdb6345-c5fc-4f7b-a7d7-07ff6646076a', '2024-05-13 21:33:26', '2024-05-13 21:33:30', '2024-05-13 21:34:12');
INSERT INTO `task_process` VALUES (76, 4, '合并标签宽表', '01:00', '2024-05-13', 'FINISHED', 200, 'application_1715585476774_0056', '3cdb6345-c5fc-4f7b-a7d7-07ff6646076a', '2024-05-13 21:33:26', '2024-05-13 21:34:45', '2024-05-13 21:35:43');
INSERT INTO `task_process` VALUES (77, 6, 'hive-to-ck', '01:00', '2024-05-13', 'FINISHED', 300, 'application_1715585476774_0057', '3cdb6345-c5fc-4f7b-a7d7-07ff6646076a', '2024-05-13 21:33:26', '2024-05-13 21:35:45', '2024-05-13 21:36:27');
INSERT INTO `task_process` VALUES (78, 7, 'ck-to-bitmap', '01:00', '2024-05-13', 'FINISHED', 400, 'application_1715585476774_0059', '3cdb6345-c5fc-4f7b-a7d7-07ff6646076a', '2024-05-13 21:33:26', '2024-05-13 21:44:45', '2024-05-13 21:45:06');
INSERT INTO `task_process` VALUES (84, 1, '标签计算:性别', '00:00', '2024-06-11', 'FINISHED', 100, 'application_1715585476774_0060', '0d52a595-d02d-47fb-9711-bdd4e8becd87', '2024-06-11 22:49:31', '2024-06-11 22:49:45', '2024-06-11 22:51:11');
INSERT INTO `task_process` VALUES (85, 3, '年龄段', '02:40', '2024-06-11', 'FINISHED', 100, 'application_1715585476774_0061', '0d52a595-d02d-47fb-9711-bdd4e8becd87', '2024-06-11 22:49:31', '2024-06-11 22:49:45', '2024-06-11 22:51:42');
INSERT INTO `task_process` VALUES (86, 4, '合并标签宽表', '01:00', '2024-06-11', 'FINISHED', 200, 'application_1715585476774_0062', '0d52a595-d02d-47fb-9711-bdd4e8becd87', '2024-06-11 22:49:31', '2024-06-11 22:51:45', '2024-06-11 22:52:51');
INSERT INTO `task_process` VALUES (87, 6, 'hive-to-ck', '01:00', '2024-06-11', 'FINISHED', 300, 'application_1715585476774_0063', '0d52a595-d02d-47fb-9711-bdd4e8becd87', '2024-06-11 22:49:31', '2024-06-11 22:53:00', '2024-06-11 22:53:54');
INSERT INTO `task_process` VALUES (88, 7, 'ck-to-bitmap', '01:00', '2024-06-11', 'FINISHED', 400, 'application_1715585476774_0064', '0d52a595-d02d-47fb-9711-bdd4e8becd87', '2024-06-11 22:49:31', '2024-06-11 22:54:00', '2024-06-11 22:54:25');
INSERT INTO `task_process` VALUES (89, 4, '合并标签宽表', '01:00', '2024-06-22', 'FAILED', 200, 'application_1718788401554_0003', '2a22245b-02e6-47db-983f-18efefeb6330', '2024-06-22 21:28:18', '2024-06-22 21:43:15', '2024-06-22 21:43:49');
INSERT INTO `task_process` VALUES (92, 8, '标签计算:good_for_kids', '00:00', '2024-06-22', 'FAILED', 100, 'application_1718788401554_0002', '2a22245b-02e6-47db-983f-18efefeb6330', '2024-06-22 21:28:18', '2024-06-22 21:28:30', '2024-06-22 21:43:14');
INSERT INTO `task_process` VALUES (93, 1, '标签计算:性别', '00:00', '2024-06-25', 'FAILED', 100, 'application_1719306145344_0004', '38b2e969-7633-4960-a684-0ae3801f80b3', '2024-06-25 19:46:55', '2024-06-25 19:47:00', '2024-06-25 19:54:45');
INSERT INTO `task_process` VALUES (94, 8, '标签计算:good_for_kids', '00:00', '2024-06-25', 'FAILED', 100, 'application_1719306145344_0005', '38b2e969-7633-4960-a684-0ae3801f80b3', '2024-06-25 19:46:55', '2024-06-25 19:47:00', '2024-06-25 20:00:38');
INSERT INTO `task_process` VALUES (95, 1, '标签计算:性别', '00:00', '2024-06-25', 'FAILED', 100, 'application_1719306145344_0009', '1e81299e-008f-449f-ad23-c5086c99526f', '2024-06-25 20:10:41', '2024-06-25 20:10:45', '2024-06-25 20:16:30');
INSERT INTO `task_process` VALUES (96, 8, '标签计算:good_for_kids', '00:00', '2024-06-25', 'FAILED', 100, 'application_1719306145344_0010', '1e81299e-008f-449f-ad23-c5086c99526f', '2024-06-25 20:10:41', '2024-06-25 20:10:45', '2024-06-25 20:16:42');
INSERT INTO `task_process` VALUES (97, 1, '标签计算:性别', '00:00', '2024-06-24', 'FAILED', 100, 'application_1719306145344_0012', 'caa53052-eca4-4d85-a28a-2ae8962103c6', '2024-06-25 20:17:56', '2024-06-25 20:18:00', '2024-06-25 20:22:31');
INSERT INTO `task_process` VALUES (98, 8, '标签计算:good_for_kids', '00:00', '2024-06-24', 'FAILED', 100, 'application_1719306145344_0011', 'caa53052-eca4-4d85-a28a-2ae8962103c6', '2024-06-25 20:17:56', '2024-06-25 20:18:00', '2024-06-25 20:22:35');
INSERT INTO `task_process` VALUES (99, 1, '标签计算:性别', '00:00', '2024-06-25', 'FAILED', 100, 'application_1719306145344_0015', 'b2847956-5eed-426a-a986-5bb7b2e65eb5', '2024-06-25 20:43:06', '2024-06-25 20:43:15', '2024-06-25 20:48:04');
INSERT INTO `task_process` VALUES (100, 8, '标签计算:good_for_kids', '00:00', '2024-06-25', 'FAILED', 100, 'application_1719306145344_0016', 'b2847956-5eed-426a-a986-5bb7b2e65eb5', '2024-06-25 20:43:06', '2024-06-25 20:43:15', '2024-06-25 20:48:08');
INSERT INTO `task_process` VALUES (101, 1, '标签计算:性别', '00:00', '2024-06-25', 'FAILED', 100, 'application_1719306145344_0017', 'e6e5f481-8b51-4af2-83f3-6812a0e87e05', '2024-06-25 20:48:26', '2024-06-25 20:48:30', '2024-06-25 20:51:16');
INSERT INTO `task_process` VALUES (102, 8, '标签计算:good_for_kids', '00:00', '2024-06-25', 'FAILED', 100, 'application_1719306145344_0018', 'e6e5f481-8b51-4af2-83f3-6812a0e87e05', '2024-06-25 20:48:26', '2024-06-25 20:48:30', '2024-06-25 20:51:20');
INSERT INTO `task_process` VALUES (103, 1, '标签计算:性别', '00:00', '2024-06-25', 'FAILED', 100, 'application_1719306145344_0020', 'efee3097-6b02-44ea-97aa-fea3beb21dcc', '2024-06-25 21:14:08', '2024-06-25 21:14:15', '2024-06-25 21:17:38');
INSERT INTO `task_process` VALUES (104, 8, '标签计算:good_for_kids', '00:00', '2024-06-25', 'FAILED', 100, 'application_1719306145344_0019', 'efee3097-6b02-44ea-97aa-fea3beb21dcc', '2024-06-25 21:14:08', '2024-06-25 21:14:15', '2024-06-25 21:17:33');
INSERT INTO `task_process` VALUES (105, 1, '标签计算:性别', '00:00', '2024-06-25', 'FAILED', 100, 'application_1719306145344_0022', '0285b040-5c09-44dc-84d7-bb9a626234b4', '2024-06-25 21:24:46', '2024-06-25 21:25:00', '2024-06-25 21:28:55');
INSERT INTO `task_process` VALUES (106, 8, '标签计算:good_for_kids', '00:00', '2024-06-25', 'FAILED', 100, 'application_1719306145344_0021', '0285b040-5c09-44dc-84d7-bb9a626234b4', '2024-06-25 21:24:46', '2024-06-25 21:25:00', '2024-06-25 21:28:51');
INSERT INTO `task_process` VALUES (107, 1, '标签计算:性别', '00:00', '2024-06-25', 'FINISHED', 100, 'application_1719306145344_0023', 'e94ceb72-6fb9-46dc-959c-b7c78fa910df', '2024-06-25 21:31:43', '2024-06-25 21:31:45', '2024-06-25 21:33:30');
INSERT INTO `task_process` VALUES (108, 8, '标签计算:good_for_kids', '00:00', '2024-06-25', 'FINISHED', 100, 'application_1719306145344_0024', 'e94ceb72-6fb9-46dc-959c-b7c78fa910df', '2024-06-25 21:31:43', '2024-06-25 21:31:45', '2024-06-25 21:34:00');
INSERT INTO `task_process` VALUES (109, 1, '标签计算:性别', '00:00', '2024-06-26', 'FINISHED', 100, 'application_1719306145344_0029', 'ab1eb8e4-5b77-4add-a3d1-855080e8e9c2', '2024-06-26 16:03:44', '2024-06-26 16:03:45', '2024-06-26 16:05:31');
INSERT INTO `task_process` VALUES (110, 3, '年龄段', '02:40', '2024-06-26', 'FINISHED', 100, 'application_1719306145344_0027', 'ab1eb8e4-5b77-4add-a3d1-855080e8e9c2', '2024-06-26 16:03:44', '2024-06-26 16:03:45', '2024-06-26 16:06:28');
INSERT INTO `task_process` VALUES (111, 8, '标签计算:good_for_kids', '00:00', '2024-06-26', 'FINISHED', 100, 'application_1719306145344_0028', 'ab1eb8e4-5b77-4add-a3d1-855080e8e9c2', '2024-06-26 16:03:44', '2024-06-26 16:03:45', '2024-06-26 16:07:04');
INSERT INTO `task_process` VALUES (112, 1, '标签计算:性别', '00:00', '2024-06-26', 'FINISHED', 100, 'application_1719306145344_0031', '16eb3cc5-eb4b-4937-a636-772d5520cccd', '2024-06-26 16:29:59', '2024-06-26 16:30:00', '2024-06-26 16:32:52');
INSERT INTO `task_process` VALUES (113, 3, '年龄段', '02:40', '2024-06-26', 'FINISHED', 100, 'application_1719306145344_0032', '16eb3cc5-eb4b-4937-a636-772d5520cccd', '2024-06-26 16:29:59', '2024-06-26 16:30:00', '2024-06-26 16:31:26');
INSERT INTO `task_process` VALUES (114, 4, '合并标签宽表', '01:00', '2024-06-26', 'FINISHED', 200, 'application_1719306145344_0033', '16eb3cc5-eb4b-4937-a636-772d5520cccd', '2024-06-26 16:29:59', '2024-06-26 16:33:00', '2024-06-26 16:35:16');
INSERT INTO `task_process` VALUES (115, 8, '标签计算:good_for_kids', '00:00', '2024-06-26', 'FINISHED', 100, 'application_1719306145344_0030', '16eb3cc5-eb4b-4937-a636-772d5520cccd', '2024-06-26 16:29:59', '2024-06-26 16:30:00', '2024-06-26 16:32:00');
INSERT INTO `task_process` VALUES (116, 1, '标签计算:性别', '00:00', '2024-06-26', 'FINISHED', 100, 'application_1719306145344_0036', '55b83a36-f2bc-4cb9-b11b-34a88ea51f33', '2024-06-26 17:11:28', '2024-06-26 17:11:30', '2024-06-26 17:14:22');
INSERT INTO `task_process` VALUES (117, 3, '年龄段', '02:40', '2024-06-26', 'FINISHED', 100, 'application_1719306145344_0035', '55b83a36-f2bc-4cb9-b11b-34a88ea51f33', '2024-06-26 17:11:28', '2024-06-26 17:11:30', '2024-06-26 17:13:29');
INSERT INTO `task_process` VALUES (118, 4, '合并标签宽表', '01:00', '2024-06-26', 'FINISHED', 200, 'application_1719306145344_0037', '55b83a36-f2bc-4cb9-b11b-34a88ea51f33', '2024-06-26 17:11:28', '2024-06-26 17:14:30', '2024-06-26 17:16:55');
INSERT INTO `task_process` VALUES (119, 6, 'hive-to-ck', '01:00', '2024-06-26', 'FINISHED', 300, 'application_1719306145344_0038', '55b83a36-f2bc-4cb9-b11b-34a88ea51f33', '2024-06-26 17:11:28', '2024-06-26 17:17:00', '2024-06-26 17:19:41');
INSERT INTO `task_process` VALUES (120, 8, '标签计算:good_for_kids', '00:00', '2024-06-26', 'FINISHED', 100, 'application_1719306145344_0034', '55b83a36-f2bc-4cb9-b11b-34a88ea51f33', '2024-06-26 17:11:28', '2024-06-26 17:11:30', '2024-06-26 17:12:29');
INSERT INTO `task_process` VALUES (121, 1, '标签计算:性别', '00:00', '2024-06-26', 'FINISHED', 100, 'application_1719306145344_0040', 'e5b23de9-262b-4678-a045-deb0f92cb481', '2024-06-26 21:06:32', '2024-06-26 21:06:45', '2024-06-26 21:10:36');
INSERT INTO `task_process` VALUES (122, 3, '年龄段', '02:40', '2024-06-26', 'FINISHED', 100, 'application_1719306145344_0039', 'e5b23de9-262b-4678-a045-deb0f92cb481', '2024-06-26 21:06:32', '2024-06-26 21:06:45', '2024-06-26 21:09:25');
INSERT INTO `task_process` VALUES (123, 4, '合并标签宽表', '01:00', '2024-06-26', 'FINISHED', 200, 'application_1719306145344_0042', 'e5b23de9-262b-4678-a045-deb0f92cb481', '2024-06-26 21:06:32', '2024-06-26 21:10:45', '2024-06-26 21:13:19');
INSERT INTO `task_process` VALUES (124, 6, 'hive-to-ck', '01:00', '2024-06-26', 'FINISHED', 300, 'application_1719306145344_0043', 'e5b23de9-262b-4678-a045-deb0f92cb481', '2024-06-26 21:06:32', '2024-06-26 21:13:30', '2024-06-26 21:16:03');
INSERT INTO `task_process` VALUES (125, 7, 'ck-to-bitmap', '01:00', '2024-06-26', 'FINISHED', 400, 'application_1719306145344_0045', 'e5b23de9-262b-4678-a045-deb0f92cb481', '2024-06-26 21:06:32', '2024-06-26 21:26:00', '2024-06-26 21:26:24');
INSERT INTO `task_process` VALUES (126, 8, '标签计算:good_for_kids', '00:00', '2024-06-26', 'FINISHED', 100, 'application_1719306145344_0041', 'e5b23de9-262b-4678-a045-deb0f92cb481', '2024-06-26 21:06:32', '2024-06-26 21:06:45', '2024-06-26 21:08:26');
INSERT INTO `task_process` VALUES (127, 11, '标签计算:好友数', '02:40', '2024-06-28', 'FINISHED', 100, 'application_1719306145344_0050', '35e82412-b5ec-4d04-a99c-68234b0f15a4', '2024-06-28 15:36:32', '2024-06-28 15:56:30', '2024-06-28 16:02:32');
INSERT INTO `task_process` VALUES (128, 1, '标签计算:性别', '00:00', '2024-06-28', 'FINISHED', 100, 'application_1719306145344_0059', '4556d505-cc64-4078-b7b6-0d043d5031df', '2024-06-28 16:46:08', '2024-06-28 16:46:15', '2024-06-28 16:52:55');
INSERT INTO `task_process` VALUES (129, 3, '年龄段', '02:40', '2024-06-28', 'FINISHED', 100, 'application_1719306145344_0055', '4556d505-cc64-4078-b7b6-0d043d5031df', '2024-06-28 16:46:08', '2024-06-28 16:46:15', '2024-06-28 16:50:43');
INSERT INTO `task_process` VALUES (130, 4, '合并标签宽表', '01:00', '2024-06-28', 'FAILED', 200, 'application_1719570400738_0014', '4556d505-cc64-4078-b7b6-0d043d5031df', '2024-06-28 16:46:08', '2024-06-28 20:52:15', '2024-06-28 20:53:16');
INSERT INTO `task_process` VALUES (131, 6, 'hive-to-ck', '01:00', '2024-06-28', 'FAILED', 300, 'application_1719570400738_0015', '4556d505-cc64-4078-b7b6-0d043d5031df', '2024-06-28 16:46:08', '2024-06-28 20:53:30', '2024-06-28 20:54:26');
INSERT INTO `task_process` VALUES (132, 7, 'ck-to-bitmap', '01:00', '2024-06-28', 'FINISHED', 400, 'application_1719570400738_0016', '4556d505-cc64-4078-b7b6-0d043d5031df', '2024-06-28 16:46:08', '2024-06-28 20:54:30', '2024-06-28 20:54:53');
INSERT INTO `task_process` VALUES (133, 8, '标签计算:good_for_kids', '00:00', '2024-06-28', 'FINISHED', 100, 'application_1719570400738_0017', '4556d505-cc64-4078-b7b6-0d043d5031df', '2024-06-28 16:46:08', '2024-06-28 21:02:45', '2024-06-28 21:03:23');
INSERT INTO `task_process` VALUES (134, 9, '标签计算:好评数', '00:00', '2024-06-28', 'FINISHED', 150, 'application_1719570400738_0003', '4556d505-cc64-4078-b7b6-0d043d5031df', '2024-06-28 16:46:08', '2024-06-28 18:54:15', '2024-06-28 19:00:38');
INSERT INTO `task_process` VALUES (135, 10, '标签计算:差评数', '00:00', '2024-06-28', 'FINISHED', 150, 'application_1719570400738_0013', '4556d505-cc64-4078-b7b6-0d043d5031df', '2024-06-28 16:46:08', '2024-06-28 20:41:45', '2024-06-28 20:50:55');
INSERT INTO `task_process` VALUES (136, 11, '标签计算:好友数', '02:40', '2024-06-28', 'FINISHED', 100, 'application_1719306145344_0060', '4556d505-cc64-4078-b7b6-0d043d5031df', '2024-06-28 16:46:08', '2024-06-28 16:46:15', '2024-06-28 16:53:55');
INSERT INTO `task_process` VALUES (137, 12, '标签计算:网红', '00:00', '2024-06-28', 'FINISHED', 100, 'application_1719306145344_0057', '4556d505-cc64-4078-b7b6-0d043d5031df', '2024-06-28 16:46:08', '2024-06-28 16:46:15', '2024-06-28 16:48:36');
INSERT INTO `task_process` VALUES (138, 13, '标签计算:类型', '00:00', '2024-06-28', 'FINISHED', 100, 'application_1719306145344_0067', '4556d505-cc64-4078-b7b6-0d043d5031df', '2024-06-28 16:46:08', '2024-06-28 17:00:00', '2024-06-28 17:01:00');
INSERT INTO `task_process` VALUES (139, 14, '标签计算:优质用户次数', '00:00', '2024-06-28', 'FINISHED', 100, 'application_1719306145344_0056', '4556d505-cc64-4078-b7b6-0d043d5031df', '2024-06-28 16:46:08', '2024-06-28 16:46:15', '2024-06-28 16:51:36');
INSERT INTO `task_process` VALUES (140, 15, '标签计算:good_for_credit', '00:00', '2024-06-28', 'FINISHED', 100, 'application_1719306145344_0064', '4556d505-cc64-4078-b7b6-0d043d5031df', '2024-06-28 16:46:08', '2024-06-28 16:56:45', '2024-06-28 16:57:20');
INSERT INTO `task_process` VALUES (141, 17, '标签计算:评分', '00:00', '2024-06-28', 'FINISHED', 100, 'application_1719306145344_0058', '4556d505-cc64-4078-b7b6-0d043d5031df', '2024-06-28 16:46:08', '2024-06-28 16:46:15', '2024-06-28 16:52:07');
INSERT INTO `task_process` VALUES (142, 10, '标签计算:差评数', '00:00', '2024-06-28', 'FINISHED', 175, 'application_1719570400738_0020', 'ec9923bb-7dcd-48f4-a145-b679fbc712dc', '2024-06-28 22:17:14', '2024-06-28 22:23:00', '2024-06-28 22:25:31');
INSERT INTO `task_process` VALUES (143, 16, '标签计算:营业', '00:00', '2024-06-28', 'FINISHED', 100, 'application_1719570400738_0018', 'ec9923bb-7dcd-48f4-a145-b679fbc712dc', '2024-06-28 22:17:14', '2024-06-28 22:17:15', '2024-06-28 22:18:05');
INSERT INTO `task_process` VALUES (144, 1, '标签计算:性别', '00:00', '2024-06-28', 'FINISHED', 100, 'application_1719570400738_0025', '91a9b280-f408-485e-858a-23a1705c3b45', '2024-06-28 22:50:27', '2024-06-28 22:50:30', '2024-06-28 22:55:21');
INSERT INTO `task_process` VALUES (145, 3, '年龄段', '02:40', '2024-06-28', 'FINISHED', 100, 'application_1719570400738_0030', '91a9b280-f408-485e-858a-23a1705c3b45', '2024-06-28 22:50:27', '2024-06-28 22:50:30', '2024-06-28 22:59:33');
INSERT INTO `task_process` VALUES (146, 4, '合并标签宽表', '01:00', '2024-06-28', 'FAILED', 200, 'application_1719629837546_0001', '91a9b280-f408-485e-858a-23a1705c3b45', '2024-06-28 22:50:27', '2024-06-29 10:58:30', '2024-06-29 11:01:55');
INSERT INTO `task_process` VALUES (147, 6, 'hive-to-ck', '01:00', '2024-06-28', 'FINISHED', 300, 'application_1719629837546_0004', '91a9b280-f408-485e-858a-23a1705c3b45', '2024-06-28 22:50:27', '2024-06-29 11:32:30', '2024-06-29 11:45:41');
INSERT INTO `task_process` VALUES (148, 7, 'ck-to-bitmap', '01:00', '2024-06-28', 'FINISHED', 400, 'application_1719629837546_0005', '91a9b280-f408-485e-858a-23a1705c3b45', '2024-06-28 22:50:27', '2024-06-29 11:45:45', '2024-06-29 11:46:08');
INSERT INTO `task_process` VALUES (149, 8, '标签计算:good_for_kids', '00:00', '2024-06-28', 'FINISHED', 100, 'application_1719570400738_0029', '91a9b280-f408-485e-858a-23a1705c3b45', '2024-06-28 22:50:27', '2024-06-28 22:50:30', '2024-06-28 22:58:42');
INSERT INTO `task_process` VALUES (150, 9, '标签计算:好评数', '00:00', '2024-06-28', 'FINISHED', 150, 'application_1719570400738_0031', '91a9b280-f408-485e-858a-23a1705c3b45', '2024-06-28 22:50:27', '2024-06-28 22:59:45', '2024-06-28 23:02:15');
INSERT INTO `task_process` VALUES (151, 10, '标签计算:差评数', '00:00', '2024-06-28', 'FINISHED', 175, 'application_1719570400738_0032', '91a9b280-f408-485e-858a-23a1705c3b45', '2024-06-28 22:50:27', '2024-06-28 23:02:30', '2024-06-28 23:04:56');
INSERT INTO `task_process` VALUES (152, 11, '标签计算:好友数', '02:40', '2024-06-28', 'FINISHED', 100, 'application_1719570400738_0026', '91a9b280-f408-485e-858a-23a1705c3b45', '2024-06-28 22:50:27', '2024-06-28 22:50:30', '2024-06-28 22:56:26');
INSERT INTO `task_process` VALUES (153, 12, '标签计算:网红', '00:00', '2024-06-28', 'FINISHED', 100, 'application_1719570400738_0027', '91a9b280-f408-485e-858a-23a1705c3b45', '2024-06-28 22:50:27', '2024-06-28 22:50:30', '2024-06-28 22:57:20');
INSERT INTO `task_process` VALUES (154, 13, '标签计算:类型', '00:00', '2024-06-28', 'FINISHED', 100, 'application_1719570400738_0022', '91a9b280-f408-485e-858a-23a1705c3b45', '2024-06-28 22:50:27', '2024-06-28 22:50:30', '2024-06-28 22:53:29');
INSERT INTO `task_process` VALUES (155, 14, '标签计算:优质用户次数', '00:00', '2024-06-28', 'FINISHED', 100, 'application_1719570400738_0028', '91a9b280-f408-485e-858a-23a1705c3b45', '2024-06-28 22:50:27', '2024-06-28 22:50:30', '2024-06-28 22:58:12');
INSERT INTO `task_process` VALUES (156, 15, '标签计算:good_for_credit', '00:00', '2024-06-28', 'FINISHED', 100, 'application_1719570400738_0021', '91a9b280-f408-485e-858a-23a1705c3b45', '2024-06-28 22:50:27', '2024-06-28 22:50:30', '2024-06-28 22:52:34');
INSERT INTO `task_process` VALUES (157, 16, '标签计算:营业', '00:00', '2024-06-28', 'FINISHED', 100, 'application_1719570400738_0024', '91a9b280-f408-485e-858a-23a1705c3b45', '2024-06-28 22:50:27', '2024-06-28 22:50:30', '2024-06-28 22:54:27');
INSERT INTO `task_process` VALUES (158, 17, '标签计算:评分', '00:00', '2024-06-28', 'FINISHED', 100, 'application_1719570400738_0023', '91a9b280-f408-485e-858a-23a1705c3b45', '2024-06-28 22:50:27', '2024-06-28 22:50:30', '2024-06-28 22:54:01');
INSERT INTO `task_process` VALUES (159, 1, '标签计算:性别', '00:00', '2024-06-29', 'FINISHED', 100, 'application_1719629837546_0014', 'ab665fcb-7d7f-4941-88e3-6cec7702cb84', '2024-06-29 12:54:41', '2024-06-29 12:54:45', '2024-06-29 13:05:04');
INSERT INTO `task_process` VALUES (160, 3, '年龄段', '02:40', '2024-06-29', 'FINISHED', 100, 'application_1719629837546_0013', 'ab665fcb-7d7f-4941-88e3-6cec7702cb84', '2024-06-29 12:54:41', '2024-06-29 12:54:45', '2024-06-29 13:04:13');
INSERT INTO `task_process` VALUES (161, 4, '合并标签宽表', '01:00', '2024-06-29', 'FINISHED', 200, 'application_1719629837546_0018', 'ab665fcb-7d7f-4941-88e3-6cec7702cb84', '2024-06-29 12:54:41', '2024-06-29 13:09:15', '2024-06-29 13:13:51');
INSERT INTO `task_process` VALUES (162, 6, 'hive-to-ck', '01:00', '2024-06-29', 'FINISHED', 300, 'application_1719629837546_0019', 'ab665fcb-7d7f-4941-88e3-6cec7702cb84', '2024-06-29 12:54:41', '2024-06-29 13:14:00', '2024-06-29 13:16:56');
INSERT INTO `task_process` VALUES (163, 7, 'ck-to-bitmap', '01:00', '2024-06-29', 'FINISHED', 400, 'application_1719629837546_0020', 'ab665fcb-7d7f-4941-88e3-6cec7702cb84', '2024-06-29 12:54:41', '2024-06-29 13:17:00', '2024-06-29 13:17:22');
INSERT INTO `task_process` VALUES (164, 8, '标签计算:good_for_kids', '00:00', '2024-06-29', 'FINISHED', 100, 'application_1719629837546_0008', 'ab665fcb-7d7f-4941-88e3-6cec7702cb84', '2024-06-29 12:54:41', '2024-06-29 12:54:45', '2024-06-29 13:01:27');
INSERT INTO `task_process` VALUES (165, 11, '标签计算:好友数', '02:40', '2024-06-29', 'FINISHED', 100, 'application_1719629837546_0006', 'ab665fcb-7d7f-4941-88e3-6cec7702cb84', '2024-06-29 12:54:41', '2024-06-29 12:54:45', '2024-06-29 12:58:41');
INSERT INTO `task_process` VALUES (166, 12, '标签计算:网红', '00:00', '2024-06-29', 'FINISHED', 100, 'application_1719629837546_0016', 'ab665fcb-7d7f-4941-88e3-6cec7702cb84', '2024-06-29 12:54:41', '2024-06-29 12:54:45', '2024-06-29 13:06:43');
INSERT INTO `task_process` VALUES (167, 13, '标签计算:类型', '00:00', '2024-06-29', 'FINISHED', 100, 'application_1719629837546_0015', 'ab665fcb-7d7f-4941-88e3-6cec7702cb84', '2024-06-29 12:54:41', '2024-06-29 12:54:45', '2024-06-29 13:05:52');
INSERT INTO `task_process` VALUES (168, 14, '标签计算:优质用户次数', '00:00', '2024-06-29', 'FINISHED', 100, 'application_1719629837546_0010', 'ab665fcb-7d7f-4941-88e3-6cec7702cb84', '2024-06-29 12:54:41', '2024-06-29 12:54:45', '2024-06-29 13:02:49');
INSERT INTO `task_process` VALUES (169, 15, '标签计算:good_for_credit', '00:00', '2024-06-29', 'FINISHED', 100, 'application_1719629837546_0012', 'ab665fcb-7d7f-4941-88e3-6cec7702cb84', '2024-06-29 12:54:41', '2024-06-29 12:54:45', '2024-06-29 13:03:20');
INSERT INTO `task_process` VALUES (170, 16, '标签计算:营业', '00:00', '2024-06-29', 'FINISHED', 100, 'application_1719629837546_0011', 'ab665fcb-7d7f-4941-88e3-6cec7702cb84', '2024-06-29 12:54:41', '2024-06-29 12:54:45', '2024-06-29 12:57:27');
INSERT INTO `task_process` VALUES (171, 17, '标签计算:评分', '00:00', '2024-06-29', 'FINISHED', 100, 'application_1719629837546_0009', 'ab665fcb-7d7f-4941-88e3-6cec7702cb84', '2024-06-29 12:54:41', '2024-06-29 12:54:45', '2024-06-29 13:01:54');
INSERT INTO `task_process` VALUES (172, 18, '标签计算:差评数量', '02:40', '2024-06-29', 'FINISHED', 100, 'application_1719629837546_0007', 'ab665fcb-7d7f-4941-88e3-6cec7702cb84', '2024-06-29 12:54:41', '2024-06-29 12:54:45', '2024-06-29 13:00:57');
INSERT INTO `task_process` VALUES (173, 19, '标签计算:好评数量', '02:40', '2024-06-29', 'FINISHED', 100, 'application_1719629837546_0017', 'ab665fcb-7d7f-4941-88e3-6cec7702cb84', '2024-06-29 12:54:41', '2024-06-29 12:54:45', '2024-06-29 13:09:04');

-- ----------------------------
-- Table structure for task_tag_rule
-- ----------------------------
DROP TABLE IF EXISTS `task_tag_rule`;
CREATE TABLE `task_tag_rule`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tag_id` bigint(20) NULL DEFAULT NULL COMMENT '标签主键',
  `task_id` bigint(20) NULL DEFAULT NULL COMMENT '任务id',
  `query_value` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '查询值',
  `sub_tag_id` bigint(20) NULL DEFAULT NULL COMMENT '对应子标签id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 237 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of task_tag_rule
-- ----------------------------
INSERT INTO `task_tag_rule` VALUES (204, 40, 16, '0', 42);
INSERT INTO `task_tag_rule` VALUES (205, 40, 16, '1', 41);
INSERT INTO `task_tag_rule` VALUES (212, 4, 1, 'male', 6);
INSERT INTO `task_tag_rule` VALUES (213, 4, 1, 'female', 7);
INSERT INTO `task_tag_rule` VALUES (214, 5, 3, '5', 9);
INSERT INTO `task_tag_rule` VALUES (215, 5, 3, '6', 10);
INSERT INTO `task_tag_rule` VALUES (216, 5, 3, '7', 11);
INSERT INTO `task_tag_rule` VALUES (217, 5, 3, '8', 12);
INSERT INTO `task_tag_rule` VALUES (218, 5, 3, '9', 13);
INSERT INTO `task_tag_rule` VALUES (219, 5, 3, '0', 14);
INSERT INTO `task_tag_rule` VALUES (220, NULL, 3, '1', 22);
INSERT INTO `task_tag_rule` VALUES (221, NULL, 3, '2', 22);
INSERT INTO `task_tag_rule` VALUES (222, NULL, 3, '3', 22);
INSERT INTO `task_tag_rule` VALUES (223, NULL, 3, '4', 22);
INSERT INTO `task_tag_rule` VALUES (224, 28, 12, '1', 29);
INSERT INTO `task_tag_rule` VALUES (225, 28, 12, '0', 30);
INSERT INTO `task_tag_rule` VALUES (226, 31, 13, '2', 34);
INSERT INTO `task_tag_rule` VALUES (227, 31, 13, '3', 33);
INSERT INTO `task_tag_rule` VALUES (228, 31, 13, '1', 32);
INSERT INTO `task_tag_rule` VALUES (229, 17, 8, 'true', 18);
INSERT INTO `task_tag_rule` VALUES (230, 17, 8, 'false', 19);
INSERT INTO `task_tag_rule` VALUES (231, NULL, 15, 'true', 38);
INSERT INTO `task_tag_rule` VALUES (232, NULL, 15, 'false', 39);
INSERT INTO `task_tag_rule` VALUES (233, 43, 17, '1', 44);
INSERT INTO `task_tag_rule` VALUES (234, 43, 17, '2', 45);
INSERT INTO `task_tag_rule` VALUES (235, 43, 17, '3', 46);
INSERT INTO `task_tag_rule` VALUES (236, 43, 17, '4', 47);

-- ----------------------------
-- Table structure for user_group
-- ----------------------------
DROP TABLE IF EXISTS `user_group`;
CREATE TABLE `user_group`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_group_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分群名称',
  `condition_json_str` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分群条件(json)',
  `condition_comment` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分群条件(描述)',
  `user_group_num` bigint(20) NULL DEFAULT NULL COMMENT '分群人数',
  `update_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新类型(手动,自动按天)',
  `user_group_comment` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分群说明',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_group
-- ----------------------------
INSERT INTO `user_group` VALUES (22, '用户_男_00后_分群', '[{\"operator\":\"eq\",\"operatorName\":\"等于\",\"tagCode\":\"TG_PERSON_BASE_GENDER\",\"tagCodePath\":[\"TG_PERSON\",\"TG_PERSON_BASE\",\"TG_PERSON_BASE_GENDER\"],\"tagName\":\"性别\",\"tagValues\":[\"男\"]},{\"operator\":\"eq\",\"operatorName\":\"等于\",\"tagCode\":\"TG_PERSON_BASE_AGEGROUP\",\"tagCodePath\":[\"TG_PERSON\",\"TG_PERSON_BASE\",\"TG_PERSON_BASE_AGEGROUP\"],\"tagName\":\"年龄段\",\"tagValues\":[\"00后\"]}]', '性别 等于 男 ;\n年龄段 等于 00后 ;\n', 110426, 'MANUAL', '用户_男_00后_分群', '2024-06-27 19:53:24', '2024-06-27 19:53:24');
INSERT INTO `user_group` VALUES (23, '用户_女_00/90后_分群', '[{\"operator\":\"eq\",\"operatorName\":\"等于\",\"tagCode\":\"TG_PERSON_BASE_GENDER\",\"tagCodePath\":[\"TG_PERSON\",\"TG_PERSON_BASE\",\"TG_PERSON_BASE_GENDER\"],\"tagName\":\"性别\",\"tagValues\":[\"女\"]},{\"operator\":\"in\",\"operatorName\":\"包含\",\"tagCode\":\"TG_PERSON_BASE_AGEGROUP\",\"tagCodePath\":[\"TG_PERSON\",\"TG_PERSON_BASE\",\"TG_PERSON_BASE_AGEGROUP\"],\"tagName\":\"年龄段\",\"tagValues\":[\"00后\",\"90后\"]}]', '性别 等于 女 ;\n年龄段 包含 00后,90后 ;\n', 268656, 'MANUAL', '用户_女_00/90后_分群', '2024-06-27 19:56:55', '2024-06-27 19:56:55');
INSERT INTO `user_group` VALUES (24, '商户_优质_营业_goodForKidsAndCredits', '[{\"operator\":\"eq\",\"operatorName\":\"等于\",\"tagCode\":\"BUSINESS_TAG_BASE_STARS\",\"tagCodePath\":[\"BUSINESS_TAG\",\"BUSINESS_TAG_BASE\",\"BUSINESS_TAG_BASE_STARS\"],\"tagName\":\"评分\",\"tagValues\":[\"优质商户\"]},{\"operator\":\"eq\",\"operatorName\":\"等于\",\"tagCode\":\"BUSINESS_TAG_BASE_OPEN\",\"tagCodePath\":[\"BUSINESS_TAG\",\"BUSINESS_TAG_BASE\",\"BUSINESS_TAG_BASE_OPEN\"],\"tagName\":\"营业\",\"tagValues\":[\"是\"]},{\"operator\":\"eq\",\"operatorName\":\"等于\",\"tagCode\":\"BUSINESS_GOOD_FOR_KIDS_TAG\",\"tagCodePath\":[\"BUSINESS_TAG\",\"BUSINESS_TAG_BASE\",\"BUSINESS_GOOD_FOR_KIDS_TAG\"],\"tagName\":\"good_for_kids\",\"tagValues\":[\"是\"]},{\"operator\":\"eq\",\"operatorName\":\"等于\",\"tagCode\":\"BUSINESS_TAG_BASE_GOOD_FOR_CREDIT\",\"tagCodePath\":[\"BUSINESS_TAG\",\"BUSINESS_TAG_BASE\",\"BUSINESS_TAG_BASE_GOOD_FOR_CREDIT\"],\"tagName\":\"good_for_credit\",\"tagValues\":[\"是\"]}]', '评分 等于 优质商户 ;\n营业 等于 是 ;\ngood_for_kids 等于 是 ;\ngood_for_credit 等于 是 ;\n', 15083, 'MANUAL', '商户_优质_营业_goodForKidsAndCredits_好评数大于10', '2024-06-29 11:54:20', '2024-06-29 11:54:19');
INSERT INTO `user_group` VALUES (25, '商户_营业_优质_good_for_kidsAndCredit_好评数大于1000', '[{\"operator\":\"eq\",\"operatorName\":\"等于\",\"tagCode\":\"BUSINESS_TAG_BASE_OPEN\",\"tagCodePath\":[\"BUSINESS_TAG\",\"BUSINESS_TAG_BASE\",\"BUSINESS_TAG_BASE_OPEN\"],\"tagName\":\"营业\",\"tagValues\":[\"是\"]},{\"operator\":\"eq\",\"operatorName\":\"等于\",\"tagCode\":\"BUSINESS_TAG_BASE_STARS\",\"tagCodePath\":[\"BUSINESS_TAG\",\"BUSINESS_TAG_BASE\",\"BUSINESS_TAG_BASE_STARS\"],\"tagName\":\"评分\",\"tagValues\":[\"优质商户\"]},{\"operator\":\"eq\",\"operatorName\":\"等于\",\"tagCode\":\"BUSINESS_GOOD_FOR_KIDS_TAG\",\"tagCodePath\":[\"BUSINESS_TAG\",\"BUSINESS_TAG_BASE\",\"BUSINESS_GOOD_FOR_KIDS_TAG\"],\"tagName\":\"good_for_kids\",\"tagValues\":[\"是\"]},{\"operator\":\"eq\",\"operatorName\":\"等于\",\"tagCode\":\"BUSINESS_TAG_BASE_GOOD_FOR_CREDIT\",\"tagCodePath\":[\"BUSINESS_TAG\",\"BUSINESS_TAG_BASE\",\"BUSINESS_TAG_BASE_GOOD_FOR_CREDIT\"],\"tagName\":\"good_for_credit\",\"tagValues\":[\"是\"]},{\"operator\":\"gt\",\"operatorName\":\"大于\",\"tagCode\":\"BUSINESS_TAG_BASE_REVIEW_GOODNUM\",\"tagCodePath\":[\"BUSINESS_TAG\",\"BUSINESS_TAG_BASE\",\"BUSINESS_TAG_BASE_REVIEW_GOODNUM\"],\"tagName\":\"好评数量\",\"tagValues\":[\"1000\"]}]', '营业 等于 是 ;\n评分 等于 优质商户 ;\ngood_for_kids 等于 是 ;\ngood_for_credit 等于 是 ;\n好评数量 大于 1000 ;\n', 222, 'MANUAL', '商户_营业_优质_good_for_kidsAndCredit_好评数大于1000', '2024-06-29 13:20:42', '2024-06-29 13:20:42');

SET FOREIGN_KEY_CHECKS = 1;
