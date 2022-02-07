package com.niici.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
        return ExchangeBuilder.directExchange("niici.topic.exchange").durable(true).build();
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
        return new Queue("niici.topic.queue");
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



