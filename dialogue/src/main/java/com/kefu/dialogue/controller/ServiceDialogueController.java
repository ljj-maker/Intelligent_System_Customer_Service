package com.kefu.dialogue.controller;

import com.kefu.dialogue.domain.dto.ChatMessageDTO;
import com.kefu.dialogue.domain.po.ChatMessage;
import com.kefu.dialogue.domain.po.ChatQueryParam;
import com.kefu.dialogue.domain.vo.ChatListResult;
import com.kefu.dialogue.service.DialogueService;
import com.kefu.icsscommon.utils.BeanUtils;
import com.kefu.icsscommon.utils.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@Tag(name = "客服对话相关接口")
@RestController
@RequestMapping("/dialogue/service")
@RequiredArgsConstructor
public class ServiceDialogueController {

    private final DialogueService dialogueService;

    @Operation(summary = "保存客服对话")
    @PostMapping
    public void saveServiceDialogue(@RequestBody ChatMessageDTO chatMessageDto) {
        log.info("保存客服对话 -> ");
        chatMessageDto.setStaffId(UserContext.getUser());
        Integer senderType = 2;
        // 新增
        dialogueService.saveServiceDialogue(chatMessageDto, senderType);
    }

    @Operation(summary = "撤销客服对话")
    @DeleteMapping("/{id}")
    public void deleteServiceDialogue(@PathVariable Long id) {
        log.info("删除客服对话 -> ");
        // 删除
        dialogueService.removeDialogueById(id);
    }

    @Operation(summary = "查询客服对话列表")
    @GetMapping
    public ChatListResult queryServiceDialogueList(
            @RequestParam @Parameter(description = "用户id") Long userId,
            @RequestParam(required = false) @Parameter(description = "客服id") Long staffId) {
        log.info("查询客服对话列表 -> ");
        ChatQueryParam param = new ChatQueryParam();
        param.setUserId(userId);
        param.setStaffId(staffId);
        return dialogueService.queryDialogueList(param);
    }
}
