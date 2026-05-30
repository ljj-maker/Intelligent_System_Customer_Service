package com.kefu.customerService.controller;

import com.kefu.customerService.domain.dto.LoginFormDTO;
import com.kefu.customerService.domain.vo.StaffLoginVO;
import com.kefu.customerService.service.CustomerServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "客服")
@RestController
@RequestMapping("/service/service")
@RequiredArgsConstructor
public class CustomerServiceController {

    private final CustomerServiceService customerServiceService;

    @Operation(summary = "登录")
    @PostMapping("login")
    public StaffLoginVO login(@RequestBody @Validated LoginFormDTO loginFormDTO) {
        return customerServiceService.login(loginFormDTO);
    }
}
