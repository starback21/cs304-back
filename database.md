# database

```
drop table if exists sys_role;
CREATE TABLE `sys_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `role_name` varchar(20) NOT NULL DEFAULT '' COMMENT '角色名称',
  `role_code` varchar(20) DEFAULT NULL COMMENT '角色编码',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0' COMMENT '删除标记（0:可用 1:不可用）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='角色';

drop table if exists sys_group;
CREATE TABLE `sys_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '课题组id',
  `group_name` varchar(20) NOT NULL DEFAULT '' COMMENT '课题组名称',
  `group_leader` varchar(20) NOT NULL DEFAULT '' COMMENT '课题组领导人',
  `group_code` varchar(20) DEFAULT NULL COMMENT '课题组编码',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0' COMMENT '删除标记（0:可用 1:不可用）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_name` (`group_name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='课题组';

drop table if exists sys_user;
CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `uid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'uid',
  `password` varchar(32) NOT NULL DEFAULT '' COMMENT '密码',
  `name` varchar(50) DEFAULT NULL COMMENT '姓名',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(11) DEFAULT NULL COMMENT '手机',
  `head_url` varchar(200) DEFAULT NULL COMMENT '头像地址',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `status` tinyint(3) DEFAULT NULL COMMENT '状态（1：正常 0：停用）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0' COMMENT '删除标记（0:可用 1:不可用）',
  PRIMARY KEY (`id`)
#   UNIQUE KEY `idx_uid` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1  DEFAULT CHARSET=utf8mb4 COMMENT='用户表';


drop table if exists sys_user_role;
CREATE TABLE `sys_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `group_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '组id',
  `group_name` varchar(20) NOT NULL DEFAULT '' COMMENT '课题组名称',
  `role_id` int(5) NOT NULL DEFAULT '0' COMMENT '角色id',
  `user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户id',
  `user_name` varchar(20) DEFAULT NULL COMMENT '昵称',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0' COMMENT '删除标记（0:可用 1:不可用）',
  PRIMARY KEY (`id`),
  KEY `idx_group_id` (`group_id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户角色';

drop table if exists sys_application;
CREATE TABLE `sys_application` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `title` varchar(50) NOT NULL DEFAULT '' COMMENT '标题',
  `group_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '组id',
  `group_name` varchar(20) NOT NULL DEFAULT '' COMMENT '组名',
  `category1` varchar(30) NOT NULL DEFAULT '' COMMENT '类别1',
  `category2` varchar(30) NOT NULL DEFAULT '' COMMENT '类别2',
  `number` int(5) NOT NULL DEFAULT 0 COMMENT '数量',
  `state` varchar(30) NOT NULL DEFAULT '' COMMENT '状态',
  `change_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '完成时间(状态更新的时间)',
  `people` varchar(20) NOT NULL DEFAULT '' COMMENT '处理人',
  `comment` varchar(100) NOT NULL DEFAULT '' COMMENT '评价',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0' COMMENT '删除标记（0:可用 1:不可用）',
  PRIMARY KEY (`id`),
  KEY `idx_group_id` (`group_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='申请';

drop table if exists sys_funding;
CREATE TABLE sys_funding (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键id',
  funding_id INT NOT NULL default 114514 COMMENT '经费id' ,
  funding_name VARCHAR(20) NOT NULL default 'test' COMMENT '经费名称',
  total_amount DECIMAL(10, 2) NOT NULL default '9999999'COMMENT '总经费',
  cost DECIMAL(10, 2) NOT NULL default 0 COMMENT '单次花费',
  remain_amount DECIMAL(10, 2) default 9999 NOT NULL COMMENT '剩余经费',
   status  VARCHAR(20) NOT NULL DEFAULT 'complete' COMMENT '状态',
  `start_time` VARCHAR(20) default '2019' NOT NULL COMMENT '开始时间',
   end_time VARCHAR(20) NOT NULL default '2021' COMMENT '结束时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0' COMMENT '删除标记（0:不可用 1:可用）    '
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='经费表';

drop table if exists sys_fund_app;
CREATE TABLE `sys_fund_app` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `fund_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '经费id',
  `fund_name` varchar(20) NOT NULL DEFAULT '' COMMENT '经费名称',
  `app_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '申请id',
  `app_name` varchar(20) DEFAULT NULL COMMENT '申请名称',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0' COMMENT '删除标记（0:可用 1:不可用）',
  PRIMARY KEY (`id`),
  KEY `idx_fund_id` (`fund_id`),
  KEY `idx_app_id` (`app_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='经费申请';

drop table if exists sys_group_funding;
CREATE TABLE sys_group_funding (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键id',
  funding_id INT NOT NULL default 114514 COMMENT '经费id' ,
  funding_name VARCHAR(20) NOT NULL default 'test' COMMENT '经费名称',
  group_id INT NOT NULL default '114514' COMMENT '课题组id',
  group_name VARCHAR(20) NOT NULL default 'test'COMMENT '课题组名称',
  total_amount DECIMAL(10, 2) NOT NULL default '9999999'COMMENT '总经费',
  cost DECIMAL(10, 2) NOT NULL default 0 COMMENT '花费',
  remain_amount DECIMAL(10, 2) default 9999 NOT NULL COMMENT '剩余经费',
    status  VARCHAR(20) NOT NULL DEFAULT 'complete' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0' COMMENT '删除标记（0:不可用 1:可用）    '
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='课题组经费';

drop table if exists sys_group_funding_detail;
CREATE TABLE sys_group_funding_detail (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键id',
  funding_id INT NOT NULL default 114514 COMMENT '经费id' ,
  group_id INT NOT NULL default '114514' COMMENT '课题组id',
  total_amount DECIMAL(10, 2) NOT NULL default '9999999'COMMENT '总经费',
   `used_amount`  DECIMAL(10, 2) NOT NULL default 0 COMMENT '花费',
   category1 VARCHAR(50) default 'test'COMMENT '一级分类',
    category2 VARCHAR(50) default'test1'  COMMENT '二级分类',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0' COMMENT '删除标记（0:不可用 1:可用）    '
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='详细经费表';

```