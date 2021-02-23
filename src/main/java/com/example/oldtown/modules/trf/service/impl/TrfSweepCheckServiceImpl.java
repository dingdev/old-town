package com.example.oldtown.modules.trf.service.impl;

import com.example.oldtown.modules.trf.model.TrfMileage;
import com.example.oldtown.modules.trf.model.TrfSweepCheck;
import com.example.oldtown.modules.trf.mapper.TrfSweepCheckMapper;
import com.example.oldtown.modules.trf.service.TrfSweepCheckService;
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
 * 交通接驳打捞船员工考核 服务实现类
 * </p>
 * @author dyp
 * @since 2020-12-28
 */
@Service
public class TrfSweepCheckServiceImpl extends ServiceImpl<TrfSweepCheckMapper, TrfSweepCheck> implements TrfSweepCheckService {
    private final Logger LOGGER = LoggerFactory.getLogger(TrfSweepCheckServiceImpl.class);

    @Resource
    TrfSweepCheckMapper trfSweepCheckMapper;

    /**
     * 分页查询交通接驳打捞船员工考核
     * @return
     */
    @Override
    public CommonResult getAll(Integer pageSize, Integer pageNum, String keyword, String fromTime, String toTime) {

        QueryWrapper<TrfSweepCheck> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<TrfSweepCheck> lambda = wrapper.lambda();
        if (StrUtil.isNotBlank(keyword)) {
            lambda.like(TrfSweepCheck::getUsername, keyword);
        }
        if (StrUtil.isNotBlank(fromTime)) {
            lambda.ge(TrfSweepCheck::getDate,fromTime);
        }
        if (StrUtil.isNotBlank(toTime)) {
            lambda.le(TrfSweepCheck::getDate,toTime);
        }
        if (pageNum == null) {
            return CommonResult.success(trfSweepCheckMapper.selectList(wrapper));
        } else {
            Page<TrfSweepCheck> page = new Page<>(pageNum, pageSize);
            return CommonResult.success( page(page,wrapper));
        }
    }


    /**
     * 增加交通接驳打捞船员工考核
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult add(TrfSweepCheck trfSweepCheck) {
        try {

            trfSweepCheckMapper.insert(trfSweepCheck);

            return CommonResult.success("成功增加交通接驳打捞船员工考核:"+trfSweepCheck.getId(),trfSweepCheck.getId()+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("增加交通接驳打捞船员工考核失败:"+e.getMessage());
        }
    }

    /**
     * 更新交通接驳打捞船员工考核
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult update(TrfSweepCheck trfSweepCheck) {
        try {

            Long id = trfSweepCheck.getId();
            if (trfSweepCheckMapper.selectById(id) == null) {
                return CommonResult.failed("该交通接驳打捞船员工考核不存在:"+id);
            }

            trfSweepCheckMapper.updateById(trfSweepCheck);

            return CommonResult.success("成功更新交通接驳打捞船员工考核:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("更新交通接驳打捞船员工考核失败:"+e.getMessage());
        }
    }

    /**
     * 删除交通接驳打捞船员工考核
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult delete(Long id) {
        try {

            if (trfSweepCheckMapper.selectById(id) == null) {
                return CommonResult.failed("该交通接驳打捞船员工考核不存在:"+id);
            }

            trfSweepCheckMapper.deleteById(id);
            return CommonResult.success("成功删除交通接驳打捞船员工考核:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除交通接驳打捞船员工考核失败:"+e.getMessage());
        }
    }

    /**
     * 批量删除交通接驳打捞船员工考核
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult batchDelete(List<Long> ids) {
        try {
            int deleteNum = trfSweepCheckMapper.deleteBatchIds(ids);
            return CommonResult.success("成功删除交通接驳打捞船员工考核"+deleteNum+"个",deleteNum+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除交通接驳打捞船员工考核失败:"+e.getMessage());
        }
    }

}
