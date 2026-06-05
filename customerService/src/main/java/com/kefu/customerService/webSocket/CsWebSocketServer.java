package com.kefu.customerService.webSocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kefu.customerService.domain.vo.ChatMessageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/service/websocket/{csId}")
@Component
public class CsWebSocketServer {

    private static final String CS_ONLINE_SET = "cs:online";
    private static final String CS_INFO_PREFIX = "cs:info:";
    private static final ConcurrentHashMap<String, Session> SESSION_MAP = new ConcurrentHashMap<>();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    static {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        // 关键：禁用时间戳，改为字符串格式
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Autowired
    private static StringRedisTemplate redisTemplate;

    // 标注链接建立时调用的方法
    @OnOpen
    public void onOpen(Session session, @PathParam("csId") String csId) {
        SESSION_MAP.put(csId, session);
    }

    // 标注连接正常关闭时的调用
    @OnClose
    public void onClose(Session session, @PathParam("csId") String csId) {
        SESSION_MAP.remove(csId);
    }

    @OnError
    public void onError(Session session, @PathParam("csId") String csId, Throwable error) {
        SESSION_MAP.remove(csId);
    }

    public void sendMessage(ChatMessageVO chatMessageVO, String csId) {
        Session session = SESSION_MAP.get(csId);
        if (session != null && session.isOpen()) {
            try {
                // 必须注册这个模块，否则 LocalDateTime 序列化会报错！
                OBJECT_MAPPER.registerModule(new JavaTimeModule());

                // 序列化为 JSON 字符串
                String json = OBJECT_MAPPER.writeValueAsString(chatMessageVO);

                // 异步发送（不阻塞，直接返回）
                session.getAsyncRemote().sendText(json);
            } catch (Exception e) {
                System.out.println("发送消息失败");
            }
        }
    }

}
