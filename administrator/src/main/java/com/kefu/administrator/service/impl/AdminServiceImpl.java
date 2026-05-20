package com.kefu.administrator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kefu.administrator.domain.po.Administrator;
import com.kefu.administrator.mapper.AdminMapper;
import com.kefu.administrator.service.AdminService;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Administrator> implements AdminService {

}
