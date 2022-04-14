nacos

## nacos简介

### 为什么叫nacos

nacos：Dynamic Naming and Configuration Service

### 是什么

一个更易于构建云原生应用的动态服务发现、配置管理和服务管理平台。

Nacos就是注册中心 + 配置中心的组合

nacos=eureka+config+Bus

### 能干嘛

替代Eureka做服务注册中心

替代Config做服务配置中心

### 比较

据说 Nacos 在阿里巴巴内部有超过 10 万的实例运行，已经过了类似双十一等各种大型流量的考验

![img](.\pic\img.png)

## 下载安装

[文档](https://nacos.io/zh-cn/docs/quick-start.html)

[下载](https://github.com/alibaba/nacos/releases)

本示例用的是2.0.4版本，由于最新版本2.0.4使用会出现问题，后面更改为1.4.2版本

下载zip包然后解压然后进入到bin目录下运行，执行命令如下

```java
startup.cmd -m standalone
```

然后访问http://192.168.125.110:8848/nacos，输入账号和密码，账号和密码分别为nacos,nacos.

## rookie-springcloud-nacos项目

创建rookie-springcloud-nacos项目

### 服务提供者

创建rookie-nacos-payment-provider,如果需要进行集群启动，直接将项目打包为jar,然后打开三个命令行窗口，直接运行如下命令即可

```java
java -jar -Dspring.profiles.active=8001 target/rookie-nacos-payment-provider-1.0.0.jar
java -jar -Dspring.profiles.active=8002 target/rookie-nacos-payment-provider-1.0.0.jar
java -jar -Dspring.profiles.active=8003 target/rookie-nacos-payment-provider-1.0.0.jar
```

### 服务消费者

创建rookie-nacos-order-consumer项目

然后直接运行，访问http://localhost/consumer/payment/nacos/1即可

## 服务注册中心对比

### nacos全景图

![img_1](.\pic\img_1.png)

### nacos和cap

![img_2](.\pic\img_2.png)

![img_3](.\pic\img_3.png)

### nacos支持AP和CP模式的切换

C是所有节点在同一时间看到的数据是一致的；而A的定义是所有的请求都会收到响应。


何时选择使用何种模式？
一般来说，
如果不需要存储服务级别的信息且服务实例是通过nacos-client注册，并能够保持心跳上报，那么就可以选择AP模式。当前主流的服务如 Spring cloud 和 Dubbo 服务，都适用于AP模式，AP模式为了服务的可能性而减弱了一致性，因此AP模式下只支持注册临时实例。

如果需要在服务级别编辑或者存储配置信息，那么 CP 是必须，K8S服务和DNS服务则适用于CP模式。
CP模式下则支持注册持久化实例，此时则是以 Raft 协议为集群运行模式，该模式下注册实例之前必须先注册服务，如果服务不存在，则会返回错误。

curl -X PUT '$NACOS_SERVER:8848/nacos/v1/ns/operator/switches?entry=serverMode&value=CP'

# nacos服务配置中心

创建项目rookie-springcloud-nacosconfig项目，利用李曼的rookie-nacosconfig-payment-provider项目进行配置

## 配置中心基础配置

### yml文件

Nacos同springcloud-config一样，在项目初始化时，要保证先从配置中心进行配置拉取，
拉取配置之后，才能保证项目的正常启动。

springboot中配置文件的加载是存在优先级顺序的，bootstrap优先级高于application

bootstrap.yml配置文件如下

```yaml
server:
  port: 8001
spring:
  application:
    name: rookie-nacos-payment-provider
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848 #配置Nacos服务配置中心的地址
      config:
        server-addr: localhost:8848 #Nacos作为配置中心地址
        file-extension: yaml #指定yaml格式的配置
        #group: DEV_GROUP
        #namespace: 7d8f0f5a-6a53-4785-9686-dd460158e5d4


# ${spring.application.name}-${spring.profile.active}.${spring.cloud.nacos.config.file-extension}
# nacos-config-client-dev.yaml

# nacos-config-client-test.yaml   ----> config.info
```

### nacos匹配规则

#### 理论

[参考官方文档](https://nacos.io/zh-cn/docs/quick-start-spring-cloud.html)

![img_4](.\pic\img_4.png)

最后公式

```java
# ${spring.application.name}-${spring.profile.active}.${spring.cloud.nacos.config.file-extension}
# nacos-config-client-dev.yaml

# nacos-config-client-test.yaml   ----> config.info
```

#### 实操

在nacos中进行配置，如图

![1649908278](.\pic\1649908278.png)

![1649908305](.\pic\1649908305.png)

访问http://localhost:8001/config/info即可查看结果

##### 历史配置

Nacos会记录配置文件的历史版本默认保留30天，此外还有一键回滚功能，回滚操作将会触发配置更新

##### 自带动态刷新

修改下Nacos中的yaml配置文件，再次调用查看配置的接口，就会发现配置已经刷新

## 配置中心分类配置

### 问题

多环境多项目管理

问题1：
实际开发中，通常一个系统会准备
dev开发环境
test测试环境
prod生产环境。
如何保证指定环境启动时服务能正确读取到Nacos上相应环境的配置文件呢？

问题2：
一个大型分布式微服务系统会有很多微服务子项目，
每个微服务项目又都会有相应的开发环境、测试环境、预发环境、正式环境......
那怎么对这些微服务配置进行管理呢？

### nacos的图形化管理界面

![img_5](.\pic\img_5.png)

命名空间

### 为什么这样设计

默认情况：
Namespace=public，Group=DEFAULT_GROUP, 默认Cluster是DEFAULT

Nacos默认的命名空间是public，Namespace主要用来实现隔离。
比方说我们现在有三个环境：开发、测试、生产环境，我们就可以创建三个Namespace，不同的Namespace之间是隔离的。

Group默认是DEFAULT_GROUP，Group可以把不同的微服务划分到同一个分组里面去

Service就是微服务；一个Service可以包含多个Cluster（集群），Nacos默认Cluster是DEFAULT，Cluster是对指定微服务的一个虚拟划分。
比方说为了容灾，将Service微服务分别部署在了杭州机房和广州机房，
这时就可以给杭州机房的Service微服务起一个集群名称（HZ），
给广州机房的Service微服务起一个集群名称（GZ），还可以尽量让同一个机房的微服务互相调用，以提升性能。

最后是Instance，就是微服务的实例。

### 三种方案加载配置

#### DataID方案

指定spring.profile.active和配置文件的DataID来使不同环境下读取不同的配置

默认空间+默认分组+新建dev和test两个DataID

新建dev配置DataID和test配置DataID。

通过spring.profile.active属性就能进行多环境下配置文件的读取

![1649917079](.\pic\1649917079.png)

测试http://localhost:8001/config/info进行访问即可

#### Group方案

通过Group实现环境区分

新建DEV_GROUP和TEST_GROUP

![1649917553](.\pic\1649917553.png)

在bootsrap.yml文件中添加如下

在config下增加一条group的配置即可。
可配置为DEV_GROUP或TEST_GROUP

#### Namespace方案

新建dev/test的Namespace

![1649917871](.\pic\1649917871.png)

回到服务管理和配置管理，可以发现多了dev和test

![1649917961](.\pic\1649917961.png)

 配置类似上面

## Nacos集群和持久化配置



 

 

 

 

 

 

 



 
