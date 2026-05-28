package com.kefu.administrator.controller;

import com.kefu.administrator.domain.vo.PageResult;
import com.kefu.administrator.domain.vo.UserVO;
import com.kefu.administrator.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name="用户相关接口")
@RequestMapping("/admin/user")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "查询用户列表")
    @GetMapping
    public PageResult<UserVO> list(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) Integer gender,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return userService.pageQuery(username, phone, gender, status, page, pageSize);
    }
}
