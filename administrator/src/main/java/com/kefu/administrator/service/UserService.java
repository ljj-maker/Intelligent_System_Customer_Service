package com.kefu.administrator.service;

import com.kefu.icsscommon.domain.PageResult;
import com.kefu.userdto.domain.vo.UserVO;

public interface UserService{

    // 分页查询用户列表
    PageResult<UserVO> pageQuery(String username, String phone, Integer gender, Integer status, Integer page, Integer pageSize);

    // 根据id查询用户
    UserVO queryUser(Long id);
}
