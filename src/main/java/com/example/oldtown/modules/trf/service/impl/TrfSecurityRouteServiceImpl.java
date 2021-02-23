package com.example.oldtown.modules.trf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.oldtown.modules.trf.mapper.TrfSecurityStaffMapper;
import com.example.oldtown.modules.trf.model.TrfSecurityRoute;
import com.example.oldtown.modules.trf.mapper.TrfSecurityRouteMapper;
import com.example.oldtown.modules.trf.model.TrfSecurityStaff;
import com.example.oldtown.modules.trf.model.TrfSweepRoute;
import com.example.oldtown.modules.trf.service.TrfSecurityRouteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import javax.annotation.Resource;
import java.beans.Transient;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.example.oldtown.common.api.CommonResult;

/**
 * <p>
 * 交通接驳安保人员标准路线 服务实现类
 * </p>
 * @author dyp
 * @since 2020-11-25
 */
@Service
public class TrfSecurityRouteServiceImpl extends ServiceImpl<TrfSecurityRouteMapper, TrfSecurityRoute> implements TrfSecurityRouteService {
    private final Logger LOGGER = LoggerFactory.getLogger(TrfSecurityRouteServiceImpl.class);

    @Resource
    TrfSecurityRouteMapper trfSecurityRouteMapper;
    @Resource
    TrfSecurityStaffMapper trfSecurityStaffMapper;

    /**
     * 分页查询交通接驳安保人员标准路线
     * @return
     */
    @Override
    public CommonResult getAll(Integer pageSize, Integer pageNum,String type, String keyword) {
        QueryWrapper<TrfSecurityRoute> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<TrfSecurityRoute> lambda = wrapper.lambda();
        if (StrUtil.isNotBlank(type)) {
            if (type.contains(",")) {
                lambda.in(TrfSecurityRoute::getType,type.split(","));
            } else {
                lambda.eq(TrfSecurityRoute::getType, type);
            }
        }
        if (StrUtil.isNotBlank(keyword)) {
            lambda.like(TrfSecurityRoute::getName, keyword);
        }
        if (pageNum == null) {
            return CommonResult.success(trfSecurityRouteMapper.selectList(wrapper));
        } else {
            Page<TrfSecurityRoute> page = new Page<>(pageNum, pageSize);
            return CommonResult.success(page(page,wrapper));
        }
    }


    /**
     * 增加交通接驳安保人员标准路线
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult add(TrfSecurityRoute trfSecurityRoute) {
        try {

            trfSecurityRoute.setCreateTime(new Date());
            trfSecurityRouteMapper.insert(trfSecurityRoute);

            // 员工更新其标准路线id
            String staffIds = trfSecurityRoute.getStaffIds();
            if (StrUtil.isNotBlank(staffIds)) {
                String[] staffIdArray = staffIds.split(",");
                if (staffIdArray != null && staffIdArray.length > 0) {
                    List<Long> staffIdList = Arrays.asList(staffIdArray).stream().map(Long::parseLong).collect(Collectors.toList());
                    TrfSecurityStaff trfSecurityStaff = new TrfSecurityStaff();
                    trfSecurityStaff.setRouteId(trfSecurityRoute.getId());
                    if (StrUtil.isNotBlank(trfSecurityRoute.getType())) {
                        trfSecurityStaff.setType(trfSecurityRoute.getType());
                    }
                    UpdateWrapper<TrfSecurityStaff> wrapper = new UpdateWrapper<>();
                    wrapper.lambda().in(TrfSecurityStaff::getId, staffIdList);
                    trfSecurityStaffMapper.update(trfSecurityStaff, wrapper);
                }
            }
            
            return CommonResult.success("成功增加交通接驳安保人员标准路线:"+trfSecurityRoute.getId(),trfSecurityRoute.getId()+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("增加交通接驳安保人员标准路线失败:"+e.getMessage());
        }
    }

    /**
     * 更新交通接驳安保人员标准路线
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult update(TrfSecurityRoute trfSecurityRoute) {
        try {

            Long id = trfSecurityRoute.getId();
            if (trfSecurityRouteMapper.selectById(id) == null) {
                return CommonResult.failed("该交通接驳安保人员标准路线不存在:"+id);
            }

            trfSecurityRouteMapper.updateById(trfSecurityRoute);

            // 员工更新其标准路线id
            String staffIds = trfSecurityRoute.getStaffIds();
            if (StrUtil.isNotBlank(staffIds)) {
                String[] staffIdArray = staffIds.split(",");
                if (staffIdArray != null && staffIdArray.length > 0) {
                    List<Long> staffIdList = Arrays.asList(staffIdArray).stream().map(Long::parseLong).collect(Collectors.toList());
                    TrfSecurityStaff trfSecurityStaff = new TrfSecurityStaff();
                    trfSecurityStaff.setRouteId(trfSecurityRoute.getId());
                    if (StrUtil.isNotBlank(trfSecurityRoute.getType())) {
                        trfSecurityStaff.setType(trfSecurityRoute.getType());
                    }
                    UpdateWrapper<TrfSecurityStaff> wrapper = new UpdateWrapper<>();
                    wrapper.lambda().in(TrfSecurityStaff::getId, staffIdList);
                    trfSecurityStaffMapper.update(trfSecurityStaff, wrapper);
                }
            }

            return CommonResult.success("成功更新交通接驳安保人员标准路线:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("更新交通接驳安保人员标准路线失败:"+e.getMessage());
        }
    }

    /**
     * 删除交通接驳安保人员标准路线
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult delete(Long id) {
        try {

            if (trfSecurityRouteMapper.selectById(id) == null) {
                return CommonResult.failed("该交通接驳安保人员标准路线不存在:"+id);
            }

            trfSecurityRouteMapper.deleteById(id);
            return CommonResult.success("成功删除交通接驳安保人员标准路线:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除交通接驳安保人员标准路线失败:"+e.getMessage());
        }
    }

    /**
     * 批量删除交通接驳安保人员标准路线
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult batchDelete(List<Long> ids) {
        try {
            int deleteNum = trfSecurityRouteMapper.deleteBatchIds(ids);
            return CommonResult.success("成功删除交通接驳安保人员标准路线"+deleteNum+"个",deleteNum+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除交通接驳安保人员标准路线失败:"+e.getMessage());
        }
    }

}
