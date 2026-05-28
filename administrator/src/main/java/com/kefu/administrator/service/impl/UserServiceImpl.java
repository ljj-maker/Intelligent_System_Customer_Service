package com.kefu.administrator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kefu.administrator.domain.po.User;
import com.kefu.administrator.domain.vo.PageResult;
import com.kefu.administrator.domain.vo.UserVO;
import com.kefu.administrator.mapper.UserMapper;
import com.kefu.administrator.service.UserService;
import com.kefu.icsscommon.utils.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

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
