CREATE TABLE `database_info` (
  `id` int(11) NOT NULL,
  `database_name` varchar(128) NOT NULL COMMENT '数据库名',
  `table_num` int(11) DEFAULT '0' COMMENT '所管理的数据表数量',
  PRIMARY KEY (`id`),
  UNIQUE KEY `database_info_database_name_uindex` (`database_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据库信息';
INSERT INTO database_info VALUES (11,941bigdata,8);
INSERT INTO database_info VALUES (12,6666666,135);
INSERT INTO database_info VALUES (14,yza,133);
INSERT INTO database_info VALUES (15,java_web_crawler_sys,70);
INSERT INTO database_info VALUES (16,crawer,6);
INSERT INTO database_info VALUES (17,java_web_crawler_test,61);
INSERT INTO database_info VALUES (18,smart_security,40);
INSERT INTO database_info VALUES (20,nacos,12);
INSERT INTO database_info VALUES (21,testdatabase,0);
