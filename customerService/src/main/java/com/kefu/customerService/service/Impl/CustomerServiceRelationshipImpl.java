package com.kefu.customerService.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kefu.customerService.domain.po.ServiceRelationship;
import com.kefu.customerService.domain.vo.RelationListResult;
import com.kefu.customerService.domain.vo.ServiceRelationshipVO;
import com.kefu.customerService.mapper.CustomerServiceRelationshipMapper;
import com.kefu.customerService.service.CustomerServiceRelationshipService;
import com.kefu.icsscommon.utils.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceRelationshipImpl extends ServiceImpl<CustomerServiceRelationshipMapper, ServiceRelationship> implements CustomerServiceRelationshipService {

    @Override
    public RelationListResult listByStaffId(Long staffId) {
        // 1.构建条件根据id查询关系列表
        LambdaQueryWrapper<ServiceRelationship> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ServiceRelationship::getStaffId, staffId);

        // 2.查询全部关系列表
        List<ServiceRelationship> list = this.list(wrapper);

        // 3.转为VO
        List<ServiceRelationshipVO> rows = list.stream().map(item -> {
            return BeanUtils.copyBean(item, ServiceRelationshipVO.class);
        }).toList();

        // 4.封装返回
        return new RelationListResult((long)rows.size(), rows);
    }
}
