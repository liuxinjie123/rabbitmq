package com.mq.consumera.controller;

import com.mq.common.pojo.MQObj;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Controller;

@Controller
@RabbitListener(queues = {"TestDirectQueue", "TestDirectQueue2"})  //监听的队列名称 TestDirectQueue
public class DirectReceiver {
    @RabbitHandler
    public void process(MQObj obj) {
        System.out.println("DirectReceiver A 消费者收到消息  : " + obj);
    }
}
