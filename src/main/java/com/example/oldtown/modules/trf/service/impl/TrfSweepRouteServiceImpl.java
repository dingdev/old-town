package com.example.oldtown.modules.trf.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.oldtown.component.GlobalData;
import com.example.oldtown.modules.com.model.ComPlace;
import com.example.oldtown.modules.trf.mapper.TrfSweepPointMapper;
import com.example.oldtown.modules.trf.model.TrfSweepPoint;
import com.example.oldtown.modules.trf.model.TrfSweepRoute;
import com.example.oldtown.modules.trf.mapper.TrfSweepRouteMapper;
import com.example.oldtown.modules.trf.service.TrfSweepPointService;
import com.example.oldtown.modules.trf.service.TrfSweepRouteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oldtown.util.excel.UploadComPlaceListener;
import com.example.oldtown.util.excel.UploadTrfSweepPointListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

import com.example.oldtown.common.api.CommonResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 交通接驳打捞船标准路线 服务实现类
 * </p>
 * @author dyp
 * @since 2020-11-25
 */
@Service
public class TrfSweepRouteServiceImpl extends ServiceImpl<TrfSweepRouteMapper, TrfSweepRoute> implements TrfSweepRouteService {
    private final Logger LOGGER = LoggerFactory.getLogger(TrfSweepRouteServiceImpl.class);
    private static final String SWEEP_ROUTE_FOLDER = "trf/sweeproute/";
    @Value("${minio.bucketSide}")
    private String BUCKET_SIDE;

    @Resource
    TrfSweepRouteMapper trfSweepRouteMapper;
    @Resource
    TrfSweepPointService trfSweepPointService;
    @Resource
    TrfSweepPointMapper trfSweepPointMapper;

    /**
     * 分页查询交通接驳打捞船标准路线
     * @return
     */
    @Override
    public CommonResult getAll(Integer pageSize, Integer pageNum, String keyword) {

        QueryWrapper<TrfSweepRoute> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<TrfSweepRoute> lambda = wrapper.lambda();
        if (StrUtil.isNotBlank(keyword)) {
            lambda.like(TrfSweepRoute::getName, keyword);
        }
        if (pageNum == null) {
            return CommonResult.success(trfSweepRouteMapper.selectList(wrapper));
        } else {
            Page<TrfSweepRoute> page = new Page<>(pageNum, pageSize);
            return CommonResult.success(page(page,wrapper));
        }
    }


    /**
     * 增加交通接驳打捞船标准路线
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult add(TrfSweepRoute trfSweepRoute, MultipartFile pointsExcel) {
        try {

            trfSweepRoute.setCreateTime(new Date());
            trfSweepRouteMapper.insert(trfSweepRoute);

            // 打捞点
            Long routeId = trfSweepRoute.getId();
            if (pointsExcel != null && GlobalData.EXCEL_TYPES.contains(pointsExcel.getContentType())) {
                EasyExcel.read(pointsExcel.getInputStream(), TrfSweepPoint.class,
                        new UploadTrfSweepPointListener(trfSweepPointService,routeId)).sheet().headRowNumber(2).doRead();
            }

            return CommonResult.success("成功增加交通接驳打捞船标准路线:"+trfSweepRoute.getId(),trfSweepRoute.getId()+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("增加交通接驳打捞船标准路线失败:"+e.getMessage());
        }
    }

    /**
     * 更新交通接驳打捞船标准路线
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult update(TrfSweepRoute trfSweepRoute) {
        try {

            Long id = trfSweepRoute.getId();
            if (trfSweepRouteMapper.selectById(id) == null) {
                return CommonResult.failed("该交通接驳打捞船标准路线不存在:"+id);
            }

            trfSweepRouteMapper.updateById(trfSweepRoute);

            return CommonResult.success("成功更新交通接驳打捞船标准路线:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("更新交通接驳打捞船标准路线失败:"+e.getMessage());
        }
    }

    /**
     * 删除交通接驳打捞船标准路线
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult delete(Long id) {
        try {

            if (trfSweepRouteMapper.selectById(id) == null) {
                return CommonResult.failed("该交通接驳打捞船标准路线不存在:"+id);
            }

            // 打捞点
            trfSweepRouteMapper.deleteById(id);
            QueryWrapper<TrfSweepPoint> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(TrfSweepPoint::getRouteId, id);
            trfSweepPointMapper.delete(wrapper);

            return CommonResult.success("成功删除交通接驳打捞船标准路线:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除交通接驳打捞船标准路线失败:"+e.getMessage());
        }
    }

    /**
     * 批量删除交通接驳打捞船标准路线
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult batchDelete(List<Long> ids) {
        try {
            int deleteNum = trfSweepRouteMapper.deleteBatchIds(ids);

            // 打捞点
            QueryWrapper<TrfSweepPoint> wrapper = new QueryWrapper<>();
            wrapper.lambda().in(TrfSweepPoint::getRouteId, ids);
            trfSweepPointMapper.delete(wrapper);

            return CommonResult.success("成功删除交通接驳打捞船标准路线"+deleteNum+"个",deleteNum+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除交通接驳打捞船标准路线失败:"+e.getMessage());
        }
    }

    /**
     * 获取增加打捞船标准路线时的pointExcel文件模板地址
     */
    @Override
    public CommonResult getPointExcelTemplate() {
        return CommonResult.success(BUCKET_SIDE+SWEEP_ROUTE_FOLDER+"打捞点导入模板表.xlsx");
    }
}
