package com.example.oldtown.modules.xcx.service.impl;

import com.example.oldtown.modules.xcx.model.XcxPropaganda;
import com.example.oldtown.modules.xcx.mapper.XcxPropagandaMapper;
import com.example.oldtown.modules.xcx.service.XcxPropagandaService;
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
 * 小程序宣传 服务实现类
 * </p>
 * @author dyp
 * @since 2020-12-02
 */
@Service
public class XcxPropagandaServiceImpl extends ServiceImpl<XcxPropagandaMapper, XcxPropaganda> implements XcxPropagandaService {
    private final Logger LOGGER = LoggerFactory.getLogger(XcxPropagandaServiceImpl.class);

    @Resource
    XcxPropagandaMapper xcxPropagandaMapper;

    /**
     * 分页查询小程序宣传
     * @return
     */
    @Override
    public CommonResult getAll(Integer pageSize, Integer pageNum, String type, String keyword) {
        QueryWrapper<XcxPropaganda> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<XcxPropaganda> lambda = wrapper.lambda();
        if (StrUtil.isNotBlank(type)) {
            if (type.contains(",")) {
                lambda.in(XcxPropaganda::getType,type.split(","));
            } else {
                lambda.eq(XcxPropaganda::getType, type);
            }
        }
        if (StrUtil.isNotBlank(keyword)) {
            lambda.like(XcxPropaganda::getName, keyword);
        }
        // 排序
        wrapper.orderByDesc("sorting_order").orderByAsc("id");
        if (pageNum == null) {
            return CommonResult.success(xcxPropagandaMapper.selectList(wrapper));
        } else {
            Page<XcxPropaganda> page = new Page<>(pageNum, pageSize);
            return CommonResult.success(page(page,wrapper));
        }
    }


    /**
     * 增加小程序宣传
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult add(XcxPropaganda xcxPropaganda) {
        try {

            xcxPropaganda.setCreateTime(new Date());
            xcxPropagandaMapper.insert(xcxPropaganda);

            return CommonResult.success("成功增加小程序宣传:"+xcxPropaganda.getId(),xcxPropaganda.getId()+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("增加小程序宣传失败:"+e.getMessage());
        }
    }

    /**
     * 更新小程序宣传
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult update(XcxPropaganda xcxPropaganda) {
        try {

            Long id = xcxPropaganda.getId();
            if (xcxPropagandaMapper.selectById(id) == null) {
                return CommonResult.failed("该小程序宣传不存在:"+id);
            }

            xcxPropagandaMapper.updateById(xcxPropaganda);

            return CommonResult.success("成功更新小程序宣传:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("更新小程序宣传失败:"+e.getMessage());
        }
    }

    /**
     * 删除小程序宣传
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult delete(Long id) {
        try {

            if (xcxPropagandaMapper.selectById(id) == null) {
                return CommonResult.failed("该小程序宣传不存在:"+id);
            }

            xcxPropagandaMapper.deleteById(id);
            return CommonResult.success("成功删除小程序宣传:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除小程序宣传失败:"+e.getMessage());
        }
    }

    /**
     * 批量删除小程序宣传
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult batchDelete(List<Long> ids) {
        try {
            int deleteNum = xcxPropagandaMapper.deleteBatchIds(ids);
            return CommonResult.success("成功删除小程序宣传"+deleteNum+"个",deleteNum+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除小程序宣传失败:"+e.getMessage());
        }
    }

}
