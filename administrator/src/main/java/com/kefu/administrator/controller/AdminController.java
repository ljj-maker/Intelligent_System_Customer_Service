package com.kefu.administrator.controller;

import com.kefu.administrator.domain.dto.AdminDTO;
import com.kefu.administrator.domain.po.Administrator;
import com.kefu.administrator.domain.vo.AdminVO;
import com.kefu.administrator.domain.vo.PageResult;
import com.kefu.administrator.service.AdminService;
import com.kefu.icsscommon.utils.BeanUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "管理员相关接口")
@RestController
@RequestMapping("/admin/administrator")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "新增管理员")
    @PostMapping
    public void saveAdmin(@RequestBody AdminDTO adminDto) {
        log.info("新增管理员列表 -> ");
        // 新增
        adminService.save(BeanUtils.copyBean(adminDto, Administrator.class));
    }

    @Operation(summary = "删除管理员")
    @DeleteMapping("/{id}")
    public void deleteAdmin(@PathVariable Long id) {
        log.info("软删除管理员 -> id={}", id);
        // 删除
        adminService.removeById(id);
    }

    @Operation(summary = "查询管理员")
    @GetMapping("/{id}")
    public AdminVO queryAdminById(@PathVariable Long id) {
        log.info("查询管理员 -> ");
        // 查询
        return adminService.selectByIdIgnoreDeleted(id);
    }

    @Operation(summary = "更新管理员")
    @PutMapping
    public void updateAdmin(@RequestBody AdminDTO adminDto) {
        log.info("更新管理员列表 -> ");
        // 更新
        adminService.updateById(BeanUtils.copyBean(adminDto, Administrator.class));
    }

    @Operation(summary = "分页查询管理员列表")
    @GetMapping
    public PageResult<AdminVO> list(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer gender,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return adminService.pageQuery(name, gender, status, page, pageSize);
    }

}
