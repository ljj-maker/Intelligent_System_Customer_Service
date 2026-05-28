package com.kefu.administrator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kefu.administrator.domain.po.Administrator;
import org.apache.ibatis.annotations.Select;

public interface AdminMapper extends BaseMapper<Administrator>{

    @Select("select * from administrator where id = #{id}")
    Administrator selectByIdIgnoreDeleted(Long id);
}
