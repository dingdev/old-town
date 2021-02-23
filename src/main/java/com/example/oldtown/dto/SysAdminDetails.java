package com.example.oldtown.dto;

import com.example.oldtown.modules.sys.model.SysAdmin;
import com.example.oldtown.modules.sys.model.SysPermission;
import com.example.oldtown.modules.sys.model.SysRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ding.yp
 * @name
 * @info
 * @date 2020/10/28
 */

public class SysAdminDetails implements UserDetails {

    private SysAdmin sysAdmin;
    private List<SysRole> sysRoleList;
    private List<SysPermission> sysPermissionList;

    public SysAdminDetails(SysAdmin sysAdmin,List<SysRole> sysRoleList, List<SysPermission> sysPermissionList) {
        this.sysAdmin = sysAdmin;
        this.sysRoleList = sysRoleList;
        this.sysPermissionList = sysPermissionList;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //返回当前用户的权限
        return sysPermissionList.stream()
                .map(permission ->new SimpleGrantedAuthority(permission.getName()))
                .collect(Collectors.toList());
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return null;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return null;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return (sysAdmin.getDeleted() == 0 );
    }

    public SysAdmin getSysAdmin() {
        return sysAdmin;
    }

    public void setSysAdmin(SysAdmin sysAdmin) {
        this.sysAdmin = sysAdmin;
    }

    public List<SysRole> getSysRoleList() {
        return sysRoleList;
    }

    public void setSysRoleList(List<SysRole> sysRoleList) {
        this.sysRoleList = sysRoleList;
    }

    public List<SysPermission> getSysPermissionList() {
        return sysPermissionList;
    }

    public void setSysPermissionList(List<SysPermission> sysPermissionList) {
        this.sysPermissionList = sysPermissionList;
    }
}
