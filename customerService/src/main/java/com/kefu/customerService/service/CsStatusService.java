package com.kefu.customerService.service;

import java.util.Map;
import java.util.Set;

public interface CsStatusService {

    // 客服登录
    void login(Long csId, String csName);

    // 客服心跳
    void heartbeat(Long csId);

    // 客服登出
    void logout(Long csId);

    // 获取在线客服id
    Set<String> getOnlineCsIds();

    // 获取客服信息
    Map<Object, Object> getCsInfo(Long csId);
}
