package com.kefu.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kefu.icsscommon.domain.PageResult;
import com.kefu.user.domain.dto.LoginFormDTO;
import com.kefu.user.domain.po.User;
import com.kefu.user.domain.vo.UserLoginVO;
import com.kefu.userdto.domain.dto.UserDTO;
import com.kefu.userdto.domain.vo.UserVO;


public interface UserService extends IService<User> {

    PageResult<UserVO> pageQuery(String username, String phone, Integer gender, Integer status, Integer page, Integer pageSize);

    UserLoginVO login(LoginFormDTO loginFormDTO);

    void saveUser(UserDTO userDTO);
}
