package com.kefu.administrator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kefu.administrator.domain.dto.CustomerServiceDTO;
import com.kefu.administrator.domain.po.CustomerService;
import com.kefu.administrator.domain.vo.CustomerServiceVO;
import com.kefu.icsscommon.domain.PageResult;

public interface CustomerServiceService extends IService<CustomerService> {

    // 查询客服列表
    PageResult<CustomerServiceVO> pageQuery(String name, Integer gender, Integer status, Integer level, Integer page, Integer pageSize);

    // 根据id查询客服
    CustomerServiceVO selectByIdIgnoreDeleted(Long id);

    // 新增客服
    void saveStaff(CustomerServiceDTO customerServiceDTO);
}
