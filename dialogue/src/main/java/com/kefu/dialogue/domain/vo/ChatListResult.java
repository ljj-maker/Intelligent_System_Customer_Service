package com.kefu.dialogue.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "对话列表返回实体")
public class ChatListResult {

    @Schema(description = "总条数")
    private Long total;

    @Schema(description = "列表")
    private List<ChatMessageVO> rows;
}
