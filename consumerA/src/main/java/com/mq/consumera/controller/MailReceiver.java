package com.mq.consumera.controller;

import com.alibaba.fastjson.JSON;
import com.mq.common.pojo.mail.Mail;
import com.mq.consumera.util.SendMailUtil;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class MailReceiver {
    @Autowired
    private SendMailUtil sendMailUtil;

    @RabbitListener(queues = "mail.queue")
    public void consume(Message message, Channel channel) throws IOException {
        Mail mail = JSON.parseObject(new String(message.getBody(), "UTF-8"), Mail.class);
        log.info(" received, mail: " + mail);

        MessageProperties properties = message.getMessageProperties();
        long tag = properties.getDeliveryTag();

        boolean success = sendMailUtil.send(mail);
        log.info("send mail, success: {}", success);
        if (success) {
            channel.basicAck(tag, false);
        } else {
            channel.basicNack(tag, false, true);
        }
    }

}
