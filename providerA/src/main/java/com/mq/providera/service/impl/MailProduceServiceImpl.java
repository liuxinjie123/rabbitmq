package com.mq.providera.service.impl;

import com.alibaba.fastjson.JSON;
import com.mq.common.pojo.mail.Mail;
import com.mq.providera.config.MailRabbitConfig;
import com.mq.providera.service.MailProduceService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class MailProduceServiceImpl implements MailProduceService {
    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Override
    public boolean send(Mail mail) {
        String id = UUID.randomUUID().toString().replace("-", "");
        mail.setMsgId(id);
        mail.setTitle("标题");
        mail.setContent("rabbitmq mail, " + LocalDateTime.now());

        // 发送消息到 rabbitMQ
        CorrelationData correlationData = new CorrelationData(id);
        rabbitTemplate.convertAndSend(MailRabbitConfig.MAIL_EXCHANGE_NAME, MailRabbitConfig.MAIL_ROUTING_KEY_NAME, JSON.toJSONString(mail), correlationData);
        return true;
    }
}
