package com.example.oldtown.modules.sys.mapper;

import com.example.oldtown.modules.sys.model.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 系统角色表 Mapper 接口
 * </p>
 *
 * @author dyp
 * @since 2020-10-27
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 根据系统用户查询系统角色
     */
    @Select("SELECT\n" +
            "\tr.* \n" +
            "FROM\n" +
            "\tsys_admin_role_relation AS ar\n" +
            "\tLEFT JOIN `sys_role` AS r ON ar.role_id = r.id \n" +
            "WHERE\n" +
            "\tar.admin_id = #{adminId}\n" +
            "\tAND r.deleted = 0 ;")
    List<SysRole> getRoleByAdminId(@Param("adminId") Long adminId);

    /**
     * 根据权限查询系统角色
     */
    @Select("SELECT\n" +
            "\tr.* \n" +
            "FROM\n" +
            "\tsys_role_permission_relation AS rp\n" +
            "\tLEFT JOIN `sys_role` AS r ON rp.role_id = r.id \n" +
            "WHERE\n" +
            "\trp.permission_id = #{permissionId}\n" +
            "\tAND r.deleted = 0 ;")
    List<SysRole> getRoleByPermissionId(@Param("permissionId") Long permissionId);

}
