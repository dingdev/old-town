package com.example.oldtown.modules.com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.oldtown.modules.com.model.ComActivity;
import com.example.oldtown.modules.com.mapper.ComActivityMapper;
import com.example.oldtown.modules.com.service.ComActivityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.hutool.core.util.StrUtil;
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
 * 通用活动演出 服务实现类
 * </p>
 * @author dyp
 * @since 2020-11-24
 */
@Service
public class ComActivityServiceImpl extends ServiceImpl<ComActivityMapper, ComActivity> implements ComActivityService {
    private final Logger LOGGER = LoggerFactory.getLogger(ComActivityServiceImpl.class);

    @Resource
    ComActivityMapper comActivityMapper;

    /**
     * 分页查询通用活动演出
     * @return
     */
    @Override
    public CommonResult getAll(Integer pageSize, Integer pageNum, String keyword) {

        QueryWrapper<ComActivity> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<ComActivity> lambda = wrapper.lambda();

        if (StrUtil.isNotBlank(keyword)) {
            lambda.like(ComActivity::getName, keyword);
        }
        // 排序
        wrapper.orderByDesc("sorting_order").orderByAsc("id");
        if (pageNum == null) {
            return CommonResult.success(comActivityMapper.selectList(wrapper));
        } else {
            Page<ComActivity> page = new Page<>(pageNum, pageSize);
            return CommonResult.success(page(page,wrapper));
        }
    }


    /**
     * 增加通用活动演出
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult add(ComActivity comActivity) {
        try {

            comActivity.setCreateTime(new Date());
            comActivityMapper.insert(comActivity);

            return CommonResult.success("成功增加通用活动演出:"+comActivity.getId(),comActivity.getId()+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("增加通用活动演出失败:"+e.getMessage());
        }
    }

    /**
     * 更新通用活动演出
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult update(ComActivity comActivity) {
        try {

            Long id = comActivity.getId();
            if (comActivityMapper.selectById(id) == null) {
                return CommonResult.failed("该通用活动演出不存在:"+id);
            }

            comActivityMapper.updateById(comActivity);

            return CommonResult.success("成功更新通用活动演出:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("更新通用活动演出失败:"+e.getMessage());
        }
    }

    /**
     * 删除通用活动演出
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult delete(Long id) {
        try {

            if (comActivityMapper.selectById(id) == null) {
                return CommonResult.failed("该通用活动演出不存在:"+id);
            }

            comActivityMapper.deleteById(id);
            return CommonResult.success("成功删除通用活动演出:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除通用活动演出失败:"+e.getMessage());
        }
    }

    /**
     * 批量删除通用活动演出
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult batchDelete(List<Long> ids) {
        try {
            int deleteNum = comActivityMapper.deleteBatchIds(ids);
            return CommonResult.success("成功删除通用活动演出"+deleteNum+"个",deleteNum+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除通用活动演出失败:"+e.getMessage());
        }
    }
}
