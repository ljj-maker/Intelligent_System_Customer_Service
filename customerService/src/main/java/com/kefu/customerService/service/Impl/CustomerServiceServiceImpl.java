package com.kefu.customerService.service.Impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kefu.customerService.domain.dto.LoginFormDTO;
import com.kefu.customerService.domain.po.CustomerService;
import com.kefu.customerService.domain.vo.StaffLoginVO;
import com.kefu.customerService.mapper.CustomerServiceMapper;
import com.kefu.customerService.service.CsStatusService;
import com.kefu.customerService.service.CustomerServiceService;
import com.kefu.icsscommon.config.JwtProperties;
import com.kefu.icsscommon.exception.BadRequestException;
import com.kefu.icsscommon.exception.ForbiddenException;
import com.kefu.icsscommon.utils.JwtTool;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CustomerServiceServiceImpl extends ServiceImpl<CustomerServiceMapper, CustomerService> implements CustomerServiceService {

    private final PasswordEncoder passwordEncoder;
    private final JwtTool jwtTool;
    private final JwtProperties jwtProperties;
    private final CsStatusService csStatusService;
    private final CustomerServiceMapper customerServiceMapper;

    /*
    * 客服登录
    * */
    @Override
    public StaffLoginVO login(LoginFormDTO loginFormDTO) {
        // 1.获取前端传来的用户名和密码
        String username = loginFormDTO.getUsername();
        String password = loginFormDTO.getPassword();
        // 2.根据用户名查询
        CustomerService customerService = lambdaQuery().eq(CustomerService::getUsername, username).one();
        // 检测是否存在用户
        Assert.notNull(customerService, "用户名错误");
        // 3.校验是否禁用
        if (customerService.getStatus() == 2) {
            throw new ForbiddenException("用户被冻结");
        }
        // 4.校验密码
        if (!passwordEncoder.matches(password, customerService.getPassword())) {
            throw new BadRequestException("用户名或密码错误");
        }
        // 5.注册id到redis,以及创建在线详情
        csStatusService.login(customerService.getId(), customerService.getName());
        // 6.更新登录时间以及最后登录时间
        LambdaUpdateWrapper<CustomerService> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(CustomerService::getId, customerService.getId())
                .set(CustomerService::getUpdateTime, LocalDateTime.now())
                .set(CustomerService::getLastLoginTime, LocalDateTime.now());
        customerServiceMapper.update(null, wrapper);
        // 7.生成TOKEN
        String token = jwtTool.createToken(customerService.getId(), jwtProperties.getTokenTTL());
        // 8.封装VO返回
        StaffLoginVO vo = new StaffLoginVO();
        vo.setStaffId(customerService.getId());
        vo.setUsername(customerService.getUsername());
        vo.setToken(token);
        return vo;
    }
}
