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
        rabbitTemplate.convertAndSend(directExchangeName,directRouteKey, obj);
        return "ok";
    }

    @GetMapping("/sendTopicMessage1")
    public String sendTopicMessage1() {
        MQObj obj = new MQObj();
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "message: M A N ";

        obj.setId(messageId);
        obj.setData(messageData);
        obj.setCreateTime(LocalDateTime.now());
        rabbitTemplate.convertAndSend("topicExchange", "topic.man", obj);
        return "ok";
    }

    @GetMapping("/sendTopicMessage2")
    public String sendTopicMessage2() {
        MQObj obj = new MQObj();

        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "message: woman is all ";

        obj.setId(messageId);
        obj.setData(messageData);
        obj.setCreateTime(LocalDateTime.now());

        rabbitTemplate.convertAndSend("topicExchange", "topic.woman", obj);
        return "ok";
    }

    @GetMapping("/sendFanoutMessage")
    public String sendFanoutMessage() {
        MQObj obj = new MQObj();

        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "message: testFanoutMessage ";

        obj.setId(messageId);
        obj.setData(messageData);
        obj.setCreateTime(LocalDateTime.now());
        rabbitTemplate.convertAndSend("fanoutExchange", null, obj);
        return "ok";
    }

    /**
     * 触发回调函数
     * ① 消息推送到server，但是在server里找不到交换机
     */
    @GetMapping("/TestMessageAck")
    public String TestMessageAck() {
        MQObj obj = new MQObj();
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "message: non-existent-exchange test message ";

        obj.setId(messageId);
        obj.setData(messageData);
        obj.setCreateTime(LocalDateTime.now());

        rabbitTemplate.convertAndSend("non-existent-exchange", "TestDirectRouting", obj);
        return "ok";
    }

    @GetMapping("/TestMessageAck2")
    public String TestMessageAck2() {
        MQObj obj = new MQObj();
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "message: lonelyDirectExchange test message ";

        obj.setId(messageId);
        obj.setData(messageData);
        obj.setCreateTime(LocalDateTime.now());
        rabbitTemplate.convertAndSend("lonelyDirectExchange", "TestDirectRouting", obj);
        return "ok";
    }

}

