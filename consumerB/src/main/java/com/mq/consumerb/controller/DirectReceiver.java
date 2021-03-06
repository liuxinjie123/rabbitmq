package com.mq.consumerb.controller;

import com.mq.common.pojo.MQObj;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Controller;

@Controller
@RabbitListener(queues = "TestDirectQueue")  //监听的队列名称 TestDirectQueue
public class DirectReceiver {
    @RabbitHandler
    public void process(MQObj obj) {
        System.out.println("DirectReceiver B 消费者收到消息  : " + obj);
    }
}
