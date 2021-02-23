package com.example.oldtown.modules.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oldtown.common.api.CommonResult;
import com.example.oldtown.modules.sys.model.SysPermission;
import com.example.oldtown.modules.sys.model.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 系统角色表 服务类
 * </p>
 *
 * @author dyp
 * @since 2020-10-27
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 查询全部系统角色
     */
    CommonResult getAllRole(Integer pageSize, Integer pageNum, String keyWord);

    /**
     * 查询单个系统角色
     */
    SysRole getRoleById(Long id);

    /**
     * 根据权限查询系统角色
     */
    List<SysRole> getRoleByPermissionId(Long permissionId);

    /**
     * 增加系统角色
     */
    CommonResult addRole(SysRole sysRole);

    /**
     * 修改系统角色
     */
    CommonResult updateRole(SysRole sysRole);

    /**
     * 删除系统角色
     */
    CommonResult deleteRole(Long id);

    /**
     * 查询全部权限
     */
    List<SysPermission> getAllPermission(String category);


    /**
     * 查询某个角色被分配的权限
     */
    List<SysPermission> getPermissionByRoleId(Long roleId);

    /**
     * 为某个角色分配权限
     */
    CommonResult setPermissionForRole(Long roleId, List<Long> permissionIds);

}
