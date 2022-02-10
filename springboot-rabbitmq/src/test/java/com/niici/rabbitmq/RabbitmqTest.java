package com.niici.rabbitmq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitmqTest {
    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 基本消息模型发送消息
     *
     * @throws InterruptedException
     */
    @Test
    public void simple() throws InterruptedException {
        String msg = "Rabbitmq simple ....";
        for (int i = 0; i < 10; i++) {
            amqpTemplate.convertAndSend("niici.simple.queue", msg);
            amqpTemplate.convertAndSend("niici.simple.queue2", msg+i);
            Thread.sleep(1000);
        }
    }

    /**
     * work 消息模型发送消息
     *
     * @throws InterruptedException
     */
    @Test
    public void work() throws InterruptedException {
        String msg = "Rabbitmq work ....";
        for (int i = 0; i < 10; i++) {
            amqpTemplate.convertAndSend("niici.work.queue", msg + i);
            Thread.sleep(1000);
        }
    }

    /**
     * fantou 广播消息模型发送消息
     *
     * @throws InterruptedException
     */
    @Test
    public void fanout() throws InterruptedException {
        String msg = "Rabbitmq fanout ....";
        for (int i = 0; i < 10; i++) {
            //广播消息模型是所有队列都能接收到的，所以没有 routeKey，即 为空
            amqpTemplate.convertAndSend("niici.fanout.exchange", "", msg + i);
            Thread.sleep(5000);
        }
    }

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
            //amqpTemplate.convertAndSend("niici.direct.exchange", "update", "修改成功");
            Thread.sleep(5000);
        }
    }


    /**
     * topic 消息模型发送消息
     *
     * @throws InterruptedException
     */
    @Test
    public void topic() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            amqpTemplate.convertAndSend("niici.topic.exchange", "user.delete", "user 删除成功");
            amqpTemplate.convertAndSend("niici.topic.exchange", "user.insert", "user 新增成功");
            amqpTemplate.convertAndSend("niici.topic.exchange", "student.delete", "student 删除成功");
            amqpTemplate.convertAndSend("niici.topic.exchange", "student.insert", "student 新增成功");
            amqpTemplate.convertAndSend("niici.topic.exchange", "student.insert", "student 过期新增成功",
                    message -> {
                        message.getMessageProperties().setExpiration("2000");
                        return message;
                    });
            /*amqpTemplate.convertAndSend("niici.direct.exchange","niici.insert","新增成功");
            amqpTemplate.convertAndSend("niici.direct.exchange","niici.update","修改成功");*/
            //Thread.sleep(5000);
        }
    }

    /**
     * 消息超时场景死信队列测试
     *
     * @throws InterruptedException
     */
    @Test
    public void deadTTL() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            amqpTemplate.convertAndSend("niici.topic.exchange", "topic.ttl", "超时场景消息发送成功",
                    /**
                     * 给消息设置超时时间的问题：
                     * Rabbitmq只会检查第一个消息是否过期, 如果第一个消息超时时间较长，第二个消息超时时间较短，则不能实现第二个消息先执行超时，导致消息堆积。
                     */
                    message -> {
                        message.getMessageProperties().setExpiration("5000");
                        return message;
                    });
            Thread.sleep(5000);
        }
    }

    /**
     * 队列达到最大长度场景死信队列测试
     *
     * @throws InterruptedException
     */
    @Test
    public void deadMaxLength() {
        for (int i = 0; i < 10; i++) {
            amqpTemplate.convertAndSend("niici.topic.exchange", "topic.max", "队列达到最大长度场景消息发送成功");
        }
    }

    /**
     * 消息被拒收场景死信队列测试
     *
     * @throws InterruptedException
     */
    @Test
    public void deadNack() {
        for (int i = 0; i < 10; i++) {
            amqpTemplate.convertAndSend("niici.topic.exchange", "topic.nack", "消息被拒收场景消息发送成功");
        }
    }

    /**
     * 基于插件的延迟队列测试
     *
     * @throws InterruptedException
     */
    @Test
    public void delay() throws InterruptedException {
        /**
         * 创建两条延迟消息，一条设置5s超时时间，一条设置1s，测试是否1s的消息先被监听到
         */
        amqpTemplate.convertAndSend("niici.delay.exchange", "delay.five", "超时时间5s的消息", message -> {
            message.getMessageProperties().setDelay(5000);
            return message;
        });

        amqpTemplate.convertAndSend("niici.delay.exchange", "delay.one", "超时时间1s的消息", message -> {
            message.getMessageProperties().setDelay(1000);
            return message;
        });
        // 消息发送完成后, 等待10s, 让监听器去监听, 在控制台打印结果
        Thread.sleep(10000);
    }
}
