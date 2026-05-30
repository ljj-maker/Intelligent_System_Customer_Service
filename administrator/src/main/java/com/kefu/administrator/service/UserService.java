package com.kefu.administrator.service;

import com.kefu.icsscommon.domain.PageResult;
import com.kefu.userdto.domain.vo.UserVO;

public interface UserService{

    PageResult<UserVO> pageQuery(String username, String phone, Integer gender, Integer status, Integer page, Integer pageSize);

    UserVO queryUser(Long id);
}
