package com.kefu.administrator.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Set;

@FeignClient("customerService")
public interface CSClient {

    // 获取客服在线详情
    @GetMapping("/service/service/info")
    Map<Object, Object> getCsInfo(@RequestParam Long csId);

    // 获取所有在线客服
    @GetMapping("/service/service/onlineList")
    Set<String> getCsList();
}
