package com.kefu.administrator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kefu.administrator.domain.po.User;
import com.kefu.administrator.domain.vo.PageResult;
import com.kefu.administrator.domain.vo.UserVO;

public interface UserService extends IService<User> {

    PageResult<UserVO> pageQuery(String username, String phone, Integer gender, Integer status, Integer page, Integer pageSize);
}
