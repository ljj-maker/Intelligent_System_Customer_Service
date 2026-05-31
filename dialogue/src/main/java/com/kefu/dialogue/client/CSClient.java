package com.kefu.dialogue.client;

import com.kefu.dialogue.domain.dto.ServiceRelationshipDTO;
import com.kefu.dialogue.domain.vo.ChatMessageVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "customerService")
public interface CSClient {

    // 向客服端发送信息
    @PostMapping("/service/service/sendMessage")
    void sendMessage(@RequestBody ChatMessageVO chatMessageVO);

    // 保存服务关系
    @PostMapping("service/relationship")
    void saveCustomerServiceRelationship(@RequestBody ServiceRelationshipDTO serviceRelationshipDto);
}
