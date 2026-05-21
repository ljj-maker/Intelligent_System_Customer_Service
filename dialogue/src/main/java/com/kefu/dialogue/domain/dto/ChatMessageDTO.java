package com.kefu.dialogue.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "对话信息接收实体")
public class ChatMessageDTO {
    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "客服id")
    private Integer staffId;

    @Schema(description = "发送者名称")
    private String username;

    @Schema(description = "发送者头像")
    private String avatar;

    @Schema(description = "消息类型，1文本，2图片，默认1")
    private Integer msgType;

    @Schema(description = "文本内容，msg_type=1时必填")
    private String content;

    @Schema(description = "图片地址，msg_type=2时必填")
    private String fileUrl;

    @Schema(description = "客户端消息幂等id，防止重复提交")
    private String clientMsgId;

    @Schema(description = "引用某条消息的id")
    private Long quoteMsgId;

    @Schema(description = "消息发送时间")
    private LocalDateTime sendTime;
}
