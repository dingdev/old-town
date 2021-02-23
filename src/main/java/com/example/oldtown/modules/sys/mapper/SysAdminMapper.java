package com.example.oldtown.modules.sys.mapper;

import com.example.oldtown.modules.sys.model.SysAdmin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 系统用户 Mapper 接口
 * </p>
 *
 * @author dyp
 * @since 2020-10-27
 */
public interface SysAdminMapper extends BaseMapper<SysAdmin> {

    @Select("SELECT\n" +
            "\ta.* \n" +
            "FROM\n" +
            "\t`sys_admin` AS a\n" +
            "\tLEFT JOIN sys_admin_role_relation AS ar ON ar.admin_id = a.id\n" +
            "\tLEFT JOIN sys_role AS r ON r.id = ar.role_id \n" +
            "WHERE\n" +
            "\tr.`name` = #{roleName}")
    List<SysAdmin> getAdminByRoleName(@Param("roleName") String roleName);

    @Select("SELECT\n" +
            "\ta.* \n" +
            "FROM\n" +
            "\t`sys_admin` AS a\n" +
            "\tLEFT JOIN sys_admin_role_relation AS ar ON ar.admin_id = a.id\n" +
            "WHERE\n" +
            "\tar.role_id = #{roleId}")
    List<SysAdmin> getAdminByRoleId(@Param("roleId") Long roleId);

    @Select("SELECT\n" +
            "\ta.* \n" +
            "FROM\n" +
            "\t`sys_admin` AS a\n" +
            "\tLEFT JOIN sys_admin_role_relation AS ar ON ar.admin_id = a.id\n" +
            "\tLEFT JOIN sys_role_permission_relation AS rp ON rp.role_id = ar.role_id \n" +
            "WHERE\n" +
            "\trp.permission_id = #{permissionId}")
    List<SysAdmin> getAdminByPermissionId(@Param("permissionId") Long permissionId);

    @Select("SELECT\n" +
            "\ta.* \n" +
            "FROM\n" +
            "\t`sys_admin` AS a\n" +
            "\tLEFT JOIN sys_admin_role_relation AS ar ON ar.admin_id = a.id\n" +
            "WHERE\n" +
            "\tar.role_id = #{roleId}\n" +
            "AND a.id = #{adminId} ;")
    SysAdmin hasThisRole(Long adminId,Long roleId);

}
