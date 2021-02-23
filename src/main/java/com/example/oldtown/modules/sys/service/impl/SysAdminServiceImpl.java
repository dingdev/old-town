package com.example.oldtown.modules.sys.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oldtown.common.api.CommonResult;
import com.example.oldtown.component.GlobalData;
import com.example.oldtown.component.JwtTokenUtil;
import com.example.oldtown.dto.SysAdminDetails;
import com.example.oldtown.modules.sys.mapper.SysAdminRoleRelationMapper;
import com.example.oldtown.modules.sys.mapper.SysPermissionMapper;
import com.example.oldtown.modules.sys.mapper.SysRoleMapper;
import com.example.oldtown.modules.sys.model.*;
import com.example.oldtown.modules.sys.mapper.SysAdminMapper;
import com.example.oldtown.modules.sys.service.SysAdminRoleRelationService;
import com.example.oldtown.modules.sys.service.SysAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统用户表 服务实现类
 * </p>
 *
 * @author dyp
 * @since 2020-10-27
 */
@Service
public class SysAdminServiceImpl extends ServiceImpl<SysAdminMapper, SysAdmin> implements SysAdminService {

    private final Logger LOGGER = LoggerFactory.getLogger(SysAdminServiceImpl.class);


    @Resource
    SysAdminMapper sysAdminMapper;
    @Resource
    SysRoleMapper sysRoleMapper;
    @Resource
    SysPermissionMapper sysPermissionMapper;
    @Resource
    SysAdminRoleRelationMapper sysAdminRoleRelationMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    SysAdminRoleRelationService sysAdminRoleRelationService;



    /**
     * 根据用户名查询后台用户
     * @param username
     * @return
     */
    @Override
    public SysAdmin getAdminByUserName(String username) {
        QueryWrapper<SysAdmin> wrapper = new QueryWrapper();

        wrapper.lambda().eq(SysAdmin::getUsername, username);
        List<SysAdmin> sysAdminList = sysAdminMapper.selectList(wrapper);
        if (sysAdminList != null && sysAdminList.size() > 0) {
            return sysAdminList.get(0);
        }
        return null;
    }

    /**
     * 获取用户的角色
     */
    @Override
    public List<SysRole> getRoleByAdminId(Long adminId) {

        return sysRoleMapper.getRoleByAdminId(adminId);
    }

    /**
     * 获取用户的权限
     */
    @Override
    public List<SysPermission> getPermissionByAdminId(Long adminId) {
        return sysPermissionMapper.getPermissionByAdminId(adminId);
    }

    /**
     * 获取后台用户详细信息(spring security 所用)
     */
    @Override
    public UserDetails loadUserByUsername(String username){
        //获取用户信息
        SysAdmin sysAdmin = getAdminByUserName(username);
        if (sysAdmin != null) {
            List<SysRole> sysRoleList = sysRoleMapper.getRoleByAdminId(sysAdmin.getId());
            List<SysPermission> sysPermissionList = sysPermissionMapper.getPermissionByAdminId(sysAdmin.getId());
            return new SysAdminDetails(sysAdmin, sysRoleList, sysPermissionList);
        }
        throw new UsernameNotFoundException("用户名或密码错误");
    }

    /**
     * 获取后台用户详细信息
     */
    @Override
    public UserDetails getAdminById(Long id){
        //获取用户信息
        SysAdmin sysAdmin = sysAdminMapper.selectById(id);
        if (sysAdmin != null) {
            List<SysRole> sysRoleList = sysRoleMapper.getRoleByAdminId(id);
            List<SysPermission> sysPermissionList = sysPermissionMapper.getPermissionByAdminId(id);
            return new SysAdminDetails(sysAdmin, sysRoleList, sysPermissionList);
        }
        return null;
    }

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    @Override
    public CommonResult login(String username, String password) {

        try {
            SysAdmin sysAdmin = getAdminByUserName(username);
            if (sysAdmin == null) {
                throw new BadCredentialsException("账号不存在");
            }

            if (!passwordEncoder.matches(password, sysAdmin.getPassword())) {
                throw new BadCredentialsException("密码不正确");
            }

            List<SysRole> sysRoleList = getRoleByAdminId(sysAdmin.getId());
            List<SysPermission> sysPermissionList = getPermissionByAdminId(sysAdmin.getId());

            UserDetails userDetails = new SysAdminDetails(sysAdmin, sysRoleList, sysPermissionList);


            if (!userDetails.isEnabled()) {
                throw new BadCredentialsException("账号已被禁用");
            }

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String permissionString = sysPermissionList.stream().map(SysPermission::getName)
                    .collect(Collectors.toList()).toString();
            String token = jwtTokenUtil.generateToken( GlobalData.SYS_ADMIN + "," + username +","+ sysAdmin.getId() , permissionString);

            return CommonResult.success(userDetails, token);
        } catch (AuthenticationException e) {
            LOGGER.warn("登录异常:{}",e.getMessage());
            return CommonResult.failed("登录异常:" + e.getMessage());
        }
    }


    /**
     * 分页查询系统用户
     * @return
     */
    @Override
    public CommonResult getAllAdmin(Integer pageSize, Integer pageNum,String keyWord) {

        QueryWrapper<SysAdmin> wrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(keyWord)) {
            wrapper.lambda().like(SysAdmin::getUsername, keyWord).or().like(SysAdmin::getNickname, keyWord);
        }
        if (pageNum == null) {
            return CommonResult.success(sysAdminMapper.selectList(wrapper));
        } else {
            Page<SysAdmin> page = new Page<>(pageNum, pageSize);
            return CommonResult.success(page(page,wrapper));
        }
    }

    /**
     * 根据角色名查询系统用户
     */
    @Override
    public List<SysAdmin> getAdminByRoleName(String roleName) {

        List<SysAdmin> sysAdminList = sysAdminMapper.getAdminByRoleName(roleName);

        return sysAdminList;
    }

    /**
     * 根据角色id查询系统用户
     */
    @Override
    public List<SysAdmin> getAdminByRoleId(Long roleId) {
        List<SysAdmin> sysAdminList = sysAdminMapper.getAdminByRoleId(roleId);

        return sysAdminList;
    }

    /**
     * 根据权限id查询系统用户
     */
    @Override
    public List<SysAdmin> getAdminByPermissionId(Long permissionId) {
        List<SysAdmin> sysAdminList = sysAdminMapper.getAdminByPermissionId(permissionId);

        return sysAdminList;
    }

    /**
     * 增加系统用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult addAdmin(SysAdmin sysAdmin) {
        try {
            if (getAdminByUserName(sysAdmin.getUsername()) != null) {
                return CommonResult.failed("该用户名已存在");
            }
            sysAdmin.setId(null);
            sysAdmin.setCreateTime(new Date());
            sysAdmin.setPassword(passwordEncoder.encode(sysAdmin.getPassword()));
            sysAdminMapper.insert(sysAdmin);
            return CommonResult.success("成功增加系统用户:" + sysAdmin.getId());
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("增加系统用户失败:"+e.getMessage());
        }
    }

    /**
     * 更新系统用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult updateAdmin(SysAdmin sysAdmin) {

        try {
            Long id = sysAdmin.getId();
            String username = sysAdmin.getUsername();
            String password = sysAdmin.getPassword();

            SysAdmin sysAdmin0 = sysAdminMapper.selectById(id);
            if (sysAdmin0 == null) {
                return CommonResult.failed("该用户不存在:"+id);
            }

            if (sysAdminMapper.hasThisRole(id,GlobalData.ROLE_SUPER_ID) != null) {
                return CommonResult.failed("不可更新具有 '超级管理员'角色 的系统用户");
            }

            if (StrUtil.isNotBlank(username) && !username.equals(sysAdmin0.getUsername())) {
                if (getAdminByUserName(sysAdmin.getUsername()) != null) {
                    return CommonResult.failed("该用户名已存在");
                }
            }

            if (StrUtil.isNotBlank(password)) {
                sysAdmin.setPassword(passwordEncoder.encode(password));
            }
            sysAdminMapper.updateById(sysAdmin);
            return CommonResult.success("更新系统用户成功:"+id);
        }catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("更新系统用户失败:"+e.getMessage());
        }
    }

    /**
     * 删除系统用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult deleteAdmin(Long id) {
        try {
            SysAdmin sysAdmin = sysAdminMapper.selectById(id);
            if (sysAdmin == null) {
                return CommonResult.failed("该用户不存在:"+id);
            }

            if (sysAdminMapper.hasThisRole(id, GlobalData.ROLE_SUPER_ID) != null) {
                return CommonResult.failed("不可删除具有 '超级管理员'角色 的系统用户");
            }

            sysAdminMapper.deleteById(id);
            // 关系删除
            QueryWrapper<SysAdminRoleRelation> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(SysAdminRoleRelation::getAdminId, id);
            sysAdminRoleRelationMapper.delete(wrapper);
            return CommonResult.success("删除系统用户成功:"+id);
        }catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除系统用户失败:"+e.getMessage());
        }
    }


    /**
     * 为系统用户分配角色
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult setRoleForAdmin(Long adminId, List<Long> roleIds) {
        try {

            if (sysAdminMapper.hasThisRole(adminId,GlobalData.ROLE_SUPER_ID) != null) {
                return CommonResult.failed("不可分配具有 '超级管理员'角色 的系统用户");
            }

            if (roleIds.contains(GlobalData.ROLE_SUPER_ID)) {
                return CommonResult.failed("不可分配'超级管理员'角色");
            }

            //先删除原有关系
            QueryWrapper<SysAdminRoleRelation> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(SysAdminRoleRelation::getAdminId, adminId);
            sysAdminRoleRelationMapper.delete(wrapper);
            //批量插入新关系
            List<SysAdminRoleRelation> relationList = new ArrayList<>();
            for (Long roleId : roleIds) {
                SysAdminRoleRelation relation = new SysAdminRoleRelation();
                relation.setAdminId(adminId);
                relation.setRoleId(roleId);
                relationList.add(relation);
            }
            sysAdminRoleRelationService.saveBatch(relationList,relationList.size());
            return CommonResult.success("成功为系用户分配角色:" + adminId);
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("为系用户分配角色失败:"+e.getMessage());
        }
    }


}
