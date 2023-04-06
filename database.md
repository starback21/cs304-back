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
  `e-mail` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(11) DEFAULT NULL COMMENT '手机',
  `head_url` varchar(200) DEFAULT NULL COMMENT '头像地址',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `status` tinyint(3) DEFAULT NULL COMMENT '状态（1：正常 0：停用）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0' COMMENT '删除标记（0:不可用 1:可用）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_username` (`uid`)
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
  KEY `idx_admin_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户角色';
drop table if exists sys_funding;
CREATE TABLE sys_funding (
  id INT PRIMARY KEY AUTO_INCREMENT,
  group_id INT NOT NULL,
  total_amount DECIMAL(10, 2) NOT NULL,
  remain_amount DECIMAL(10, 2) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0' COMMENT '删除标记（0:不可用 1:可用）'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='经费表';
```