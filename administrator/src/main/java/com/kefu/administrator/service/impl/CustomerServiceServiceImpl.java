package com.kefu.administrator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kefu.administrator.domain.po.CustomerService;
import com.kefu.administrator.mapper.CustomerServiceMapper;
import com.kefu.administrator.service.CustomerServiceService;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceServiceImpl extends ServiceImpl<CustomerServiceMapper, CustomerService> implements CustomerServiceService {
}
