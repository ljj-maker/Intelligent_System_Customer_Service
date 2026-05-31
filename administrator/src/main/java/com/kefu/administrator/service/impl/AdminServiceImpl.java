package com.kefu.administrator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kefu.administrator.domain.dto.AdminDTO;
import com.kefu.administrator.domain.dto.LoginFormDTO;
import com.kefu.administrator.domain.po.Administrator;
import com.kefu.administrator.domain.vo.AdminLoginVO;
import com.kefu.administrator.domain.vo.AdminVO;
import com.kefu.administrator.mapper.AdminMapper;
import com.kefu.administrator.service.AdminService;
import com.kefu.icsscommon.config.JwtProperties;
import com.kefu.icsscommon.domain.PageResult;
import com.kefu.icsscommon.exception.BadRequestException;
import com.kefu.icsscommon.exception.ForbiddenException;
import com.kefu.icsscommon.utils.BeanUtils;
import com.kefu.icsscommon.utils.JwtTool;
import com.kefu.icsscommon.utils.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Administrator> implements AdminService {

    private final AdminMapper adminMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTool jwtTool;
    private final JwtProperties jwtProperties;

    /*
    * 保存管理员账号
    * */
    @Override
    public void saveAdmin(AdminDTO adminDto) {
        Administrator administrator = BeanUtils.copyBean(adminDto, Administrator.class);
        // 将收到的密码加密
        administrator.setPassword(passwordEncoder.encode(adminDto.getPassword()));
        // 存储到数据库
        save(administrator);
    }

    /*
    * 管理员登录
    * */
    @Override
    public AdminLoginVO login(LoginFormDTO loginFormDTO) {
        // 1.获取前端传来的用户名和密码
        String username = loginFormDTO.getUsername();
        String password = loginFormDTO.getPassword();
        // 2.根据用户名查询
        Administrator administrator = lambdaQuery().eq(Administrator::getUsername, username).one();
        // 检查用户名是否存在
        Assert.notNull(administrator, "用户名错误");
        // 3.校验账号是否禁用
        if (administrator.getStatus() == 2) {
            throw new ForbiddenException("用户被冻结");
        }
        // 4.校验密码
        if (!passwordEncoder.matches(password, administrator.getPassword())) {
            throw new BadRequestException("用户名或密码错误");
        }
        // 5.生成TOKEN
        String token = jwtTool.createToken(administrator.getId(), jwtProperties.getTokenTTL());
        // 6.封装VO返回
        AdminLoginVO vo = new AdminLoginVO();
        vo.setAdminId(administrator.getId());
        vo.setUsername(administrator.getUsername());
        vo.setToken(token);
        log.info("管理员登录 -> id:{}", administrator.getId());
        return vo;
    }

    /*
    * 分页查询管理员列表
    * */
    @Override
    public PageResult<AdminVO> pageQuery(String name, Integer gender, Integer status, Integer page, Integer pageSize) {

        // 1.构建分页对象
        Page<Administrator> pageParam = new Page<>(page, pageSize);

        // 2.构建查询条件
        LambdaQueryWrapper<Administrator> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(name), Administrator::getName, name)    // 模糊查询，且非空
                .eq(gender != null, Administrator::getGender,gender)       // 匹配性别
                .eq(status != null, Administrator::getStatus, status)      // 匹配账号状态
                .orderByAsc(Administrator::getId);                                  // 根据id升序

        // 3.查询
        Page<Administrator> pageResult = this.page(pageParam, wrapper);

        // 4.转为VO
        List<AdminVO> rows = pageResult.getRecords().stream().map(item -> {
            return BeanUtils.copyBean(item, AdminVO.class);
        }).toList();

        // 5.封装返回
        return new PageResult<AdminVO>(pageResult.getTotal(), rows);
    }

    /*
    * 查询管理员详情
    * */
    @Override
    public AdminVO selectByIdIgnoreDeleted(Long id) {
        log.info("{}查询管理员{}", UserContext.getUser(), id);
        return BeanUtils.copyBean(adminMapper.selectByIdIgnoreDeleted(id), AdminVO.class);
    }
}
