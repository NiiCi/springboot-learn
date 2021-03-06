package com.spring.rabbitmq.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
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
            amqpTemplate.convertAndSend("niici.create.simple.queue", msg);
            Thread.sleep(5000);
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
            amqpTemplate.convertAndSend("niici.create.work.queue", msg + i);

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
            amqpTemplate.convertAndSend("niici.direct.exchange", "update", "修改成功");
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
            //广播消息模型是所有队列都能接收到的，所以没有 routeKey，即 为空
            amqpTemplate.convertAndSend("niici.topic.exchange", "user.delete", "user 删除成功");
            amqpTemplate.convertAndSend("niici.topic.exchange", "student.delete", "student 删除成功");
            amqpTemplate.convertAndSend("niici.direct.exchange","niici.insert","新增成功");
            amqpTemplate.convertAndSend("niici.direct.exchange","niici.update","修改成功");
            Thread.sleep(5000);
        }
    }
}
