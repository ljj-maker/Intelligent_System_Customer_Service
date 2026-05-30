package com.kefu.administrator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kefu.administrator.domain.dto.CustomerServiceDTO;
import com.kefu.administrator.domain.po.CustomerService;
import com.kefu.administrator.domain.vo.CustomerServiceVO;
import com.kefu.administrator.mapper.CustomerServiceMapper;
import com.kefu.administrator.service.CustomerServiceService;
import com.kefu.icsscommon.domain.PageResult;
import com.kefu.icsscommon.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceServiceImpl extends ServiceImpl<CustomerServiceMapper, CustomerService> implements CustomerServiceService {

    private final CustomerServiceMapper customerServiceMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void saveStaff(CustomerServiceDTO customerServiceDTO) {
        CustomerService customerService = BeanUtils.copyBean(customerServiceDTO, CustomerService.class);
        customerService.setPassword(passwordEncoder.encode(customerServiceDTO.getPassword()));
        save(customerService);
    }

    @Override
    public PageResult<CustomerServiceVO> pageQuery(String name, Integer gender, Integer status, Integer level, Integer page, Integer pageSize) {

        // 1.构建分页对象
        Page<CustomerService> pageParam = new Page<>(page, pageSize);

        // 2.构建查询条件
        LambdaQueryWrapper<CustomerService> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(name), CustomerService::getName, name)
                .eq(gender != null, CustomerService::getGender, gender)
                .eq(status != null, CustomerService::getStatus, status)
                .eq(level != null, CustomerService::getLevel, level)
                .orderByAsc(CustomerService::getId);

        // 3.查询
        Page<CustomerService> pageResult = this.page(pageParam, wrapper);

        // 4.转为VO
        List<CustomerServiceVO> rows = pageResult.getRecords().stream().map(item -> {
            return BeanUtils.copyBean(item, CustomerServiceVO.class);
        }).toList();

        // 5.封装返回
        return new PageResult<CustomerServiceVO>(pageResult.getTotal(), rows);
    }

    @Override
    public CustomerServiceVO selectByIdIgnoreDeleted(Long id) {
        return BeanUtils.copyBean(customerServiceMapper.selectByIdIgnoreDeleted(id), CustomerServiceVO.class);
    }
}
