package com.kefu.dialogue.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "对话信息返回实体")
public class ChatMessageVO {
    @Schema(description = "消息id")
    private Long id;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "客服id，0机器人/系统通知")
    private Integer staffId;

    @Schema(description = "发送人，1用户，2客服，3机器人，4系统消息")
    private Integer senderType;

    @Schema(description = "发送者名称")
    private String senderName;

    @Schema(description = "发送者头像")
    private String senderAvatar;

    @Schema(description = "消息类型，1文本，2图片")
    private Integer msgType;

    @Schema(description = "文本内容")
    private String content;

    @Schema(description = "图片地址")
    private String fileUrl;

    @Schema(description = "是否已读，0未读，1已读")
    private Integer isRead;

    @Schema(description = "已读时间")
    private LocalDateTime readTime;

    @Schema(description = "消息幂等id")
    private String clientMsgId;

    @Schema(description = "引用消息id")
    private Long quoteMsgId;

    @Schema(description = "消息发送时间")
    private LocalDateTime sendTime;
}
