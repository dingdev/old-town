package com.example.oldtown.modules.com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.oldtown.modules.com.model.ComQa;
import com.example.oldtown.modules.com.mapper.ComQaMapper;
import com.example.oldtown.modules.com.service.ComQaService;
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
import java.util.*;

import com.example.oldtown.common.api.CommonResult;

/**
 * <p>
 * 通用问答 服务实现类
 * </p>
 * @author dyp
 * @since 2020-11-13
 */
@Service
public class ComQaServiceImpl extends ServiceImpl<ComQaMapper, ComQa> implements ComQaService {
    private final Logger LOGGER = LoggerFactory.getLogger(ComQaServiceImpl.class);

    @Resource
    ComQaMapper comQaMapper;

    /**
     * 分页查询通用问答
     * @return
     */
    @Override
    public CommonResult getAll(Integer pageSize, Integer pageNum,String type, String keyword) {

        QueryWrapper<ComQa> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<ComQa> lambda = wrapper.lambda();
        if (StrUtil.isNotBlank(type)) {
            if (type.contains(",")) {
                lambda.in(ComQa::getType,type.split(","));
            } else {
                lambda.eq(ComQa::getType, type);
            }
        }
        if (StrUtil.isNotBlank(keyword)) {
            lambda.like(ComQa::getQuestion, keyword);
        }
        if (pageNum == null) {
            return CommonResult.success(comQaMapper.selectList(wrapper));
        } else {
            Page<ComQa> page = new Page<>(pageNum, pageSize);
            return CommonResult.success(page(page,wrapper));
        }
    }


    /**
     * 增加通用问答
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult add(ComQa comQa) {
        try {

            comQa.setCreateTime(new Date());
            comQaMapper.insert(comQa);

            return CommonResult.success("成功增加通用问答:"+comQa.getId(),comQa.getId()+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("增加通用问答失败:"+e.getMessage());
        }
    }

    /**
     * 更新通用问答
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult update(ComQa comQa) {
        try {

            Long id = comQa.getId();
            if (comQaMapper.selectById(id) == null) {
            return CommonResult.failed("该通用问答不存在:"+id);
            }

            comQaMapper.updateById(comQa);

            return CommonResult.success("成功更新通用问答:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("更新通用问答失败:"+e.getMessage());
        }
    }

    /**
     * 删除通用问答
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult delete(Long id) {
        try {

            if (comQaMapper.selectById(id) == null) {
            return CommonResult.failed("该通用问答不存在:"+id);
            }

            comQaMapper.deleteById(id);
            return CommonResult.success("成功删除通用问答:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除通用问答失败:"+e.getMessage());
        }
    }

    /**
     * 批量删除通用问答
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult batchDelete(List<Long> ids) {
        try {
            int deleteNum = comQaMapper.deleteBatchIds(ids);
            return CommonResult.success("成功删除通用问答"+deleteNum+"个",deleteNum+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除通用问答失败:"+e.getMessage());
        }
    }

    /**
     * 根据问查询答
     */
    @Override
    public CommonResult getAnswerByQuestion(String question) {
        QueryWrapper<ComQa> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ComQa::getQuestion, question).eq(ComQa::getDeleted,0);
        List<ComQa> comQaList = comQaMapper.selectList(wrapper);
        if (comQaList!=null&& comQaList.size()>0) {
            return CommonResult.success(comQaList.get(0));
        } else {
            return CommonResult.success("暂无此问答");
        }
    }
}
