package com.example.oldtown.modules.trf.service.impl;

import com.example.oldtown.modules.trf.model.TrfMileage;
import com.example.oldtown.modules.trf.mapper.TrfMileageMapper;
import com.example.oldtown.modules.trf.service.TrfMileageService;
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
 * 交通接驳行驶里程 服务实现类
 * </p>
 * @author dyp
 * @since 2021-01-14
 */
@Service
public class TrfMileageServiceImpl extends ServiceImpl<TrfMileageMapper, TrfMileage> implements TrfMileageService {
    private final Logger LOGGER = LoggerFactory.getLogger(TrfMileageServiceImpl.class);

    @Resource
    TrfMileageMapper trfMileageMapper;

    /**
     * 分页查询交通接驳行驶里程
     * @return
     */
    @Override
    public CommonResult getAll(Integer pageSize, Integer pageNum, String type, String keyword, String fromTime, String toTime) {

        QueryWrapper<TrfMileage> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<TrfMileage> lambda = wrapper.lambda();
        if (StrUtil.isNotBlank(type)) {
            lambda.eq(TrfMileage::getType, type);
        }
        if (StrUtil.isNotBlank(keyword)) {
            lambda.like(TrfMileage::getName, keyword);
        }
        if (StrUtil.isNotBlank(fromTime)) {
            lambda.ge(TrfMileage::getDate,fromTime);
        }
        if (StrUtil.isNotBlank(toTime)) {
            lambda.le(TrfMileage::getDate,toTime);
        }
        if (pageNum == null) {
            return CommonResult.success(trfMileageMapper.selectList(wrapper));
        } else {
            Page<TrfMileage> page = new Page<>(pageNum, pageSize);
            return CommonResult.success( page(page,wrapper));
        }

    }


    /**
     * 增加交通接驳行驶里程
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult add(TrfMileage trfMileage) {
        try {
            trfMileageMapper.insert(trfMileage);

            return CommonResult.success("成功增加交通接驳行驶里程:"+trfMileage.getId(),trfMileage.getId()+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("增加交通接驳行驶里程失败:"+e.getMessage());
        }
    }

    /**
     * 更新交通接驳行驶里程
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult update(TrfMileage trfMileage) {
        try {

            Long id = trfMileage.getId();
            if (trfMileageMapper.selectById(id) == null) {
                return CommonResult.failed("该交通接驳行驶里程不存在:"+id);
            }

            trfMileageMapper.updateById(trfMileage);

            return CommonResult.success("成功更新交通接驳行驶里程:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("更新交通接驳行驶里程失败:"+e.getMessage());
        }
    }

    /**
     * 删除交通接驳行驶里程
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult delete(Long id) {
        try {

            if (trfMileageMapper.selectById(id) == null) {
                return CommonResult.failed("该交通接驳行驶里程不存在:"+id);
            }

            trfMileageMapper.deleteById(id);
            return CommonResult.success("成功删除交通接驳行驶里程:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除交通接驳行驶里程失败:"+e.getMessage());
        }
    }

    /**
     * 批量删除交通接驳行驶里程
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult batchDelete(List<Long> ids) {
        try {
            int deleteNum = trfMileageMapper.deleteBatchIds(ids);
            return CommonResult.success("成功删除交通接驳行驶里程"+deleteNum+"个",deleteNum+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除交通接驳行驶里程失败:"+e.getMessage());
        }
    }

}
