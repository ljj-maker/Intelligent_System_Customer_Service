package com.kefu.administrator.config;

import com.kefu.icsscommon.utils.UserContext;
import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;

public class DefaultFeignConfig {

    // 配置Feign 客户端的日志级别配置
    @Bean
    public Logger.Level fullFeignLoggerLevel(){ return Logger.Level.FULL;}

    //
    @Bean
    public RequestInterceptor userInfoRequestInterceptor(){
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                // 1. 从当前线程的上下文中取出用户ID
                Long userId = UserContext.getUser();
                // 2.如果当前线程包含用户ID
                if (userId != null) {
                    // 给即将发出的HTTP请求添加一个名为user-info，值为userId的请求头
                    requestTemplate.header("user-info", userId.toString());
                }
            }
        };
    }
}
