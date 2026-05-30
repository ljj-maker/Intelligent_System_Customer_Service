package com.kefu.administrator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kefu.administrator.domain.dto.AdminDTO;
import com.kefu.administrator.domain.dto.LoginFormDTO;
import com.kefu.administrator.domain.po.Administrator;
import com.kefu.administrator.domain.vo.AdminLoginVO;
import com.kefu.administrator.domain.vo.AdminVO;
import com.kefu.icsscommon.domain.PageResult;

public interface AdminService extends IService<Administrator>{

    // 分页查询
    PageResult<AdminVO> pageQuery(String name, Integer gender, Integer status, Integer page, Integer pageSize);

    // 根据id查询
    AdminVO selectByIdIgnoreDeleted(Long id);

    // 登录
    AdminLoginVO login(LoginFormDTO loginFormDTO);

    // 新增
    void saveAdmin(AdminDTO adminDto);
}
