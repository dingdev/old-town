package com.example.oldtown.modules.com.service.impl;

import com.example.oldtown.modules.com.model.ComTel;
import com.example.oldtown.modules.com.mapper.ComTelMapper;
import com.example.oldtown.modules.com.service.ComTelService;
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
 * 通用电话 服务实现类
 * </p>
 * @author dyp
 * @since 2021-01-29
 */
@Service
public class ComTelServiceImpl extends ServiceImpl<ComTelMapper, ComTel> implements ComTelService {
    private final Logger LOGGER = LoggerFactory.getLogger(ComTelServiceImpl.class);

    @Resource
    ComTelMapper comTelMapper;

    /**
     * 分页查询通用电话
     * @return
     */
    @Override
    public CommonResult getAll(Integer pageSize, Integer pageNum, String keyword) {

        QueryWrapper<ComTel> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<ComTel> lambda = wrapper.lambda();
        if (StrUtil.isNotBlank(keyword)) {
            lambda.like(ComTel::getName, keyword);
        }
        if (pageNum == null){
            return CommonResult.success(comTelMapper.selectList(wrapper));
        } else {
            Page<ComTel> page = new Page<>(pageNum, pageSize);
            return CommonResult.success( page(page,wrapper));
        }
    }


    /**
     * 增加通用电话
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult add(ComTel comTel) {
        try {

            comTel.setCreateTime(new Date());
            comTelMapper.insert(comTel);

            return CommonResult.success("成功增加通用电话:"+comTel.getId(),comTel.getId()+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("增加通用电话失败:"+e.getMessage());
        }
    }

    /**
     * 更新通用电话
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult update(ComTel comTel) {
        try {

            Long id = comTel.getId();
            if (comTelMapper.selectById(id) == null) {
                return CommonResult.failed("该通用电话不存在:"+id);
            }

            comTelMapper.updateById(comTel);

            return CommonResult.success("成功更新通用电话:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("更新通用电话失败:"+e.getMessage());
        }
    }

    /**
     * 删除通用电话
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult delete(Long id) {
        try {

            if (comTelMapper.selectById(id) == null) {
                return CommonResult.failed("该通用电话不存在:"+id);
            }

            comTelMapper.deleteById(id);
            return CommonResult.success("成功删除通用电话:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除通用电话失败:"+e.getMessage());
        }
    }

    /**
     * 批量删除通用电话
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult batchDelete(List<Long> ids) {
        try {
            int deleteNum = comTelMapper.deleteBatchIds(ids);
            return CommonResult.success("成功删除通用电话"+deleteNum+"个",deleteNum+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除通用电话失败:"+e.getMessage());
        }
    }

}
