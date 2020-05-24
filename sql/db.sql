CREATE  DATABASE seckill;
USE seckill;

CREATE TABLE seckill(
	`seckill_id`  BIGINT NOT NULL AUTO_INCREMENT COMMENT '商品库存id',
	`name`  VARCHAR(120)  NOT NULL COMMENT '商品名称',
	`price`  VARCHAR(20)  NOT NULL COMMENT '单价',
	`number` INT NOT NULL COMMENT '库存数量',
	`start_time` TIMESTAMP NOT NULL COMMENT '秒杀开启时间',
	`end_time` TIMESTAMP NOT NULL COMMENT '秒杀结束时间',
	`create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	PRIMARY KEY (seckill_id),
	KEY idx_start_time(start_time),
	KEY idx_end_time(end_time),
	KEY idx_create_time(create_time)
)ENGINE=INNODB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT '秒杀库存表'


--初始化数据
INSERT INTO seckill(NAME,price,number,start_time,end_time)
VALUES
('苹果',0.01,100,'2020-5-12 00:00:00','2025-8-20 00:00:00'),
('西瓜',0.01,100,'2020-5-12 00:00:00','2026-5-20 00:00:00'),
('香蕉',0.01,100,'2019-5-12 00:00:00','2019-5-20 00:00:00'),
('橙子',0.01,100,'2025-5-12 00:00:00','2026-5-20 00:00:00'),
('雪梨',0.01,100,'2020-5-12 00:00:00','2026-5-20 00:00:00'),
('芒果',0.01,100,'2020-5-12 00:00:00','2026-5-20 00:00:00')

--秒杀成功明细表
--用户登录认证相关的信息
CREATE TABLE success_kill(
	seckill_id  BIGINT NOT NULL COMMENT '秒杀商品id',
	user_phone  BIGINT NOT NULL COMMENT '用户手机号',
	state  TINYINT NOT NULL DEFAULT-3 COMMENT '状态标识，-3无效， -2订单过期，-1支付时间过期订单不可见，0下单未付款可支付，1已付款',
    order_number VARCHAR(50) NOT NULL COMMENT '订单号',
    price  VARCHAR(20)  NOT NULL COMMENT '单价',
	create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	PRIMARY KEY(order_number),
	KEY idx_create_time(create_time)
)ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT '秒杀成功明细表'

SELECT * FROM seckill;
SELECT * FROM success_kill;


CREATE TABLE user
(
   user_id  BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户id',
   user_phone  BIGINT NOT NULL COMMENT '用户手机号',
   PRIMARY KEY (user_id)
)ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT '用户表'


CREATE TABLE setting_info
(
   id  BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id',
   public_ip  VARCHAR(200) NOT NULL COMMENT '公网IP',
   app_id  VARCHAR(200) NOT NULL COMMENT 'APPID对应支付宝账号',
   merchant_private_key  TEXT NOT NULL COMMENT '商户私钥',
   alipay_public_key  TEXT NOT NULL COMMENT '支付宝公钥',
   username  VARCHAR(200) NOT NULL COMMENT '用户账户',
   PRIMARY KEY (username),
   KEY info_id(id)
)ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT '配置信息表'



