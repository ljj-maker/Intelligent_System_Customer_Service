package com.kefu.administrator.client;

import com.kefu.icsscommon.domain.PageResult;
import com.kefu.userdto.domain.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("user")
public interface UserClient {

    // 查询用户详情
    @GetMapping("/user/user/{id}")
    UserVO queryUser(@PathVariable Long id);

    // 查询用户列表
    @Operation(summary = "查询用户列表")
    @GetMapping("/user/user")
    PageResult<UserVO> list(
        @RequestParam(required = false) String username,
        @RequestParam(required = false) String phone,
        @RequestParam(required = false) Integer gender,
        @RequestParam(required = false) Integer status,
        @RequestParam(defaultValue = "1") Integer page,
        @RequestParam(defaultValue = "10") Integer pageSize);
}
