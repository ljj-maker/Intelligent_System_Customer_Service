package com.kefu.customerService.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kefu.customerService.domain.po.ServiceRelationship;
import com.kefu.customerService.domain.vo.RelationListResult;

public interface CustomerServiceRelationshipService extends IService<ServiceRelationship> {

    RelationListResult listByStaffId(Long staffId);
}
