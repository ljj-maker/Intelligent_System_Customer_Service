package com.kefu.administrator.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "客服在线信息")
public class CsInfo {
    private Long id;
    private String name;
    private LocalDateTime loginTime;
}