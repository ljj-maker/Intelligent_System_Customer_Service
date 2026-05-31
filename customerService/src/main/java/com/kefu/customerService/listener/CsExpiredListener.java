package com.kefu.customerService.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class CsExpiredListener implements MessageListener {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        // message.getBody() 是过期的 Key 名称
        String expiredKey = new String(message.getBody(), StandardCharsets.UTF_8);

        System.out.println("监听到过期 Key: " + expiredKey);

        // 只处理客服相关的 Key
        if (expiredKey.startsWith("cs:info:")) {
            String csId = expiredKey.replace("cs:info:", "");

            // 1. 从在线集合中移除（如果还存在的话）
            redisTemplate.opsForSet().remove("cs:online", csId);

            // 2. 记录下线日志/通知
            System.out.println("客服 " + csId + " 因心跳超时，被 Redis 自动清理下线");

            // 3. 这里可以触发：将该客服的会话转给其他客服
            // conversationService.reassignCsSessions(Long.valueOf(csId));
        }
    }
}