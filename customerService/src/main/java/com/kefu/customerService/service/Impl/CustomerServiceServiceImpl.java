package com.kefu.customerService.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kefu.customerService.domain.po.CustomerService;
import com.kefu.customerService.mapper.CustomerServiceMapper;
import com.kefu.customerService.service.CustomerServiceService;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceServiceImpl extends ServiceImpl<CustomerServiceMapper, CustomerService> implements CustomerServiceService {
}
