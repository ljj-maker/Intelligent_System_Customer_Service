package com.kefu.customerService.controller;

import com.kefu.customerService.domain.dto.LoginFormDTO;
import com.kefu.customerService.domain.vo.ChatMessageVO;
import com.kefu.customerService.domain.vo.StaffLoginVO;
import com.kefu.customerService.service.CsStatusService;
import com.kefu.customerService.service.CustomerServiceService;
import com.kefu.customerService.webSocket.CsWebSocketServer;
import com.kefu.icsscommon.utils.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@Slf4j
@Tag(name = "客服")
@RestController
@RequestMapping("/service/service")
@RequiredArgsConstructor
public class CustomerServiceController {

    private final CustomerServiceService customerServiceService;
    private final CsStatusService csStatusService;
    private final CsWebSocketServer csWebSocketServer;

    @Operation(summary = "发送消息给客服")
    @PostMapping("sendMessage")
    public void sendMessage(@RequestBody ChatMessageVO chatMessageVO) {
        log.info("用户 {} 给客服{}发送消息 -> ", chatMessageVO.getUserId(), UserContext.getUser());
        csWebSocketServer.sendMessage(chatMessageVO, chatMessageVO.getStaffId().toString());
    }

    @Operation(summary = "登录")
    @PostMapping("login")
    public StaffLoginVO login(@RequestBody @Validated LoginFormDTO loginFormDTO) {
        log.info("客服{}登录 -> ", loginFormDTO.getUsername());
        return customerServiceService.login(loginFormDTO);
    }

    @Operation(summary = "客服心跳接口")
    @PostMapping("heartbeat")
    public void heartbeat(@RequestParam Long csId) {
        csStatusService.heartbeat(csId);
    }

    @Operation(summary = "客服登出接口")
    @PostMapping("logout")
    public void logout(@RequestParam Long csId) {
        log.info("客服{}登出 -> ", csId);
        csStatusService.logout(csId);
    }

    @Operation(summary = "获取所有在线客服")
    @GetMapping("online")
    public Set<String> getOnlineCsIds() {
        return csStatusService.getOnlineCsIds();
    }

    @Operation(summary = "获取客服在线详情")
    @GetMapping("info")
    public Map<Object, Object> getCsInfo(@RequestParam Long csId) {
        return csStatusService.getCsInfo(csId);
    }

    @Operation(summary = "获取客服在线列表")
    @GetMapping("onlineList")
    public Set<String> getCsList() {
        return csStatusService.getOnlineCsIds();
    }
}
