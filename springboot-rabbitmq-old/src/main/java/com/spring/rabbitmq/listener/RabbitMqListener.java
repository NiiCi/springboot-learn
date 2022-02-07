package com.spring.rabbitmq.listener;

import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.mockito.internal.util.StringUtil;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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

    /**
     * direct 消息类型监听
     * @param msg
     * @throws Exception
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "niici.create.direct.queue", durable = "true"),
            // 交换机默认的是 direct 类型，默认持久化 为 true，所有不用设置
            exchange = @Exchange(value = "niici.direct.exchange", ignoreDeclarationExceptions = "true",type = ExchangeTypes.DIRECT),
            // 指定路由规则
            key = "delete")
    )
    public void directListener2(String msg)throws Exception{
        if (StringUtils.isEmpty(msg)){
            return;
        }
        log.info("DirectListener2 listen 接收到消息：" + msg);
    }

    /**
     * topic 消息类型监听
     * @param msg
     * @throws Exception
     */
    @RabbitListener(bindings = @QueueBinding(
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
}
