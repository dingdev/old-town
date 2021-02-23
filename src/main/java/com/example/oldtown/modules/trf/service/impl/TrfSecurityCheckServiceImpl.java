package com.example.oldtown.modules.trf.service.impl;

import com.example.oldtown.modules.trf.model.TrfMileage;
import com.example.oldtown.modules.trf.model.TrfSecurityCheck;
import com.example.oldtown.modules.trf.mapper.TrfSecurityCheckMapper;
import com.example.oldtown.modules.trf.service.TrfSecurityCheckService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
 * 交通接驳安保人员考核 服务实现类
 * </p>
 * @author dyp
 * @since 2020-12-28
 */
@Service
public class TrfSecurityCheckServiceImpl extends ServiceImpl<TrfSecurityCheckMapper, TrfSecurityCheck> implements TrfSecurityCheckService {
    private final Logger LOGGER = LoggerFactory.getLogger(TrfSecurityCheckServiceImpl.class);

    @Resource
    TrfSecurityCheckMapper trfSecurityCheckMapper;

    /**
     * 分页查询交通接驳安保人员考核
     * @return
     */
    @Override
    public CommonResult getAll(Integer pageSize, Integer pageNum, String keyword, String fromTime, String toTime) {

        QueryWrapper<TrfSecurityCheck> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<TrfSecurityCheck> lambda = wrapper.lambda();
        if (StrUtil.isNotBlank(keyword)) {
            lambda.like(TrfSecurityCheck::getUsername, keyword);
        }
        if (StrUtil.isNotBlank(fromTime)) {
            lambda.ge(TrfSecurityCheck::getDate,fromTime);
        }
        if (StrUtil.isNotBlank(toTime)) {
            lambda.le(TrfSecurityCheck::getDate,toTime);
        }
        if (pageNum == null) {
            return CommonResult.success(trfSecurityCheckMapper.selectList(wrapper));
        } else {
            Page<TrfSecurityCheck> page = new Page<>(pageNum, pageSize);
            return CommonResult.success( page(page,wrapper));
        }

    }


    /**
     * 增加交通接驳安保人员考核
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult add(TrfSecurityCheck trfSecurityCheck) {
        try {

            trfSecurityCheckMapper.insert(trfSecurityCheck);

            return CommonResult.success("成功增加交通接驳安保人员考核:"+trfSecurityCheck.getId(),trfSecurityCheck.getId()+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("增加交通接驳安保人员考核失败:"+e.getMessage());
        }
    }

    /**
     * 更新交通接驳安保人员考核
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult update(TrfSecurityCheck trfSecurityCheck) {
        try {

            Long id = trfSecurityCheck.getId();
            if (trfSecurityCheckMapper.selectById(id) == null) {
                return CommonResult.failed("该交通接驳安保人员考核不存在:"+id);
            }

            trfSecurityCheckMapper.updateById(trfSecurityCheck);

            return CommonResult.success("成功更新交通接驳安保人员考核:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("更新交通接驳安保人员考核失败:"+e.getMessage());
        }
    }

    /**
     * 删除交通接驳安保人员考核
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult delete(Long id) {
        try {

            if (trfSecurityCheckMapper.selectById(id) == null) {
                return CommonResult.failed("该交通接驳安保人员考核不存在:"+id);
            }

            trfSecurityCheckMapper.deleteById(id);
            return CommonResult.success("成功删除交通接驳安保人员考核:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除交通接驳安保人员考核失败:"+e.getMessage());
        }
    }

    /**
     * 批量删除交通接驳安保人员考核
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult batchDelete(List<Long> ids) {
        try {
            int deleteNum = trfSecurityCheckMapper.deleteBatchIds(ids);
            return CommonResult.success("成功删除交通接驳安保人员考核"+deleteNum+"个",deleteNum+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除交通接驳安保人员考核失败:"+e.getMessage());
        }
    }

}
