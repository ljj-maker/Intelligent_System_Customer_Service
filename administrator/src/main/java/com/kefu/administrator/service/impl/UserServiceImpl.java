package com.kefu.administrator.service.impl;

import com.kefu.administrator.client.UserClient;
import com.kefu.administrator.service.UserService;
import com.kefu.icsscommon.domain.PageResult;
import com.kefu.userdto.domain.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserClient userClient;

    @Override
    public PageResult<UserVO> pageQuery(String username, String phone, Integer gender, Integer status, Integer page, Integer pageSize) {
        return userClient.list(username, phone, gender, status, page, pageSize);
    }


    @Override
    public UserVO queryUser(Long id) {
        return userClient.queryUser(id);
    }
}
