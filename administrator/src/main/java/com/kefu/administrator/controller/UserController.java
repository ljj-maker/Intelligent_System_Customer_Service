package com.kefu.administrator.controller;


import com.kefu.administrator.service.UserService;
import com.kefu.icsscommon.domain.PageResult;
import com.kefu.icsscommon.utils.UserContext;
import com.kefu.userdto.domain.vo.UserVO;
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
        log.info("查询用户列表 -> ");
        System.out.println(UserContext.getUser());
        return userService.pageQuery(username, phone, gender, status, page, pageSize);
    }

    @Operation(summary = "根据id查询用户")
    @GetMapping("/{id}")
    public UserVO queryUser(@PathVariable Long id) {
        log.info("查询用户 -> ");
        return userService.queryUser(id);
    }
}
