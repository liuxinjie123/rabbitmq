package com.mq.providera.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置相关的消息确认回调函数
 *
 * 到这里，生产者推送消息的消息确认调用回调函数已经完毕。
 *
 * 可以看到上面写了两个回调函数，
 * 一个叫 ConfirmCallback ，
 * 一个叫 RetrunCallback；
 * 那么以上这两种回调函数都是在什么情况会触发呢？
 *
 * 先从总体的情况分析，推送消息存在四种情况：
 *
 * ①消息推送到server，但是在server里找不到交换机
 * ②消息推送到server，找到交换机了，但是没找到队列
 * ③消息推送到sever，交换机和队列啥都没找到
 * ④消息推送成功
 */
@Slf4j
@Configuration
public class RabbitConfig {

    @Bean
    public RabbitTemplate createRabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        // 设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
        rabbitTemplate.setMandatory(true);

        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                if (ack) {
                    log.info(" send success to exchange.");
                } else {
                    log.info(" send failure to exchange.");
                }
                log.info("");
                log.info("ConfirmCallback:     " + "相关数据：" + correlationData);
                log.info("ConfirmCallback:     " + "确认情况：" + ack);
                log.info("ConfirmCallback:     " + "原因：" + cause);
                log.info("");
            }
        });

        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            @Override
            public void returnedMessage(ReturnedMessage returned) {
                log.info("");
                log.info("ReturnCallback:     " + "消息：" + returned.getMessage());
                log.info("ReturnCallback:     " + "回应码：" + returned.getReplyCode());
                log.info("ReturnCallback:     " + "回应信息：" + returned.getReplyText());
                log.info("ReturnCallback:     " + "交换机：" + returned.getExchange());
                log.info("ReturnCallback:     " + "路由键：" + returned.getRoutingKey());
                log.info("");
            }
        });

        return rabbitTemplate;
    }

    /**
     *  此处设置，将对象以json的方式发送出去，存储在队列中
     *  若不配置默认是对象序列化以后发送出去，在rabbitmq web端看到队列中存储的是一串序列化后的乱码
     * @return
     */
    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

}