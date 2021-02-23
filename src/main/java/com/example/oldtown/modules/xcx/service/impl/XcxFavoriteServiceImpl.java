package com.example.oldtown.modules.xcx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.oldtown.component.GlobalData;
import com.example.oldtown.modules.com.mapper.ComPlaceMapper;
import com.example.oldtown.modules.com.model.ComPlace;
import com.example.oldtown.modules.xcx.model.XcxFavorite;
import com.example.oldtown.modules.xcx.mapper.XcxFavoriteMapper;
import com.example.oldtown.modules.xcx.service.XcxFavoriteService;
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
import java.util.List;

import com.example.oldtown.common.api.CommonResult;

/**
 * <p>
 * 小程序用户收藏 服务实现类
 * </p>
 *
 * @author dyp
 * @since 2020-11-13
 */
@Service
public class XcxFavoriteServiceImpl extends ServiceImpl<XcxFavoriteMapper, XcxFavorite> implements XcxFavoriteService {
    private final Logger LOGGER = LoggerFactory.getLogger(XcxFavoriteServiceImpl.class);

    @Resource
    XcxFavoriteMapper xcxFavoriteMapper;
    @Resource
    ComPlaceMapper comPlaceMapper;

    /**
     * 小程序用户分页查询本人收藏
     *
     * @return
     */
    @Override
    public Page<XcxFavorite> getSelf(Integer pageSize, Integer pageNum, Long userId, String type, String keyword) {
        Page<XcxFavorite> page = new Page<>(pageNum, pageSize);
        QueryWrapper<XcxFavorite> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<XcxFavorite> lambda = wrapper.lambda();
        lambda.eq(XcxFavorite::getUserId, userId);
        if (StrUtil.isNotBlank(type)) {
            if (type.contains(",")) {
                lambda.in(XcxFavorite::getType,type.split(","));
            } else {
                lambda.eq(XcxFavorite::getType, type);
            }
        }
        if (StrUtil.isNotBlank(keyword)) {
            lambda.like(XcxFavorite::getContentName, keyword);
        }
        return page(page, wrapper);
    }

    /**
     * 小程序用户根据id查询本人收藏
     */
    @Override
    public XcxFavorite getFavoriteById(Long id, Long userId) {
        QueryWrapper<XcxFavorite> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(XcxFavorite::getId, id).eq(XcxFavorite::getUserId, userId);
        return xcxFavoriteMapper.selectOne(wrapper);
    }

    /**
     * 增加小程序用户收藏
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult add(XcxFavorite xcxFavorite) {
        try {
            String contentFrom = xcxFavorite.getContentFrom();
            if (StrUtil.isBlank(contentFrom)) {
                return CommonResult.failed("请输入内容来源(contentFrom),前端跳转详情时使用,比如值为comPlace,则查询通用场所)");
            }
            Long contentId = xcxFavorite.getContentId();

            if (contentId == null) {
                return CommonResult.failed("请输入内容Id(contentId),前端跳转详情时使用,比如值为2,则查询来源表里id为2的数据");
            }

            ComPlace comPlace = comPlaceMapper.selectById(contentId);
            if (comPlace!=null) {
                xcxFavorite.setContentName(comPlace.getName());
                String pictureUrl = comPlace.getPictureUrl();
                if (StrUtil.isNotBlank(pictureUrl)) {
                    String contentCover = pictureUrl.split(",")[0];
                    xcxFavorite.setContentCover(contentCover);
                }
            }

            Long userId = xcxFavorite.getUserId();
            QueryWrapper<XcxFavorite> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(XcxFavorite::getContentFrom, contentFrom).eq(XcxFavorite::getContentId, contentId).eq(XcxFavorite::getUserId, userId);
            List<XcxFavorite> xcxFavoriteList = xcxFavoriteMapper.selectList(wrapper);
            if (xcxFavoriteList != null && xcxFavoriteList.size() >0) {
                return CommonResult.success(" 该小程序用户收藏已存在:" + xcxFavorite.getId(),xcxFavorite.getId() + "");
            }
            xcxFavorite.setCreateTime(new Date());
            xcxFavoriteMapper.insert(xcxFavorite);

            return CommonResult.success("成功增加小程序用户收藏:" + xcxFavorite.getId(), xcxFavorite.getId() + "");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("增加小程序用户收藏失败:" + e.getMessage());
        }
    }

    /**
     * 更新小程序用户收藏
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult update(XcxFavorite xcxFavorite) {
        try {

            Long id = xcxFavorite.getId();
            Long userId = xcxFavorite.getUserId();
            QueryWrapper<XcxFavorite> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(XcxFavorite::getId, id).eq(XcxFavorite::getUserId, userId);
            XcxFavorite xcxFavorite0 = xcxFavoriteMapper.selectOne(wrapper);
            if (xcxFavorite0 == null) {
                return CommonResult.failed("该小程序用户收藏不存在:" + id);
            }

            xcxFavoriteMapper.updateById(xcxFavorite);

            return CommonResult.success("成功更新小程序用户收藏:" + id, id + "");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("更新小程序用户收藏失败:" + e.getMessage());
        }
    }

    /**
     * 删除小程序用户收藏
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult delete(Long id, Long userId) {
        try {
            QueryWrapper<XcxFavorite> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(XcxFavorite::getId, id).eq(XcxFavorite::getUserId, userId);
            XcxFavorite xcxFavorite0 = xcxFavoriteMapper.selectOne(wrapper);
            if (xcxFavorite0 == null) {
                return CommonResult.failed("该小程序用户收藏不存在:" + id);
            }

            xcxFavoriteMapper.deleteById(id);
            return CommonResult.success("成功删除小程序用户收藏:" + id, id + "");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除小程序用户收藏失败:" + e.getMessage());
        }
    }

}
