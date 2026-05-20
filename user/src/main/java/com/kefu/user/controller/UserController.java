package com.kefu.user.controller;

import com.kefu.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name="用户相关接口")
@RequestMapping("/user/user")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
}
