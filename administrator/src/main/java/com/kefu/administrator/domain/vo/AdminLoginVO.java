package com.kefu.administrator.domain.vo;

import lombok.Data;

@Data
public class AdminLoginVO {

    private String token;
    private Long adminId;
    private String username;
}
