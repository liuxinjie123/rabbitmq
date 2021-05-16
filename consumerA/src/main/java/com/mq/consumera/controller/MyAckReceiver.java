package com.mq.consumera.controller;

import com.alibaba.fastjson.JSON;
import com.mq.common.pojo.MQObj;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyAckReceiver implements ChannelAwareMessageListener {

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            //因为传递消息的时候用的map传递,所以将Map从Message内取出需要做些处理
            System.out.println("body : " + new String(message.getBody(),"utf-8"));
            MQObj obj = JSON.parseObject(new String(message.getBody(),"utf-8"), MQObj.class);
            System.out.println("obj : " + obj);

            if ("TestDirectQueue".equals(message.getMessageProperties().getConsumerQueue())){
                System.out.println("消费的消息来自的队列名为 : " + message.getMessageProperties().getConsumerQueue());
                System.out.println("消息成功消费到 : " + obj);
                System.out.println("执行 TestDirectQueue 中的消息的业务处理流程......");

            }

            if ("fanout.A".equals(message.getMessageProperties().getConsumerQueue())){
                System.out.println("消费的消息来自的队列名为 : "+message.getMessageProperties().getConsumerQueue());
                System.out.println("消息成功消费到 : " + obj);
                System.out.println("执行 fanout.A 中的消息的业务处理流程......");

            }

            //第二个参数，手动确认可以被批处理，当该参数为 true 时，则可以一次性确认 delivery_tag 小于等于传入值的所有消息
            channel.basicAck(deliveryTag, true);
//			channel.basicReject(deliveryTag, true);//第二个参数，true会重新放回队列，所以需要自己根据业务逻辑判断什么时候使用拒绝
        } catch (Exception e) {
            channel.basicReject(deliveryTag, false);
            e.printStackTrace();
        }
    }

}