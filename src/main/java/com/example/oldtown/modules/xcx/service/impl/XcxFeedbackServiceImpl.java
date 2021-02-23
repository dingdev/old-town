package com.example.oldtown.modules.xcx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.oldtown.modules.xcx.model.XcxFeedback;
import com.example.oldtown.modules.xcx.mapper.XcxFeedbackMapper;
import com.example.oldtown.modules.xcx.service.XcxFeedbackService;
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
import com.example.oldtown.common.api.CommonResult;

/**
 * <p>
 * 小程序用户反馈 服务实现类
 * </p>
 * @author dyp
 * @since 2020-11-13
 */
@Service
public class XcxFeedbackServiceImpl extends ServiceImpl<XcxFeedbackMapper, XcxFeedback> implements XcxFeedbackService {
    private final Logger LOGGER = LoggerFactory.getLogger(XcxFeedbackServiceImpl.class);

    @Resource
    XcxFeedbackMapper xcxFeedbackMapper;

    /**
     * 小程序用户分页查询本人反馈
     */
    @Override
    public Page<XcxFeedback> getSelf(Integer pageSize, Integer pageNum, Long userId, Integer status, String type) {
        Page<XcxFeedback> page = new Page<>(pageNum, pageSize);
        QueryWrapper<XcxFeedback> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<XcxFeedback> lambda = wrapper.lambda();
        lambda.eq(XcxFeedback::getUserId, userId);
        if (status != null) {
            lambda.eq(XcxFeedback::getStatus, status);
        }
        if (StrUtil.isNotBlank(type)) {
            if (type.contains(",")) {
                lambda.in(XcxFeedback::getType,type.split(","));
            } else {
                lambda.eq(XcxFeedback::getType, type);
            }
        }
        return page(page,wrapper);
    }

    /**
     * 小程序用户根据id查询本人反馈
     */
    @Override
    public XcxFeedback getFeedbackById(Long id, Long userId) {
        QueryWrapper<XcxFeedback> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(XcxFeedback::getId,id).eq(XcxFeedback::getUserId, userId);
        return xcxFeedbackMapper.selectOne(wrapper);
    }


    /**
     * 增加小程序用户反馈
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult add(XcxFeedback xcxFeedback) {
        try {

            xcxFeedback.setCreateTime(new Date());
            xcxFeedback.setStatus(0);
            xcxFeedbackMapper.insert(xcxFeedback);

            return CommonResult.success("成功增加小程序用户反馈:"+xcxFeedback.getId(),xcxFeedback.getId()+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("增加小程序用户反馈失败:"+e.getMessage());
        }
    }

    /**
     * 更新小程序用户反馈
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult update(XcxFeedback xcxFeedback) {
        try {

            Long id = xcxFeedback.getId();
            Long userId = xcxFeedback.getUserId();
            QueryWrapper<XcxFeedback> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(XcxFeedback::getId,id).eq(XcxFeedback::getUserId, userId);
            XcxFeedback xcxFeedback0 = xcxFeedbackMapper.selectOne(wrapper);
            if (xcxFeedback0 == null) {
            return CommonResult.failed("该小程序用户反馈不存在:"+id);
            }

            xcxFeedback.setStatus(null);
            xcxFeedbackMapper.updateById(xcxFeedback);

            return CommonResult.success("成功更新小程序用户反馈:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("更新小程序用户反馈失败:"+e.getMessage());
        }
    }

    /**
     * 删除小程序用户反馈
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult delete(Long id,Long userId) {
        try {

            QueryWrapper<XcxFeedback> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(XcxFeedback::getId,id).eq(XcxFeedback::getUserId, userId);
            XcxFeedback xcxFeedback0 = xcxFeedbackMapper.selectOne(wrapper);
            if (xcxFeedback0 == null) {
                return CommonResult.failed("该小程序用户反馈不存在:"+id);
            }

            xcxFeedbackMapper.deleteById(id);
            return CommonResult.success("成功删除小程序用户反馈:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除小程序用户反馈失败:"+e.getMessage());
        }
    }

    /**
     * 管理员分页查询反馈
     */
    @Override
    public CommonResult getAll(Integer pageSize, Integer pageNum, Integer status, String type) {
        QueryWrapper<XcxFeedback> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<XcxFeedback> lambda = wrapper.lambda();
        if (status != null) {
            lambda.eq(XcxFeedback::getStatus, status);
        }
        if (StrUtil.isNotBlank(type)) {
            if (type.contains(",")) {
                lambda.in(XcxFeedback::getType,type.split(","));
            } else {
                lambda.eq(XcxFeedback::getType, type);
            }
        }
        if (pageNum == null) {
            return CommonResult.success(xcxFeedbackMapper.selectList(wrapper));
        } else {
            Page<XcxFeedback> page = new Page<>(pageNum, pageSize);
            return CommonResult.success(page(page,wrapper));
        }
    }

    /**
     * 管理员更新反馈状态
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult updateStatus(Long id, Integer status) {
        try {

            if (xcxFeedbackMapper.selectById(id) == null) {
                return CommonResult.failed("该小程序用户反馈不存在:"+id);
            }
            XcxFeedback xcxFeedback = new XcxFeedback();
            xcxFeedback.setUserId(id);
            xcxFeedback.setStatus(status);
            xcxFeedbackMapper.updateById(xcxFeedback);
            return CommonResult.success("管理员更新反馈状态成功");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("管理员更新反馈状态失败:"+e.getMessage());
        }
    }

}
