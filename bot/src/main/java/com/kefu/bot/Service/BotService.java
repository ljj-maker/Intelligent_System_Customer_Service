package com.kefu.bot.Service;

import com.kefu.bot.domain.dto.ChatMessageDTO;
import com.kefu.bot.domain.vo.ChatMessageVO;

public interface BotService {

    // 处理对话请求
    ChatMessageVO chat(ChatMessageDTO chatMessageDto);
}
