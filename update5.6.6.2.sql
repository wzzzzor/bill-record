DROP TABLE IF EXISTS `mfa_device_verify`;
CREATE TABLE `mfa_device_verify` (
  `id` varchar(36) NOT NULL COMMENT 'uuid',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建者',
  `updated_at` datetime DEFAULT NULL COMMENT '最后更新时间',
  `last_modifier` varchar(50) DEFAULT NULL COMMENT '最后的修改者',
  `name` varchar(64) DEFAULT NULL COMMENT '设备名称',
  `type` int(2) DEFAULT NULL COMMENT '类型：1虚拟化设备，2网络设备 3VPN 4防火墙 5堡垒机',
  `shard_key` varchar(64) DEFAULT NULL COMMENT '共享密钥',
  `server_ip` varchar(1024) DEFAULT NULL COMMENT '服务端ip地址',
  `verrify_type` int(2) DEFAULT NULL COMMENT '认证方式：1 静态密码 2 OTP 3 短信验证码 4 静态密码+OTP 5 静态密码+短信验证码',
  `auto_send_sms` int(2) DEFAULT '0' COMMENT '自动发送短信 0否 1是',
  `step_verrify` int(2) DEFAULT '0' COMMENT '分布认证 0否 1是',
  `status` int(2) DEFAULT '0' COMMENT '状态',
  `domain_id` varchar(36) DEFAULT NULL COMMENT '多租户Id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `namecheck` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='设备认证表';

DROP TABLE IF EXISTS `mfa_device_verified_server_ip_detail`;
CREATE TABLE `mfa_device_verified_server_ip_detail` (
  `id` varchar(36) NOT NULL COMMENT 'uuid',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建者',
  `updated_at` datetime DEFAULT NULL COMMENT '最后更新时间',
  `last_modifier` varchar(50) DEFAULT NULL COMMENT '最后的修改者',
  `device_verify_id` varchar(64) DEFAULT NULL COMMENT '设备认证id',
  `ip_value` varchar(64) DEFAULT NULL COMMENT '服务端iP地址',
  `num_start` bigint(10) DEFAULT NULL,
  `num_end` bigint(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='设备服务端ip表';
