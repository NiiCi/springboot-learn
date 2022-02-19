package com.niici.rabbitmq.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.connection.RabbitUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Listener {

    /**
     * 基本类型消息监听
     * @param msg
     */
    @RabbitListener(queuesToDeclare = {
            @Queue(value = "niici.simple.queue"),
            @Queue(value = "niici.simple.queue2")
    })
    public void simpleListen(String msg) {
        System.out.println("simple接收到消息：" + msg);
    }

    /**
     * 模拟work队列监听，一个消费消息快，一个消费消息慢
     * @param msg
     * @throws InterruptedException
     */
    @RabbitListener(queuesToDeclare = {
            @Queue(value = "niici.work.queue")
    })
    public void workListen1(String msg) throws InterruptedException {
        System.out.println("work1接收到消息：" + msg);
        Thread.sleep(5000);
    }

    @RabbitListener(queuesToDeclare = {
            @Queue(value = "niici.work.queue")
    })
    public void workListen2(String msg) {
        System.out.println("work2接收到消息：" + msg);
    }

    /**
     * 模拟两个队列都绑定了同一个fanout交换机，查看是否都能收到广播消息
     * @param msg
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "niici.fanout.queue", durable = "true"),
            exchange = @Exchange(
                    value = "niici.fanout.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.FANOUT,
                    declare = "false"
            ),
            key = {"#.#"}))
    public void fanoutListen1(String msg) {
        System.out.println("fanout接收到消息：" + msg);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "niici.fanout.queue2", durable = "true"),
            exchange = @Exchange(
                    value = "niici.fanout.exchange",
                    ignoreDeclarationExceptions = "true ",
                    type = ExchangeTypes.FANOUT,
                    declare = "false"
            ),
            key = {"#.#"}))
    public void fanoutListen2(String msg) {
        System.out.println("fanout2接收到消息：" + msg);
    }

    /**
     * 模拟队列都绑定了direct交换机，查看是否都能收到订阅消息
     * direct和fanout区别：可以指定Routingkey
     * @param msg
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "niici.direct.insert.queue", durable = "true"),
            exchange = @Exchange(
                    value = "niici.direct.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.DIRECT,
                    declare = "false"
            ),
            key = {"insert"}))
    public void directListen(String msg) {
        System.out.println("insert接收到消息：" + msg);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "niici.direct.insert.delete.queue", durable = "true"),
            exchange = @Exchange(
                    value = "niici.direct.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.DIRECT,
                    declare = "false"
            ),
            key = {"insert", "delete"}))
    public void directListen2(String msg) {
        System.out.println("insert、delete接收到消息：" + msg);
    }

    @RabbitListener(
            bindings = @QueueBinding(
            value = @Queue(value = "niici.topic.user.queue", durable = "true"),
            exchange = @Exchange(
                    value = "niici.topic.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC,
                    declare = "false"
            ),
            key = {"user.#"}))
    public void topicListen(String msg) {
        System.out.println("topic接收到消息：" + msg);
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "niici.topic.student.queue", durable = "true"),
                    exchange = @Exchange(
                            value = "niici.topic.exchange",
                            ignoreDeclarationExceptions = "true",
                            type = ExchangeTypes.TOPIC,
                            declare = "false"
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
            System.out.println("消息在队列中的索引：" + deliveryTag);
            // 异常测试，测试接收异常
            // int i = 3 / 0;
            channel.basicAck(deliveryTag, false);
        } catch (IOException e) {
            /**
             * 拒收消息
             * basicNack(long deliveryTag, boolean multiple, boolean requeue)
             * requeue: true为将消息返回到队列, 并重新发送给消费者
             *          false则丢弃消息
             */
            channel.basicNack(deliveryTag, false, true);
        }
        System.out.println("topic接收到消息：" + msg);
    }

    /**
     * 队列达到最大长度时，死信队列监听
     * @param msg
     * @param channel
     * @param message
     * @throws IOException
     */
    /*@RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "niici.dead.queue", durable = "true"),
                    exchange = @Exchange(
                            value = "niici.dead.exchange",
                            ignoreDeclarationExceptions = "true",
                            type = ExchangeTypes.TOPIC,
                            declare = "false"
                    ),
                    key = {"dead.#"}))
    @RabbitHandler
    public void deadListen1(String msg, Channel channel, Message message) throws IOException {
        // 消息在队列中对应的索引
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            System.out.println("队列达到最大长度死信队列监听到消息: " + msg);
            channel.basicAck(deliveryTag, false);
        } catch (IOException e) {
            channel.basicNack(deliveryTag, false, true);
        }
    }*/

    /**
     * 消息超时时，死信队列监听
     * 拒绝消息场景就是再写一个topic监听器监听topic.#路由key的消息, 调用channel.basicNack方法
     * 延迟队列就是死信队列中的消息超时场景, 如超时订单自动关闭的实现
     * @param msg
     * @param channel
     * @param message
     * @throws IOException
     */
/*    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "niici.dead.queue", durable = "true"),
                    exchange = @Exchange(
                value = "niici.dead.exchange",
                ignoreDeclarationExceptions = "true",
                type = ExchangeTypes.TOPIC,
                declare = "false"
        ),
        key = {"dead.#"}))
        @RabbitHandler
        public void deadListen2(String msg, Channel channel, Message message) throws IOException {
            // 消息在队列中对应的索引
            long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            System.out.println("消息超时场景死信队列监听到消息: " + msg);
            channel.basicAck(deliveryTag, false);
        } catch (IOException e) {
            channel.basicNack(deliveryTag, false, true);
        }
    }*/


    @RabbitListener(
            bindings = @QueueBinding(
                    // niici.topic.queue在config中已经配置, 使用@RabbitListener注解监听时, 会再创建一次queue
                    // 使用ignoreDeclarationExceptions = true 忽略声明异常
                    value = @Queue(value = "niici.topic.queue", durable = "true",
                            ignoreDeclarationExceptions = "true",
                            declare = "false"),
                    exchange = @Exchange(
                            value = "niici.topic.exchange",
                            ignoreDeclarationExceptions = "true",
                            type = ExchangeTypes.TOPIC,
                            declare = "false"
                    ),
                    key = {"topic.nack"}))
    @RabbitHandler
    public void topicListen4DeadNack(String msg, Channel channel, Message message) throws IOException {
        // 消息在队列中对应的索引
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            System.out.println("测试消息拒收监听器接收到消息：" + msg);
            channel.basicNack(deliveryTag, false, true);
        } catch (IOException e) {
            channel.basicNack(deliveryTag, false, true);
        }
    }

    /**
     * 消息被拒收时，死信队列监听
     * @param msg
     * @param channel
     * @param message
     * @throws IOException
     */
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "niici.dead.queue", durable = "true"),
                    exchange = @Exchange(
                            value = "niici.dead.exchange",
                            ignoreDeclarationExceptions = "true",
                            type = ExchangeTypes.TOPIC,
                            declare = "false"
                    ),
                    key = {"dead.#"}))
    @RabbitHandler
    public void deadListen3(String msg, Channel channel, Message message) throws IOException {
        // 消息在队列中对应的索引
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            System.out.println("消息被拒收场景死信队列监听到消息: " + msg);
            channel.basicAck(deliveryTag, false);
        } catch (IOException e) {
            channel.basicNack(deliveryTag, false, true);
        }
    }

    /**
     * 基于插件的延迟队列监听
     * @param msg
     * @param channel
     * @param message
     * @throws IOException
     */
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "niici.delay.queue", durable = "true", ignoreDeclarationExceptions = "true"),
                    exchange = @Exchange(
                            value = "niici.delay.exchange",
                            ignoreDeclarationExceptions = "true",
                            type = ExchangeTypes.DIRECT,
                            declare = "false"
                    ),
                    key = {"delay.#"}))
    @RabbitHandler
    public void delayListen(String msg, Channel channel, Message message) throws IOException {
        // 消息在队列中对应的索引
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            System.out.println("基于插件的延迟队列监听器到消息: " + msg);
            channel.basicAck(deliveryTag, false);
        } catch (IOException e) {
            channel.basicNack(deliveryTag, false, true);
        }
    }

    /**
     * 消息发送确认监听
     * @param msg
     * @param channel
     * @param message
     * @throws IOException
     */
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "niici.confirm.queue", durable = "true", ignoreDeclarationExceptions = "true"),
                    exchange = @Exchange(
                            value = "niici.confirm.exchange",
                            ignoreDeclarationExceptions = "true",
                            type = ExchangeTypes.TOPIC,
                            declare = "false"
                    ),
                    key = {"confirm.#"}))
    @RabbitHandler
    public void confirmListen(String msg, Channel channel, Message message) throws IOException {
        // 消息在队列中对应的索引
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            System.out.println("消息发送确认监听器到消息: " + msg);
            channel.basicAck(deliveryTag, false);
        } catch (IOException e) {
            channel.basicNack(deliveryTag, false, true);
        }
    }

    /**
     * 备份交换机，告警队列监听
     * @param msg
     * @param channel
     * @param message
     * @throws IOException
     */
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "niici.warn.queue", durable = "true", ignoreDeclarationExceptions = "true"),
                    exchange = @Exchange(
                            value = "niici.backup.exchange",
                            ignoreDeclarationExceptions = "true",
                            type = ExchangeTypes.FANOUT,
                            declare = "false"
                    )))
    @RabbitHandler
    public void warnListen(String msg, Channel channel, Message message) throws IOException {
        // 消息在队列中对应的索引
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            System.out.println("告警！！！消息发送失败, 消息内容: " + msg);
            channel.basicAck(deliveryTag, false);
        } catch (IOException e) {
            channel.basicNack(deliveryTag, false, true);
        }
    }

    /**
     * 优先队列监听
     * 测试优先队列，需要先将消息存放在队列中一段时间，再由队列去排序
     * 即先将监听器注释掉，发送消息，再把监听器打开，运行springboot项目
     * @param msg
     * @param channel
     * @param message
     * @throws IOException
     */
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "niici.priority.queue", durable = "true",
                            ignoreDeclarationExceptions = "true",
                            declare = "false"),
                    exchange = @Exchange(
                            value = "niici.priority.exchange",
                            ignoreDeclarationExceptions = "true",
                            type = ExchangeTypes.TOPIC,
                            declare = "false"
                    )))
    @RabbitHandler
    public void priorityListen(String msg, Channel channel, Message message) throws IOException {
        // 消息在队列中对应的索引
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            System.out.println("优先队列监听接受到消息, 消息内容: " + msg);
            channel.basicAck(deliveryTag, false);
        } catch (IOException e) {
            channel.basicNack(deliveryTag, false, true);
        }
    }
}
