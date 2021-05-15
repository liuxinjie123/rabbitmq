package com.mq.providera.controller;

import com.mq.common.pojo.MQObj;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
public class SendMessageController {
    @Value("${queue.direct.name}")
    private String directQueueName;
    @Value("${exchange.direct.name}")
    private String directExchangeName;
    @Value("${route.direct.key}")
    private String directRouteKey;

    @Resource
    RabbitTemplate rabbitTemplate;  //使用RabbitTemplate,这提供了接收/发送等等方法

    @GetMapping("/sendDirectMessage")
    public String sendDirectMessage() {
        MQObj obj = new MQObj();
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "test message, hello!";

        obj.setId(messageId);
        obj.setData(messageData);
        obj.setCreateTime(LocalDateTime.now());

        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        rabbitTemplate.convertAndSend(directExchangeName, directRouteKey, obj);
        return "ok";
    }


}
