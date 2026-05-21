package com.kefu.user.controller;

import com.kefu.icsscommon.utils.BeanUtils;
import com.kefu.user.domain.dto.UserDTO;
import com.kefu.user.domain.po.User;
import com.kefu.user.domain.vo.UserVO;
import com.kefu.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name="用户相关接口")
@RequestMapping("/user/user")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "新增用户")
    @PostMapping
    public void saveUser(@RequestBody UserDTO userDTO) {
        log.info("新增用户 -> ");
        userService.save(BeanUtils.copyBean(userDTO, User.class));
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        log.info("删除用户 -> ");
        userService.removeById(id);
    }

    @Operation(summary = "查询用户")
    @GetMapping("/{id}")
    public UserVO queryUser(@PathVariable Long id) {
        log.info("查询用户 -> ");
        return BeanUtils.copyBean(userService.getById(id), UserVO.class);
    }

    @Operation(summary = "更新用户")
    @PutMapping
    public void updateUser(@RequestBody UserDTO userDTO) {
        log.info("更新用户 -> ");
        userService.updateById(BeanUtils.copyBean(userDTO, User.class));
    }
}
