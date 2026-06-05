package com.kefu.customerService.listener;

import com.kefu.customerService.domain.dto.ServiceRelationshipDTO;
import com.kefu.customerService.domain.po.ServiceRelationship;
import com.kefu.customerService.domain.vo.ChatMessageVO;
import com.kefu.customerService.service.CustomerServiceRelationshipService;
import com.kefu.customerService.webSocket.CsWebSocketServer;
import com.kefu.icsscommon.utils.BeanUtils;
import com.kefu.icsscommon.utils.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RelationshipListener {

    private final CustomerServiceRelationshipService customerServiceRelationshipService;
    private final CsWebSocketServer csWebSocketServer;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "save.relationship.queue", durable = "true", arguments = {@Argument(name = "x-max-length", value = "10000", type = "java.lang.Integer"), @Argument(name = "x-message-ttl", value = "600000", type = "java.lang.Integer")}),
            exchange = @Exchange(name = "CS.direct", type = ExchangeTypes.DIRECT),
            key = "save.relationship"
    ))
    public void listenSaveRelationship(ServiceRelationshipDTO serviceRelationshipDto) {
        log.info("保存客服服务关系 -> ");
        // 新增
        customerServiceRelationshipService.saveRelationship(BeanUtils.copyBean(serviceRelationshipDto, ServiceRelationship.class));
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "user.message.queue", durable = "true", arguments = {@Argument(name = "x-max-length", value = "10000", type = "java.lang.Integer"), @Argument(name = "x-message-ttl", value = "600000", type = "java.lang.Integer")}),
            exchange = @Exchange(name = "CS.direct", type = ExchangeTypes.DIRECT),
            key = "user.message"
    ))
    public void listenUserMessage(ChatMessageVO chatMessageVO) {
        log.info("用户 {} 给客服{}发送消息 -> ", UserContext.getUser(), chatMessageVO.getUserId());
        csWebSocketServer.sendMessage(chatMessageVO, chatMessageVO.getStaffId().toString());
    }
}
