## springboot整合rabbitmq
消息队列是典型的：生产者、消费者模型。生产者不断向消息队列中生产消息，消费者不断的从队列中获取消息。因为消息的生产和消费都是异步的，而且只关心消息的发送和接收，没有业务逻辑的侵入，这样就实现了生产者和消费者的解耦。

**支持作者就Star Mua~**

## RabbitMQ 

RabbitMQ是基于AMQP的一款消息管理系统

官网： http://www.rabbitmq.com/

官方教程：http://www.rabbitmq.com/getstarted.html

## 1.1 下载和安装

详见该项目 rabbitmq 目录下的安装帮助文档:

![rabbit下载和安装帮助文档](rabbitmq/assets/rabbit下载和安装帮助文档.png)

## Srping AMPQ

Spring有很多不同的项目，其中就有对AMQP的支持: 

![1527089338661](rabbitmq/assets/1527089338661.png)

Spring AMQP的页面：<http://projects.spring.io/spring-amqp/> 

![1527089365281](rabbitmq/assets/1527089365281.png)

## 依赖
```xml
 <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
 </dependency>
```

## 配置 application.yml

````
spring:
  rabbitmq:
    host: 148.70.3.235
    username: niici
    password: niici
    virtual-host: /niici
    template:
        retry:
          #启用重试
          enabled: true
            #第一次重试的间隔时长
          initial-interval: 10000ms
            # 最长重试间隔，超过间隔将不在重试
          max-interval: 210000ms
             # 下次重试间隔的倍数，即下次重试时 间隔时间是上次的几倍
          multiplier: 2
    #生产者确认机制，确保消息会正确发送，如果发送失败会有错误回执，从而触发重试
    publisher-confirms: true
````

在SpringAmqp中，对消息的消费者进行了封装和抽象，一个普通的JavaBean中的普通方法，只要通过简单的注解，就可以成为一个消费者。

```java
@Component
public class Listener {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "spring.test.queue", durable = "true"),
            exchange = @Exchange(
                    value = "spring.test.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC
            ),
            key = {"#.#"}))
    public void listen(String msg){
        System.out.println("接收到消息：" + msg);
    }
}
```

- `@Componet`：类上的注解，注册到Spring容器
- `@RabbitListener`：方法上的注解，声明这个方法是一个消费者方法，需要指定下面的属性：
  - `bindings`：指定绑定关系，可以有多个。值是`@QueueBinding`的数组。`@QueueBinding`包含下面属性：
    - `value`：这个消费者关联的队列。值是`@Queue`，代表一个队列
    - `exchange`：队列所绑定的交换机，值是`@Exchange`类型
    - `key`：队列和交换机绑定的`RoutingKey`

类似listen这样的方法在一个类中可以写多个，就代表多个消费者。

**详情请查看该项目下的 RebbitMqListener 类**

## 1.2 AmpqTemplate

Spring为AMQP提供了统一的消息处理模板：AmqpTemplate，非常方便的发送消息，其发送方法：

![1527090258083](rabbitmq/assets/1527090258083.png)

红框圈起来的是比较常用的3个方法，分别是：

- 指定交换机、RoutingKey和消息体
- 指定消息
- 指定RoutingKey和消息，会向默认的交换机发送消息


## 1.3 rabbitmq的五种消息模型

RabbitMQ提供了6种消息模型，但是第6种其实是RPC，并不是MQ，因此不予学习。那么也就剩下5种。
但是其实3、4、5这三种都属于订阅模型，只不过进行路由的方式不同。

![1527068544487](rabbitmq/assets/1527068544487.png)

### 1.3.1 基本消息模型

 ![1527070619131](rabbitmq/assets/1527070619131.png)
 
 在上图的模型中，有以下概念：
 
 - P：生产者，也就是要发送消息的程序
 - C：消费者：消息的接受者，会一直等待消息到来。
 - queue：消息队列，图中红色部分。类似一个邮箱，可以缓存消息；生产者向其中投递消息，消费者从其中取出消息。
 
 #### 定义消息生产者
 
 ````
    /**
      * 基本消息模型发送消息
      * @throws InterruptedException
      */
     @Test
     public void  simple() throws InterruptedException {
         String msg = "Rabbitmq simple ....";
         for (int i = 0; i < 10; i++) {
             amqpTemplate.convertAndSend("niici.create.simple.queue",msg);
             Thread.sleep(5000);
         }
     }
````

#### 定义消费者
````
@Component
@Log4j2
public class RabbitMqListener {
    /**
     * 基本消息类型监听
     * @param msg
     * @throws Exception
     */
     @RabbitListener(queuesToDeclare = @Queue(value = "niici.create.simple.queue"))
        public void simpleListener(String msg)throws Exception{
        if (StringUtils.isEmpty(msg)){
            return;
        }
        log.info("SimpleListener listen 接收到消息：" + msg);
    }
}
````

### 1.3.2 消费者的消息确认机制 (AcKnowlage)

消息一旦被消费者接收，队列中的消息就会被删除.

那么问题来了，RabbitMQ 怎么知道消息被接受了呢?

这就要通过消息确认机制（Acknowlege）来实现了。当消费者获取消息后，会向RabbitMQ发送回执ACK，告知消息已经被接收。不过这种回执ACK分两种情况：

- 自动ACK：消息一旦被接收，消费者自动发送ACK
- 手动ACK：消息接收后，不会发送ACK，需要手动调用

这需要看消息的重要性：

- 如果消息不太重要，丢失也没有影响，那么自动ACK会比较方便
- 如果消息非常重要，不容丢失。那么最好在消费完成后手动ACK，否则接收消息后就自动ACK，RabbitMQ就会把消息从队列中删除。如果此时消费者宕机，那么消息就丢失了。

springboot 集成 rabbitmq 的情况下，可以在 application.yml 中设置

````
spring:
  rabbitmq:
    listener:
      direct:
        // 默认为auto 自动ack,manul 为 手动 ack
        acknowledge-mode: manual
      simple:
        acknowledge-mode: manual
````
### 1.3.3 work消息模型

#### 说明

在刚才的基本模型中，一个生产者，一个消费者，生产的消息直接被消费者消费。比较简单。

Work queues，也被称为（Task queues），任务模型。

当消息处理比较耗时的时候，可能生产消息的速度会远远大于消息的消费速度。长此以往，消息就会堆积越来越多，无法及时处理。此时就可以使用work 模型：**让多个消费者绑定到一个队列，共同消费队列中的消息**。队列中的消息一旦消费，就会消失，因此任务是不会被重复执行的。

 ![1527078437166](rabbitmq/assets/1527078437166.png)

角色：

- P：生产者：任务的发布者
- C1：消费者，领取任务并且完成任务，假设完成速度较慢
- C2：消费者2：领取任务并完成任务，假设完成速度快

面试题：避免消息堆积？

1） 采用workqueue，多个消费者监听同一队列。

2）接收到消息以后，而是通过线程池，异步消费

 #### 定义消息生产者
 ````
    /**
      * work 消息模型发送消息
      * @throws InterruptedException
      */
     @Test
     public void  work() throws InterruptedException {
         String msg = "Rabbitmq work ....";
         for (int i = 0; i < 10; i++) {
             amqpTemplate.convertAndSend("niici.create.work.queue",msg+i);
 
         }
     }
````

 #### 定义消息消费者
 ````
    /**
      * work消息类型监听
      * @param msg
      * @throws Exception
      */
     @RabbitListener(queuesToDeclare =@Queue(value = "niici.create.work.queue"))
     public void workListener1(String msg)throws Exception{
         if (StringUtils.isEmpty(msg)){
             return;
         }
         log.info("WorkListener1 listen 接收到消息：" + msg);
         Thread.sleep(5000);
     }
     
     /**
      * 创建两个 work 队列共同消费
      * @param msg
      * @throws Exception
      */
      @RabbitListener(queuesToDeclare =@Queue(value = "niici.create.work.queue"))
        public void workListener2(String msg)throws Exception{
        if (StringUtils.isEmpty(msg)){
            return;
        }
        log.info("WorkListener2 listen 接收到消息：" + msg);
      }
 ````
 
 ### 1.3.4 持久化
 
 如何避免消息丢失？
 
 1) 消费者的手动ACK机制，可以防止消费者丢失消息
 
 2) 但是，如果在消费者消费之前，MQ就宕机了，消息就没了
 
 如何将消息进行持久化？
 
 要将消息持久化，前提是: 队列、交换机都持久化

### 1.3.5 订阅模型分类

### 1.3.6 订阅模型 - Fanout

Fanout，也称为广播。

#### 流程说明

流程图：

![1527086564505](rabbitmq/assets/1527086564505.png)

在广播模式下，消息发送流程是这样的：

- 1）  可以有多个消费者
- 2）  每个**消费者有自己的queue**（队列）
- 3）  每个**队列都要绑定到Exchange**（交换机）
- 4）  **生产者发送的消息，只能发送到交换机**，交换机来决定要发给哪个队列，生产者无法决定。
- 5）  交换机把消息发送给绑定过的所有队列
- 6）  队列的消费者都能拿到消息。实现一条消息被多个消费者消费

 #### 定义消息生产者
 
 ````
     /**
      * fantou 广播消息模型发送消息
      * @throws InterruptedException
      */
     @Test
     public void  fanout() throws InterruptedException {
         String msg = "Rabbitmq fanout ....";
         for (int i = 0; i < 10; i++) {
             //广播消息模型是所有队列都能接收到的，所以没有 routeKey，即 为空
             amqpTemplate.convertAndSend("niici.fanout.exchange","",msg+i);
 
             Thread.sleep(5000);
         }
     }
````

 #### 定义消息消费者
 ````
    /**
      * fanout消息类型监听
      * 创建两个fanout 队列 查看是否广播成功
      * @param msg
      * @throws Exception
      */
     @RabbitListener(bindings = @QueueBinding(
             value = @Queue(value = "niici.create.fanout1.queue", durable = "true"),
             exchange = @Exchange(value = "niici.fanout.exchange", ignoreDeclarationExceptions = "true", type = ExchangeTypes.FANOUT))
     )
     public void fanoutListener1(String msg)throws Exception{
         if (StringUtils.isEmpty(msg)){
             return;
         }
         log.info("FanoutListener1 listen 接收到消息：" + msg);
     }
     @RabbitListener(bindings = @QueueBinding(
                 value = @Queue(value = "niici.create.fanout2.queue", durable = "true"),
                 exchange = @Exchange(value = "niici.fanout.exchange", ignoreDeclarationExceptions = "true", type = ExchangeTypes.FANOUT)))
         public void fanoutListener2(String msg)throws Exception{
             if (StringUtils.isEmpty(msg)){
                 return;
             }
         log.info("FanoutListener2 listen 接收到消息：" + msg);
     }
 ````

### 1.3.7 订阅模型 - Direct

在Fanout模式中，一条消息，会被所有订阅的队列都消费。但是，在某些场景下，我们希望不同的消息被不同的队列消费。这时就要用到Direct类型的Exchange。

 在Direct模型下：

- 队列与交换机的绑定，不能是任意绑定了，而是要指定一个`RoutingKey`（路由key）
- 消息的发送方在 向 Exchange发送消息时，也必须指定消息的 `RoutingKey`。
- Exchange不再把消息交给每一个绑定的队列，而是根据消息的`Routing Key`进行判断，只有队列的`Routingkey`与消息的 `Routing key`完全一致，才会接收到消息

流程图：

 ![1527087677192](rabbitmq/assets/1527087677192.png)

图解：

- P：生产者，向Exchange发送消息，发送消息时，会指定一个routing key。
- X：Exchange（交换机），接收生产者的消息，然后把消息递交给 与routing key完全匹配的队列
- C1：消费者，其所在队列指定了需要routing key 为 error 的消息
- C2：消费者，其所在队列指定了需要routing key 为 info、error、warning 的消息

 #### 定义消息生产者
 ````
     /**
      * direct 广播消息模型发送消息
      *
      * @throws InterruptedException
      */
     @Test
     public void direct() throws InterruptedException {
         for (int i = 0; i < 10; i++) {
             //direct消息模型是所有队列都能接收到的，所以没有 routeKey，即 为空
             amqpTemplate.convertAndSend("niici.direct.exchange", "delete", "删除成功");
             amqpTemplate.convertAndSend("niici.direct.exchange", "insert", "新增成功");
             amqpTemplate.convertAndSend("niici.direct.exchange", "update", "修改成功");
             Thread.sleep(5000);
         }
     }
````

#### 定义消息消费者
 
 ````
    /**
      * direct 消息类型监听
      * @param msg
      * @throws Exception
      */
     @RabbitListener(bindings = @QueueBinding(
             value = @Queue(value = "niici.create.direct.queue", durable = "true"),
             // 交换机默认的是 direct 类型，默认持久化 为 true，所以不用设置
             exchange = @Exchange(value = "niici.direct.exchange", ignoreDeclarationExceptions = "true",type = ExchangeTypes.DIRECT),
             // 指定路由规则
             key = "insert")
     )
     public void directListener(String msg)throws Exception{
         if (StringUtils.isEmpty(msg)){
             return;
         }
         log.info("DirectListener1 listen 接收到消息：" + msg);
     }
````

### 1.3.8 订阅模型 - Topic

#### 说明

`Topic`类型的`Exchange`与`Direct`相比，都是可以根据`RoutingKey`把消息路由到不同的队列。只不过`Topic`类型`Exchange`可以让队列在绑定`Routing key` 的时候使用通配符！



`Routingkey` 一般都是有一个或多个单词组成，多个单词之间以”.”分割，例如： `item.insert`

 通配符规则：

​         `#`：匹配一个或多个词

​         `*`：匹配不多不少恰好1个词

举例：

​         `audit.#`：能够匹配`audit.irs.corporate` 或者 `audit.irs`

​         `audit.*`：只能匹配`audit.irs`

图示：

 ![1527088518574](rabbitmq/assets/1527088518574.png)

解释：

- 红色Queue：绑定的是`usa.#` ，因此凡是以 `usa.`开头的`routing key` 都会被匹配到
- 黄色Queue：绑定的是`#.news` ，因此凡是以 `.news`结尾的 `routing key` 都会被匹配

 #### 定义消息生产者
 
 ````
    /**
     * topic 消息模型发送消息
     *
     * @throws InterruptedException
     */
    @Test
    public void topic() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            //广播消息模型是所有队列都能接收到的，所以没有 routeKey，即 为空
            amqpTemplate.convertAndSend("niici.topic.exchange", "user.delete", "user 删除成功");
            amqpTemplate.convertAndSend("niici.topic.exchange", "student.delete", "student 删除成功");
            amqpTemplate.convertAndSend("niici.direct.exchange","niici.insert","新增成功");
            amqpTemplate.convertAndSend("niici.direct.exchange","niici.update","修改成功");
            Thread.sleep(5000);
        }
    }
````

 #### 定义消息消费者

````
    /**
     * topic 消息类型监听
     * @param msg
     * @throws Exception
     */
    @RabbitListener(
            bindings = @QueueBinding(
            value = @Queue(value = "niici.create.topic.queue", durable = "true"),
            exchange = @Exchange(value = "niici.topic.exchange", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),
            key = {"user.#"})
    )
    public void topicListener1(String msg )throws Exception{
        if (StringUtils.isEmpty(msg)){
            return;
        }
        log.info("TopicListener1 listen 接收到消息：" + msg);
    }

    /**
     * topic 消息类型监听
     * @param msg
     * @throws Exception
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "niici.create.topic.queue", durable = "true"),
            exchange = @Exchange(value = "niici.topic.exchange", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),
            key = {"student.#"})
    )
    public void topicListener2(String msg )throws Exception{
        if (StringUtils.isEmpty(msg)){
            return;
        }
        log.info("TopicListener2 listen 接收到消息：" + msg);
    }
 ````

#### 如何手动ACK？
#### 说明

`手动ACK`需要在application.yml中配置ack模式为手动, 并配置消息发送失败, 放回队列。
````
listener:
      direct:
        acknowledge-mode: manual
 # 消息发送失败时，返回到队列
publisher-returns: true
````

Code:
````
@RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "niici.topic.student.queue", durable = "true"),
                    exchange = @Exchange(
                            value = "niici.topic.exchange",
                            ignoreDeclarationExceptions = "true",
                            type = ExchangeTypes.TOPIC
                    ),
                    key = {"student.#"}))
    @RabbitHandler
    public void topicListenAck(String msg, Channel channel, Message message) throws IOException {
        // 消息在队列中对应的索引
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            /**
             * 无异常确认消息
             * channel.basicAck(long deliveryTag, boolean multiple);
             * deliveryTag: 取出来的消息在队列中的索引
             * multiple: true表示一次性的将小于deliveryTag的值进行ack
             * 如果当前deliveryTag为5, 则确认5及5之前的消息, 一般为false
             */
            channel.basicAck(deliveryTag, false);
            System.out.println("topic接收到消息：" + msg);
        } catch (IOException e) {
            /**
             * 异常拒收消息
             * basicNack(long deliveryTag, boolean multiple, boolean requeue)
             * requeue: true为将消息返回到队列, 并重新发送给消费者
             *          false则丢弃消息
             */
            channel.basicNack(deliveryTag, false , true);
        }
    }
````


