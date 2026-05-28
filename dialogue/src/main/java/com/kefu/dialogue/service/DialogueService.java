package com.kefu.dialogue.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kefu.dialogue.domain.po.ChatMessage;
import com.kefu.dialogue.domain.po.ChatQueryParam;
import com.kefu.dialogue.domain.vo.ChatListResult;

public interface DialogueService extends IService<ChatMessage> {

    // 查询客服对话列表
    ChatListResult queryDialogueList(ChatQueryParam param);


}
