package com.example.oldtown.modules.trf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.oldtown.dto.StringIntegerDTO;
import com.example.oldtown.modules.trf.model.TrfSecurityStaff;
import com.example.oldtown.modules.trf.mapper.TrfSecurityStaffMapper;
import com.example.oldtown.modules.trf.model.TrfSweepStaff;
import com.example.oldtown.modules.trf.model.TrfYacht;
import com.example.oldtown.modules.trf.service.TrfSecurityStaffService;
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
import java.util.Date;
import java.util.List;

import com.example.oldtown.common.api.CommonResult;

/**
 * <p>
 * 交通接驳安保人员 服务实现类
 * </p>
 * @author dyp
 * @since 2020-11-25
 */
@Service
public class TrfSecurityStaffServiceImpl extends ServiceImpl<TrfSecurityStaffMapper, TrfSecurityStaff> implements TrfSecurityStaffService {
    private final Logger LOGGER = LoggerFactory.getLogger(TrfSecurityStaffServiceImpl.class);

    @Resource
    TrfSecurityStaffMapper trfSecurityStaffMapper;

    /**
     * 分页查询交通接驳安保人员
     */
    @Override
    public CommonResult getAll(Integer pageSize, Integer pageNum, String type, Integer ifCaptain, String keyword, Integer orderByCurrentStatus) {

        QueryWrapper<TrfSecurityStaff> ew = new QueryWrapper<>();
        if (StrUtil.isNotBlank(type)) {
            if (!type.contains(",")) {
                ew.eq("f.type", type);
            } else {
                ew.in("f.type",type.split(","));
            }
        }

        if (ifCaptain != null) {
            ew.eq("f.if_captain", ifCaptain);
        }

        if (StrUtil.isNotBlank(keyword)) {
            ew.and(wrapper -> wrapper.like("f.username", keyword).or().like("f.nickname", keyword)
                    .or().like("f.serial", keyword));
        }

        if (orderByCurrentStatus > 0) {
            ew.orderByDesc("f.current_status");
        }

        if (pageNum == null) {
            return CommonResult.success(trfSecurityStaffMapper.getAllList(ew));
        } else {
            Page<TrfSecurityStaff> page = new Page<>(pageNum, pageSize);
            return CommonResult.success(trfSecurityStaffMapper.getPageList(page,ew));
        }
    }

    /**
     * 根据id查询交通接驳安保人员
     */
    @Override
    public CommonResult getSecurityStaffById(Long id) {
        TrfSecurityStaff trfSecurityStaff = trfSecurityStaffMapper.getById(id);
        return CommonResult.success(trfSecurityStaff);
    }


    /**
     * 增加交通接驳安保人员
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult add(TrfSecurityStaff trfSecurityStaff) {
        try {

            trfSecurityStaff.setCreateTime(new Date());
            trfSecurityStaffMapper.insert(trfSecurityStaff);

            return CommonResult.success("成功增加交通接驳安保人员:"+trfSecurityStaff.getId(),trfSecurityStaff.getId()+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("增加交通接驳安保人员失败:"+e.getMessage());
        }
    }

    /**
     * 更新交通接驳安保人员
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult update(TrfSecurityStaff trfSecurityStaff) {
        try {

            Long id = trfSecurityStaff.getId();
            if (trfSecurityStaffMapper.selectById(id) == null) {
                return CommonResult.failed("该交通接驳安保人员不存在:"+id);
            }

            trfSecurityStaffMapper.updateById(trfSecurityStaff);

            return CommonResult.success("成功更新交通接驳安保人员:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("更新交通接驳安保人员失败:"+e.getMessage());
        }
    }

    /**
     * 删除交通接驳安保人员
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult delete(Long id) {
        try {

            if (trfSecurityStaffMapper.selectById(id) == null) {
                return CommonResult.failed("该交通接驳安保人员不存在:"+id);
            }

            trfSecurityStaffMapper.deleteById(id);
            return CommonResult.success("成功删除交通接驳安保人员:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除交通接驳安保人员失败:"+e.getMessage());
        }
    }

    /**
     * 批量删除交通接驳安保人员
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult batchDelete(List<Long> ids) {
        try {
            int deleteNum = trfSecurityStaffMapper .deleteBatchIds(ids);
            return CommonResult.success("成功删除交通接驳安保人员"+deleteNum+"个",deleteNum+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除交通接驳安保人员失败:"+e.getMessage());
        }
    }

    /**
     * 按类型统计交通接驳安保人员数量
     */
    @Override
    public CommonResult countByType() {
        try {
            List<StringIntegerDTO> list = trfSecurityStaffMapper.countByType();
            int sum = 0;
            if (list!=null) {
                sum = list.stream().mapToInt(StringIntegerDTO::getValue).sum();
            }
            return CommonResult.success(list,sum+"");
        } catch (Exception e) {
            LOGGER.error("按类型统计交通接驳安保人员数量失败:", e);
            return CommonResult.failed("按类型统计交通接驳安保人员数量失败:"+e.getMessage());
        }
    }

}
