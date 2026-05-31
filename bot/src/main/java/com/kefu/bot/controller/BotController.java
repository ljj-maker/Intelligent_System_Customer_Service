package com.kefu.bot.controller;

import com.kefu.bot.Service.BotService;
import com.kefu.bot.domain.dto.ChatMessageDTO;
import com.kefu.bot.domain.vo.ChatMessageVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "机器人对话接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/bot/chat")
public class BotController {

    private final BotService botService;

    @Operation(summary = "对话")
    @PostMapping
    public ChatMessageVO chat(@RequestBody ChatMessageDTO chatMessageDto) {
        log.info("收到对话请求");
        // 调用服务
        return botService.chat(chatMessageDto);
    }
}
