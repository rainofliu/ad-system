# 项目简介



本项目是基于Spring Cloud搭建的广告系统，主要分为如下几个模块



- ad-eureka 		服务注册中心
- ad-gateway      网关
- ajin-ad-service  核心业务部分
  - ad-common   通用模块
  - ad-search  广告检索系统
  - ad-sponsor  广告投放系统





> 项目最重要的是广告检索系统和广告投放系统，广告投放系统给广告主投放广告数据，广告检索系统负责同步广告主投放的广告信息，并负责对外提供广告检索服务。





# 技术选择



1. 微服务
   - Spring Cloud Eureka     注册中心
   - Spring Cloud Zuul      网关



2. 数据库相关

   

   - [mysql-binlog-connector-java](https://github.com/shyiko/mysql-binlog-connector-java)

     

     开源binlog同步工具 同步binlog

     

   - JPA  



