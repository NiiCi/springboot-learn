package com.niici.rabbitmq.listener;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Listener {
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "spring.test.queue2", durable = "true"),
            exchange = @Exchange(
                    value = "spring.test.exchange2",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC
            ),
            key = {"#.#"}))
    public void listen(String msg) {
        System.out.println("接收到消息：" + msg);
    }

}
