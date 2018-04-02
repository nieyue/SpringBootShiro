# 数据库
#创建数据库
DROP DATABASE IF EXISTS shiro_db;
CREATE DATABASE shiro_db;

#使用数据库
use shiro_db;

#创建角色表
CREATE TABLE role_tb(
role_id int(11) NOT NULL AUTO_INCREMENT COMMENT '角色id',
name varchar(255) COMMENT '角色名',
duty varchar(255) COMMENT '角色职责',
update_date datetime COMMENT '更新时间',
PRIMARY KEY (role_id)
)ENGINE = InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='角色表';

#创建权限表
CREATE TABLE permission_tb(
permission_id int(11) NOT NULL AUTO_INCREMENT COMMENT '权限id',
type tinyint(4) COMMENT '权限类型，默认0开放，1鉴权',
manager_name varchar(255) COMMENT '权限管理名',
name varchar(255) COMMENT '权限名',
route varchar(255) unique COMMENT '权限路由',
update_date datetime COMMENT '更新时间',
PRIMARY KEY (permission_id),
INDEX INDEX_TYPE (type) USING BTREE,
INDEX INDEX_MANAGERNAME (manager_name) USING BTREE,
INDEX INDEX_NAME (name) USING BTREE,
INDEX INDEX_ROUTE (route) USING BTREE
)ENGINE = InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='权限表';

#创建角色权限表
CREATE TABLE role_permission_tb(
role_permission_id int(11) NOT NULL AUTO_INCREMENT COMMENT '角色权限id',
region tinyint(4) COMMENT "范围，1公共，2自身",
role_id int(11) COMMENT '角色id,外键',
permission_id int(11) COMMENT '权限id,外键',
update_date datetime COMMENT '更新时间',
PRIMARY KEY (role_permission_id),
UNIQUE KEY UNIQUE_ROLEID_PERMISSIONID (role_id,permission_id),
INDEX INDEX_REGION (region) USING BTREE,
INDEX INDEX_ROLEID (role_id) USING BTREE,
INDEX INDEX_PERMISSIONID (permission_id) USING BTREE
)ENGINE = InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='角色权限表';

#创建账户表
CREATE TABLE account_tb(
account_id int(11) NOT NULL AUTO_INCREMENT COMMENT '账户id',
phone varchar(255) COMMENT '注册手机号',
password varchar(255) COMMENT '密码',
nickname varchar(255) COMMENT '昵称',
icon varchar(255) COMMENT '图像',
sex tinyint(4) DEFAULT 0 COMMENT '性别,默认为0未知，为1男性，为2女性',
country varchar(255) COMMENT '国家,默认中国',
realname varchar(255) COMMENT '真实姓名',
email varchar(255) COMMENT 'email',
safety_grade tinyint(4) COMMENT '安全等级，1低，2中，3高',
auth tinyint(4) COMMENT '认证，0没认证，1审核中，2已认证',
card_secret_receive tinyint(4) COMMENT '卡密接受方式，0全部接收，1本账号内，2邮箱接收，3手机接收',
identity_cards varchar(255) COMMENT '身份证',
identity_cards_hold_img varchar(255) COMMENT '手持身份证上半身照',
identity_cards_front_img varchar(255) COMMENT '身份证正面',
identity_cards_back_img varchar(255) COMMENT '身份证反面',
create_date datetime COMMENT '创建时间',
login_date datetime COMMENT '登陆时间',
status tinyint DEFAULT 0 COMMENT '状态，默认0正常，1锁定，2，异常',
role_id int(11) COMMENT '角色id外键',
role_name varchar(255) COMMENT '角色名',
PRIMARY KEY (account_id),
INDEX INDEX_AUTH (auth) USING BTREE,
INDEX INDEX_PHONE (phone) USING BTREE,
INDEX INDEX_REALNAME (realname) USING BTREE,
INDEX INDEX_CREATEDATE (create_date) USING BTREE,
INDEX INDEX_LOGINDATE (login_date) USING BTREE,
INDEX INDEX_ROLEID (role_id) USING BTREE,
INDEX INDEX_STATUS (status) USING BTREE
)ENGINE = InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='账户表';

#设置初始管理员密码MD5加密123456
INSERT IGNORE INTO account_tb (nickname,phone,email,password,create_date,login_date,role_id,role_name)
VALUES ("聂跃","15111336587","278076304@qq.com","11874bb6149dd45428da628c9766b252",now(),now(),"1000","超级管理员");