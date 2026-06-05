package com.kefu.dialogue.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kefu.dialogue.client.CSClient;
import com.kefu.dialogue.domain.dto.ChatMessageDTO;
import com.kefu.dialogue.domain.dto.ServiceRelationshipDTO;
import com.kefu.dialogue.domain.po.ChatMessage;
import com.kefu.dialogue.domain.po.ChatQueryParam;
import com.kefu.dialogue.domain.vo.ChatListResult;
import com.kefu.dialogue.domain.vo.ChatMessageVO;
import com.kefu.dialogue.mapper.DialogueMapper;
import com.kefu.dialogue.service.DialogueService;
import com.kefu.dialogue.webSocket.UserWebSocketServer;
import com.kefu.icsscommon.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DialogueServiceImpl extends ServiceImpl<DialogueMapper, ChatMessage> implements DialogueService {

    private final StringRedisTemplate redisTemplate;
    private final UserWebSocketServer userSessionManager;
    private final CSClient csClient;
    private final RabbitTemplate rabbitTemplate;

    /*
    * 保存客服的对话并通知用户
    * */
    @Override
    public void saveServiceDialogue(ChatMessageDTO chatMessageDto, Integer senderType) {
        String userId = String.valueOf(chatMessageDto.getUserId());
        String relationKey = "chat:relation:" + userId;

        // 1.刷新关系过期时间
        redisTemplate.expire(relationKey, 30, TimeUnit.MINUTES);

        // 2.保存到数据库
        this.saveDialogue(chatMessageDto, senderType);

        // 3.推送给用户
        ChatMessageVO vo = BeanUtils.copyBean(chatMessageDto, ChatMessageVO.class);
        vo.setSenderType(senderType);
        log.info("{}发送消息给用户：{}", chatMessageDto.getStaffId(), userId);
        System.out.println(vo);
        userSessionManager.sendMessage(userId, vo);
    }

    /*
    * 保存用户的与客服的对话
    * */
    @Override
    public void saveUserMessage(ChatMessageDTO chatMessageDto) {
        String userId = String.valueOf(chatMessageDto.getUserId());
        String relationKey = "chat:relation:" + userId;

        // 1.确认服务关系,默认为关系已经断开
        // 查询现在有的关系
        String csId = redisTemplate.opsForValue().get(relationKey);

        if (csId == null || csId.equals("0")) {
            // 关系已断开, 或上一个是机器人，重新分配客服
            csId = redisTemplate.opsForSet().randomMember("cs:online");
            if (csId == null) {
                // 没有客服在线
                return;
            }
            // 保存到关系表
            ServiceRelationshipDTO serviceRelationshipDto = new ServiceRelationshipDTO();
            serviceRelationshipDto.setUserId(Long.parseLong(userId));
            serviceRelationshipDto.setStaffId(Long.parseLong(csId));
            csClient.saveCustomerServiceRelationship(serviceRelationshipDto);
            // 重新建立关系，30min
            redisTemplate.opsForValue().set(relationKey, csId, 30, TimeUnit.MINUTES);
        } else {
            // 有关系，客服继续服务,续期
            redisTemplate.opsForValue().set(relationKey, csId, 30, TimeUnit.MINUTES);
        }
        // 2.保存对话
        chatMessageDto.setStaffId(Long.parseLong(csId));
        this.saveDialogue(chatMessageDto, 1);
        // 异步调用 3.提醒客服
        ChatMessageVO vo = BeanUtils.copyBean(chatMessageDto, ChatMessageVO.class);
        vo.setSenderType(1);
//        //同步调用
//        csClient.sendMessage(vo);
        // 异步调用
        try {
            rabbitTemplate.convertAndSend("CS.direct", "user.message", vo);
        } catch (Exception e) {
            log.info("异步提醒客服失败");
        }
    }

    /*
    * 查询对话历史
    * 用户查询与自己相关的全部对话
    * 客服查询自己与某个用户的全部对话
    * */
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

    /*
    * 保存对话
    * */
    @Override
    public void saveDialogue(ChatMessageDTO chatMessageDto, Integer senderType) {
        // 构建基本信息
        ChatMessage chatMessage = BeanUtils.copyBean(chatMessageDto, ChatMessage.class);
        chatMessage.setSenderType(senderType);
        chatMessage.setSendTime(LocalDateTime.now());
        chatMessage.setCreateTime(LocalDateTime.now());
        chatMessage.setUpdateTime(LocalDateTime.now());
        this.save(chatMessage);
    }

    /*
    * 撤回消息
    * */
    @Override
    public void removeDialogueById(Long id) {
        LambdaUpdateWrapper<ChatMessage> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(ChatMessage::getId, id)
                .set(ChatMessage::getUpdateTime, LocalDateTime.now())
                .set(ChatMessage::getIsRecalled, 1);

        this.update(null, wrapper);

        // TODO 推送撤销对话的消息id
    }
}
