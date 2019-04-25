package com.spring.task.task;

import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@EnableScheduling //开启定时任务
@Log4j2
public class ScheduleTask {
    /**
     * 定义一个 cron 表达式 每秒执行一次
     */
    @Scheduled(cron="0/1 * * * * ?")
    public void outMessage(){
        log.info("当前时间: "+System.currentTimeMillis());
    }
}
