package com.example.oldtown.modules.sys.mapper;

import com.example.oldtown.modules.sys.model.SysPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 系统权限表 Mapper 接口
 * </p>
 *
 * @author dyp
 * @since 2020-10-27
 */
public interface SysPermissionMapper extends BaseMapper<SysPermission> {


    @Select("SELECT\n" +
            "\tp.* \n" +
            "FROM\n" +
            "\tsys_admin_role_relation AS ar\n" +
            "\tLEFT JOIN `sys_role` AS r ON ar.role_id = r.id\n" +
            "\tLEFT JOIN `sys_role_permission_relation` AS rp ON r.id = rp.role_id\n" +
            "\tLEFT JOIN `sys_permission` AS p ON rp.permission_id = p.id \n" +
            "WHERE\n" +
            "\tar.admin_id = #{adminId} \n" +
            "\tAND r.deleted = 0 \n" +
            "\tAND p.deleted = 0;")
    List<SysPermission> getPermissionByAdminId(@Param("adminId") Long adminId);

    @Select("SELECT\n" +
            "\tp.* \n" +
            "FROM\n" +
            "\t`sys_role_permission_relation` AS rp\n" +
            "\tLEFT JOIN `sys_permission` AS p ON rp.permission_id = p.id \n" +
            "WHERE\n" +
            "\trp.role_id = #{roleId} \n" +
            "\tAND p.deleted = 0;")
    List<SysPermission> getPermissionByRoleId(@Param("roleId") Long roleId);
}
