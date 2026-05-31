package com.kefu.customerService.service.Impl;

import com.kefu.customerService.service.CsStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class CsStatusServiceImpl implements CsStatusService {

    private final StringRedisTemplate redisTemplate;

    private static final String CS_ONLINE_SET = "cs:online";
    private static final String CS_INFO_PREFIX = "cs:info:";
    private static final long HEARTBEAT_TIMEOUT = 60 * 5;

    /*
    * 客服登录后将信息加入redis
    * */
    @Override
    public void login(Long csId, String csName) {
        String infoKey = CS_INFO_PREFIX + csId;

        // 1.加入在线集合
        redisTemplate.opsForSet().add(CS_ONLINE_SET, csId.toString());

        // 2.存储客服详情到Hash
        Map<String, String> info = new HashMap<>();
        info.put("id", csId.toString());
        info.put("name", csName);
        info.put("status", "online");
        info.put("loginTime", LocalDateTime.now().toString());
        info.put("lastHeartbeat", String.valueOf(System.currentTimeMillis()));

        redisTemplate.opsForHash().putAll(infoKey, info);

        // 3.设置信息的过期机制
        redisTemplate.expire(infoKey, HEARTBEAT_TIMEOUT, TimeUnit.SECONDS);

        log.info("客服登录 -> csId={}, csName={}", csId, csName);
    }

    /*
    * 客服心跳接口(每30s调用一次)
    *
    * 每次心跳重置过期时间为HEARTBEAT_TIMEOUT
    * */
    @Override
    public void heartbeat(Long csId) {
        String infoKey = CS_INFO_PREFIX + csId;

        // 1.检查客服是否在线
        Boolean exists = redisTemplate.hasKey(infoKey);
        if (!exists) {
            // 如果没有key说明心跳断很久了，需要重新登录
            throw new RuntimeException("客服已下线");
        }

        // 2.更新最后心跳时间
        redisTemplate.opsForHash().put(infoKey, "lastHeartbeat", String.valueOf(System.currentTimeMillis()));

        // 重新提交信息，重置过期时间
        redisTemplate.expire(infoKey, HEARTBEAT_TIMEOUT, TimeUnit.SECONDS);
    }

    /*
    * 客服下线
    * */
    @Override
    public void logout(Long csId) {
        String infoKey = CS_INFO_PREFIX + csId;

        // 1.从在线集合中移除
        redisTemplate.opsForSet().remove(CS_ONLINE_SET, csId.toString());

        // 2.删除详情key
        redisTemplate.delete(infoKey);
    }

    /*
    * 咨询在线客服列表
    * */
    @Override
    public Set<String> getOnlineCsIds() {
        return redisTemplate.opsForSet().members(CS_ONLINE_SET);
    }

    /*
    * 获取在线客服详情
    * */
    @Override
    public Map<Object, Object> getCsInfo(Long csId) {
        String infoKey = CS_INFO_PREFIX + csId;
        return redisTemplate.opsForHash().entries(infoKey);
    }
}
