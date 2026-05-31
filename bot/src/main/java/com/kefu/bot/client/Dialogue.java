package com.kefu.bot.client;

import com.kefu.bot.domain.dto.ChatMessageDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("dialogue")
public interface Dialogue {

    // 添加用户对话
    @PostMapping("/dialogue/user")
    void saveUserDialogue(@RequestBody ChatMessageDTO chatMessageDto);

    // 添加机器人对话
    @PostMapping("/dialogue/bot")
    void saveBotDialogue(@RequestBody ChatMessageDTO chatMessageDto);
}
