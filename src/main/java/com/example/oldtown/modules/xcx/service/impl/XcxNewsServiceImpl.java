package com.example.oldtown.modules.xcx.service.impl;

import com.example.oldtown.modules.xcx.model.XcxNews;
import com.example.oldtown.modules.xcx.mapper.XcxNewsMapper;
import com.example.oldtown.modules.xcx.service.XcxNewsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oldtown.util.ScheduledUtil;
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
 * 小程序资讯 服务实现类
 * </p>
 * @author dyp
 * @since 2020-12-03
 */
@Service
public class XcxNewsServiceImpl extends ServiceImpl<XcxNewsMapper, XcxNews> implements XcxNewsService {
    private final Logger LOGGER = LoggerFactory.getLogger(XcxNewsServiceImpl.class);

    @Resource
    XcxNewsMapper xcxNewsMapper;

    /**
     * 分页查询小程序资讯
     * @return
     */
    @Override
    public CommonResult getAll(Integer pageSize, Integer pageNum, String keyword) {
        QueryWrapper<XcxNews> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<XcxNews> lambda = wrapper.lambda();

        if (StrUtil.isNotBlank(keyword)) {
            lambda.like(XcxNews::getTitle, keyword);
        }
        lambda.eq(XcxNews::getDeleted, 0);
        lambda.orderByDesc(XcxNews::getId);
        if (pageNum == null) {
            return CommonResult.success(xcxNewsMapper.selectList(wrapper));
        } else {
            Page<XcxNews> page = new Page<>(pageNum, pageSize);
            return CommonResult.success(page(page,wrapper));
        }
    }


    /**
     * 增加小程序资讯
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult add(XcxNews xcxNews) {
        try {

            xcxNews.setCreateTime(new Date());
            xcxNewsMapper.insert(xcxNews);

            return CommonResult.success("成功增加小程序资讯:"+xcxNews.getId(),xcxNews.getId()+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("增加小程序资讯失败:"+e.getMessage());
        }
    }

    /**
     * 更新小程序资讯
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult update(XcxNews xcxNews) {
        try {

            Long id = xcxNews.getId();
            if (xcxNewsMapper.selectById(id) == null) {
                return CommonResult.failed("该小程序资讯不存在:"+id);
            }

            xcxNewsMapper.updateById(xcxNews);

            return CommonResult.success("成功更新小程序资讯:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("更新小程序资讯失败:"+e.getMessage());
        }
    }

    /**
     * 删除小程序资讯
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult delete(Long id) {
        try {

            if (xcxNewsMapper.selectById(id) == null) {
                return CommonResult.failed("该小程序资讯不存在:"+id);
            }

            xcxNewsMapper.deleteById(id);
            return CommonResult.success("成功删除小程序资讯:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除小程序资讯失败:"+e.getMessage());
        }
    }

    /**
     * 批量删除小程序资讯
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult batchDelete(List<Long> ids) {
        try {
            int deleteNum = xcxNewsMapper.deleteBatchIds(ids);
            return CommonResult.success("成功删除小程序资讯"+deleteNum+"个",deleteNum+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除小程序资讯失败:"+e.getMessage());
        }
    }

}
