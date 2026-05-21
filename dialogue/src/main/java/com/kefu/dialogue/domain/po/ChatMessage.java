package com.kefu.dialogue.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("chat_message")
public class ChatMessage {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 客服id: 0机器人/系统通知
     */
    private Integer staffId;

    /**
     * 发送人: 1用户, 2客服, 3机器人, 4系统消息
     */
    private Integer senderType;

    /**
     * 发送者名称
     */
    private String senderName;

    /**
     * 发送者头像
     */
    private String senderAvatar;

    /**
     * 发送信息类型: 1文本, 2图片
     */
    private Integer msgType;

    /**
     * 文本内容
     */
    private String content;

    /**
     * 图片地址
     */
    private String fileUrl;

    /**
     * 是否已读: 0未读, 1已读
     */
    private Integer isRead;

    /**
     * 已读时间
     */
    private LocalDateTime readTime;

    /**
     * 消息幂等id
     */
    private String clientMsgId;

    /**
     * 引用某条消息的id
     */
    private Long quoteMsgId;

    /**
     * 是否撤回: 0正常, 1撤回
     */
    private Integer isRecalled;

    /**
     * 消息发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
