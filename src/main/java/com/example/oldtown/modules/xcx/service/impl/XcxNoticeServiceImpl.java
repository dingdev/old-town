package com.example.oldtown.modules.xcx.service.impl;

import com.example.oldtown.modules.xcx.model.XcxNotice;
import com.example.oldtown.modules.xcx.mapper.XcxNoticeMapper;
import com.example.oldtown.modules.xcx.service.XcxNoticeService;
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
 * 小程序公告 服务实现类
 * </p>
 * @author dyp
 * @since 2020-12-01
 */
@Service
public class XcxNoticeServiceImpl extends ServiceImpl<XcxNoticeMapper, XcxNotice> implements XcxNoticeService {
    private final Logger LOGGER = LoggerFactory.getLogger(XcxNoticeServiceImpl.class);

    @Resource
    XcxNoticeMapper xcxNoticeMapper;

    /**
     * 分页查询小程序公告
     * @return
     */
    @Override
    public CommonResult getAll(Integer pageSize, Integer pageNum, String keyword) {

        QueryWrapper<XcxNotice> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<XcxNotice> lambda = wrapper.lambda();
        if (StrUtil.isNotBlank(keyword)) {
            lambda.like(XcxNotice::getTitle, keyword);
        }
        if (pageNum == null) {
            return CommonResult.success(xcxNoticeMapper.selectList(wrapper));
        } else {
            Page<XcxNotice> page = new Page<>(pageNum, pageSize);
            return CommonResult.success(page(page,wrapper));
        }
    }


    /**
     * 增加小程序公告
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult add(XcxNotice xcxNotice) {
        try {

            xcxNotice.setCreateTime(new Date());
            xcxNoticeMapper.insert(xcxNotice);

            return CommonResult.success("成功增加小程序公告:"+xcxNotice.getId(),xcxNotice.getId()+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("增加小程序公告失败:"+e.getMessage());
        }
    }

    /**
     * 更新小程序公告
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult update(XcxNotice xcxNotice) {
        try {

            Long id = xcxNotice.getId();
            if (xcxNoticeMapper.selectById(id) == null) {
                return CommonResult.failed("该小程序公告不存在:"+id);
            }

            xcxNoticeMapper.updateById(xcxNotice);

            return CommonResult.success("成功更新小程序公告:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("更新小程序公告失败:"+e.getMessage());
        }
    }

    /**
     * 删除小程序公告
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult delete(Long id) {
        try {

            if (xcxNoticeMapper.selectById(id) == null) {
                return CommonResult.failed("该小程序公告不存在:"+id);
            }

            xcxNoticeMapper.deleteById(id);
            return CommonResult.success("成功删除小程序公告:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除小程序公告失败:"+e.getMessage());
        }
    }

    /**
     * 批量删除小程序公告
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult batchDelete(List<Long> ids) {
        try {
            int deleteNum = xcxNoticeMapper.deleteBatchIds(ids);
            return CommonResult.success("成功删除小程序公告"+deleteNum+"个",deleteNum+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除小程序公告失败:"+e.getMessage());
        }
    }

}
