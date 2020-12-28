package com.zz.rabbitmq.producer;


import com.rabbitmq.client.Channel;
import com.zz.rabbitmq.entity.Order;
import com.zz.rabbitmq.utils.RedisUtil;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class OrderReceive {

    @Autowired
    private RedisUtil redisUtil;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "order-queue", durable = "true"),
            exchange = @Exchange(value = "order-exchange", durable = "true", type = "topic"),
            key = "order.*"))
    @RabbitHandler
    public void onOrderMessage(@Payload Order order,
                               @Headers Map<String, Object> headers,
                               Channel channel) {

        Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        try {
            System.out.println("====收到消息，开始消费=====");
            System.out.println("==消息为：" + order);
//            new SocketConnect(2001, "110,110,110,10").connect();
            //避免重复消费的问题
            boolean b = redisUtil.orderReced(order);
            if (b) {
                System.out.println("处理成功 " + order);
            } else {
                System.out.println("该消息已被处理过，暂不处理 " + order);
            }
            //手动ack
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("丢弃消息");
            try {
                channel.basicNack(deliveryTag, false, false);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }
}
