package com.example.oldtown.modules.sys.controller;


import cn.hutool.core.util.StrUtil;
import com.example.oldtown.common.api.CommonResult;
import com.example.oldtown.modules.sys.model.SysAdmin;
import com.example.oldtown.modules.sys.service.SysAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 系统用户表 前端控制器
 * </p>
 *
 * @author dyp
 * @since 2020-10-27
 */
@RestController
@RequestMapping("/sys/sysAdmin")
@Api(value = "SysAdminController", tags = "系统用户相关")
public class SysAdminController {
    private final Logger LOGGER = LoggerFactory.getLogger(SysAdminController.class);

    @Autowired
    private SysAdminService sysAdminService;

    @ApiOperation("登录")
    @PostMapping("/login")
    public CommonResult login(@RequestParam("username") @NotBlank String username,
                              @RequestParam("password") @NotBlank String password) {
        return sysAdminService.login(username, password);
    }

    @ApiOperation("分页查询系统用户")
    @GetMapping("/getAllAdmin")
    @PreAuthorize("isAuthenticated()")
    public CommonResult getAllAdmin(@RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize,
                                         @RequestParam(value = "pageNum",required = false)Integer pageNum,
                                         @RequestParam(value = "keyword", required = false) String keyword) {
        return sysAdminService.getAllAdmin(pageSize, pageNum,keyword) ;
    }

    @ApiOperation("根据名称查询系统用户")
    @GetMapping("/getAdminByName")
    @PreAuthorize("hasAnyAuthority('系统用户管理')")
    public CommonResult<SysAdmin> getAdminByName(@RequestParam(value = "name")@NotBlank String name) {
        return CommonResult.success(sysAdminService.getAdminByUserName(name));
    }

    @ApiOperation("查询本人详细信息")
    @GetMapping("/getSelfDetails")
    @PreAuthorize("isAuthenticated()")
    public CommonResult getSelfDetails(@ApiIgnore Authentication authentication) {
        if (authentication == null) {
            return CommonResult.unauthorized(null);
        }

        String username = authentication.getName();
        username = username.substring(username.indexOf(",") + 1,username.lastIndexOf(","));
        return CommonResult.success(sysAdminService.loadUserByUsername(username)) ;
    }

    @ApiOperation("根据id查询系统用户详细信息")
    @GetMapping("/getAdminById")
    @PreAuthorize("hasAnyAuthority('系统用户管理')")
    public CommonResult getAdminById(@RequestParam(value = "id")@NotNull Long id) {
        return CommonResult.success(sysAdminService.getAdminById(id),"sysRoleList,sysPermissionList,sysAdmin");
    }

    @ApiOperation("根据角色Id查询系统用户")
    @GetMapping("/getAdminByRoleId")
    @PreAuthorize("isAuthenticated()")
    public CommonResult getAdminByRoleId(@RequestParam("roleId")@NotNull Long roleId) {
        return CommonResult.success(sysAdminService.getAdminByRoleId(roleId)) ;
    }

    @ApiOperation("根据权限Id查询系统用户")
    @GetMapping("/getAdminByPermissionId")
    @PreAuthorize("isAuthenticated()")
    public CommonResult getAdminByPermissionId(@RequestParam("permissionId")@NotNull Long permissionId) {
        return CommonResult.success(sysAdminService.getAdminByPermissionId(permissionId)) ;
    }

    @ApiOperation("增加系统用户")
    @PostMapping("/addAdmin")
    @PreAuthorize("hasAnyAuthority('系统用户管理')")
    public CommonResult addAdmin(@Validated(SysAdmin.class) SysAdmin sysAdmin) {
        if (StrUtil.isBlank(sysAdmin.getUsername()) || StrUtil.isBlank(sysAdmin.getPassword()) ) {
            return CommonResult.failed("请输入用户名(username)和密码(password)");
        }

        return sysAdminService.addAdmin(sysAdmin);
    }

    @ApiOperation("更新系统用户(包括禁用启用)")
    @PostMapping("/updateAdmin")
    @PreAuthorize("hasAnyAuthority('系统用户管理')")
    public CommonResult updateAdmin(@Validated(SysAdmin.class) SysAdmin sysAdmin) {
        if (sysAdmin.getId() == null) {
            return CommonResult.failed("请输入用户id");
        }
        return sysAdminService.updateAdmin(sysAdmin) ;
    }

    @ApiOperation("删除系统用户")
    @DeleteMapping("/deleteAdmin")
    @PreAuthorize("hasAnyAuthority('系统用户管理')")
    public CommonResult deleteAdmin(@RequestParam("Id") @NotNull Long id) {

        return sysAdminService.deleteAdmin(id);
    }

    @ApiOperation("为系统用户分配角色")
    @PostMapping("/setRoleForAdmin")
    @PreAuthorize("hasAnyAuthority('系统用户管理')")
    public CommonResult setRoleForAdmin(@RequestParam(value = "adminId") @NotNull Long adminId,
                                        @RequestParam(value = "roleIds") @NotEmpty List<Long> roleIds) {

        return sysAdminService.setRoleForAdmin(adminId,roleIds);
    }



}

