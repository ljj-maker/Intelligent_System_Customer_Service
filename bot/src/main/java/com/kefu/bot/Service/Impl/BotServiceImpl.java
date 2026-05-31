package com.kefu.bot.Service.Impl;

import com.kefu.bot.Service.BotService;
import com.kefu.bot.client.CSRelationship;
import com.kefu.bot.client.Dialogue;
import com.kefu.bot.domain.dto.ChatMessageDTO;
import com.kefu.bot.domain.dto.ServiceRelationshipDTO;
import com.kefu.bot.domain.po.ChatMessage;
import com.kefu.bot.domain.vo.ChatMessageVO;
import com.kefu.icsscommon.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class BotServiceImpl implements BotService {

    private final Dialogue dialogue;
    private final StringRedisTemplate redisTemplate;
    private final CSRelationship csRelationship;

    /*
    * 处理对话请求
    * */
    @Override
    public ChatMessageVO chat(ChatMessageDTO chatMessageDto) {

        // 保存用户的对话信息
        chatMessageDto.setStaffId(0L);  // 设置交流对象为bot
        dialogue.saveUserDialogue(chatMessageDto);

        // 获取消息内容
        String content = chatMessageDto.getContent();
        // 判断转人工意图
        if (content != null && content.contains("转人工")){
            // 调用转人工流程
            return handleTransferToCS(chatMessageDto);
        }

        // 用户没有转人工意图。消息调用ai模型,并返回结果
        ChatMessageVO msg = LLMMessage(chatMessageDto);
        msg.setSenderType(3);
        return LLMMessage(chatMessageDto);
    }

    //TODO 实现大模型处理消息
    public ChatMessageVO LLMMessage(ChatMessageDTO chatMessageDto){
        ChatMessage chatMessage = BeanUtils.copyBean(chatMessageDto, ChatMessage.class);
        // 大模型处理消息
        ChatMessageVO chatMessageVO = BeanUtils.copyBean(chatMessage, ChatMessageVO.class);
        chatMessageVO.setSenderType(3);
        chatMessageVO.setStaffId(0L);

        // 保存机器人的对话信息
        dialogue.saveBotDialogue(chatMessageDto);
        // 返回结果
        return chatMessageVO;
    }

    /*
    * 转人工处理消息流程
    * */
    public ChatMessageVO handleTransferToCS(ChatMessageDTO chatMessageDto){
        String relationKey = "chat:relation:" + chatMessageDto.getUserId();  // 客服关系key
        // 1.分配在线客服
        // 查询现在有的关系
        String csId = redisTemplate.opsForValue().get(relationKey);
        if (csId != null) {
            // 客服已存在
            // 刷新服务关系
            redisTemplate.opsForValue().set(relationKey, csId, 30, TimeUnit.MINUTES);
        } else {
            // 如果没有现成的服务关系就分配
            csId = redisTemplate.opsForSet().randomMember("cs:online");
            // 2.如果没有客服在线
            if (StringUtils.isEmpty(csId)) {
                // 构建简单的回复提示
                ChatMessageVO vo = new ChatMessageVO();
                vo.setSenderType(4);
                vo.setMsgType(1);
                vo.setContent("当前没有客服在线，请稍后再试");
                return vo;
            }
            // 3.建立服务关系，并存储到redis
            redisTemplate.opsForValue().set(relationKey, csId, 30, TimeUnit.MINUTES);
            // 4.保存客服关系到数据库
            ServiceRelationshipDTO serviceRelationshipDto = new ServiceRelationshipDTO();
            serviceRelationshipDto.setUserId(chatMessageDto.getUserId());
            serviceRelationshipDto.setStaffId(Long.valueOf(csId));
            csRelationship.saveCustomerServiceRelationship(serviceRelationshipDto);
        }
        // 6.返回msgType=3,让前端收到后将http请求改为dialogue接口，以及建立websocket连接到dialogue模块的ws//user/websocket/{userId}
        ChatMessageVO vo = new ChatMessageVO();
        vo.setMsgType(3);
        vo.setContent("已为您转接人工客服");
        return vo;
    }
}
