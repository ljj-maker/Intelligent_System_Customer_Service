package com.kefu.user.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kefu.icsscommon.config.JwtProperties;
import com.kefu.icsscommon.domain.PageResult;
import com.kefu.icsscommon.exception.BadRequestException;
import com.kefu.icsscommon.exception.ForbiddenException;
import com.kefu.icsscommon.utils.BeanUtils;
import com.kefu.icsscommon.utils.JwtTool;
import com.kefu.user.domain.dto.LoginFormDTO;
import com.kefu.user.domain.po.User;
import com.kefu.user.domain.vo.UserLoginVO;
import com.kefu.user.mapper.UserMapper;
import com.kefu.user.service.UserService;
import com.kefu.userdto.domain.dto.UserDTO;
import com.kefu.userdto.domain.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final JwtTool jwtTool;
    private final JwtProperties jwtProperties;

    @Override
    public void saveUser(UserDTO userDTO) {
        User user = BeanUtils.copyBean(userDTO, User.class);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        save(user);
    }

    @Override
    public UserLoginVO login(LoginFormDTO loginFormDTO) {
        // 1.数据校验
        String username = loginFormDTO.getUsername();
        String password = loginFormDTO.getPassword();
        // 2.根据用户名查询
        User user = lambdaQuery().eq(User::getUsername, username).one();
        Assert.notNull(user, "用户名错误");
        // 3.校验是否禁用
        if (user.getStatus() == 2) {
            throw new ForbiddenException("用户被冻结");
        }
        // 4.校验密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadRequestException("用户名或密码错误");
        }
        // 5.生成TOKEN
        String token = jwtTool.createToken(user.getId(), jwtProperties.getTokenTTL());
        // 6.封装VO返回
        UserLoginVO vo = new UserLoginVO();
        vo.setUserId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setToken(token);
        return vo;
    }

    @Override
    public PageResult<UserVO> pageQuery(String username, String phone, Integer gender, Integer status, Integer page, Integer pageSize) {

        // 1.构建分页对象
        Page<User> pageParam = new Page<>(page, pageSize);

        // 2.构建查询条件
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(username), User::getUsername, username)
                .eq(StringUtils.isNotBlank(phone), User::getPhone, phone)
                .eq(gender != null, User::getGender, gender)
                .eq(status != null, User::getStatus, status)
                .orderByAsc(User::getId);

        // 3.查询
        Page<User> pageResult = this.page(pageParam, wrapper);

        // 4.转为VO
        List<UserVO> rows = pageResult.getRecords().stream().map(item -> {
            return BeanUtils.copyBean(item, UserVO.class);
        }).toList();

        // 5.封装返回
        System.out.println(pageResult.getTotal());
        return new PageResult<UserVO>(pageResult.getTotal(), rows);
    }
}
