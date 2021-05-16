package com.mq.consumera.controller;

import com.alibaba.fastjson.JSONObject;
import com.mq.common.pojo.MQObj;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = {"TestDirectQueue", "TestDirectQueue-2"})  //监听的队列名称 TestDirectQueue
public class DirectReceiver {
    @RabbitHandler
    public void process(String msg) {
        MQObj obj = JSONObject.parseObject(msg, MQObj.class);
        System.out.println("DirectReceiver A 消费者收到消息  : " + obj);
    }

}
