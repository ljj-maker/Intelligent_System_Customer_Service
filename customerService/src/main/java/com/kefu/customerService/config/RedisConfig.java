package com.kefu.customerService.config;

import com.kefu.customerService.listener.CsExpiredListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisConfig {

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
        return new StringRedisTemplate(factory);
    }

    // 创建 RedisMessageListenerContainer检测过期的 Key
    @Bean
    public RedisMessageListenerContainer container(
            RedisConnectionFactory connectionFactory,
            CsExpiredListener csExpiredListener) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        // 在方法内部创建 Adapter，不再作为独立 Bean 暴露
        MessageListenerAdapter adapter = new MessageListenerAdapter(csExpiredListener, "onMessage");

        // 监听 0 号数据库的过期事件
        container.addMessageListener(adapter, new PatternTopic("__keyevent@0__:expired"));

        return container;
    }
}
