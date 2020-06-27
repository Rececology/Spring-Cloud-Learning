# Spring Cloud Alibaba Learning

花了几天的时间了解了一下 Spring Cloud Alibaba 技术栈，做一下总结；

采用微服务系统架构，就势必会遇到这样几个问题：

* 这么多小服务，如何管理他们？(服务治理 注册中心[服务注册 发现 剔除])
* 这么多小服务，他们之间如何通讯？(restful rpc)
* 这么多小服务，客户端怎么访问他们？(网关)
* 这么多小服务，一旦出现问题了，应该如何自处理？(容错)
* 这么多小服务，一旦出现问题了，应该如何排错? (链路追踪)

## contents

[TOC]

了解项目相关可直奔 “三、实践”

## 一、Spring Cloud

> [Spring Cloud Offical Website]( https://spring.io/projects/spring-cloud )
>
> Spring Cloud 是 **一系列框架的有序集合 **

![Spring Cloud](https://s1.ax1x.com/2020/06/27/N6UT6U.jpg)

首先了解了 Spring Cloud，在知乎读到一篇文章 “[SpringCloud应该怎么入门]( https://www.zhihu.com/question/283286745/answer/763040709 )” 觉得不错，可以先有个**全貌：**

* 集群/分布式/微服务/SOA

  * 集群：**同一个业务**，部署在多个服务器上(不同的服务器运行同样的代码，干同一件事)
  * 分布式：一个业务分拆多个**子业务**，部署在不同的服务器上(不同的服务器，运行不同的代码，为了同一个目的)
  * 微服务：

* CAP理论

  **CAP理论定义的其实是在容忍网络分区的条件下，“强一致性”和“极致可用性”无法同时达到**

  * C：数据一致性(consistency) | **所有**节点拥有数据的最新版本
  * A：可用性(availability) | 数据具备高可用性

  * P：分区容错性(partition-tolerance) |**容忍网络出现分区**，分区之间网络不可达。

* SpringCloud 出现的缘由

  从分布式/微服务的角度而言：就是把我们一**大**的项目，**分解**成多个**小**的模块。这些小的模块组合起来，完成功能，拆分出多个模块以后，就会出现**各种各样**的问题，而SpringCloud提供了**一整套**的解决方案！

  SpringCloud的**基础功能**：

  - 服务治理： Spring Cloud Eureka
  - 客户端负载均衡： Spring Cloud Ribbon
  - 服务容错保护： Spring Cloud Hystrix
  - 声明式服务调用： Spring Cloud Feign
  - API网关服务：Spring Cloud Zuul
  - 分布式配置中心： Spring Cloud Config

  SpringCloud的高级功能：

  - 消息总线： Spring Cloud Bus
  - 消息驱动的微服务： Spring Cloud Stream
  - 分布式服务跟踪： Spring Cloud Sleuth

* 引出 Eureka

  子系统与子系统之间需要远程调用。（不能硬配置其他服务 ip 地址）

  为了解决微服务架构中的**服务实例维护问题(ip地址)**， 产生了大量的**服务治理**框架和产品。 这些框架和产品的实现都围绕着**服务注册与服务发现机制**来完成对微服务应用实例的**自动化管理**。

* 引出 RestTemplate 和 Ribbon

  使用Spring封装好的**RestTemplate**工具类，进行远程调用

  Ribbon：客户端负载均衡（服务实例的**清单在客户端**，客户端进行负载均衡算法分配）

  * Nginx 是服务端负载均衡

* 引出 Hystrix

  服务保护：如果某个服务出现问题，怎么解决？

  - Fallback(失败快速返回)：当某个服务单元发生故障（类似用电器发生短路）之后，通过断路器的故障监控（类似熔断保险丝）， **向调用方返回一个错误响应， 而不是长时间的等待**。这样就不会使得线程因调用故障服务被长时间占用不释放，**避免**了故障在分布式系统中的**蔓延**。
  - 资源/依赖隔离(线程池隔离)：它会为**每一个依赖服务创建一个独立的线程池**，这样就算某个依赖服务出现延迟过高的情况，也只是对该依赖服务的调用产生影响， 而**不会拖慢其他的依赖服务**。

* 引出 Feign

  声明式的（优雅的）服务调用，**整合**了 Spring Cloud Ribbon 与 Spring Cloud Hystrix

* 引出 Zuul

  路由规则与服务实例维护问题 | 签名校验登录校验冗余问题

  - SpringCloud Zuul通过与SpringCloud Eureka进行整合，将自身注册为Eureka服务治理下的应用，同时从Eureka中获得了所有其他微服务的实例信息。**外层调用都必须通过API网关**，使得**将维护服务实例的工作交给了服务治理框架自动完成**。
  - 在API网关服务上进行统一调用来**对微服务接口做前置过滤**，以实现对微服务接口的**拦截和校验**。

  注：

  - SpringCloud Zuul通过与SpringCloud Eureka进行整合，将自身注册为Eureka服务治理下的应用，同时从Eureka中获得了所有其他微服务的实例信息。**外层调用都必须通过API网关**，使得**将维护服务实例的工作交给了服务治理框架自动完成**。
  - 在API网关服务上进行统一调用来**对微服务接口做前置过滤**，以实现对微服务接口的**拦截和校验**。

* 引出 Spring Cloud Config

  服务配置文件修改繁琐

  Spring Cloud Config项目是一个解决分布式系统的配置管理方案。它包含了Client和Server两个部分，**server提供配置文件的存储、以接口的形式将配置文件的内容提供出去，client通过接口获取数据、并依据此数据初始化自己的应用**。

  - 简单来说，使用Spring Cloud Config就是将配置文件放到**统一的位置管理**(比如GitHub)，客户端通过接口去获取这些配置文件。
  - 在GitHub上修改了某个配置文件，应用加载的就是修改后的配置文件。

## 二、Spring Cloud Alibaba

> [Spring Cloud Alibaba Github Site](  https://github.com/alibaba/spring-cloud-alibaba  )
>
> Spring Cloud 的升级版 | 提供微服务开发的一站式解决方案

> 在 [Spring Cloud Alibaba Github Site](  https://github.com/alibaba/spring-cloud-alibaba ) 应该浏览的东西：

* Readme（描绘了**技术栈的全貌**）
* spring-cloud-alibaba-examples（给出了**组件们的编码实例**）

![spring-cloud-alibaba](https://s1.ax1x.com/2020/06/27/N6U7XF.jpg)

其实如果对 Spring Cloud 有一些了解那么对 Spring Cloud Alibaba 也不会陌生，在我看来，只是技术栈的不同，目的是一致的：解决微服务架构下可能出现的各种问题

> Spring Cloud Alibaba

### 2.1 主要功能

- **服务限流降级**：默认支持 WebServlet、WebFlux, OpenFeign、RestTemplate、Spring Cloud Gateway, Zuul, Dubbo 和 RocketMQ 限流降级功能的接入，可以在运行时通过控制台实时修改限流降级规则，还支持查看限流降级 Metrics 监控。
- **服务注册与发现**：适配 Spring Cloud 服务注册与发现标准，默认集成了 Ribbon 的支持。
- **分布式配置管理**：支持分布式系统中的外部化配置，配置更改时自动刷新。
- **消息驱动能力**：基于 Spring Cloud Stream 为微服务应用构建消息驱动能力。
- **分布式事务**：使用 @GlobalTransactional 注解， 高效并且对业务零侵入地解决分布式事务问题。。
- **阿里云对象存储**：阿里云提供的海量、安全、低成本、高可靠的云存储服务。支持在任何应用、任何时间、任何地点存储和访问任意类型的数据。
- **分布式任务调度**：提供秒级、精准、高可靠、高可用的定时（基于 Cron 表达式）任务调度服务。同时提供分布式的任务执行模型，如网格任务。网格任务支持海量子任务均匀分配到所有 Worker（schedulerx-client）上执行。
- **阿里云短信服务**：覆盖全球的短信服务，友好、高效、智能的互联化通讯能力，帮助企业迅速搭建客户触达通道。

### 2.2 组件

**[Sentinel](https://github.com/alibaba/Sentinel)**：把流量作为切入点，从流量控制、熔断降级、系统负载保护等多个维度保护服务的稳定性。

**[Nacos](https://github.com/alibaba/Nacos)**：一个更易于构建云原生应用的动态服务发现、配置管理和服务管理平台。

**[RocketMQ](https://rocketmq.apache.org/)**：一款开源的分布式消息系统，基于高可用分布式集群技术，提供低延时的、高可靠的消息发布与订阅服务。

**[Dubbo](https://github.com/apache/dubbo)**：Apache Dubbo™ 是一款高性能 Java RPC 框架。

**[Seata](https://github.com/seata/seata)**：阿里巴巴开源产品，一个易于使用的高性能微服务分布式事务解决方案。

**[Alibaba Cloud ACM](https://www.aliyun.com/product/acm)**：一款在分布式架构环境中对应用配置进行集中管理和推送的应用配置中心产品。

**[Alibaba Cloud OSS](https://www.aliyun.com/product/oss)**: 阿里云对象存储服务（Object Storage Service，简称 OSS），是阿里云提供的海量、安全、低成本、高可靠的云存储服务。您可以在任何应用、任何时间、任何地点存储和访问任意类型的数据。

**[Alibaba Cloud SchedulerX](https://help.aliyun.com/document_detail/43136.html)**: 阿里中间件团队开发的一款分布式任务调度产品，提供秒级、精准、高可靠、高可用的定时（基于 Cron 表达式）任务调度服务。

**[Alibaba Cloud SMS](https://www.aliyun.com/product/sms)**: 覆盖全球的短信服务，友好、高效、智能的互联化通讯能力，帮助企业迅速搭建客户触达通道。

## 三、实践

>素材：[spring cloud Alibaba技术栈讲解]( https://www.bilibili.com/video/BV1R7411774f ) 等
>
>topic：电商项目中的商品、订单、用户

**技术栈：Spring Cloud Alibaba**

* Nacos：服务治理与发现
* Sentinel：服务容错
* Gateway：服务网关
* Sleuth：链路追踪
* RocketMQ：消息驱动
* Seata：分布式事务
* Dubbo：rpc 通信

**技术选型：**

* maven：3.6.2
* mysql：5.7 （使用 8 版本在 nacos 配置时需要自己编译）
* 持久层：SpringData JPA
* 服务器：阿里云 CentOS 7

```shell
# 模块：
springcloud-alibaba 父工程
|--shop-common 公共模块【实体类】
|--shop-user 用户微服务 【端口: 807x】
|--shop-product 商品微服务 【端口: 808x】
|--shop-order 订单微服务 【端口: 809x】

# 注: 在父工程中设定依赖版本,避免冲突
```

下面只是对一些关键部分做一些记录：

### 3.1 Nacos

---

> 发现、配置管理和服务管理平台 | 注册中心

#### 3.1.1 nacos-discovery

服务治理与发现（管理 ip）

#### 3.1.2 nacos-config

服务配置（管理配置文件）

* 动态刷新 @RefreshScope
* 配置共享 profiles.active | shared-dataids

#### 3.1.3 基于 Ribbon 实现负载均衡

​	RestTemplate 上 @LoadBalanced

#### 3.1.4 基于 Feign 实现服务调用

​	@FeignClient 声明式调用（根据服务名）



### 3.2 Sentinel 服务容错

---

> 把**流量**作为切入点，从**流量控制、熔断降级、系统负载保护**等多个维度保护服务的稳定性

**backgroud**

Question：如果单个服务出现问题，调用这个服务就会出现网络延迟，此时若有大量的网络涌入，会形成任务堆积，最终导致服务瘫痪。**（服务雪崩）**

Tool：JMETER 压测

Solutions：隔离、超时、限流、熔断、降级

---





#### 3.2.1 Console

```bash
# Console: localhost:8080
# 直接使用jar命令启动项目(控制台本身是一个SpringBoot项目)
java -Dserver.port=8080 -Dcsp.sentinel.dashboard.server=localhost:8080 -
Dproject.name=sentinel-dashboard -jar sentinel-dashboard-1.7.0.jar
```

#### 3.2.2 流量控制

* 阈值：QPS | 线程数
* 流控模式：
  * 直接：对该资源
  * 关联：关联资源达到某个阈值
  * 链路：针对上级接口（需要改版本）

* 流控效果：快速失败 | 排队等待 | warm up

#### 3.2.3 降级规则

降级规则：平均响应时间 | 异常比例 | 异常数

#### 3.2.4 热点规则

更细粒度，允许将规则具体到参数上

#### 3.2.5 授权规则

根据来源：白名单 | 黑名单

#### 3.2.6 系统规则

整体维度，仅对入口流量有效；

Load | RT | 线程数 | 入口QPS | CPU 使用率

#### 3.2.7 @SentinelResource

定义资源，并提供可选的异常处理和 fallback 配置项

```java
@SentinelResource(
	value = "message",
	blockHandler = "blockHandler",//指定发生BlockException时进入的方法
	fallback = "fallback"//指定发生Throwable时进入的方法)
```

#### 3.2.8 持久化

#### 3.2.9 Feign 整合 Sentinel

```java
@FeignClient(value = "service-product"
        fallback = ProductServiceFallback.class,
        fallbackFactory = ProductServiceFallbackFactory.class)
```



### 3.3 Gateway

---

#### 3.3.1 路由配置

基础版：配置文件配置路由 route

增强版：从 nacos 中获取微服务地址

简写版：网关默认与nacos配合，根据服务名访问

#### 3.3.2 断言 | 过滤器

断言：条件判断，为真才会执行路由

过滤器：对请求响应做手脚，可做 token 检验

#### 3.3.3 网关限流

自定义



### 3.4 Sleth

---

> 链路追踪

同样的还有 skywalking，不少开源项目再用 skywalking

集成 Zipkin Console

```shell
java -jar zipkin-server-2.12.9-exec.jar
```

数据持久化：mysql | elasticsearch



### 3.5 Rocketmq

---

> 分布式消息系统，基于高可用分布式集群技术，提供低延时的、高可靠的**消息发布与订阅服务**

异步解耦 | 流量削峰

**：：踩坑最多的一个服务：：**

Nameserver | Broker | Topic | Producer （Group） | Consumer（Group）

RocketMQ Console

#### 3.5.1 消息传送

```java
Message:

1.producer.send(msg,timeout);
  consumer.subscribe();

2.rocketMQTemplate.convertAndSend(topic,tag)
  @RocketMQMessageListener(consumerGroup = "shop-user", topic = "order-topic")
```

#### 3.5.2 消息类型

```
Message
|普通消息：
|--可靠同步	syncSend
|--可靠异步	asyncSend
|--单向发送	sendOneWay
|   
|顺序消息：syncSendOrderly
|
|事务消息：createOrderBefore---executeLocalTransaction---checkLocalTransaction
|
```

#### 3.5.3 @RocketMQMessageListener

```java
@RocketMQMessageListener(
	consumerGroup = "shop",//消费者分组
	topic = "order-topic",//要消费的主题
	consumeMode = ConsumeMode.CONCURRENTLY, //消费模式:无序和有序
	messageModel = MessageModel.CLUSTERING, //消息模式:广播和集群,默认是集群
)
```



### 3.6 Seata

---

> 分布式事务解决方案

* TC：事务协调器
* TM：事务管理器
* RM：资源管理器

registry.conf | undo_log | DataSourceProxyConfig | @GlobalTransactional



### 3.7 Dubbo

---

> 高性能 Java RPC 框架

```yaml
dubbo:
	scan:
		base-packages: com.itheima.service.impl # 开启包扫描
	protocols:
		dubbo:
			name: dubbo # 服务协议
			port: -1 # 服务端口
	registry:
		address: spring-cloud://localhost # 注册中心
	cloud:
		subscribed-services: service-product # 订阅的提供者名称	
```





