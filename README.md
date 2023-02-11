# 考核

[TOC]

## Tips

### 数据库

| 描述             | 地址        | 端口  | 账号       | 密码      |
| ---------------- | ----------- | ----- | ---------- | --------- |
| Oracle测试数据库 | 175.6.36.9  | 21521 | hnpadisall | Hnwjw123  |
| MySQL测试数据库  | 175.6.129.5 | 3306  | root       | Xuyue128@ |

### 时间

2022.11.28~2022.12.01

### 目标

实现任务描述所有功能，最终代码上传至gitlab仓库，将Java可执行程序打包成Jar包并完成部署（部署环境不限于Linux或Win）。

**注：项目涉及多个数据库，建议使用多数据源**。



## 车辆信息录入系统（Oracle）

### 1、数据库搭建

```sql
-- 自行完成数据库搭建，测试数据自行需保留数据库脚本语句，该处保留数据脚本语句

```

### 2、接口服务

- **接口以 Restful API 为接口规范**

1. #### 新增

   提供一个服务，接收数据，4个字段（出站口编号、车牌、出站时间、出站口编号），保存数据库（ID、出站口编号、车牌、出站时间、出站口编号、入库时间、状态、处理人、处理时间）。

2. #### 查询

   分页查询，支持多条件查询，主要实现：根据出站口编号查询状态为 0 的车牌，按时间倒序。

3. #### 统计分析

   根据数据库车牌号码，统计各省份车牌号码数据，JSON数据主体格式如下：

   ```json
   {
       //截至时间
       date:'2022-11-28 11:30:23',
       //数据数组
       data:[
           {
               province:'湖南',
               code:'43',
               plate:'湘',
               num:'21'
           },
           {
               'province':'广东',
               'code':'44',
               'plate':'广',
               'num':'7'
           },
           .....
       ]
   }
   ```



## 信访系统（MySql）

数据库在测试服务器已经存在，以下为数据初始设计SQL语句：

```sql
drop table if exists tb_dict;
drop table if exists tb_dict_item;
drop table if exists tb_department;
drop table if exists tb_user;
drop table if exists tb_role;
drop table if exists tb_menu;
drop table if exists tb_user_role;
drop table if exists tb_role_menu;
drop table if exists tb_petition;
drop table if exists tb_petition_annex;
drop table if exists tb_petition_details;
drop table if exists tb_petition_result;
drop table if exists tb_petition_source;
drop table if exists tb_petition_type;

CREATE TABLE `tb_dict`  (
  `id` bigint unsigned   NOT NULL AUTO_INCREMENT COMMENT 'id 主键 自增',
  `dict_name` varchar(20) NOT NULL DEFAULT '' COMMENT '字典名称 20位',
  `dict_code` varchar(20) NOT NULL DEFAULT '' COMMENT '字典编码 20位',
  `description` varchar(20) NOT NULL DEFAULT '' COMMENT '描述20位',
  `default_value` varchar(30) NOT NULL DEFAULT '' COMMENT '默认值 10位',
  `create_id` bigint unsigned NOT NULL COMMENT '创建人id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_id` bigint unsigned NOT NULL COMMENT '修改人id',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_delete` tinyint unsigned NOT NULL DEFAULT 0 COMMENT '逻辑删除 0 未删除 1 已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE uk_dict_code(`dict_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_is_0900_ai_ci COMMENT = '数据字典表';


CREATE TABLE `tb_dict_item`  (
  `id` bigint unsigned   NOT NULL AUTO_INCREMENT COMMENT 'id 主键 自增',
  `dict_id` bigint unsigned   NOT NULL COMMENT '字典id',
  `item_text` varchar(20) NOT NULL DEFAULT '' COMMENT '字典项文本 20位',
  `item_value` varchar(30) NOT NULL DEFAULT '' COMMENT '字典项值 10位',
  `sort_order` int(10) NULL DEFAULT 0 COMMENT '排序',
  `create_id` bigint unsigned NOT NULL COMMENT '创建人id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_id` bigint unsigned NOT NULL COMMENT '修改人id',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_delete` tinyint unsigned NOT NULL DEFAULT 0 COMMENT '逻辑删除 0 未删除 1 已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX idx_dict_id(`dict_id`) USING BTREE,
  INDEX idx_sort_order(`sort_order`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_is_0900_ai_ci COMMENT = '数据字典项表';

CREATE TABLE `tb_department`  (
  `id` bigint unsigned  NOT NULL AUTO_INCREMENT COMMENT '部门id 主键 自增',
  `department_name` varchar(20) NOT NULL COMMENT '部门名称 20位',
  `pid` bigint unsigned NOT NULL COMMENT '上级部门id 20位',
  `create_id` bigint unsigned NOT NULL COMMENT '创建人id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_id` bigint unsigned NOT NULL COMMENT '修改人id',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_delete` tinyint unsigned NOT NULL DEFAULT 0 COMMENT '逻辑删除 字典is_delete 0 未删除 1 已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE uk_department_name(`department_name`) USING BTREE,
  INDEX idx_pid(`pid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_is_0900_ai_ci COMMENT = '系统账号信息表';



CREATE TABLE `tb_user`  (
  `id` bigint unsigned  NOT NULL COMMENT '用户id 主键 雪花生成',
  `name` varchar(20) NOT NULL COMMENT '用户姓名 20位',
  `phone` varchar(20) NOT NULL COMMENT '手机号码 20位',
  `idcard` varchar(20) NOT NULL COMMENT '身份证 20位',
  `username` varchar(20) NOT NULL COMMENT '账号 20位',
  `password` varchar(64) NOT NULL COMMENT '密码 20位',
  `salt` varchar(10) NOT NULL DEFAULT '' COMMENT '盐',
  `orgcode` varchar(12) NOT NULL DEFAULT '' COMMENT '组织机构代码 12位',
  `addresscode` varchar(12) NOT NULL DEFAULT '' COMMENT '地址码 12位',
  `department_id` bigint unsigned NOT NULL DEFAULT 0 COMMENT '部门id',
  `create_id` bigint unsigned NOT NULL COMMENT '创建人id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_id` bigint unsigned NOT NULL COMMENT '修改人id',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_delete` tinyint unsigned NOT NULL DEFAULT 0 COMMENT '逻辑删除 字典is_delete 0 未删除 1 已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE `uk_username`(`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_is_0900_ai_ci COMMENT = '系统账号信息表';


CREATE TABLE `tb_role`  (
  `id` bigint unsigned  NOT NULL AUTO_INCREMENT COMMENT '角色id 自增',
  `role_code` varchar(20) NOT NULL COMMENT '角色code, 唯一约束',
  `role_info` varchar(200) DEFAULT NULL COMMENT '角色描述',
  `create_id` bigint unsigned NOT NULL COMMENT '创建人id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_id` bigint unsigned NOT NULL COMMENT '修改人id',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_delete` tinyint unsigned NOT NULL DEFAULT 0 COMMENT '逻辑删除 0 未删除 1 已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE `uk_role_code`(`role_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_is_0900_ai_ci COMMENT = '系统角色表';

CREATE TABLE `tb_menu`  (
  `id` bigint unsigned  NOT NULL AUTO_INCREMENT COMMENT '权限id 自增',
  `menu_name` varchar(50) NOT NULL DEFAULT '' COMMENT '菜单名称',
  `menu_code` varchar(50) NOT NULL DEFAULT '' COMMENT '菜单唯一标识码',
  `parent_id` bigint unsigned DEFAULT NULL COMMENT '父节点',
  `node_type` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '节点类型，字典menu_type 1菜单，2页面，3按钮',
  `icon_url` varchar(255) DEFAULT '' COMMENT '图标地址',
  `sort` int(11) NOT NULL DEFAULT '1' COMMENT '排序号',
  `link_url` varchar(500) DEFAULT '' COMMENT '页面对应的地址',
  `create_id` bigint unsigned NOT NULL COMMENT '创建人id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_id` bigint unsigned NOT NULL COMMENT '修改人id',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_delete` tinyint unsigned NOT NULL DEFAULT 0 COMMENT '逻辑删除 0 未删除 1 已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX idx_parent_id (`parent_id`) USING BTREE,
  UNIQUE uk_menu_code (`menu_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_is_0900_ai_ci COMMENT = '系统菜单表';

CREATE TABLE `tb_user_role`  (
  `id` bigint unsigned  NOT NULL AUTO_INCREMENT COMMENT 'id 自增',
  `user_id` bigint unsigned NOT NULL COMMENT '用户id',
  `role_id` bigint unsigned NOT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX idx_user_id (user_id) USING BTREE,
  KEY idx_role_id (role_id) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_is_0900_ai_ci COMMENT = '用户-角色表';

CREATE TABLE `tb_role_menu`  (
  `id` bigint unsigned  NOT NULL AUTO_INCREMENT COMMENT 'id 自增',
  `role_id` bigint unsigned NOT NULL COMMENT '角色id',
  `menu_id` bigint unsigned NOT NULL COMMENT '菜单id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX idx_role_id (role_id) USING BTREE,
  INDEX idx_menu_id (menu_id) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_is_0900_ai_ci COMMENT = '角色-菜单表';
-- 方式 来源

CREATE TABLE `tb_petition`  (
  `id` bigint unsigned  NOT NULL COMMENT '信访id 主键 雪花生成',
  `details_id` bigint unsigned NULL COMMENT '详细内容id',
  `result_id` bigint unsigned NULL COMMENT '处理结果id',
  `petition_source_id` bigint unsigned  NOT NULL COMMENT '信访来源id',
  `petition_type_id` bigint unsigned  NOT NULL COMMENT '信访类型id',
  `petition_way` varchar(20) NOT NULL DEFAULT '' COMMENT '信访方式 字典 petition_way 20位',
  `petition_title` varchar(100) NOT NULL DEFAULT '' COMMENT '标题 100位',
  `petition_oid` varchar(30) NOT NULL DEFAULT '' COMMENT '原始编号 30位',
  `petition_sid` varchar(30) NOT NULL DEFAULT '' COMMENT '查询编码 30位',
  `is_open` tinyint unsigned NOT NULL DEFAULT 1 COMMENT '是否公开 字典 is_open  0 不公开 1 公开',
  `owning_site` varchar(30) NOT NULL DEFAULT '' COMMENT '所属站点 30位',
  `occur_area_code` varchar(12) NOT NULL DEFAULT '' COMMENT '发生地区 地址码',
  `occur_area_add` varchar(100) NOT NULL DEFAULT '' COMMENT '发生地区补充',
  `over_time` datetime  COMMENT '超时时间',
  `visitor_sum` varchar(20) NOT NULL DEFAULT '' COMMENT '来访人数 4位',
  `register_name` varchar(20) NOT NULL DEFAULT '' COMMENT '登记人姓名 20位',
  `submit_time` datetime COMMENT '提交时间',
  `talk_name` varchar(100) NOT NULL DEFAULT '' COMMENT '接谈人姓名 20位',
  `talk_time` datetime COMMENT '接谈日期',
  `manager_name` varchar(100) NOT NULL DEFAULT '' COMMENT '经办人姓名 20位',
  `manager_time` datetime COMMENT '经办人日期',
  `petition_name` varchar(20) NOT NULL DEFAULT '' COMMENT '信访人姓名 20位',
  `petition_phone` varchar(20) NOT NULL DEFAULT '' COMMENT '信访人电话 20位',
  `petition_id_card` varchar(20) NOT NULL DEFAULT '' COMMENT '信访人证件号码 20位',
  `petition_id_card_type` tinyint unsigned NOT NULL DEFAULT 0 COMMENT '信访人证件类型 字典id_type 1 身份证',
  `petition_email` varchar(40) NOT NULL DEFAULT '' COMMENT '信访人邮箱 40位',
  `petition_ip` varchar(20) NOT NULL DEFAULT '' COMMENT '信访人提交ip 20位',
  `petition_job_title` varchar(20) NOT NULL DEFAULT '' COMMENT '信访人职务 20位',
  `petition_contact_address_code` varchar(12) NOT NULL DEFAULT '' COMMENT '信访人联系地址码',
  `petition_contact_address` varchar(100) NOT NULL DEFAULT '' COMMENT '信访人联系地址补充 100位',
  `petition_post_code` varchar(10) NOT NULL DEFAULT '' COMMENT '信访人邮编 10位',
  `hidden_content` varchar(100) NOT NULL DEFAULT '' COMMENT '隐藏内容 100位',
  `department_id` bigint unsigned DEFAULT NULL COMMENT '受理部门id',
  `handler_id`  bigint unsigned  DEFAULT NULL COMMENT '处理人id',
  `handler_status` tinyint unsigned NOT NULL DEFAULT 0 COMMENT '处理状态 字典handler_status 1 待分配 2待处理 3已处理 4已退回',
  `is_time_out` tinyint unsigned NOT NULL DEFAULT 0 COMMENT '是否超时 0 未超时 1 已超时',
  `create_id` bigint unsigned NOT NULL COMMENT '创建人id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_id` bigint unsigned NOT NULL COMMENT '修改人id',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_delete` tinyint unsigned NOT NULL DEFAULT 0 COMMENT '逻辑删除 0 未删除 1 已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX idx_petition_title (petition_title) USING BTREE,
  INDEX idx_handler_id (handler_id) USING BTREE,
  INDEX idx_petition_oid (petition_oid) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_is_0900_ai_ci COMMENT = '信访表';


CREATE TABLE `tb_petition_annex`  (
  `id` bigint unsigned  NOT NULL COMMENT '信访附件id 主键 雪花生成',
  `petition_id` bigint unsigned  NOT NULL COMMENT '信访id',
  `annex_name` varchar(100) NOT NULL COMMENT '附件名称 100位',
  `annex_url` varchar(500) NOT NULL COMMENT '附件下载地址 500位',
  `annex_type` tinyint unsigned NOT NULL DEFAULT 1  COMMENT '附件类型 annex_type 1 信访信息附件 2 信访处理附件',
  `create_id` bigint unsigned NOT NULL COMMENT '创建人id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_id` bigint unsigned NOT NULL COMMENT '修改人id',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_delete` tinyint unsigned NOT NULL DEFAULT 0 COMMENT '逻辑删除 0 未删除 1 已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX idx_petition_id (petition_id) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_is_0900_ai_ci COMMENT = '信访附件表';

CREATE TABLE `tb_petition_details`  (
  `id` bigint unsigned  NOT NULL COMMENT '信访详细内容id 主键 雪花生成',
  `content` text NOT NULL COMMENT '详细内容',
  `create_id` bigint unsigned NOT NULL COMMENT '创建人id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_id` bigint unsigned NOT NULL COMMENT '修改人id',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_delete` tinyint unsigned NOT NULL DEFAULT 0 COMMENT '逻辑删除 0 未删除 1 已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_is_0900_ai_ci COMMENT = '信访内容表';

CREATE TABLE `tb_petition_result`  (
  `id` bigint unsigned  NOT NULL COMMENT '信访详细内容id 主键 雪花生成',
  `content` text NOT NULL COMMENT '处理结果',
  `create_id` bigint unsigned NOT NULL COMMENT '创建人id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_id` bigint unsigned NOT NULL COMMENT '修改人id',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_delete` tinyint unsigned NOT NULL DEFAULT 0 COMMENT '逻辑删除 0 未删除 1 已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_is_0900_ai_ci COMMENT = '信访结果表';

CREATE TABLE `tb_petition_source`  (
  `id` bigint unsigned  NOT NULL COMMENT '信访来源id 主键 雪花生成',
  `content` text NOT NULL COMMENT '信访来源',
  `create_id` bigint unsigned NOT NULL COMMENT '创建人id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_id` bigint unsigned NOT NULL COMMENT '修改人id',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_delete` tinyint unsigned NOT NULL DEFAULT 0 COMMENT '逻辑删除 0 未删除 1 已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_is_0900_ai_ci COMMENT = '信访来源表';


CREATE TABLE `tb_petition_type`  (
  `id` bigint unsigned  NOT NULL COMMENT '信访分类id 主键 雪花生成',
  `content` text NOT NULL COMMENT '信访分类',
  `create_id` bigint unsigned NOT NULL COMMENT '创建人id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_id` bigint unsigned NOT NULL COMMENT '修改人id',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_delete` tinyint unsigned NOT NULL DEFAULT 0 COMMENT '逻辑删除 0 未删除 1 已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_is_0900_ai_ci COMMENT = '信访分类表';



INSERT INTO `tb_department`(`department_name`, `pid`,`create_id`,`update_id`)
VALUES ('湖南省卫生健康委员会', '0','1539076856779440129','1539076856779440129');
INSERT INTO `tb_department`(`department_name`, `pid`,`create_id`,`update_id`)
VALUES ('卫健委信访办', '1','1539076856779440129','1539076856779440129');
INSERT INTO `tb_department`(`department_name`, `pid`,`create_id`,`update_id`)
VALUES ('卫健委办公室', '1','1539076856779440129','1539076856779440129');
INSERT INTO `tb_department`(`department_name`, `pid`,`create_id`,`update_id`)
VALUES ('卫健委人事处', '1','1539076856779440129','1539076856779440129');
INSERT INTO `tb_department`(`department_name`, `pid`,`create_id`,`update_id`)
VALUES ('卫健委规划处', '1','1539076856779440129','1539076856779440129');
INSERT INTO `tb_department`(`department_name`, `pid`,`create_id`,`update_id`)
VALUES ('卫健委财务处', '1','1539076856779440129','1539076856779440129');
INSERT INTO `tb_department`(`department_name`, `pid`,`create_id`,`update_id`)
VALUES ('卫健委法规处', '1','1539076856779440129','1539076856779440129');
INSERT INTO `tb_department`(`department_name`, `pid`,`create_id`,`update_id`)
VALUES ('卫健委体改处', '1','1539076856779440129','1539076856779440129');
INSERT INTO `tb_department`(`department_name`, `pid`,`create_id`,`update_id`)
VALUES ('卫健委疾控处', '1','1539076856779440129','1539076856779440129');
INSERT INTO `tb_department`(`department_name`, `pid`,`create_id`,`update_id`)
VALUES ('卫健委医政医管处', '1','1539076856779440129','1539076856779440129');
INSERT INTO `tb_department`(`department_name`, `pid`,`create_id`,`update_id`)
VALUES ('卫健委基层处', '1','1539076856779440129','1539076856779440129');
INSERT INTO `tb_department`(`department_name`, `pid`,`create_id`,`update_id`)
VALUES ('卫健委应急办', '1','1539076856779440129','1539076856779440129');
INSERT INTO `tb_department`(`department_name`, `pid`,`create_id`,`update_id`)
VALUES ('卫健委科教处', '1','1539076856779440129','1539076856779440129');
INSERT INTO `tb_department`(`department_name`, `pid`,`create_id`,`update_id`)
VALUES ('卫健委监督处', '1','1539076856779440129','1539076856779440129');
INSERT INTO `tb_department`(`department_name`, `pid`,`create_id`,`update_id`)
VALUES ('卫健委医政处', '1','1539076856779440129','1539076856779440129');
INSERT INTO `tb_department`(`department_name`, `pid`,`create_id`,`update_id`)
VALUES ('卫健委食品处', '1','1539076856779440129','1539076856779440129');
INSERT INTO `tb_department`(`department_name`, `pid`,`create_id`,`update_id`)
VALUES ('卫健委老龄处', '1','1539076856779440129','1539076856779440129');
INSERT INTO `tb_department`(`department_name`, `pid`,`create_id`,`update_id`)
VALUES ('卫健委妇幼处', '1','1539076856779440129','1539076856779440129');
INSERT INTO `tb_department`(`department_name`, `pid`,`create_id`,`update_id`)
VALUES ('卫健委职业健康处', '1','1539076856779440129','1539076856779440129');
INSERT INTO `tb_department`(`department_name`, `pid`,`create_id`,`update_id`)
VALUES ('卫健委人口家庭处', '1','1539076856779440129','1539076856779440129');
INSERT INTO `tb_department`(`department_name`, `pid`,`create_id`,`update_id`)
VALUES ('卫健委宣传处', '1','1539076856779440129','1539076856779440129');
INSERT INTO `tb_department`(`department_name`, `pid`,`create_id`,`update_id`)
VALUES ('卫健委国际处', '1','1539076856779440129','1539076856779440129');
INSERT INTO `tb_department`(`department_name`, `pid`,`create_id`,`update_id`)
VALUES ('卫健委保健办', '1','1539076856779440129','1539076856779440129');
INSERT INTO `tb_department`(`department_name`, `pid`,`create_id`,`update_id`)
VALUES ('卫健委离退处', '1','1539076856779440129','1539076856779440129');
INSERT INTO `tb_department`(`department_name`, `pid`,`create_id`,`update_id`)
VALUES ('卫健委机关党委', '1','1539076856779440129','1539076856779440129');
INSERT INTO `tb_department`(`department_name`, `pid`,`create_id`,`update_id`)
VALUES ('卫健委机关纪委', '1','1539076856779440129','1539076856779440129');

INSERT INTO `tb_user`(`id`, `name`, `username`,`phone`,`idcard`,`password`, `salt`,`department_id`,`create_id`,`update_id`)
VALUES ('1539076856779440129', '刘宇康', 'admin','17673944369','430524199910023279','09a7c2a0726bb3dd955ae8d3c93017c2',
  'sM7P^D','1','1539076856779440129','1539076856779440129');
INSERT INTO `tb_role`(`role_code`, `role_info`,`create_id`,`update_id`)
VALUES ('system_admin', '系统管理员','1539076856779440129','1539076856779440129');
INSERT INTO `tb_role`(`role_code`, `role_info`,`create_id`,`update_id`)
VALUES ('wjw_admin', '卫建委信访办','1539076856779440129','1539076856779440129');
INSERT INTO `tb_role`(`role_code`, `role_info`,`create_id`,`update_id`)
VALUES ('wjw_user', '卫建委机关处室、直属单位','1539076856779440129','1539076856779440129');
INSERT INTO `tb_user_role`(`user_id`, `role_id`) VALUES ('1539076856779440129', '1');
INSERT INTO `tb_role_menu`(`role_id`, `menu_id`)
VALUES ('2', '1'),('2', '2'),('2', '5'),('2', '6'),('2', '7'),('2', '8'),('2', '9'),('2', '10'),('2', '11'),('2', '12'),('2', '19'),('2', '20'),('2', '21'),('2', '22'),('2', '23'),('2', '24'),('2', '25'),('2', '26'),('2', '27');
INSERT INTO `tb_role_menu`(`role_id`, `menu_id`)
VALUES ('3', '6'),('3', '26'),('3', '27');

INSERT INTO `tb_dict`(`dict_name`, `dict_code`,`description`,`default_value`,`create_id`,`update_id`)
VALUES ('逻辑删除字段', 'is_delete','逻辑删除字段的字典','0','1539076856779440129','1539076856779440129');
INSERT INTO `tb_dict_item`(`dict_id`, `item_text`,`item_value`,`create_id`,`update_id`)
VALUES ('1', '不删除','0','1539076856779440129','1539076856779440129');
INSERT INTO `tb_dict_item`(`dict_id`, `item_text`,`item_value`,`create_id`,`update_id`)
VALUES ('1', '删除','1','1539076856779440129','1539076856779440129');

INSERT INTO `tb_dict`(`dict_name`, `dict_code`,`description`,`default_value`,`create_id`,`update_id`)
VALUES ('菜单类型字段', 'menu_type','菜单类型字段字典','1','1539076856779440129','1539076856779440129');
INSERT INTO `tb_dict_item`(`dict_id`, `item_text`,`item_value`,`create_id`,`update_id`)
VALUES ('2', '菜单','1','1539076856779440129','1539076856779440129');
INSERT INTO `tb_dict_item`(`dict_id`, `item_text`,`item_value`,`create_id`,`update_id`)
VALUES ('2', '页面','2','1539076856779440129','1539076856779440129');
INSERT INTO `tb_dict_item`(`dict_id`, `item_text`,`item_value`,`create_id`,`update_id`)
VALUES ('2', '按钮','3','1539076856779440129','1539076856779440129');

INSERT INTO `tb_dict`(`dict_name`, `dict_code`,`description`,`default_value`,`create_id`,`update_id`)
VALUES ('是否公开信访信息字段', 'is_open','是否公开信访信息字段字典','0','1539076856779440129','1539076856779440129');
INSERT INTO `tb_dict_item`(`dict_id`, `item_text`,`item_value`,`create_id`,`update_id`)
VALUES ('3', '不公开','0','1539076856779440129','1539076856779440129');
INSERT INTO `tb_dict_item`(`dict_id`, `item_text`,`item_value`,`create_id`,`update_id`)
VALUES ('3', '公开','1','1539076856779440129','1539076856779440129');

INSERT INTO `tb_dict`(`dict_name`, `dict_code`,`description`,`default_value`,`create_id`,`update_id`)
VALUES ('信访方式字段', 'petition_way','信访方式字段字典','1','1539076856779440129','1539076856779440129');
INSERT INTO `tb_dict_item`(`dict_id`, `item_text`,`item_value`,`create_id`,`update_id`)
VALUES ('4', '来信','1','1539076856779440129','1539076856779440129');
INSERT INTO `tb_dict_item`(`dict_id`, `item_text`,`item_value`,`create_id`,`update_id`)
VALUES ('4', '来电','2','1539076856779440129','1539076856779440129');
INSERT INTO `tb_dict_item`(`dict_id`, `item_text`,`item_value`,`create_id`,`update_id`)
VALUES ('4', '来访','3','1539076856779440129','1539076856779440129');

INSERT INTO `tb_dict`(`dict_name`, `dict_code`,`description`,`default_value`,`create_id`,`update_id`)
VALUES ('身份证类型字段', 'id_type','身份证类型字段字典','1','1539076856779440129','1539076856779440129');
INSERT INTO `tb_dict_item`(`dict_id`, `item_text`,`item_value`,`create_id`,`update_id`)
VALUES ('5', '身份证','1','1539076856779440129','1539076856779440129');
INSERT INTO `tb_dict_item`(`dict_id`, `item_text`,`item_value`,`create_id`,`update_id`)
VALUES ('5', '护照','2','1539076856779440129','1539076856779440129');
INSERT INTO `tb_dict_item`(`dict_id`, `item_text`,`item_value`,`create_id`,`update_id`)
VALUES ('5', '港澳台','3','1539076856779440129','1539076856779440129');
INSERT INTO `tb_dict_item`(`dict_id`, `item_text`,`item_value`,`create_id`,`update_id`)
VALUES ('5', '其他','4','1539076856779440129','1539076856779440129');

INSERT INTO `tb_dict`(`dict_name`, `dict_code`,`description`,`default_value`,`create_id`,`update_id`)
VALUES ('处理状态字段', 'handler_status','处理状态字段字典','1','1539076856779440129','1539076856779440129');
INSERT INTO `tb_dict_item`(`dict_id`, `item_text`,`item_value`,`create_id`,`update_id`)
VALUES ('6', '待分配','1','1539076856779440129','1539076856779440129');
INSERT INTO `tb_dict_item`(`dict_id`, `item_text`,`item_value`,`create_id`,`update_id`)
VALUES ('6', '已分配','2','1539076856779440129','1539076856779440129');
INSERT INTO `tb_dict_item`(`dict_id`, `item_text`,`item_value`,`create_id`,`update_id`)
VALUES ('6', '已处理','3','1539076856779440129','1539076856779440129');
INSERT INTO `tb_dict_item`(`dict_id`, `item_text`,`item_value`,`create_id`,`update_id`)
VALUES ('6', '已退回','4','1539076856779440129','1539076856779440129');

INSERT INTO `tb_dict`(`dict_name`, `dict_code`,`description`,`default_value`,`create_id`,`update_id`)
VALUES ('信访附件类型字段', 'annex_type','信访附件类型字段字典','1','1539076856779440129','1539076856779440129');
INSERT INTO `tb_dict_item`(`dict_id`, `item_text`,`item_value`,`create_id`,`update_id`)
VALUES ('7', '信访信息附件','1','1539076856779440129','1539076856779440129');
INSERT INTO `tb_dict_item`(`dict_id`, `item_text`,`item_value`,`create_id`,`update_id`)
VALUES ('7', '信访处理结果附件','2','1539076856779440129','1539076856779440129');

INSERT INTO `tb_dict`(`dict_name`, `dict_code`,`description`,`default_value`,`create_id`,`update_id`)
VALUES ('导入模板类型', 'template_type','导入模板类型字典','1','1539076856779440129','1539076856779440129');
INSERT INTO `tb_dict_item`(`dict_id`, `item_text`,`item_value`,`create_id`,`update_id`)
VALUES ('8', '来访基本情况登记表','1','1539076856779440129','1539076856779440129');
INSERT INTO `tb_dict_item`(`dict_id`, `item_text`,`item_value`,`create_id`,`update_id`)
VALUES ('8', '省长信箱·信件下载','2','1539076856779440129','1539076856779440129');

INSERT INTO `tb_dict`(`dict_name`, `dict_code`,`description`,`default_value`,`create_id`,`update_id`)
VALUES ('是否超时', 'is_time_out','是否超时字典','0','1539076856779440129','1539076856779440129');
INSERT INTO `tb_dict_item`(`dict_id`, `item_text`,`item_value`,`create_id`,`update_id`)
VALUES ('9', '未超时','0','1539076856779440129','1539076856779440129');
INSERT INTO `tb_dict_item`(`dict_id`, `item_text`,`item_value`,`create_id`,`update_id`)
VALUES ('9', '已超时','1','1539076856779440129','1539076856779440129');

-- 1
INSERT INTO `tb_menu`(`menu_name`, `menu_code`,`parent_id`,`node_type`,`icon_url`,`link_url`,`create_id`,`update_id`)
VALUES ('系统管理', '','0','1','el-icon-s-tools','','1539076856779440129','1539076856779440129');
-- 2
INSERT INTO `tb_menu`(`menu_name`, `menu_code`,`parent_id`,`node_type`,`icon_url`,`link_url`,`create_id`,`update_id`)
VALUES ('用户管理', 'system-user','1','2','el-icon-s-tools','/user_manage','1539076856779440129','1539076856779440129');
-- 3
INSERT INTO `tb_menu`(`menu_name`, `menu_code`,`parent_id`,`node_type`,`icon_url`,`link_url`,`create_id`,`update_id`)
VALUES ('角色管理', 'system-role','1','2','el-icon-s-tools','/role_manage','1539076856779440129','1539076856779440129');
-- 4
INSERT INTO `tb_menu`(`menu_name`, `menu_code`,`parent_id`,`node_type`,`icon_url`,`link_url`,`create_id`,`update_id`)
VALUES ('菜单管理', 'system-menu','1','2','el-icon-s-tools','/menu_manage','1539076856779440129','1539076856779440129');
-- 5
INSERT INTO `tb_menu`(`menu_name`, `menu_code`,`parent_id`,`node_type`,`icon_url`,`link_url`,`create_id`,`update_id`)
VALUES ('部门管理', 'system-department','1','2','el-icon-s-tools','/department_manage','1539076856779440129','1539076856779440129');
-- 6
INSERT INTO `tb_menu`(`menu_name`, `menu_code`,`parent_id`,`node_type`,`icon_url`,`link_url`,`create_id`,`update_id`)
VALUES ('信访记录管理', 'system-petition','0','2','el-icon-s-tools','/petition_manage','1539076856779440129','1539076856779440129');
-- 7
INSERT INTO `tb_menu`(`menu_name`, `menu_code`,`parent_id`,`node_type`,`icon_url`,`link_url`,`create_id`,`update_id`)
VALUES ('信访记录统计', 'petition-count','0','2','el-icon-s-tools','/petition_count','1539076856779440129','1539076856779440129');
-- 8
INSERT INTO `tb_menu`(`menu_name`, `menu_code`,`parent_id`,`node_type`,`icon_url`,`link_url`,`create_id`,`update_id`)
VALUES ('分类管理', 'petition-type','1','2','el-icon-s-tools','/xfsort_manage','1539076856779440129','1539076856779440129');
-- 9
INSERT INTO `tb_menu`(`menu_name`, `menu_code`,`parent_id`,`node_type`,`icon_url`,`link_url`,`create_id`,`update_id`)
VALUES ('来源管理', 'petition-source','1','2','el-icon-s-tools','/xfsource_manage','1539076856779440129','1539076856779440129');
-- 10
INSERT INTO `tb_menu`(`menu_name`, `menu_code`,`parent_id`,`node_type` ,`icon_url`,`link_url`,`sort`,`create_id`,`update_id`)
VALUES ('首页', 'petition-home','0','2','el-icon-s-tools','/home','2','1539076856779440129','1539076856779440129');


INSERT INTO `tb_menu`(`menu_name`, `menu_code`,`parent_id`,`node_type`,`icon_url`,`link_url`,`create_id`,`update_id`)
VALUES ('添加用户', 'system-user-add','2','3','','','1539076856779440129','1539076856779440129');
INSERT INTO `tb_menu`(`menu_name`, `menu_code`,`parent_id`,`node_type`,`icon_url`,`link_url`,`create_id`,`update_id`)
VALUES ('修改用户', 'system-user-update','2','3','','','1539076856779440129','1539076856779440129');
INSERT INTO `tb_menu`(`menu_name`, `menu_code`,`parent_id`,`node_type`,`icon_url`,`link_url`,`create_id`,`update_id`)
VALUES ('删除用户', 'system-user-delete','2','3','','','1539076856779440129','1539076856779440129');

INSERT INTO `tb_menu`(`menu_name`, `menu_code`,`parent_id`,`node_type`,`icon_url`,`link_url`,`create_id`,`update_id`)
VALUES ('添加角色', 'system-role-add','3','3','','','1539076856779440129','1539076856779440129');
INSERT INTO `tb_menu`(`menu_name`, `menu_code`,`parent_id`,`node_type`,`icon_url`,`link_url`,`create_id`,`update_id`)
VALUES ('修改角色', 'system-role-update','3','3','','','1539076856779440129','1539076856779440129');
INSERT INTO `tb_menu`(`menu_name`, `menu_code`,`parent_id`,`node_type`,`icon_url`,`link_url`,`create_id`,`update_id`)
VALUES ('删除角色', 'system-role-delete','3','3','','','1539076856779440129','1539076856779440129');


INSERT INTO `tb_menu`(`menu_name`, `menu_code`,`parent_id`,`node_type`,`icon_url`,`link_url`,`create_id`,`update_id`)
VALUES ('添加菜单', 'system-menu-add','4','3','','','1539076856779440129','1539076856779440129');
INSERT INTO `tb_menu`(`menu_name`, `menu_code`,`parent_id`,`node_type`,`icon_url`,`link_url`,`create_id`,`update_id`)
VALUES ('修改菜单', 'system-menu-update','4','3','','','1539076856779440129','1539076856779440129');
INSERT INTO `tb_menu`(`menu_name`, `menu_code`,`parent_id`,`node_type`,`icon_url`,`link_url`,`create_id`,`update_id`)
VALUES ('删除菜单', 'system-menu-delete','4','3','','','1539076856779440129','1539076856779440129');


INSERT INTO `tb_menu`(`menu_name`, `menu_code`,`parent_id`,`node_type`,`icon_url`,`link_url`,`create_id`,`update_id`)
VALUES ('添加部门', 'system-department-add','5','3','','','1539076856779440129','1539076856779440129');
INSERT INTO `tb_menu`(`menu_name`, `menu_code`,`parent_id`,`node_type`,`icon_url`,`link_url`,`create_id`,`update_id`)
VALUES ('修改部门', 'system-department-update','5','3','','','1539076856779440129','1539076856779440129');
INSERT INTO `tb_menu`(`menu_name`, `menu_code`,`parent_id`,`node_type`,`icon_url`,`link_url`,`create_id`,`update_id`)
VALUES ('删除部门', 'system-department-delete','5','3','','','1539076856779440129','1539076856779440129');


INSERT INTO `tb_menu`(`menu_name`, `menu_code`,`parent_id`,`node_type`,`icon_url`,`link_url`,`create_id`,`update_id`)
VALUES ('添加信访记录', 'system-petition-add','6','3','','','1539076856779440129','1539076856779440129');
INSERT INTO `tb_menu`(`menu_name`, `menu_code`,`parent_id`,`node_type`,`icon_url`,`link_url`,`create_id`,`update_id`)
VALUES ('修改信访记录', 'system-petition-update','6','3','','','1539076856779440129','1539076856779440129');
INSERT INTO `tb_menu`(`menu_name`, `menu_code`,`parent_id`,`node_type`,`icon_url`,`link_url`,`create_id`,`update_id`)
VALUES ('删除信访记录', 'system-petition-delete','6','3','','','1539076856779440129','1539076856779440129');
INSERT INTO `tb_menu`(`menu_name`, `menu_code`,`parent_id`,`node_type`,`icon_url`,`link_url`,`create_id`,`update_id`)
VALUES ('分配信访记录', 'system-petition-assign','6','3','','','1539076856779440129','1539076856779440129');
INSERT INTO `tb_menu`(`menu_name`, `menu_code`,`parent_id`,`node_type`,`icon_url`,`link_url`,`create_id`,`update_id`)
VALUES ('处理信访记录', 'system-petition-process','6','3','','','1539076856779440129','1539076856779440129');
INSERT INTO `tb_menu`(`menu_name`, `menu_code`,`parent_id`,`node_type`,`icon_url`,`link_url`,`create_id`,`update_id`)
VALUES ('退回信访记录', 'system-petition-back','6','3','','','1539076856779440129','1539076856779440129');

-- 省长信箱  省政府12315  政协云微建议  金点子意见收集 等平台
INSERT INTO `tb_petition_source`(`id`, `content`,`create_id`,`update_id`)
VALUES ('1539076856779440001', '省长信箱','1539076856779440129','1539076856779440129');
INSERT INTO `tb_petition_source`(`id`, `content`,`create_id`,`update_id`)
VALUES ('1539076856779440002', '省政府12315','1539076856779440129','1539076856779440129');
INSERT INTO `tb_petition_source`(`id`, `content`,`create_id`,`update_id`)
VALUES ('1539076856779440003', '政协云微建议','1539076856779440129','1539076856779440129');
INSERT INTO `tb_petition_source`(`id`, `content`,`create_id`,`update_id`)
VALUES ('1539076856779440004', '金点子意见收集','1539076856779440129','1539076856779440129');

-- 医疗医患纠纷 计划生育节育手术并发症  计生奖扶政策和失独家庭问题  要求解决乡村接生员和医生等待遇问题 其他

INSERT INTO `tb_petition_type`(`id`, `content`,`create_id`,`update_id`)
VALUES ('1539076856779440005', '医疗医患纠纷','1539076856779440129','1539076856779440129');
INSERT INTO `tb_petition_type`(`id`, `content`,`create_id`,`update_id`)
VALUES ('1539076856779440006', '计生奖扶政策和失独家庭问题','1539076856779440129','1539076856779440129');
INSERT INTO `tb_petition_type`(`id`, `content`,`create_id`,`update_id`)
VALUES ('1539076856779440007', '计划生育节育手术并发症','1539076856779440129','1539076856779440129');
INSERT INTO `tb_petition_type`(`id`, `content`,`create_id`,`update_id`)
VALUES ('1539076856779440008', '乡村接生员和医生等待遇问题','1539076856779440129','1539076856779440129');
INSERT INTO `tb_petition_type`(`id`, `content`,`create_id`,`update_id`)
VALUES ('1539076856779440009', '其他','1539076856779440129','1539076856779440129');
```

### 1、接口服务

- **接口以 Restful API 为接口规范**

1. #### 通过用户id查询用户的全部菜单

   数据结果以接口的形式返回

2. #### 统计指定时间范围内不同信访方式的数量

   数据结果以接口的形式返回

### 

