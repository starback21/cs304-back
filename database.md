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
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0' COMMENT '删除标记（0:不可用 1:可用）',
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
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0' COMMENT '删除标记（0:不可用 1:可用）',
  PRIMARY KEY (`id`)
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
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0' COMMENT '删除标记（0:不可用 1:可用）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_uid` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1  DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

drop table if exists sys_user_role;
CREATE TABLE `sys_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `group_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '组id',
  `role_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '角色id',
  `user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0' COMMENT '删除标记（0:不可用 1:可用）',
  PRIMARY KEY (`id`),
  KEY `idx_group_id` (`group_id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户角色';

CREATE TABLE `sys_Application` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `title` varchar(50) NOT NULL DEFAULT '' COMMENT '标题',
  `group` bigint(20) NOT NULL DEFAULT '0' COMMENT '组id',
  `category1` varchar(30) NOT NULL DEFAULT '' COMMENT '类别1',
  `category2` varchar(30) NOT NULL DEFAULT '' COMMENT '类别2',
  `number` int(5) NOT NULL DEFAULT '0' COMMENT '数量',
  `state` varchar(30) NOT NULL DEFAULT '' COMMENT '状态',
  `people` varchar(20) NOT NULL DEFAULT '' COMMENT '处理人',
  `comment` varchar(100) NOT NULL DEFAULT '' COMMENT '评价',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0' COMMENT '删除标记（0:不可用 1:可用）',
  PRIMARY KEY (`id`),
  KEY `idx_group_id` (`group`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='申请';

drop table if exists sys_funding;
CREATE TABLE sys_funding (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键id',
  funding_id INT NOT NULL COMMENT '经费id',
  funding_name VARCHAR(20) NOT NULL COMMENT '经费名称',
  group_id INT NOT NULL COMMENT '课题组id',
  group_name VARCHAR(20) NOT NULL COMMENT '课题组名称',
  total_amount DECIMAL(10, 2) NOT NULL COMMENT '总经费',
  cost DECIMAL(10, 2) NOT NULL COMMENT '单次花费',
  remain_amount DECIMAL(10, 2) NOT NULL COMMENT '剩余经费',
   status  VARCHAR(20) NOT NULL DEFAULT 'COMPLETED' COMMENT '状态',
  `start_time` VARCHAR(20) NOT NULL COMMENT '开始时间',
   end_time VARCHAR(20) NOT NULL COMMENT '结束时间',
   category1 VARCHAR(50)  COMMENT '一级分类',
category2 VARCHAR(50) COMMENT '二级分类',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0' COMMENT '删除标记（0:不可用 1:可用）    '
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='经费表';
```