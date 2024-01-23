/*
 Navicat Premium Data Transfer

 Source Server         : CSS
 Source Server Type    : MySQL
 Source Server Version : 50729
 Source Host           : localhost:3306
 Source Schema         : test_excel

 Target Server Type    : MySQL
 Target Server Version : 50729
 File Encoding         : 65001

 Date: 23/01/2024 16:02:38
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for table_info
-- ----------------------------
DROP TABLE IF EXISTS `table_info`;
CREATE TABLE `table_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `database_id` int(11) NOT NULL COMMENT '对应于所属数据库的数据库名在database_info表中的id',
  `table_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据表名',
  `column_num` int(11) NULL DEFAULT 0 COMMENT '数据表列数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据表信息' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
