package com.kefu.customerService.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kefu.customerService.domain.po.ServiceRelationship;
import com.kefu.customerService.domain.vo.RelationListResult;

public interface CustomerServiceRelationshipService extends IService<ServiceRelationship> {

    // 获取客服关系列表
    RelationListResult listByStaffId(Long staffId);

    // 保存客服关系
    void saveRelationship(ServiceRelationship serviceRelationship);
}
