package com.example.oldtown.modules.xcx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.oldtown.dto.MinPointDTO;
import com.example.oldtown.dto.XcxTravelDTO;
import com.example.oldtown.modules.com.mapper.ComPlaceMapper;
import com.example.oldtown.modules.com.mapper.ComRouteMapper;
import com.example.oldtown.modules.com.model.ComPlace;
import com.example.oldtown.modules.com.model.ComRoute;
import com.example.oldtown.modules.com.service.ComRouteService;
import com.example.oldtown.modules.xcx.model.XcxTravel;
import com.example.oldtown.modules.xcx.mapper.XcxTravelMapper;
import com.example.oldtown.modules.xcx.service.XcxTravelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
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
 * 小程序用户行程 服务实现类
 * </p>
 * @author dyp
 * @since 2020-11-13
 */
@Service
public class XcxTravelServiceImpl extends ServiceImpl<XcxTravelMapper, XcxTravel> implements XcxTravelService {
    private final Logger LOGGER = LoggerFactory.getLogger(XcxTravelServiceImpl.class);

    @Resource
    XcxTravelMapper xcxTravelMapper;
    @Resource
    ComRouteMapper comRouteMapper;
    @Autowired
    ComRouteService comRouteService;
    @Resource
    ComPlaceMapper comPlaceMapper;

    /**
     * 分页查询小程序用户行程
     * @return
     */
    @Override
    public Page<XcxTravel> getSelf(Integer pageSize, Integer pageNum, Long userId, String keyword) {
        Page<XcxTravel> page = new Page<>(pageNum, pageSize);
        QueryWrapper<XcxTravel> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<XcxTravel> lambda = wrapper.lambda();
        lambda.eq(XcxTravel::getUserId, userId);
        if (StrUtil.isNotBlank(keyword)) {
            lambda.like(XcxTravel::getName, keyword);
        }
        return page(page,wrapper);
    }

    /**
     * 根据id查询小程序用户行程
     */
    @Override
    public CommonResult getTravelById(Long id, Long userId) {
        QueryWrapper<XcxTravel> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(XcxTravel::getId, id).eq(XcxTravel::getUserId, userId);
        XcxTravel xcxTravel = xcxTravelMapper.selectOne(wrapper);
        if (xcxTravel == null) {
            return CommonResult.success(null);
        }
        Long routeId = xcxTravel.getRouteId();
        ComRoute comRoute = comRouteMapper.selectById(routeId);
        if (comRoute == null) {
            return CommonResult.success(new XcxTravelDTO(xcxTravel,null,null,null,null,null));
        }
        String contentIds = comRoute.getContentIds();
        if (StrUtil.isBlank(contentIds)) {
            return CommonResult.success(new XcxTravelDTO(xcxTravel, comRoute.getIntro(), comRoute.getLength(), comRoute.getLineString(),comRoute.getDetailsUrl(),null));
        } else {
            contentIds = "(" + contentIds + ")";
            List<MinPointDTO> pointList = comPlaceMapper.getMinPointList(contentIds);
            for (MinPointDTO minPointDTO : pointList) {
                if (StrUtil.isNotBlank(minPointDTO.getPictureUrl())) {
                    minPointDTO.setPictureUrl(minPointDTO.getPictureUrl().split(",")[0]);
                }
            }
            return CommonResult.success(new XcxTravelDTO(xcxTravel, comRoute.getIntro(), comRoute.getLength(),comRoute.getLineString(),comRoute.getDetailsUrl(),pointList));
        }

    }


    /**
     * 增加小程序用户行程
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult add(XcxTravel xcxTravel) {
        try {
            String label = xcxTravel.getLabel();
            Integer dayNum = xcxTravel.getDayNum();

            ComRoute comRoute = comRouteService.getByLabelAndDayNum(label, dayNum);
            xcxTravel.setRouteId(comRoute.getId());
            String pictureUrl = comRoute.getPictureUrl();
            if (StrUtil.isNotBlank(pictureUrl)) {
                xcxTravel.setCoverUrl(pictureUrl.split(",")[0]);
            }
            xcxTravel.setName(comRoute.getName());
            xcxTravel.setCreateTime(new Date());

            xcxTravelMapper.insert(xcxTravel);

            return CommonResult.success("成功增加小程序用户行程:"+xcxTravel.getId(),xcxTravel.getId()+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("增加小程序用户行程失败:"+e.getMessage());
        }
    }

    /**
     * 更新小程序用户行程
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult update(XcxTravel xcxTravel) {
        try {

            Long id = xcxTravel.getId();
            Long userId = xcxTravel.getUserId();
            QueryWrapper<XcxTravel> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(XcxTravel::getId, id).eq(XcxTravel::getUserId, userId);
            XcxTravel xcxTravel0 = xcxTravelMapper.selectOne(wrapper);
            if (xcxTravel0 == null) {
                return CommonResult.failed("该小程序用户行程不存在:"+id);
            }

            xcxTravelMapper.updateById(xcxTravel);

            return CommonResult.success("成功更新小程序用户行程:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("更新小程序用户行程失败:"+e.getMessage());
        }
    }

    /**
     * 删除小程序用户行程
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult delete(Long id,Long userId) {
        try {
            QueryWrapper<XcxTravel> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(XcxTravel::getId, id).eq(XcxTravel::getUserId, userId);
            XcxTravel xcxTravel0 = xcxTravelMapper.selectOne(wrapper);
            if (xcxTravelMapper.selectById(id) == null) {
                return CommonResult.failed("该小程序用户行程不存在:"+id);
            }

            xcxTravelMapper.deleteById(id);
            return CommonResult.success("成功删除小程序用户行程:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除小程序用户行程失败:"+e.getMessage());
        }
    }

}
