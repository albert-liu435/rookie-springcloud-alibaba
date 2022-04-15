sentinel

## Sentinel

[官网](https://github.com/alibaba/Sentinel)

类似之前说到的Hystrix,用来解决服务雪崩，服务降级，服务熔断，服务限流等。

## 安装Sentinel控制台

Sentinel 分为两个部分:

- 核心库（Java 客户端）不依赖任何框架/库，能够运行于所有 Java 运行时环境，同时对 Dubbo / Spring Cloud 等框架也有较好的支持。

- 控制台（Dashboard）基于 Spring Boot 开发，打包后可以直接运行，不需要额外的 Tomcat 等应用容器。

[下载地址](https://github.com/alibaba/Sentinel/releases)

运行命令

我的jdk版本是17.所以需要这样运行，如果是 jdk1.8的话直接按照官网运行即可

```java
java --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/sun.net.util=ALL-UNNAMED -Dserver.port=8080 -Dcsp.sentinel.dashboard.server=localhost:8080 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard-1.8.4.jar
```

jdk1.8

```java
java -Dserver.port=8080 -Dcsp.sentinel.dashboard.server=localhost:8080 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard-1.8.4.jar
```

访问http://localhost:8080地址，账号和密码都是sentinel

## 初始化演示工程

启动nacos和rookie-sentinel-payment-provider项目

rookie-sentinel-payment-provider项目的配置文件如下

```java
server:
  port: 8401
spring:
  application:
    name: rookie-sentinel-payment-provider
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848 #配置Nacos服务配置中心的地址
    sentinel:
      transport:
        #配置Sentinel dashboard地址
        dashboard: localhost:8080
        ##默认8719端口，假如被占用会自动从8719开始依次+1扫描,直至找到未被占用的端口
        port: 8719


management:
  endpoints:
    web:
      exposure:
        include: '*'

```

启动之后，分别访问http://localhost:8401/testB和http://localhost:8401/testA,然后再查看sentinel控制台

## 流控规则

介绍

![img](.\pic\img.png)

### 流控模式

#### 直接(默认)

![1650000752](.\pic\1650000752.png)

访问http://localhost:8401/testA,当1s内访问的次数超过1的时候会默认返回Blocked by Sentinel (flow limiting)

如果设置线程数，是在应用里面进行流控

![1650001369](.\pic\1650001369.png)

```java
//流控设置线程数为1
@GetMapping("/testA")
public String testA() {

    try {
        Thread.sleep(800);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }

    return "------testA";
}
```

访问http://localhost:8401/testA,当1s内访问的次数超过1的时候会默认返回Blocked by Sentinel (flow limiting)

#### 关联

##### 是什么

当关联的资源达到阈值时，就限流自己

当与A关联的资源B达到阀值后，就限流A自己

B惹事，A挂了

##### 配置A

![1650001552](.\pic\1650001552.png)

设置B流控跟A类似，然后再postman里新建多线程集合

![img_1](.\pic\img_1.png)

运行postman脚本，再在浏览器上执行http://localhost:8401/testA，发现浏览器也会返回Blocked by Sentinel (flow limiting)

#### 链路

多个请求调用同一个微服务

### 流控效果

#### 直接

源码：com.alibaba.csp.sentinel.slots.block.flow.controller.DefaultController

#### 预热

##### 说明

公式：阈值除以coldFactor(默认值为3),经过预热时长后才会达到阈值

##### 官网

![img_2](.\pic\img_2.png)

默认coldFactor为3，即请求 QPS 从 threshold / 3 开始，经预热时长逐渐升至设定的 QPS 阈值。

源码com.alibaba.csp.sentinel.slots.block.flow.controller.WarmUpController

##### WarmUp配置

默认 coldFactor 为 3，即请求QPS从(threshold / 3) 开始，经多少预热时长才逐渐升至设定的 QPS 阈值。

案例，阀值为10+预热时长设置5秒。
系统初始化的阀值为10 / 3 约等于3,即阀值刚开始为3；然后过了5秒后阀值才慢慢升高恢复到10

![img_3](.\pic\img_3.png)

多次点击http://localhost:8401/testB,刚开始不行，后面慢慢OK

##### 应用

如：秒杀系统在开启的瞬间，会有很多流量上来，很有可能把系统打死，预热方式就是把为了保护系统，可慢慢的把流量放进来，慢慢的把阀值增长到设置的阀值。

#### 等待排队

匀速排队，阈值必须设置为QPS

![img_4](.\pic\img_4.png)

源码：com.alibaba.csp.sentinel.slots.block.flow.controller.RateLimiterController

测试

![img_5](.\pic\img_5.png)

## 降级规则
