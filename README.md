# 基于 SSM 的高并发下单+支付订单项目

**开发环境：** IntelliJ IDEA + maven + git + windows

**部署环境：** Centos+tomcat8

**软件架构：** Spring + SpringMVC + Mybatis   + Redis + MySQL 

**系统描述：** 本项目是基于 SSM 框架开发的前后端分离电商网站，缓存采用Redis，数据库采用的是 MySQL。包含产品，下单和在线支付3个模块。整个项目使用到事务管理，定时任务，Redis缓存和Cookies存取等技术。

**项目介绍：**

系统分为展示层，服务层和持久层。展示层主要包括商 品列表、下单页面、订单页面和二维码付款页面。服务层处理相关的业务流程，整合了下单和支付的功能，保证订单状态和商品数量的同步。持久层使用MySQL数据库保存数据，并且使用Redis缓存提高系统反应速度。

**项目架构图：**

![avatar](/pic/shop架构图.jpg)

**业务流程图：**

![avatar](/pic/shop业务流程图.jpg)

**调用支付宝接口时序图：**

![avatar](/pic/shop调用支付宝接口时序图.jpg)





**项目展示图：**

![avatar](/pic/项目展示图.jpg)



参考文章：

http://www.imooc.com/article/302820?block_id=tuijian_wz

https://opendocs.alipay.com/open/194/106078