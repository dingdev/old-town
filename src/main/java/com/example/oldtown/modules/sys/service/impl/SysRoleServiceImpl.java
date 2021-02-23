package com.example.oldtown.modules.sys.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oldtown.common.api.CommonResult;
import com.example.oldtown.component.GlobalData;
import com.example.oldtown.modules.sys.mapper.SysAdminRoleRelationMapper;
import com.example.oldtown.modules.sys.mapper.SysPermissionMapper;
import com.example.oldtown.modules.sys.mapper.SysRolePermissionRelationMapper;
import com.example.oldtown.modules.sys.model.*;
import com.example.oldtown.modules.sys.mapper.SysRoleMapper;
import com.example.oldtown.modules.sys.service.SysRolePermissionRelationService;
import com.example.oldtown.modules.sys.service.SysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 系统角色表 服务实现类
 * </p>
 *
 * @author dyp
 * @since 2020-10-27
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private final Logger LOGGER = LoggerFactory.getLogger(SysRoleServiceImpl.class);

    @Resource
    SysRoleMapper sysRoleMapper;
    @Resource
    SysAdminRoleRelationMapper sysAdminRoleRelationMapper;
    @Resource
    SysPermissionMapper sysPermissionMapper;
    @Resource
    SysRolePermissionRelationMapper sysRolePermissionRelationMapper;

    @Autowired
    SysRolePermissionRelationService sysRolePermissionRelationService;


    /**
     * 查询全部系统角色
     * @return
     */
    @Override
    public CommonResult getAllRole(Integer pageSize, Integer pageNum,String keyWord) {

        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(keyWord)) {
            wrapper.lambda().like(SysRole::getName, keyWord).or().like(SysRole::getNameEn, keyWord);
        }
        if (pageNum == null) {
            return CommonResult.success(sysRoleMapper.selectList(wrapper));
        } else {
            Page<SysRole> page = new Page<>(pageNum, pageSize);
            return CommonResult.success(page(page,wrapper));
        }
    }

    /**
     * 查询单个系统角色
     */
    @Override
    public SysRole getRoleById(Long id) {

        return sysRoleMapper.selectById(id);
    }

    /**
     * 根据权限查询系统角色
     */
    @Override
    public List<SysRole> getRoleByPermissionId(Long permissionId) {
        return sysRoleMapper.getRoleByPermissionId(permissionId) ;
    }

    /**
     * 增加系统角色
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult addRole(SysRole sysRole) {

        try {
            String name = sysRole.getName();
            if (StrUtil.isBlank(name) ) {
                return CommonResult.failed("请输入角色名(name)");
            }

            QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(SysRole::getName, name);
            if (sysRoleMapper.selectCount(wrapper) >0) {
                return CommonResult.failed("该角色名已存在");
            }

            sysRole.setId(null);
            sysRole.setCreateTime(new Date());
            sysRole.setAdminCount(0);
            sysRoleMapper.insert(sysRole);
            return CommonResult.success("成功增加一个系统角色:"+sysRole.getId());
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("增加系统角色失败:"+e.getMessage());
        }
    }

    /**
     * 更新系统角色
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult updateRole(SysRole sysRole) {

        try {

            Long id = sysRole.getId();
            if (id == null) {
                return CommonResult.failed("请输入角色id");
            }
            if (GlobalData.ROLE_SUPER_ID.equals(id)) {
                return CommonResult.failed("不可更新'超级管理员'角色");
            }

            sysRoleMapper.updateById(sysRole);
            return CommonResult.success("成功更新一个系统角色:"+sysRole.getId());
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("更新系统角色失败:"+e.getMessage());
        }
    }

    /**
     * 删除系统角色
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult deleteRole(Long id) {

        try {

            if (id == null) {
                return CommonResult.failed("请输入角色id");
            }

            if (GlobalData.ROLE_SUPER_ID.equals(id)) {
                return CommonResult.failed("不可删除'超级管理员'角色");
            }

            int deleteNum = sysRoleMapper.deleteById(id);
            if (deleteNum == 0) {
                return CommonResult.failed("删除系统角色失败,deleteNum为0");
            }
            QueryWrapper<SysAdminRoleRelation> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(SysAdminRoleRelation::getRoleId, id);
            sysAdminRoleRelationMapper.delete(wrapper);
            return CommonResult.success("成功删除一个系统角色");

        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除系统角色失败:"+e.getMessage());
        }
    }


    /**
     * 查询全部系统权限
     */
    @Override
    public List<SysPermission> getAllPermission(String category) {
        QueryWrapper<SysPermission> wrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(category)) {
            wrapper.lambda().eq(SysPermission::getCategory, category);
        }
        wrapper.lambda().eq(SysPermission::getDeleted,0).orderByAsc(SysPermission::getCategory, SysPermission::getId);

        return sysPermissionMapper.selectList(wrapper);
    }


    /**
     * 查询某个角色被分配的权限
     */
    @Override
    public List<SysPermission> getPermissionByRoleId(Long roleId) {

        return sysPermissionMapper.getPermissionByRoleId(roleId);
    }

    /**
     * 为某个角色分配权限
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult setPermissionForRole(Long roleId, List<Long> permissionIds) {
        try {

            if (roleId == null) {
                return CommonResult.failed("请输入角色id(roleId");
            }

            if (GlobalData.ROLE_SUPER_ID.equals(roleId)) {
                return CommonResult.failed("不可分配'超级管理员'角色具有的权限");
            }

            for (Long permissionId : permissionIds) {
                if (permissionId < GlobalData.PERMISSION_SUPER_ID) {
                    return CommonResult.failed("不可分配'超级管理员'角色具有的权限");
                }
            }

            //先删除原有关系
            QueryWrapper<SysRolePermissionRelation> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(SysRolePermissionRelation::getRoleId, roleId);
            sysRolePermissionRelationMapper.delete(wrapper);
            //批量插入新关系
            List<SysRolePermissionRelation> relationList = new ArrayList<>();
            for (Long permissionId : permissionIds) {
                SysRolePermissionRelation relation = new SysRolePermissionRelation();
                relation.setRoleId(roleId);
                relation.setPermissionId(permissionId);
                relationList.add(relation);
            }
            sysRolePermissionRelationService.saveBatch(relationList);
            return CommonResult.success("成功为角色分配权限:" + roleId);
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("为角色分配权限失败:"+e.getMessage());
        }


    }




}
