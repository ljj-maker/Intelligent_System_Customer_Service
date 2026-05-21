package com.kefu.administrator.controller;

import com.kefu.administrator.domain.dto.CustomerServiceDTO;
import com.kefu.administrator.domain.po.CustomerService;
import com.kefu.administrator.domain.vo.CustomerServiceVO;
import com.kefu.administrator.service.CustomerServiceService;
import com.kefu.icsscommon.utils.BeanUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name="客服相关接口")
@RestController
@RequestMapping("/admin/staff")
@RequiredArgsConstructor
public class CustomerServiceController {

    private final CustomerServiceService customerServiceService;

    @Operation(summary = "新增客服")
    @PostMapping
    public void saveService(@RequestBody CustomerServiceDTO customerServiceDTO) {
        log.info("新增客服 -> ");
        customerServiceService.save(BeanUtils.copyBean(customerServiceDTO, CustomerService.class));
    }

    @Operation(summary = "删除客服")
    @DeleteMapping("/{id}")
    public void deleteService(@PathVariable Long id) {
        log.info("删除客服 -> ");
        customerServiceService.removeById(id);
    }

    @Operation(summary = "查询客服")
    @GetMapping("/{id}")
    public CustomerServiceVO queryService(@PathVariable Long id) {
        log.info("查询客服 -> ");
        return BeanUtils.copyBean(customerServiceService.getById(id), CustomerServiceVO.class);
    }

    @Operation(summary = "更新客服")
    @PutMapping
    public void updateService(@RequestBody CustomerServiceDTO customerServiceDTO) {
        log.info("更新客服 -> ");
        customerServiceService.updateById(BeanUtils.copyBean(customerServiceDTO, CustomerService.class));
    }
}
