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

## 熔断规则

[官网地址](https://github.com/alibaba/Sentinel/wiki/%E7%86%94%E6%96%AD%E9%99%8D%E7%BA%A7)

### 基本介绍

![1650004926](.\pic\1650004926.png)

RT（平均响应时间，秒级）
      平均响应时间   超出阈值  且   在时间窗口内通过的请求>=5，两个条件同时满足后触发降级
      窗口期过后关闭断路器
      RT最大4900（更大的需要通过-Dcsp.sentinel.statistic.max.rt=XXXX才能生效）

异常比列（秒级）
    QPS >= 5 且异常比例（秒级统计）超过阈值时，触发降级；时间窗口结束后，关闭降级

异常数（分钟级）
     异常数（分钟统计）超过阈值时，触发降级；时间窗口结束后，关闭降级



Sentinel 熔断降级会在调用链路中某个资源出现不稳定状态时（例如调用超时或异常比例升高），对这个资源的调用进行限制，
让请求快速失败，避免影响到其它的资源而导致级联错误。

当资源被降级后，在接下来的降级时间窗口之内，对该资源的调用都自动熔断（默认行为是抛出 DegradeException）。

### 熔断实战

#### 慢调用比例

#### 异常比例

#### 异常数

## 热点Key限流

### 基本介绍

何为热点
热点即经常访问的数据，很多时候我们希望统计或者限制某个热点数据中访问频次最高的TopN数据，并对其访问进行限流或者其它操作

[官方网站](https://github.com/alibaba/Sentinel/wiki/%E7%83%AD%E7%82%B9%E5%8F%82%E6%95%B0%E9%99%90%E6%B5%81)

### 注解@SentinelResource

兜底方法
分为系统默认和客户自定义，两种

  之前的case，限流出问题后，都是用sentinel系统默认的提示：Blocked by Sentinel (flow limiting)

  我们能不能自定?类似hystrix，某个方法出问题了，就找对应的兜底降级方法？

源码：com.alibaba.csp.sentinel.slots.block.BlockException

代码

```java
@GetMapping("/testHotKey")
@SentinelResource(value = "testHotKey", blockHandler = "deal_testHotKey")
public String testHotKey(@RequestParam(value = "p1", required = false) String p1,
                         @RequestParam(value = "p2", required = false) String p2) {
    //int age = 10/0;
    return "------testHotKey";
}

public String deal_testHotKey(String p1, String p2, BlockException exception) {
    return "------deal_testHotKey,o(╥﹏╥)o";  //sentinel系统默认的提示：Blocked by Sentinel (flow limiting)
}
```

配置

![img_6](.\pic\img_6.png)

限流模式只支持QPS模式，固定写死了。（这才叫热点）
@SentinelResource注解的方法参数索引，0代表第一个参数，1代表第二个参数，以此类推
单机阀值以及统计窗口时长表示在此窗口时间超过阀值就限流。
上面的抓图就是第一个参数有值的话，1秒的QPS为1，超过就限流，限流后调用dealHandler_testHotKey支持方法。

启动项目，然后分别点击http://localhost:8401/testHotKey?p1=abc，http://localhost:8401/testHotKey?p1=abc&p2=33和http://localhost:8401/testHotKey?p2=abc，快速点击即可发现问题所在

#### 参数例外项

特例情况

我们期望p1参数当它是某个特殊值时，它的限流值和平时不一样

假如当p1的值等于5时，它的阈值可以达到200

![img_7](.\pic\img_7.png)

快速点击http://localhost:8401/testHotKey?p1=5和http://localhost:8401/testHotKey?p1=3进行测试，当p1等于5的时候，阈值变为200。当p1不等于5的时候，阈值就是平常的1。

热点参数的注意点，参数必须是基本类型或者String

## 系统规则

![img_8](.\pic\img_8.png)

## @SentinelResource

### 按资源名称限流+后续处理

![img_9](.\pic\img_9.png)

代码

```java
@GetMapping("/byResource")
@SentinelResource(value = "byResource", blockHandler = "handleException")
public CommonResult byResource() {
    return new CommonResult(200, "按资源名称限流测试OK", new Payment(2020L, "serial001"));
}

public CommonResult handleException(BlockException exception) {
    return new CommonResult(444, exception.getClass().getCanonicalName() + "\t 服务不可用");
}
```

访问http://localhost:8401/byResource即可观察结果

### 按照Url地址限流+后续处理

![img_10](.\pic\img_10.png)

代码

```java
@GetMapping("/rateLimit/byUrl")
@SentinelResource(value = "byUrl")
public CommonResult byUrl() {
    return new CommonResult(200, "按url限流测试OK", new Payment(2020L, "serial002"));
}
```

疯狂点击http://localhost:8401/rateLimit/byUrl查看結果

### 存在的问题

1    系统默认的，没有体现我们自己的业务要求。

2  依照现有条件，我们自定义的处理方法又和业务代码耦合在一块，不直观。

3  每个业务方法都添加一个兜底的，那代码膨胀加剧。

4  全局统一的处理方法没有体现。

### 客户自定义限流处理逻辑

![img_11](.\pic\img_11.png)

代码

```java
//自定义限流处理逻辑
@GetMapping("/rateLimit/customerBlockHandler")
@SentinelResource(value = "customerBlockHandler",
        blockHandlerClass = CustomerBlockHandler.class,
        blockHandler = "handlerException2")
public CommonResult customerBlockHandler() {
    return new CommonResult(200, "按客戶自定义", new Payment(2020L, "serial003"));
}
```



## 服务熔断功能









比较

![img_12](.\pic\img_12.png)





































