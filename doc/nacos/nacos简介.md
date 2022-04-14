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

本示例用的是2.0.4版本

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

 
