package com.kefu.administrator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kefu.administrator.domain.po.CustomerService;
import org.apache.ibatis.annotations.Select;

public interface CustomerServiceMapper extends BaseMapper<CustomerService> {

    @Select("select * from customer_service where id = #{id}")
    CustomerService selectByIdIgnoreDeleted(Long id);
}
