package com.example.oldtown.modules.sys.controller;


import com.example.oldtown.modules.sys.model.SysPermission;
import com.example.oldtown.modules.sys.service.SysPermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * <p>
 * 系统权限表 前端控制器
 * </p>
 *
 * @author dyp
 * @since 2020-10-27
 */
@RestController
@RequestMapping("/sys/sysPermission")
public class SysPermissionController {

    private final Logger LOGGER = LoggerFactory.getLogger(SysPermissionController.class);

    @Autowired
    SysPermissionService sysPermissionService;


    // @PostMapping("/addPermission")
    // @PreAuthorize("hasAnyAuthority('系统配置权限')")
    // public String addPermission() {
    //     try {
    //         SysPermission sysPermission = new SysPermission();
    //         sysPermission.setName("测试权限1");
    //         sysPermission.setNameEn("sys_test_1");
    //         sysPermission.setCreateTime(new Date());
    //         sysPermission.setCategory("系统管理模块");
    //
    //         boolean a = sysPermissionService.save(sysPermission);
    //         return String.valueOf(a);
    //     } catch (Exception e) {
    //         LOGGER.error("", e);
    //         return "失败: " + e.getMessage();
    //     }
    // }

}

