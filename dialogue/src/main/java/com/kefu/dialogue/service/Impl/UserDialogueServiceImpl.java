package com.kefu.dialogue.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kefu.dialogue.domain.po.ChatMessage;
import com.kefu.dialogue.mapper.UserDialogueMapper;
import com.kefu.dialogue.service.UserDialogueService;
import org.springframework.stereotype.Service;

@Service
public class UserDialogueServiceImpl extends ServiceImpl<UserDialogueMapper, ChatMessage> implements UserDialogueService {
}
