CREATE DATABASE IF NOT EXISTS md4x DEFAULT CHARACTER SET utf8;
USE md4x;

/*****************************[1.sys_permission]****************************************/
DROP TABLE IF EXISTS sys_permission;
CREATE TABLE sys_permission (
  permissionId int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  permission_name varchar(255) NOT NULL unique COMMENT '权限名称',
  description varchar(255) NOT NULL unique COMMENT '权限描述',
  permission_type enum('menu','button') DEFAULT NULL COMMENT '权限类型',
  group_name varchar(255) DEFAULT NULL COMMENT '权限分类',
  available bit(1) DEFAULT b'1' COMMENT '可用标志',
  deleted bit(1) DEFAULT b'0' COMMENT '删除标志',
  url varchar(255) DEFAULT NULL COMMENT '地址',
  PRIMARY KEY (permissionId)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

/*****************************[2.sys_role]****************************************/
DROP TABLE IF EXISTS sys_role;
CREATE TABLE sys_role (
  roleId int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  role_name varchar(255) NOT NULL unique COMMENT '角色名称',
  description varchar(255) DEFAULT NULL COMMENT '角色描述',
  available bit(1) DEFAULT 1 COMMENT '可用标志',
  deleted bit(1) DEFAULT b'0' COMMENT '删除标志',
  roleGroupId int(11) NOT NULL COMMENT '角色组(部门)ID',
  PRIMARY KEY (roleId)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

/*****************************[3.sys_role_permission]****************************************/
DROP TABLE IF EXISTS sys_role_permission;
CREATE TABLE sys_role_permission (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  permissionId int(11) NOT NULL COMMENT '权限ID',
  roleId int(11) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (id),
  UNIQUE KEY `roleId_permissionId` (`roleId`,`permissionId`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

/*****************************[4.sys_user]****************************************/
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
  userId int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  user_name varchar(255) NOT NULL unique COMMENT '用户名(OA号)',
  passwd varchar(255) NOT NULL COMMENT '密码',
  available bit(1) NOT NULL DEFAULT b'0' COMMENT '是否可用标志',
  deleted bit(1) NOT NULL DEFAULT b'0' COMMENT '删除标志',
  goldwinder bit(1) NOT NULL COMMENT '是否是金风用户',
  nick_name varchar(255) NOT NULL COMMENT '昵称',
  position_code varchar(255) NOT NULL COMMENT '职位编码',
  position_name varchar(255) NOT NULL COMMENT '职位名称',
  email varchar(255) DEFAULT NULL COMMENT '邮箱',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  last_login_time datetime DEFAULT NULL COMMENT '最近登录时间',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  expired_time date DEFAULT NULL COMMENT '过期时间',
  salt varchar(255) DEFAULT NULL COMMENT '盐',
  department_code varchar(255) DEFAULT NULL COMMENT '用户所属部门编号',
  department_name varchar(255) DEFAULT NULL COMMENT '用户所属部门编号',
  center_code varchar(255) DEFAULT NULL COMMENT '用户所属中心编号',
  center_name varchar(255) DEFAULT NULL COMMENT '用户所属中心编号',
  PRIMARY KEY (userId)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

/*****************************[5.sys_user_role]****************************************/
DROP TABLE IF EXISTS sys_user_role;
CREATE TABLE sys_user_role (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  userId int(11) NOT NULL COMMENT '用户ID',
  roleId int(11) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (id),
  UNIQUE KEY `userId_roleId` (`userId`,`roleId`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

/*****************************[6.sys_token]****************************************/
DROP TABLE IF EXISTS sys_token;
CREATE TABLE sys_token (
  userId int(11) NOT NULL unique COMMENT '用户ID',
  token varchar(255) DEFAULT NULL COMMENT '用户Login Token',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  expire_time datetime DEFAULT NULL COMMENT '过期时间',
  PRIMARY KEY (userId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*****************************[7.sys_role_group]****************************************/
DROP TABLE IF EXISTS sys_role_group;
create table sys_role_group(
  id int(11) NOT NULL AUTO_INCREMENT comment '角色组(部门)ID，自增主键',
  role_group_name varchar(255) DEFAULT NULL unique comment '角色组(部门)名称',
  role_group_code varchar(255) DEFAULT NULL unique comment '角色组(部门)名称编号',
  description varchar(255) DEFAULT NULL comment '备注描述',
  available bit(1) DEFAULT 1 comment '是否可用:1为可用,0为不可用',
  deleted bit(1) DEFAULT 0 comment '删除状态:1为删除,0为未删除',
  PRIMARY KEY (id)
);

/*****************************[8.sys_user_role_group]****************************************/
DROP TABLE IF EXISTS `sys_user_role_group`;
CREATE TABLE `sys_user_role_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `role_group_id` int(11) NOT NULL COMMENT '部门ID',
  `at_group` bit(1) DEFAULT b'1' COMMENT '是否还属于当前部门',
  `begin_time` date DEFAULT NULL COMMENT '用户所属部门：所属开始时间',
  `end_time` date DEFAULT NULL COMMENT '用户所属部门：所属结束时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;