package com.kefu.bot.client;

import com.kefu.bot.domain.dto.ServiceRelationshipDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("customerService")
public interface CSRelationship {

    // 添加用户关系
    @PostMapping("service/relationship")
    void saveCustomerServiceRelationship(@RequestBody ServiceRelationshipDTO serviceRelationshipDto);
}
