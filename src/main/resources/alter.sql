
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



ALTER TABLE `sm_weixin`.`resource`
CHANGE COLUMN `banner` `banner` VARCHAR(200) CHARACTER SET 'ascii' NULL COMMENT 'url。' ,
CHANGE COLUMN `thumbnail` `thumbnail` VARCHAR(200) CHARACTER SET 'ascii' NULL COMMENT 'url。' ;

-- 20150327
ALTER TABLE `sm_weixin`.`resource`
ADD COLUMN `email` VARCHAR(100) CHARACTER SET 'utf8mb4' NULL DEFAULT NULL COMMENT '售前人员提醒邮箱' AFTER `attachment`;
ALTER TABLE `sm_weixin`.`consult`
ADD COLUMN `scheme_id` INT(11) NULL DEFAULT NULL COMMENT 'resourceId' AFTER `intro`;




/**

 {"button": [
    {
        "name": "资料",
        "sub_button": [
            {
                "type": "view",
                "name": "产品",
                "url":"http://productone.chinacloudapp.cn/weixin/web/productIntro",
                "sub_button": [ ]
            },
            {
                "type": "view",
                "name": "方案",
            "url":"http://productone.chinacloudapp.cn/weixin/web/resourceList?categoryType=schemes",
                "sub_button": [ ]
            },
            {
                "type": "view",
                "name": "案例",
                "url":"http://productone.chinacloudapp.cn/weixin/web/resourceList?categoryType=cases",
                "sub_button": [ ]
            }
        ]
    },
    {
        "name": "演示",
        "sub_button": [
            {
                "type": "view",
                "name": "视频",
                "url": "http://productone.chinacloudapp.cn/weixin/web/videoList",
                "sub_button": [ ]
            },
            {
                "type": "view",
                "name": "Demo",
                "url":"http://productone.chinacloudapp.cn/weixin/web/resourceList?categoryType=demos",
                "sub_button": [ ]
            }
        ]
    },
    {
        "name":"咨询",
     "sub_button":[
     {
         "type":"click",
         "name":"我要咨询",
         "key":"consult"
      },
      {
         "type":"click",
         "name":"我的咨询历史",
         "key":"consults"
      },
      {
         "type":"click",
         "name":"微社区",
         "key":"community"
      }]
    }
]}


 */