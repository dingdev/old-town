package com.example.oldtown.modules.trf.service.impl;

import com.example.oldtown.modules.trf.model.TrfSweepPoint;
import com.example.oldtown.modules.trf.mapper.TrfSweepPointMapper;
import com.example.oldtown.modules.trf.service.TrfSweepPointService;
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
 * 交通接驳打捞点 服务实现类
 * </p>
 * @author dyp
 * @since 2020-12-28
 */
@Service
public class TrfSweepPointServiceImpl extends ServiceImpl<TrfSweepPointMapper, TrfSweepPoint> implements TrfSweepPointService {
    private final Logger LOGGER = LoggerFactory.getLogger(TrfSweepPointServiceImpl.class);

    @Resource
    TrfSweepPointMapper trfSweepPointMapper;

    /**
     * 分页查询交通接驳打捞点
     * @return
     */
    @Override
    public CommonResult getAll(Integer pageSize, Integer pageNum, String keyword) {
        QueryWrapper<TrfSweepPoint> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<TrfSweepPoint> lambda = wrapper.lambda();
        if (StrUtil.isNotBlank(keyword)) {
            lambda.like(TrfSweepPoint::getName, keyword);
        }
        if (pageNum == null) {
            return CommonResult.success(trfSweepPointMapper.selectList(wrapper));
        } else {
            Page<TrfSweepPoint> page = new Page<>(pageNum, pageSize);
            return CommonResult.success(page(page,wrapper));
        }
    }


    /**
     * 增加交通接驳打捞点
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult add(TrfSweepPoint trfSweepPoint) {
        try {

            trfSweepPoint.setCreateTime(new Date());
            trfSweepPointMapper.insert(trfSweepPoint);

            return CommonResult.success("成功增加交通接驳打捞点:"+trfSweepPoint.getId(),trfSweepPoint.getId()+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("增加交通接驳打捞点失败:"+e.getMessage());
        }
    }

    /**
     * 更新交通接驳打捞点
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult update(TrfSweepPoint trfSweepPoint) {
        try {

            Long id = trfSweepPoint.getId();
            if (trfSweepPointMapper.selectById(id) == null) {
                return CommonResult.failed("该交通接驳打捞点不存在:"+id);
            }

            trfSweepPointMapper.updateById(trfSweepPoint);

            return CommonResult.success("成功更新交通接驳打捞点:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("更新交通接驳打捞点失败:"+e.getMessage());
        }
    }

    /**
     * 删除交通接驳打捞点
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult delete(Long id) {
        try {

            if (trfSweepPointMapper.selectById(id) == null) {
                return CommonResult.failed("该交通接驳打捞点不存在:"+id);
            }

            trfSweepPointMapper.deleteById(id);
            return CommonResult.success("成功删除交通接驳打捞点:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除交通接驳打捞点失败:"+e.getMessage());
        }
    }

    /**
     * 批量删除交通接驳打捞点
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult batchDelete(List<Long> ids) {
        try {
            int deleteNum = trfSweepPointMapper.deleteBatchIds(ids);
            return CommonResult.success("成功删除交通接驳打捞点"+deleteNum+"个",deleteNum+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除交通接驳打捞点失败:"+e.getMessage());
        }
    }

}
