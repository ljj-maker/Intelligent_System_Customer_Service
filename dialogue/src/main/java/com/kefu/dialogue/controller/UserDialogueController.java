package com.kefu.dialogue.controller;

import com.kefu.dialogue.domain.dto.ChatMessageDTO;
import com.kefu.dialogue.domain.po.ChatMessage;
import com.kefu.dialogue.domain.po.ChatQueryParam;
import com.kefu.dialogue.domain.vo.ChatListResult;
import com.kefu.dialogue.service.DialogueService;
import com.kefu.icsscommon.utils.BeanUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@Tag(name = "用户对话相关接口")
@RestController
@RequestMapping("/dialogue/user")
@RequiredArgsConstructor
public class UserDialogueController {

    private final DialogueService dialogueService;

    @Operation(summary = "保存与人工交流的用户对话")
    @PostMapping("/message")
    public void saveUserMessage(@RequestBody ChatMessageDTO chatMessageDto) {
        log.info("保存用户与客服对话 -> ");
        dialogueService.saveUserMessage(chatMessageDto);
    }

    @Operation(summary = "保存用户对话")
    @PostMapping
    public void saveUserDialogue(@RequestBody ChatMessageDTO chatMessageDto) {
        log.info("保存用户对话 -> ");
        Integer senderType = 1;
        // 新增
        dialogueService.saveDialogue(chatMessageDto, senderType);
    }

    @Operation(summary = "撤销用户对话")
    @DeleteMapping("/{id}")
    public void deleteUserDialogue(@PathVariable Long id) {
        log.info("删除用户对话 -> ");
        // 删除
        dialogueService.removeById(id);
    }

    @Operation(summary = "查询用户对话列表")
    @GetMapping
    public ChatListResult queryUserDialogueList(@RequestParam @Parameter(description = "用户id") Long userId) {
        log.info("查询用户对话列表 -> ");
        // 查询
        ChatQueryParam param = new ChatQueryParam();
        param.setUserId(userId);
        return dialogueService.queryDialogueList(param);
    }
}
