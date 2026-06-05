package com.kefu.administrator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kefu.administrator.client.CSClient;
import com.kefu.administrator.domain.dto.CustomerServiceDTO;
import com.kefu.administrator.domain.po.CustomerService;
import com.kefu.administrator.domain.vo.CsInfo;
import com.kefu.administrator.domain.vo.CustomerServiceVO;
import com.kefu.administrator.mapper.CustomerServiceMapper;
import com.kefu.administrator.service.CustomerServiceService;
import com.kefu.icsscommon.domain.PageResult;
import com.kefu.icsscommon.utils.BeanUtils;
import com.kefu.icsscommon.utils.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomerServiceServiceImpl extends ServiceImpl<CustomerServiceMapper, CustomerService> implements CustomerServiceService {

    private final CustomerServiceMapper customerServiceMapper;
    private final PasswordEncoder passwordEncoder;
    private final CSClient csClient;

    /*
    * 保存客服信息
    * */
    @Override
    public void saveStaff(CustomerServiceDTO customerServiceDTO) {
        CustomerService customerService = BeanUtils.copyBean(customerServiceDTO, CustomerService.class);
        // 密码加密
        customerService.setPassword(passwordEncoder.encode(customerServiceDTO.getPassword()));
        // 设置创建人，创建时间，更新时间
        customerService.setCreateBy(UserContext.getUser());
        customerService.setCreateTime(LocalDateTime.now());
        customerService.setUpdateTime(LocalDateTime.now());
        // 保存
        System.out.println(customerService.getStaffNo());
        save(customerService);
    }

    /*
    * 分页查询客服信息
    * */
    @Override
    public PageResult<CustomerServiceVO> pageQuery(String name, Integer gender, Integer status, Integer level, Integer page, Integer pageSize) {

        // 1.构建分页对象
        Page<CustomerService> pageParam = new Page<>(page, pageSize);

        // 2.构建查询条件
        LambdaQueryWrapper<CustomerService> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(name), CustomerService::getName, name)  // 模糊查询，且非空
                .eq(gender != null, CustomerService::getGender, gender)    // 匹配性别
                .eq(status != null, CustomerService::getStatus, status)    // 匹配账号状态
                .eq(level != null, CustomerService::getLevel, level)       // 匹配职位等级
                .orderByAsc(CustomerService::getId);                                // 根据id升序

        // 3.查询
        Page<CustomerService> pageResult = this.page(pageParam, wrapper);

        // 4.转为VO
        List<CustomerServiceVO> rows = pageResult.getRecords().stream().map(item -> {
            return BeanUtils.copyBean(item, CustomerServiceVO.class);
        }).toList();

        // 5.封装返回
        return new PageResult<CustomerServiceVO>(pageResult.getTotal(), rows);
    }

    /*
    * 查询客服详情
    * */
    @Override
    public CustomerServiceVO selectByIdIgnoreDeleted(Long id) {
        return BeanUtils.copyBean(customerServiceMapper.selectByIdIgnoreDeleted(id), CustomerServiceVO.class);
    }

    /*
    * 获取所有在线的客服在线信息
    * */
    @Override
    public CsInfo getCsInfo(Long csId) {
        // 通过openfeign 调用 customerService 获取当前客服在线的在线信息
        Map<Object, Object> csInfo = csClient.getCsInfo(csId);
        // 如果是空的直接返回
        if (csInfo == null || csInfo.isEmpty()) {
            return null;
        }
        // 构建返回对象
        CsInfo info = new CsInfo();
        info.setId(Long.valueOf(String.valueOf(csInfo.get("id"))));
        info.setName(String.valueOf(csInfo.get("name")));
        info.setLoginTime(LocalDateTime.parse(String.valueOf(csInfo.get("loginTime"))));
        return info;
    }

    /*
    * 获取当前客服的在线列表
    * */
    @Override
    public List<Long> getCsList() {
        // 通过openfeign 调用 customerService 获取当前客服的在线列表
        Set<String> csList = csClient.getCsList();
        // 转为Long列表返回前端
        return csList.stream().map(Long::valueOf).toList();
    }

    /*
    * 删除客服
    * */
    @Override
    public void removeCSById(Long id) {
        // 构建逻辑删除
        LambdaUpdateWrapper<CustomerService> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(CustomerService::getId, id)                              // 根据id删除
                .set(CustomerService::getUpdateTime, LocalDateTime.now())   // 设置更新时间
                .set(CustomerService::getIsDeleted, 1);                     // 逻辑删除

        customerServiceMapper.update(null, wrapper);                  // 执行逻辑删除
    }

    /*
    * 更新客服信息
    * */
    @Override
    public void updateCSById(CustomerServiceDTO customerServiceDTO) {
        CustomerService customerService = BeanUtils.copyBean(customerServiceDTO, CustomerService.class);
        // 设置更新时间
        customerService.setUpdateTime(LocalDateTime.now());
        this.updateById(customerService);
    }
}
