package com.kefu.dialogue.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kefu.dialogue.domain.po.ChatMessage;
import com.kefu.dialogue.domain.po.ChatQueryParam;
import com.kefu.dialogue.domain.vo.ChatListResult;
import com.kefu.dialogue.domain.vo.ChatMessageVO;
import com.kefu.dialogue.mapper.DialogueMapper;
import com.kefu.dialogue.service.DialogueService;
import com.kefu.icsscommon.utils.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DialogueServiceImpl extends ServiceImpl<DialogueMapper, ChatMessage> implements DialogueService {

    @Override
    public ChatListResult queryDialogueList(ChatQueryParam param) {
        // 1. 构建条件：查该用户与该客服的对话
        LambdaQueryWrapper<ChatMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChatMessage::getUserId, param.getUserId())
                .eq(param.getStaffId() != null, ChatMessage::getStaffId, param.getStaffId())
                .orderByAsc(ChatMessage::getSendTime);      // 按时间正序

        // 2. 查询全部
        List<ChatMessage> list = this.list(wrapper);

        // 3. 转 VO
        List<ChatMessageVO> rows = list.stream().map(msg -> {
            return BeanUtils.copyBean(msg, ChatMessageVO.class);
        }).collect(Collectors.toList());

        // 4. 组装返回（total 就是总条数）
        return new ChatListResult((long) rows.size(), rows);
    }

}
