package com.kefu.administrator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kefu.administrator.domain.po.Administrator;
import com.kefu.administrator.domain.vo.AdminVO;
import com.kefu.administrator.domain.vo.PageResult;
import com.kefu.administrator.mapper.AdminMapper;
import com.kefu.administrator.service.AdminService;
import com.kefu.icsscommon.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Administrator> implements AdminService {

    private final AdminMapper adminMapper;

    @Override
    public PageResult<AdminVO> pageQuery(String name, Integer gender, Integer status, Integer page, Integer pageSize) {

        // 1.构建分页对象
        Page<Administrator> pageParam = new Page<>(page, pageSize);

        // 2.构建查询条件
        LambdaQueryWrapper<Administrator> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(name), Administrator::getName, name)
                .eq(gender != null, Administrator::getGender,gender)
                .eq(status != null, Administrator::getStatus, status)
                .orderByAsc(Administrator::getId);

        // 3.查询
        Page<Administrator> pageResult = this.page(pageParam, wrapper);

        // 4.转为VO
        List<AdminVO> rows = pageResult.getRecords().stream().map(item -> {
            return BeanUtils.copyBean(item, AdminVO.class);
        }).toList();

        // 5.封装返回
        return new PageResult<AdminVO>(pageResult.getTotal(), rows);
    }

    @Override
    public AdminVO selectByIdIgnoreDeleted(Long id) {
        return BeanUtils.copyBean(adminMapper.selectByIdIgnoreDeleted(id), AdminVO.class);
    }
}
