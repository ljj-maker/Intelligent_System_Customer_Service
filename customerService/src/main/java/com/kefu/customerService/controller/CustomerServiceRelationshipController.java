package com.kefu.customerService.controller;

import com.kefu.customerService.domain.dto.ServiceRelationshipDTO;
import com.kefu.customerService.domain.po.ServiceRelationship;
import com.kefu.customerService.domain.vo.RelationListResult;
import com.kefu.customerService.service.CustomerServiceRelationshipService;
import com.kefu.icsscommon.utils.BeanUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "客服服务关系相关接口")
@RestController
@RequestMapping("/service/relationship")
@RequiredArgsConstructor
public class CustomerServiceRelationshipController {

    private final CustomerServiceRelationshipService customerServiceRelationshipService;

    @Operation(summary = "保存客服服务关系")
    @PostMapping
    public void saveCustomerServiceRelationship(@RequestBody ServiceRelationshipDTO serviceRelationshipDto) {
        log.info("保存客服服务关系 -> ");
        // 新增
        customerServiceRelationshipService.save(BeanUtils.copyBean(serviceRelationshipDto, ServiceRelationship.class));
    }

    @Operation(summary = "查询客服服务关系")
    @GetMapping("/{staffId}")
    public RelationListResult queryCustomerServiceRelationship(@PathVariable Long staffId) {
        log.info("查询客服服务关系 -> ");
        // 查询
        return customerServiceRelationshipService.listByStaffId(staffId);
    }
}
