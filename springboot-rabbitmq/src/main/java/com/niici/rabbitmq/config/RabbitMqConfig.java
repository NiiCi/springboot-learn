package com.niici.rabbitmq.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
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
        args.put("x-delayed-type", "topic");
        return new CustomExchange("niici.delay.exchange", "x-delayed-message", true, false, args);
    }

    /**
     * 配置消息确认交换机
     * @return
     */
    @Bean
    public Exchange confirmExchange() {
        return ExchangeBuilder.topicExchange("niici.confirm.exchange").durable(true).build();
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
     * 定义一个确认队列，用于测试消息发送确认
     * @return
     */
    @Bean
    public Queue confirmQueue() {
        return new Queue("niici.confirm.queue");
    }

    /**
     * 定义一个延迟队列
     * @return
     */
    @Bean
    public Queue delayQueue() {
        return new Queue("niici.delay.queue");
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
        return rabbitTemplate;
    }


}



