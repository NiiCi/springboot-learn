package com.niici.rabbitmq.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;

/**
 * rabbitmq交换机、队列、绑定关系配置类
 * 当使用@rabbitmqListener注解时，会自动生成交换机、队列、和绑定关系，如Listener类
 */
@Configuration
public class RabbitMqConfig {

    @Resource
    private RabbitTemplate rabbitTemplate;
    /**
     * 配置fanout交换机
     * ExchangeBuilder提供了fanout、direct、topic类型的交换机配置
     * @return
     */
    @Bean
    public Exchange fanoutExchange() {
        // durable 配置交换机持久化
        return ExchangeBuilder.fanoutExchange("niici.fanout.exchange").durable(true).build();
    }

    /**
     * 配置direct交换机
     * @return
     */
    @Bean
    public Exchange directExchange() {
        return ExchangeBuilder.directExchange("niici.direct.exchange").durable(true).build();
    }

    /**
     * 配置topic交换机
     * @return
     */
    @Bean
    public Exchange topicExchange() {
        return ExchangeBuilder.topicExchange("niici.topic.exchange").durable(true).build();
    }

    /**
     * 配置死信交换机
     * @return
     */
    @Bean
    public Exchange deadExchange() {
        return ExchangeBuilder.topicExchange("niici.dead.exchange").durable(true).build();
    }

    /**
     * 配置延迟交换机
     * @return
     */
    @Bean
    public CustomExchange delayExchange() {
        // CustomExchange 允许自定义交换机类型
        HashMap<String, Object> args = new HashMap<>();
        // 指定延迟队列的类型
        args.put("x-delayed-type", "direct");
        return new CustomExchange("niici.delay.exchange", "x-delayed-message", true, false, args);
    }

    /**
     * 配置备份交换机
     * 备份交换机的类型需要是fanout类型
     * @return
     */
    @Bean
    public FanoutExchange backupExchange() {
        return ExchangeBuilder.fanoutExchange("niici.backup.exchange").durable(true).build();
    }

    /**
     * 配置消息确认交换机
     * @return
     */
    @Bean
    public Exchange confirmExchange() {
        //return ExchangeBuilder.topicExchange("niici.confirm.exchange").durable(true).build();
        // 消息确认交换机需要将无法投递的消息发送给备份交换机
        return ExchangeBuilder.topicExchange("niici.confirm.exchange").durable(true)
                // alternate参数用于指定备份交换机
                .alternate("niici.backup.exchange").build();
    }

    /**
     * 配置优先交换机
     * @return
     */
    @Bean
    public Exchange priorityExchange() {
        return ExchangeBuilder.topicExchange("niici.priority.exchange").durable(true).build();
    }

    /**
     * 声明基本类型的队列
     * @return
     */
    @Bean
    public Queue simpleQueue() {
        return new Queue("niici.simple.queue");
    }

    /**
     * 声明基本类型的队列
     * @return
     */
    @Bean
    public Queue simpleQueue2() {
        // new Queue构造方法默认durable为true
        return new Queue("niici.simple.queue2");
    }

    /**
     * 声明基本类型的队列
     * @return
     */
    @Bean
    public Queue workQueue() {
        // new Queue构造方法默认durable为true
        return new Queue("niici.work.queue");
    }

    @Bean
    public Queue fanoutQueue() {
        // new Queue构造方法默认durable为true
        return new Queue("niici.fanout.queue");
    }

    @Bean
    public Queue directQueue() {
        return new Queue("niici.direct.queue");
    }

    @Bean
    public Queue topicQueue() {
        // new Queue构造方法默认durable为true
        HashMap<String, Object> args = new HashMap();
        // 设置队列中消息的过期时间, 单位为毫秒
        // args.put("x-message-ttl", 4000);
        // 指定队列最大长度
        //args.put("x-max-length", 2);
        // 设置死信交换机
        // 进入死信队列的三种场景：消息被拒绝、消息过期、队列达到最大长度
        // 队列把消息发给死信交换机, 交换机再通过不同的routingKey, 将消息分发到不同的队列中
        args.put("x-dead-letter-exchange", "niici.dead.exchange");
        // 指定死信队列的路由key
        args.put("x-dead-letter-routing-key", "dead.test");
        // 队列名称、是否持久化、是否独占、是否自动删除
        return new Queue("niici.topic.queue", true, false, false, args);
    }

    /**
     * 定义一个死信队列
     * @return
     */
    @Bean
    public Queue deadQueue() {
        return new Queue("niici.dead.queue");
    }

    /**
     * 定义一个延迟队列
     * @return
     */
    @Bean
    public Queue delayQueue() {
        return new Queue("niici.delay.queue");
    }

    /**
     * 定义一个确认队列，用于测试消息发送确认
     * @return
     */
    @Bean
    public Queue confirmQueue() {
        return new Queue("niici.confirm.queue");
    }

    /**
     * 定义一个备份队列
     * @return
     */
    @Bean
    public Queue backupQueue() {
        return new Queue("niici.backup.queue");
    }

    /**
     * 定义一个告警队列，用于接受备份队列中的消息
     * @return
     */
    @Bean
    public Queue warnQueue() {
        return new Queue("niici.warn.queue");
    }

    /**
     * 定义一个优先队列
     * @return
     */
    @Bean
    public Queue priorityQueue() {
        // maxPriority参数用于设置队列的最大优先级
        return QueueBuilder.durable("niici.priority.queue").maxPriority(10).build();
    }

    @Bean
    public Binding fanoutQueueBind(@Qualifier("fanoutQueue") Queue queue, @Qualifier("fanoutExchange") Exchange exchange) {
        // 将队列绑定到指定的交换机上, 并指定路由key
        return BindingBuilder.bind(queue).to(exchange).with("#.#").noargs();
    }

    @Bean
    public Binding directQueueBind(@Qualifier("directQueue") Queue queue, @Qualifier("directExchange") Exchange exchange) {
        // 将队列绑定到指定的交换机上, 并指定路由key
        return BindingBuilder.bind(queue).to(exchange).with("direct").noargs();
    }

    @Bean
    public Binding topicQueueBind(@Qualifier("topicQueue") Queue queue, @Qualifier("topicExchange") Exchange exchange) {
        // 将队列绑定到指定的交换机上, 并指定路由key
        return BindingBuilder.bind(queue).to(exchange).with("topic.#").noargs();
    }

    @Bean
    public Binding deadQueueBind(@Qualifier("deadQueue") Queue queue, @Qualifier("deadExchange") Exchange exchange) {
        // 将队列绑定到指定的交换机上, 并指定路由key
        return BindingBuilder.bind(queue).to(exchange).with("dead.#").noargs();
    }

    @Bean
    public Binding delayQueueBind(@Qualifier("delayQueue") Queue queue, @Qualifier("delayExchange") Exchange exchange) {
        // 将队列绑定到指定的交换机上, 并指定路由key
        return BindingBuilder.bind(queue).to(exchange).with("delay.#").noargs();
    }

    @Bean
    public Binding confirmQueueBind(@Qualifier("confirmQueue") Queue queue, @Qualifier("confirmExchange") Exchange exchange) {
        // 将队列绑定到指定的交换机上, 并指定路由key
        return BindingBuilder.bind(queue).to(exchange).with("confirm.#").noargs();
    }

    @Bean
    public Binding backupQueueBind(@Qualifier("backupQueue") Queue queue, @Qualifier("backupExchange") FanoutExchange exchange) {
        // 将队列绑定到指定的交换机上, 并指定路由key
        return BindingBuilder.bind(queue).to(exchange);
    }

    @Bean
    public Binding warnQueueBind(@Qualifier("warnQueue") Queue queue, @Qualifier("backupExchange") FanoutExchange exchange) {
        // 将队列绑定到指定的交换机上, 并指定路由key
        return BindingBuilder.bind(queue).to(exchange);
    }

    @Bean
    public Binding priorityQueueBind(@Qualifier("priorityQueue") Queue queue, @Qualifier("priorityExchange") Exchange exchange) {
        // 将队列绑定到指定的交换机上, 并指定路由key
        return BindingBuilder.bind(queue).to(exchange).with("priority.#").noargs();
    }

    @PostConstruct
    public RabbitTemplate configRabbitTemplate() {
        Logger log = LoggerFactory.getLogger(RabbitTemplate.class);
        // 设置消息发送确认失败回调, yml需要配合publisher-confirm-type: correlated 使用
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (!ack) {
                log.info("发送消息失败回调, error: {}", cause);
            }
        });
        // 设置消息发送失败返回回调, yml需要配合publisher-returns: true 使用
        rabbitTemplate.setReturnsCallback(returned ->
                log.info("消息发送失败, 应答码: {}, 原因: {}, 交换机: {}, 路由key: {}",
                returned.getReplyCode(), returned.getReplyText(), returned.getExchange(), returned.getRoutingKey()));

        // 当消息传递过程中不可达时, 将消息返回给生产者
        rabbitTemplate.setMandatory(true);
        return rabbitTemplate;
    }


}



