package com.example.oldtown.modules.sys.controller;


import cn.hutool.core.util.StrUtil;
import com.example.oldtown.common.api.CommonResult;
import com.example.oldtown.modules.sys.model.SysPermission;
import com.example.oldtown.modules.sys.model.SysRole;
import com.example.oldtown.modules.sys.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 系统角色表 前端控制器
 * </p>
 *
 * @author dyp
 * @since 2020-10-27
 */
@RestController
@RequestMapping("/sys/sysRole")
@Api(value = "SysRoleController",tags = "系统角色相关")
public class SysRoleController {

    private final Logger LOGGER = LoggerFactory.getLogger(SysRoleController.class);

    @Autowired
    SysRoleService sysRoleService;


    @ApiOperation("分页查询系统角色")
    @GetMapping("/getAllRole")
    @PreAuthorize("isAuthenticated()")
    public CommonResult getAllRole(@RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize,
                                   @RequestParam(value = "pageNum",required = false)Integer pageNum,
                                   @RequestParam(value = "keyword", required = false) String keyword) {
        return sysRoleService.getAllRole(pageSize,pageNum,keyword) ;
    }


    @ApiOperation("根据id查询系统角色")
    @GetMapping("/getRoleById")
    @PreAuthorize("isAuthenticated()")
    public CommonResult<SysRole> getRoleById(@RequestParam(value = "id") @NotNull Long id) {
        return CommonResult.success(sysRoleService.getRoleById(id));
    }

    @ApiOperation("根据权限查询系统角色")
    @GetMapping("/getRoleByPermissionId")
    @PreAuthorize("isAuthenticated()")
    public CommonResult getRoleByPermissionId(@RequestParam(value = "permissionId") Long permissionId) {
        return CommonResult.success(sysRoleService.getRoleByPermissionId(permissionId));
    }

    @ApiOperation("增加系统角色")
    @PostMapping("/addRole")
    @PreAuthorize("hasAnyAuthority('系统角色管理')")
    public CommonResult addRole(@Validated(SysRole.class) SysRole sysRole) {

        return sysRoleService.addRole(sysRole);
    }

    @ApiOperation("更新系统角色(包括禁用启用)")
    @PostMapping("/updateRole")
    @PreAuthorize("hasAnyAuthority('系统角色管理')")
    public CommonResult updateRole(@Validated(SysRole.class) SysRole sysRole) {

        return sysRoleService.updateRole(sysRole);
    }

    @ApiOperation("删除系统角色")
    @DeleteMapping("/deleteRole")
    @PreAuthorize("hasAnyAuthority('系统角色管理')")
    public CommonResult deleteRole(@RequestParam("id") @NotNull Long id) {

        return sysRoleService.deleteRole(id);
    }


    @ApiOperation("查询全部权限")
    @GetMapping("/getAllPermission")
    @PreAuthorize("isAuthenticated()")
    public CommonResult getAllPermission(@RequestParam(value = "category",required = false) String category) {

        return CommonResult.success(sysRoleService.getAllPermission(category));
    }

    @ApiOperation("查询某个角色被分配的权限")
    @GetMapping("/getPermissionByRoleId")
    @PreAuthorize("isAuthenticated()")
    public CommonResult getPermissionByRoleId(@RequestParam(value = "roleId") @NotNull Long roleId) {

        return CommonResult.success(sysRoleService.getPermissionByRoleId(roleId));
    }


    @ApiOperation("为某个角色分配权限")
    @PostMapping("/setPermissionForRole")
    @PreAuthorize("hasAnyAuthority('系统角色管理')")
    public CommonResult setPermissionForRole(@RequestParam(value = "roleId") @NotNull Long roleId,
                                             @RequestParam(value = "permissionIds") @NotEmpty List<Long> permissionIds) {

        return sysRoleService.setPermissionForRole(roleId,permissionIds);
    }


}

