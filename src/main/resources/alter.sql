
CREATE TABLE `consult` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(20) DEFAULT NULL COMMENT '姓名。',
  `mobile` varchar(20) DEFAULT NULL COMMENT '电话。',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱。',
  `scheme` varchar(100) DEFAULT NULL COMMENT '方案名称。',
  `intro` text COMMENT '问题描述。',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间。',
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间。',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='咨询表';


ALTER TABLE `sm_weixin`.`video`
ADD COLUMN `intro` TEXT CHARACTER SET 'utf8mb4' NULL COMMENT '视频简介。' AFTER `length`;

ALTER TABLE `sm_weixin`.`resource`
ADD COLUMN `attachment` VARCHAR(500) CHARACTER SET 'utf8mb4' NULL COMMENT 'demo附件。' AFTER `ref_demo`;

ALTER TABLE `sm_weixin`.`consult`
ADD COLUMN `open_id` VARCHAR(50) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'open_id' AFTER `id`;
