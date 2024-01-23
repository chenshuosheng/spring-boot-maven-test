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

 Date: 23/01/2024 16:04:57
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for database_info
-- ----------------------------
DROP TABLE IF EXISTS `database_info`;
CREATE TABLE `database_info`
(
    `id`            int(11)                                                      NOT NULL AUTO_INCREMENT COMMENT 'id',
    `database_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据库名',
    `table_num`     int(11)                                                      NULL DEFAULT 0 COMMENT '所管理的数据表数量',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `database_info_database_name_uindex` (`database_name`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 0
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '数据库信息' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
