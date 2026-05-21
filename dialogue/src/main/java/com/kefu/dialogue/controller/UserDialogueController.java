package com.kefu.dialogue.controller;

import com.kefu.dialogue.service.UserDialogueService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "用户对话相关接口")
@RestController
@RequestMapping("/dialogue/user")
@RequiredArgsConstructor
public class UserDialogueController {

    private final UserDialogueService userDialogueService;
}
