package com.mq.consumera.controller;

import com.mq.common.pojo.MQObj;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "fanout.C")
public class FanoutReceiverC {
    @RabbitHandler
    public void process(MQObj obj) {
        System.out.println("FanoutReceiverA - C 消费者收到消息  : " + obj);
    }
}
