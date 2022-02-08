package com.niici.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * rabbitmq交换机、队列、绑定关系配置类
 * 当使用@rabbitmqListener注解时，会自动生成交换机、队列、和绑定关系，如Listener类
 */
@Configuration
public class RabbitMqConfig {
    /**
     * 配置fanout交换机
     * ExchangeBuilder提供了fanout、direct、topic类型的交换机配置
     * @return
     */
    @Bean
    public Exchange fanoutExchange() {
        // durable 配置交换机持久化
        return ExchangeBuilder.directExchange("niici.fanout.exchange").durable(true).build();
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
        return new Queue("niici.direct.queue");    }

    @Bean
    public Queue topicQueue() {
        // new Queue构造方法默认durable为true
        HashMap<String, Object> args = new HashMap();
        // 设置队列中消息的过期时间, 单位为毫秒
        args.put("x-message-ttl", 5000);
        // 设置死信交换机
        // 进入死信队列的三种场景：消息被拒绝、消息过期、队列达到最大长度
        // 队列把消息发给死信交换机, 交换机再通过不同的routingKey, 将消息分发到不同的队列中
        args.put("x-dead-letter-exchange", "niici.dead.exchange");
        // 队列名称、是否持久化、是否独占、是否自动删除
        //return new Queue("niici.simple.queue", true, false, false, args);
        return new Queue("niici.topic.queue", true, false, false, args);
    }

    @Bean
    public Binding fanoutQueueBind(@Qualifier("fanoutQueue") Queue queue, @Qualifier("fanoutExchange") Exchange exchange) {
        // 将队列绑定到指定的交换机上, 并指定路由key
        return BindingBuilder.bind(queue).to(exchange).with("fanout.#").noargs();
    }

    @Bean
    public Binding directQueueBind(@Qualifier("directQueue") Queue queue, @Qualifier("directExchange") Exchange exchange) {
        // 将队列绑定到指定的交换机上, 并指定路由key
        return BindingBuilder.bind(queue).to(exchange).with("direct.#").noargs();
    }

    @Bean
    public Binding topicQueueBind(@Qualifier("topicQueue") Queue queue, @Qualifier("topicExchange") Exchange exchange) {
        // 将队列绑定到指定的交换机上, 并指定路由key
        return BindingBuilder.bind(queue).to(exchange).with("topic.#").noargs();
    }
}



