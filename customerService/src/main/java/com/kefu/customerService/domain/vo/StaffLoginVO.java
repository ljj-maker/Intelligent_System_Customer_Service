package com.kefu.customerService.domain.vo;

import lombok.Data;

@Data
public class StaffLoginVO {

    private String token;
    private Long staffId;
    private String username;
}
