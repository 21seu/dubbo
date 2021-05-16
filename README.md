# [dubbo](https://www.yuque.com/xialuodideshougao/tcriy3/vecruv)
## 1、分布式基础



### 1.1 什么是分布式系统

> 《分布式系统原理与范型》定义：分布式系统是若干独立计算机的集合，这些计算机对于用户来说就像单个相关系统。分布式系统（distributed system）是建立在网络之上的软件系统。



随着互联网的发展，网站应用的规模不断扩大，常规的垂直应用架构已无法应对，分布式服务架构以及流动计算架构势在必行，亟需**一个治理系统**确保架构有条不紊的演进。





### 1.2 发展演变

![image.png](https://cdn.nlark.com/yuque/0/2021/png/12759906/1619753043844-1629b6c6-4949-4f4e-ae49-d8ae955f5cc6.png)



> 单一应用架构

当网站流量很小时，只需一个应用，将所有功能都部署在一起，以减少部署节点和成本。此时，用于简化增删改查工作量的数据访问框架(ORM)是关键。

![image.png](https://cdn.nlark.com/yuque/0/2021/png/12759906/1619972059144-444801d9-49bb-4d50-8975-b0d01320b556.png)

适用于小型网站，小型管理系统，将所有功能都部署到一个功能里，简单易用。

缺点：

- 性能扩展性比较差
- 协同开发问题
- 不利于升级维护



> 垂直应用架构

当访问量逐渐增大，单一应用增加机器带来的加速度越来越小，将应用拆成互不相干的几个应用，以提升效率。此时，用于加速前端页面开发的Web框架(MVC)是关键。



![image.png](https://cdn.nlark.com/yuque/0/2021/png/12759906/1619972141382-65132b1c-0872-40b7-9f0a-b06d222e782c.png)



通过切分业务来实现各个模块独立部署，降低了维护和部署的难度，团队各司其职更易管理，性能扩展也更方便，更有针对性。

缺点：公用模块无法重复利用，开发性的浪费。



> 分布式服务架构

当垂直应用越来越多，应用之间交互不可避免，将核心业务抽取出来，作为独立的服务，逐渐形成稳定的服务中心，使前端应用能更快速的响应多变的市场需求。此时，用于提高业务复用及整合的**分布式服务框架****(RPC)**是关键。



![image.png](https://cdn.nlark.com/yuque/0/2021/png/12759906/1619972318026-9f7e3d74-48e1-4185-857c-04644de077e2.png)



> 流动计算架构



当服务越来越多，容量的评估，小服务资源的浪费等问题逐渐显现，此时需增加一个调度中心基于访问压力实时管理集群容量，提高集群利用率。此时，用于**提高机器利用率的资源调度和治理中心****(SOA)[ Service Oriented Architecture]****是关键**。

![image.png](https://cdn.nlark.com/yuque/0/2021/png/12759906/1619972440516-2b748437-203e-4b82-8ee8-e3b8c70d3b09.png)





### 1.3 RPC

#### 1.3.1 什么是RPC

> RPC【Remote Procedure Call】是指远程过程调用，是一种进程间通信方式，他是一种技术的思想，而不是规范。它允许程序调用另一个地址空间（通常是共享网络的另一台机器上）的过程或函数，而不用程序员显式编码这个远程调用的细节。即程序员无论是调用本地的还是远程的函数，本质上编写的调用代码基本相同.



#### 1.3.2 RPC基本原理

![image.png](https://cdn.nlark.com/yuque/0/2021/png/12759906/1619972714194-19285e6e-bcc7-4a71-ab3a-f3e0912b8c4c.png)



![image.png](https://cdn.nlark.com/yuque/0/2021/png/12759906/1619972850337-a098ea0a-53b4-4ecc-85c3-7a1d647b2fca.png)

注意：

1. RPC框架两个核心模块：通讯、序列化
2. 常见的RPC框架：dubbo、gRPC、Thrift、HSF(High Speed Service Framework)





## 2、[dubbo](https://dubbo.apache.org/zh/)



### 2.1 dubbo简介



Apache Dubbo (incubating) |ˈdʌbəʊ| 是一款高性能、轻量级的开源Java RPC框架，它提供了三大核心能力：面向接口的远程方法调用，智能容错和负载均衡，以及服务自动注册和发现。



![image.png](https://cdn.nlark.com/yuque/0/2021/png/12759906/1619973296815-5877e0c4-97bf-43b6-abd0-734ceca0abc2.png?x-oss-process=image%2Fresize%2Cw_2000)

灰度发布：比如有1个用户服务，在100台服务器上跑，现在对用户服务进行升级，但是不是很稳定，所以先选定20台服务器使用，等服务在这20台慢慢稳定了，则一点点更新剩余的80台服务。



### 2.2 基本概念

![image.png](https://cdn.nlark.com/yuque/0/2021/png/12759906/1619973685437-6f83b979-85cd-421e-98ab-9c57ce696217.png)

**`服务提供者（Provider）`** ：暴露服务的服务提供方，服务提供者在启动时，向注册中心注册自己提供的服务。



**`服务消费者（Consumer）`** ：调用远程服务的服务消费方，服务消费者在启动时，向注册中心订阅自己所需的服务，服务消费者，从提供者地址列表中，基于软负载均衡算法，选一台提供者进行调用，如果调用失败，再选另一台调用。



**`注册中心（Registry）`** ：注册中心返回服务提供者地址列表给消费者，如果有变更，注册中心将基于长连接推送变更数据给消费者。



**`监控中心（Monitor）`** ：服务消费者和提供者，在内存中累计调用次数和调用时间，定时每分钟发送一次统计数据到监控中心。



调用关系说明：

- 服务容器负责启动，加载，运行服务提供者。
- 服务提供者在启动时，向注册中心注册自己提供的服务。
- 服务消费者在启动时，向注册中心订阅自己所需的服务。
- 注册中心返回服务提供者地址列表给消费者，如果有变更，注册中心将基于长连接推送变更数据给消费者。
- 服务消费者，从提供者地址列表中，基于软负载均衡算法，选一台提供者进行调用，如果调用失败，再选另一台调用。
- 服务消费者和提供者，在内存中累计调用次数和调用时间，定时每分钟发送一次统计数据到监控中心。







## 3、dubbo环境搭建（windows）



### 3.1 安装zookeeper



1. [下载zookeeper](https://archive.apache.org/dist/zookeeper/zookeeper-3.4.13/)
2. 解压+安装zookeeper
3. 需要修改下zookeeper的配置文件：将conf下的zoo_sample.cfg复制一份改名为zoo.cfg即可，需要注意下面几个重要配置：

dataDir=./ 临时数据存储的目录（可写相对路径）

clientPort=2181  zookeeper的端口号

1. 使用zkCil.cmd测试：

| ls /                | 列出zookeeper根下保存的所有结点 |
| ------------------- | ------------------------------- |
| create -e /test 123 | 创建一个test节点，值为123       |
| get /test           | 获取/test节点的值               |



### 3.2 安装dubbo-admin（windows）



> dubbo本身并不是一个服务软件。它其实就是一个jar包能够帮你的java程序连接到zookeeper，并利用zookeeper消费、提供服务。所以你不用在Linux上启动什么dubbo服务。
>
> 但是为了让用户更好的管理监控众多的dubbo服务，官方提供了一个可视化的监控程序，不过这个监控即使不装也不影响使用。



1. [下载dubbo-admin](https://github.com/apache/incubator-dubbo-ops)

![image.png](https://cdn.nlark.com/yuque/0/2021/png/12759906/1620110016868-a9fc695f-81c8-4dd2-a6ba-5427612f84e5.png)

1. 进入目录，修改dubbo-admin的配置文件配置

![image.png](https://cdn.nlark.com/yuque/0/2021/png/12759906/1620110129627-a4b91f41-5d86-4545-b127-7c8d2d692d3d.png)

1. 使用`mvn clean package -Dmaven.test.skip=true`打包
2. 使用`java -jar xxx`运行jar；**注意：【有可能控制台看着启动了，但是网页打不开，需要在控制台按下ctrl+c即可】**

**![image.png](https://cdn.nlark.com/yuque/0/2021/png/12759906/1620110183963-e5342485-7662-4e76-a4b1-01d5284174be.png)**







## 4、dubbo程序入门



### 4.1 需求



> 现在有一个电商系统，其中的订单服务需要访问用户服务，去获取某个用户的所有地址；订单服务在服务器A，用户服务在服务器B，现在需要A可以去远程调用B的功能

| 序号 | 模块               | 功能           |
| ---- | ------------------ | -------------- |
| 1    | 订单服务web模块    | 创建订单等     |
| 2    | 用户服务server模块 | 查询用户地址等 |



### 4.2 工程架构设计

根据 dubbo《服务化最佳实践》



1. 分包：建议将服务接口，服务模型，服务异常等均放在 API 包中，因为服务模型及异常也是 API 的一部分，同时，这样做也符合分包原则：重用发布等价原则(REP)，共同重用原则(CRP)。如果需要，也可以考虑在 API 包中放置一份 spring 的引用配置，这样使用方，只需在spring 加载过程中引用此配置即可，配置建议放在模块的包目录下，以免冲突，如： com/alibaba/china/xxx/dubbo-reference.xml。
2. 粒度：服务接口尽可能大粒度，每个服务方法应代表一个功能，而不是某功能的一个步骤，否则将面临分布式事务问题，Dubbo 暂未提供分布式事务支持。服务接口建议以业务场景为单位划分，并对相近业务做抽象，防止接口数量爆炸。不建议使用过于抽象的通用接口，如：Map query(Map)，这样的接口没有明确语义，会给后期维护带来不便。

![image.png](https://cdn.nlark.com/yuque/0/2021/png/12759906/1620110701226-338c80da-1551-4d02-9c51-6674c4eb4ebc.png)





### 4.3 创建模块



1、gmall-interface公共模块：公共接口层（bean、service）

```java
package com.ftj.bean;

import java.io.Serializable;

/**
 * 用户地址
 * @author ftj
 *
 */
public class UserAddress implements Serializable {
    
    private Integer id;
    private String userAddress; //用户地址
    private String userId; //用户id
    private String consignee; //收货人
    private String phoneNum; //电话号码
    private String isDefault; //是否为默认地址    Y-是     N-否
    
    public UserAddress() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public UserAddress(Integer id, String userAddress, String userId, String consignee, String phoneNum,
                       String isDefault) {
        super();
        this.id = id;
        this.userAddress = userAddress;
        this.userId = userId;
        this.consignee = consignee;
        this.phoneNum = phoneNum;
        this.isDefault = isDefault;
    }
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getUserAddress() {
        return userAddress;
    }
    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getConsignee() {
        return consignee;
    }
    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }
    public String getPhoneNum() {
        return phoneNum;
    }
    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
    public String getIsDefault() {
        return isDefault;
    }
    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }
}
```

```java
public interface OrderService {
    
    /**
     * 初始化订单
     * @param userId
     */
    public List<UserAddress> initOrder(String userId);

}
```

```java
/**
 * 用户服务
 * @author lfy
 *
 */
public interface UserService {
    
    /**
     * 按照用户id返回所有的收货地址
     * @param userId
     * @return
     */
    public List<UserAddress> getUserAddressList(String userId);

}
```

2、创建服务提供者user-service-provider模块，对用户接口的实现；且首先需要在 `pom.xml` 引入gmall-interface模块。

```java
<dependency>
  <groupId>com.ftj.gmall</groupId>
  <artifactId>gmall-interface</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
public class UserServiceImpl implements UserService {
        
    @Override
    public List<UserAddress> getUserAddressList(String userId) {
        // TODO Auto-generated method stub
        return userAddressDao.getUserAddressById(userId);
    }
}
```



3、创建服务消费者order-service-consumer模块，同样首先需要在 `pom.xml` 引入gmall-interface模块。

```java
<dependency>
  <groupId>com.ftj.gmall</groupId>
  <artifactId>gmall-interface</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
public class OrderService {
    
    UserService userService;
    /**
     * 初始化订单，查询用户的所有地址并返回
     * @param userId
     * @return
     */
    public List<UserAddress> initOrder(String userId){
        return userService.getUserAddressList(userId);
    }
}
```



4、使用dubbo改造上述两个模块，首先引入dubbo和zookeeper相关的依赖。

```xml
        <!-- 引入dubbo -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>dubbo</artifactId>
            <version>2.6.2</version>
        </dependency>
    <!-- 由于我们使用zookeeper作为注册中心，所以需要操作zookeeper
             dubbo 2.6以前的版本引入zkclient操作zookeeper 
       dubbo 2.6及以后的版本引入curator操作zookeeper
       下面两个zk客户端根据dubbo版本2选1即可
    -->
        <dependency>
            <groupId>com.101tec</groupId>
            <artifactId>zkclient</artifactId>
            <version>0.10</version>
        </dependency>
        <!-- curator-framework -->
        <dependency>
            <groupId>org.apache.curator</groupId>
            <artifactId>curator-framework</artifactId>
            <version>2.12.0</version>
        </dependency>   
```



5、配置服务提供方`provider.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:dubbo="http://dubbo.apache.org/schema/dubbo"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans-4.3.xsd
       http://dubbo.apache.org/schema/dubbo
       http://dubbo.apache.org/schema/dubbo/dubbo.xsd">

    <!-- 指定当前服务/应用的名字（同样的服务名字相同，不要和别的服务同名） -->
    <dubbo:application name="user-service-provider"  />
    <!-- 指定注册中心的位置    使用multicast广播注册中心暴露服务地址   -->
    <!--<dubbo:registry address="multicast://224.5.6.7:1234" />-->
    <dubbo:registry address="zookeeper://127.0.0.1:2181"/>
    <!-- 指定通信规则（协议+端口）  用dubbo协议在20880端口暴露服务 -->
    <dubbo:protocol name="dubbo" port="20880" />
    <!-- 声明需要暴露的服务接口 -->
    <dubbo:service interface="com.ftj.service.UserService" ref="userServiceImpl" />
    <!--服务的实现-->
    <bean id="userServiceImpl" class="com.ftj.service.impl.UserServiceImpl"/>
    <!-- 和本地bean一样实现服务 -->
    <!--连接监控中心-->
    <dubbo:monitor protocol="registry"/>
</beans>
```



6、配置服务消费者的`consumer.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:dubbo="http://dubbo.apache.org/schema/dubbo" xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans-4.3.xsd
       http://dubbo.apache.org/schema/dubbo
       http://dubbo.apache.org/schema/dubbo/dubbo.xsd
       http://www.springframework.org/schema/context
       https://www.springframework.org/schema/context/spring-context.xsd">

    <!--包扫描-->
    <context:component-scan base-package="com.ftj.service.impl"/>
    <!-- 消费方应用名，用于计算依赖关系，不是匹配条件，不要与提供方一样 -->
    <dubbo:application name="oder-service-consumer"/>
    <!-- 使用multicast广播注册中心暴露发现服务地址 -->
    <dubbo:registry address="zookeeper://127.0.0.1:2181"/>
    <!-- 生成远程服务代理，可以和本地bean一样使用demoService check="false"-->
    <!--方法级优先级最高 精确优先，消费者设置优先（级别一样的情况下）-->
    <dubbo:reference timeout="5000" interface="com.ftj.service.UserService" id="userService">
        <dubbo:method name="getUserAddressList" timeout="1000"></dubbo:method>
    </dubbo:reference>
    <dubbo:monitor protocol="registry"/>
    <!--<dubbo:monitor address="127.0.0.1:7001"/>-->
    <!--配置当前消费者的统一规则：所有的服务都不检查-->
    <dubbo:consumer timeout="3000" check="false"></dubbo:consumer>
    <!--注册中心见检查-->
    <dubbo:registry check="false"/>
</beans>
```



7、分别启动服务提供方和服务消费者

```java
public class MainApplication {
    //服务提供者
    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext ioc = new ClassPathXmlApplicationContext("provider.xml");
        ioc.start();
        System.in.read();
    }
}

-------------------------------------------------------------------------------
    
public class MainApplication {

    //服务消费者
    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("consumer.xml");

        applicationContext.start();
        OrderService orderService = applicationContext.getBean(OrderService.class);
        orderService.initOrder("1");

        System.out.println("调用完成....");
        System.in.read();
    }
}
```

![image.png](https://cdn.nlark.com/yuque/0/2021/png/12759906/1620112408741-445a9676-86ab-4610-b327-f1eb5cf1dc64.png?x-oss-process=image%2Fresize%2Cw_2000)



## 5、监控中心



### 5.1 安装监控中心



1、首先需要[下载](https://github.com/apache/incubator-dubbo-ops)监控中心

2、解压之后就可使用，修改配置指定监控中心地址

![image.png](https://cdn.nlark.com/yuque/0/2021/png/12759906/1620117545173-3a446d4f-5d93-4458-8d1f-86f3ecd58d68.png)

3、启动监控中心（必须先启动zookeeper）

![image.png](https://cdn.nlark.com/yuque/0/2021/png/12759906/1620117617270-12d98a6e-0014-4435-9513-2efc7bc95661.png)

![image.png](https://cdn.nlark.com/yuque/0/2021/png/12759906/1620117691649-f99e5e08-55d7-47cb-881c-715c53d39d40.png)



### 5.2 监控中心配置

在需要监控服务中配置连接监控中心，进行监控统计。

```xml
<!-- 监控中心协议，如果为protocol="registry"，
表示从注册中心发现监控中心地址，否则直连监控中心 -->
<dubbo:monitor protocol="registry"></dubbo:monitor>
```

Simple Monitor 挂掉不会影响到 Consumer 和 Provider 之间的调用，所以用于生产环境不会有风险。

Simple Monitor 采用磁盘存储统计信息，请注意安装机器的磁盘限制，如果要集群，建议用mount共享磁盘。



## 6、整合springboot



1. 引入spring-boot-starter以及dubbo和curator依赖。

```xml
<dependency>
  <groupId>com.alibaba.boot</groupId>
  <artifactId>dubbo-spring-boot-starter</artifactId>
  <version>0.2.0</version>
</dependency>
```

1. 分别配置好服务提供者和服务消费者两个模块的`application.properties`。

```yaml
dubbo.application.name=user-service-provider
#指定注册中心的位置
dubbo.registry.address=127.0.0.1:2181
dubbo.registry.protocol=zookeeper
#指定通信规则（协议+端口）
dubbo.protocol.name=dubbo
dubbo.protocol.port=20881
#监控中心ym
dubbo.monitor.protocol=registry

server.port=8044
dubbo.application.name=order-service-consumer
dubbo.registry.address=zookeeper://localhost:2181
#监控中心协议，自己找
dubbo.monitor.protocol=registry

server.port=8045
```

1. 使用dobbo注解，进行测试。

​    注意：

- - @Service注解：服务提供方，在需要提供服务的接口实现类上注解，给到注册中心
  - @Reference注解：服务消费方，使用这个注解实现远程调用
  - @@EnableDubbo注解：如果`application.properties`配置文件中没有配置自动扫描注解`dubbo.scan.base-package`，那么可以使用这个注解自动扫描被dubbo注解的类或接口。

```java
@com.alibaba.dubbo.config.annotation.Service //暴露服务
@Component
public class UserServiceImpl implements UserService {

    @Override
    public List<UserAddress> getUserAddressList(String userId) {
        // TODO Auto-generated method stub
        System.out.println("用户ID====>" + userId);

        UserAddress address1 = new UserAddress(1, "北京市昌平区宏福科技园综合楼3层", "1", "李老师", "010-56253825", "Y");
        UserAddress address2 = new UserAddress(2, "深圳市宝安区西部硅谷大厦B座3层（深圳分校）", "1", "王老师", "010-56253825", "N");

        return Arrays.asList(address1, address2);
    }
}
```

```java
@Service
public class OrderServiceImpl implements OrderService {

    @Reference //远程调用
    UserService userService;

    @Override
    public List<UserAddress> initOrder(String userId) {

        // TODO Auto-generated method stub
        //1、查询用户的收货地址
        List<UserAddress> addressList = userService.getUserAddressList(userId);
        /*for (UserAddress userAddress : addressList) {
            System.out.println(userAddress);
        }
        System.out.println(addressList);*/
        return addressList;
    }
}
```

```java
/**
 *  1、导入dubbo-starter
 *  2、导入dubbo的其他依赖
 *  3、配置dubbo
 */
@EnableDubbo //开启基于注解的dubbo功能
@SpringBootApplication
public class MainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}
```





## 7、dubbo配置

### 7.1 配置原则

![image.png](https://cdn.nlark.com/yuque/0/2021/png/12759906/1621156210632-ca929a1a-e374-4100-9585-88df5868a85e.png)

JVM 启动 -D 参数优先，这样可以使用户在部署和启动时进行参数重写，比如在启动时需改变协议的端口。

XML 次之，如果在 XML 中有配置，则 dubbo.properties 中的相应配置项无效。

Properties 最后，相当于缺省值，只有 XML 没有配置时，dubbo.properties 的相应配置项才会生效，通常用于共享公共配置，比如应用名。



### 7.2 重试次数

失败自动切换，当出现失败，重试其它服务器，但重试会带来更长延迟。可通过 retries="2" 来设置重试次数(不含第一次)。

```xml
<!--重试次数配置如下：-->
<dubbo:service retries="2" />
<!--或-->
<dubbo:reference retries="2" />
<!--或-->
<dubbo:reference>
    <dubbo:method name="findFoo" retries="2" />
</dubbo:reference>
```



### 7.3 超时时间

由于网络或服务端不可靠，会导致调用出现一种不确定的中间状态（超时）。为了避免超时导致客户端资源（线程）挂起耗尽，必须设置超时时间。



#### 7.3.1 dubbo消费端

```xml
全局超时配置
<dubbo:consumer timeout="5000" />

指定接口以及特定方法超时配置
<dubbo:reference interface="com.foo.BarService" timeout="2000">
    <dubbo:method name="sayHello" timeout="3000" />
</dubbo:reference>
```

#### 7.3.2 dubbo服务端

```xml
全局超时配置
<dubbo:provider timeout="5000" />

指定接口以及特定方法超时配置
<dubbo:provider interface="com.foo.BarService" timeout="2000">
    <dubbo:method name="sayHello" timeout="3000" />
</dubbo:provider>
```

#### 7.3.3 dubbo配置原则

dubbo推荐在Provider上尽量多配置Consumer端属性：

```markdown
1、作服务的提供者，比服务使用方更清楚服务性能参数，如调用的超时时间，合理的重试次数，等等
2、在Provider配置后，Consumer不配置则会使用Provider的配置值，即Provider配置可以作为Consumer的缺省值。否则，Consumer会使用Consumer端的全局设置，这对于Provider不可控的，并且往往是不合理的
```

配置的覆盖规则：

1. 方法级配置别优于接口级别，即小Scope优先 
2. Consumer端配置 优于 Provider配置 优于 全局配置
3. 最后是Dubbo Hard Code的配置值（见配置文档）



![image.png](https://cdn.nlark.com/yuque/0/2021/png/12759906/1621158271551-8da31fea-d227-433d-8075-4568872eef92.png)



### 7.4 多版本

当一个接口实现，出现不兼容升级时，可以用版本号过渡，版本号不同的服务相互间不引用。

可以按照以下的步骤进行版本迁移：

- 在低压力时间段，先升级一半提供者为新版本
- 再将所有消费者升级为新版本
- 然后将剩下的一半提供者升级为新版本

```xml
老版本服务提供者配置：
<dubbo:service interface="com.foo.BarService" version="1.0.0" />

新版本服务提供者配置：
<dubbo:service interface="com.foo.BarService" version="2.0.0" />

老版本服务消费者配置：
<dubbo:reference id="barService" interface="com.foo.BarService" version="1.0.0" />

新版本服务消费者配置：
<dubbo:reference id="barService" interface="com.foo.BarService" version="2.0.0" />

如果不需要区分版本，可以按照以下的方式配置：
<dubbo:reference id="barService" interface="com.foo.BarService" version="*" />
```



### 7.5 本地存根

远程服务后，客户端通常只剩下接口，而实现全在服务器端，但提供方有些时候想在客户端也执行部分逻辑，比如：做 ThreadLocal 缓存，提前验证参数，调用失败后伪造容错数据等等，此时就需要在 API 中带上 Stub，客户端生成 Proxy 实例，会把 Proxy 通过构造函数传给 Stub（Stub 必须有可传入 Proxy 的构造函数。），然后把 Stub 暴露给用户，Stub 可以决定要不要去调 Proxy。

![image](https://cdn.nlark.com/yuque/0/2021/jpeg/12759906/1621169311238-63cbf69a-e406-4648-9ddd-5db4fd5a9a2f.jpeg)



创建一个本地存根（在 interface 旁边放一个 Stub 实现，它实现 BarService 接口，并有一个传入远程 BarService 实例的构造函数）

```java
package com.foo;
public class BarServiceStub implements BarService {
    private final BarService barService;
    
    // 构造函数传入真正的远程代理对象
    public BarServiceStub(BarService barService){
        this.barService = barService;
    }
 
    public String sayHello(String name) {
        // 此代码在客户端执行, 你可以在客户端做ThreadLocal本地缓存，或预先验证参数是否合法，等等
        try {
            return barService.sayHello(name);
        } catch (Exception e) {
            // 你可以容错，可以做任何AOP拦截事项
            return "容错数据";
        }
    }
}
```

消费者端配置本地存根

```xml
<dubbo:service interface="com.foo.BarService" stub="true" />
  或
<dubbo:service interface="com.foo.BarService" stub="com.foo.BarServiceStub" />
```

