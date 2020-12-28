package com.zz.rabbitmq.utils;

import com.zz.rabbitmq.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author zzj
 * @className RedisUtil
 * @description TODO
 * @date 2020/12/28
 */

@Component
public class RedisUtil {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String ORDER_TAG = "order_reced";

    public boolean orderReced(Order order){
        return redisTemplate.opsForValue().setIfAbsent(generateKey(order.getMessageId()), "true", 30, TimeUnit.MINUTES);
    }

    private String generateKey(String... str){
        StringBuilder sb = new StringBuilder();
        for (String s : str) {
            sb.append("_").append(s);
        }

        return sb.toString();
    }
}
