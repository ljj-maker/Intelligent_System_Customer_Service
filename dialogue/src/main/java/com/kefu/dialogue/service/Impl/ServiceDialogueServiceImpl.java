package com.kefu.dialogue.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kefu.dialogue.domain.po.ChatMessage;
import com.kefu.dialogue.mapper.ServiceDialogueMapper;
import com.kefu.dialogue.mapper.UserDialogueMapper;
import com.kefu.dialogue.service.ServiceDialogueService;
import com.kefu.dialogue.service.UserDialogueService;
import org.springframework.stereotype.Service;

@Service
public class ServiceDialogueServiceImpl extends ServiceImpl<ServiceDialogueMapper, ChatMessage> implements ServiceDialogueService {
}
