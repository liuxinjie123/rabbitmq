package com.mq.consumerb.controller;

import com.mq.common.pojo.MQObj;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "fanout.A")
public class FanoutReceiverA {

    @RabbitHandler
    public void process(MQObj obj) {
        System.out.println("FanoutReceiverB - A 消费者收到消息  : " + obj);
    }

}
