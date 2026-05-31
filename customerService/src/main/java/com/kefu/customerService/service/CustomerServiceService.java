package com.kefu.customerService.service;

import com.kefu.customerService.domain.dto.LoginFormDTO;
import com.kefu.customerService.domain.vo.StaffLoginVO;

public interface CustomerServiceService {

    // 登录
    StaffLoginVO login(LoginFormDTO loginFormDTO);
}
