package com.example.oldtown.modules.com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.oldtown.dto.*;
import com.example.oldtown.modules.com.mapper.ComPlaceMapper;
import com.example.oldtown.modules.com.model.ComRoute;
import com.example.oldtown.modules.com.model.ComSpecialty;
import com.example.oldtown.modules.com.mapper.ComSpecialtyMapper;
import com.example.oldtown.modules.com.service.ComSpecialtyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oldtown.modules.trf.model.TrfSweepStaff;
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
 * 通用特产 服务实现类
 * </p>
 * @author dyp
 * @since 2020-12-01
 */
@Service
public class ComSpecialtyServiceImpl extends ServiceImpl<ComSpecialtyMapper, ComSpecialty> implements ComSpecialtyService {
    private final Logger LOGGER = LoggerFactory.getLogger(ComSpecialtyServiceImpl.class);

    @Resource
    ComSpecialtyMapper comSpecialtyMapper;
    @Resource
    ComPlaceMapper comPlaceMapper;

    /**
     * 分页查询通用特产
     * @return
     */
    @Override
    public CommonResult getAll(Integer pageSize, Integer pageNum, String type, String keyword) {

        QueryWrapper<ComSpecialty> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<ComSpecialty> lambda = wrapper.lambda();
        if (StrUtil.isNotBlank(type)) {
            if (type.contains(",")) {
                lambda.in(ComSpecialty::getType,type.split(","));
            } else {
                lambda.like(ComSpecialty::getType, type);
            }
        }
        if (StrUtil.isNotBlank(keyword)) {
            lambda.like(ComSpecialty::getName, keyword);
        }
        // 排序
        wrapper.orderByDesc("sorting_order").orderByAsc("id");
        if (pageNum == null) {
            return CommonResult.success(comSpecialtyMapper.selectList(wrapper));
        } else {
            Page<ComSpecialty> page = new Page<>(pageNum, pageSize);
            return CommonResult.success(page(page,wrapper));
        }
    }

    /**
     * 根据id查询通用特产
     */
    @Override
    public CommonResult getSpecialtyById(Long id) {
        ComSpecialty comSpecialty = comSpecialtyMapper.selectById(id);

        if (comSpecialty == null) {
            return CommonResult.success(null);
        }

        String placeIds = comSpecialty.getPlaceIds();
        if (StrUtil.isBlank(placeIds)) {
            return CommonResult.success(new ComSpecialtyDTO(comSpecialty, null));
        } else {
            placeIds = "(" + placeIds + ")";
            List<MinPlaceDTO> plcaeList = comPlaceMapper.getMinPlaceList(placeIds);
            return CommonResult.success(new ComSpecialtyDTO(comSpecialty, plcaeList));
        }
    }


    /**
     * 增加通用特产
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult add(ComSpecialty comSpecialty) {
        try {

            comSpecialty.setCreateTime(new Date());
            comSpecialtyMapper.insert(comSpecialty);

            return CommonResult.success("成功增加通用特产:"+comSpecialty.getId(),comSpecialty.getId()+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("增加通用特产失败:"+e.getMessage());
        }
    }

    /**
     * 更新通用特产
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult update(ComSpecialty comSpecialty) {
        try {

            Long id = comSpecialty.getId();
            if (comSpecialtyMapper.selectById(id) == null) {
                return CommonResult.failed("该通用特产不存在:"+id);
            }

            comSpecialtyMapper.updateById(comSpecialty);

            return CommonResult.success("成功更新通用特产:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("更新通用特产失败:"+e.getMessage());
        }
    }

    /**
     * 删除通用特产
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult delete(Long id) {
        try {

            if (comSpecialtyMapper.selectById(id) == null) {
                return CommonResult.failed("该通用特产不存在:"+id);
            }

            comSpecialtyMapper.deleteById(id);
            return CommonResult.success("成功删除通用特产:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除通用特产失败:"+e.getMessage());
        }
    }

    /**
     * 批量删除通用特产
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult batchDelete(List<Long> ids) {
        try {
            int deleteNum = comSpecialtyMapper.deleteBatchIds(ids);
            return CommonResult.success("成功删除通用特产"+deleteNum+"个",deleteNum+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除通用特产失败:"+e.getMessage());
        }
    }

}
