package com.example.oldtown.modules.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oldtown.common.api.CommonResult;
import com.example.oldtown.modules.sys.model.SysAdmin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.oldtown.modules.sys.model.SysPermission;
import com.example.oldtown.modules.sys.model.SysRole;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 系统用户表 服务类
 * </p>
 *
 * @author dyp
 * @since 2020-10-27
 */
public interface SysAdminService extends IService<SysAdmin> {


    /**
     * 登录
     */
    CommonResult login(String username, String password);

    /**
     * 根据用户名查询用户
     */
    SysAdmin getAdminByUserName(String username);

    /**
     * 获取用户的角色
     */
    List<SysRole> getRoleByAdminId(Long adminId);

    /**
     * 获取用户的权限
     */
    List<SysPermission> getPermissionByAdminId(Long adminId);

    /**
     * 获取后台用户详细信息(spring security 所用)
     */
    UserDetails loadUserByUsername(String username);

    /**
     * 获取后台用户详细信息
     */
    UserDetails getAdminById(Long id);

    /**
     * 查询全部系统用户
     */
    CommonResult getAllAdmin(Integer pageSize, Integer pageNum,String keyWord);

    /**
     * 根据角色名查询系统用户
     */
    List<SysAdmin> getAdminByRoleName(String roleName);

    /**
     * 根据角色Id查询系统用户
     */
    List<SysAdmin> getAdminByRoleId(Long roleId);

    /**
     * 根据权限Id查询系统用户
     */
    List<SysAdmin> getAdminByPermissionId(Long permissionId);

    /**
     * 增加系统用户
     */
    CommonResult addAdmin(SysAdmin sysAdmin);
    /**
     * 更新系统用户
     */
    CommonResult updateAdmin(SysAdmin sysAdmin);
    /**
     * 删除系统用户
     */
    CommonResult deleteAdmin(Long id);

    /**
     * 为系用户分配角色
     */
    CommonResult setRoleForAdmin(Long adminId, List<Long> roleIds);
}
