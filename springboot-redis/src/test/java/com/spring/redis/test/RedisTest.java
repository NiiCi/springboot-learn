package com.spring.redis.test;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log4j2
public class RedisTest {
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * redis 基础操作 get set
     */
    @Test
    public void testBasicRedis(){
        // 向 redis 添加数据
        // 添加一个10秒过期的key
        // test:user 代表 test 目录下的 user key
        redisTemplate.opsForValue().set("test:user:niici","value".toUpperCase(),10,TimeUnit.DAYS);
        log.info(redisTemplate.opsForValue().get("test:user:niici"));
    }

    /**
     * redis 操作 hash
     */
    @Test
    public void testHash(){
        // redis中 存入 key 为user的 hash 数据
        BoundHashOperations<String, String, Object> hashOps =
                this.redisTemplate.boundHashOps("user");
        // 操作hash数据
        hashOps.put("name", "jack");
        hashOps.put("age", "21");

        // 获取单个数据
        Object name = hashOps.get("name");
        System.out.println("name = " + name);

        // 获取所有数据
        Map<String, Object> map = hashOps.entries();

        for (Map.Entry<String, Object> me : map.entrySet()) {
            System.out.println(me.getKey() + " : " + me.getValue());
        }
    }


}
