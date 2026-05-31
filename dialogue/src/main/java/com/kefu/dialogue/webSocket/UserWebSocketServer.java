package com.kefu.dialogue.webSocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kefu.dialogue.domain.vo.ChatMessageVO;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/user/websocket/{userId}")
public class UserWebSocketServer {

    private static final Map<String, Session> USER_SESSION_MAP = new ConcurrentHashMap<>();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    static {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        // 关键：禁用时间戳，改为字符串格式
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @OnOpen
    public void OnOpen (Session session, @PathParam("userId") String userId) {
        USER_SESSION_MAP.put(userId, session);
    }

    @OnClose
    public void OnClose(@PathParam("userId") String userId) {
        USER_SESSION_MAP.remove(userId);
    }

    public void sendMessage(String userId, ChatMessageVO message) {
        Session session = USER_SESSION_MAP.get(userId);
        System.out.println("正在查询用户session:" + session);
        if (session != null && session.isOpen()) {
            try {
                // 必须注册这个模块，否则 LocalDateTime 序列化会报错！
                OBJECT_MAPPER.registerModule(new JavaTimeModule());

                // 序列化为 JSON 字符串
                String json = OBJECT_MAPPER.writeValueAsString(message);

                // 异步发送（不阻塞，直接返回）
                session.getAsyncRemote().sendText(json);
            } catch (Exception e) {
                System.out.println("推送失败");
            }
        }
    }
}
