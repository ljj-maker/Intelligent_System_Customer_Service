package com.kefu.dialogue.controller;

import com.kefu.dialogue.domain.dto.ChatMessageDTO;
import com.kefu.dialogue.service.DialogueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "机器人对话相关接口")
@RestController
@RequestMapping("/dialogue/bot")
@RequiredArgsConstructor
public class BotDialogueController {

    private final DialogueService dialogueService;

    @Operation(summary = "保存机器人对话")
    @PostMapping
    public void saveBotDialogue(@RequestBody ChatMessageDTO chatMessageDto) {
        log.info("保存机器人对话 -> ");
        Integer senderType = 3;
        // 新增
        dialogueService.saveDialogue(chatMessageDto, senderType);
    }
}
